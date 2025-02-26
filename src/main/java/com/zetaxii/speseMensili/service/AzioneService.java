package com.zetaxii.speseMensili.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zetaxii.speseMensili.dto.AzioneDTO;
import com.zetaxii.speseMensili.dto.TotaliDTO;
import com.zetaxii.speseMensili.entity.AzioneEntity;
import com.zetaxii.speseMensili.repository.AzioneRepository;
import com.zetaxii.speseMensili.repository.AzioneSpecifications;

@Service
public class AzioneService {

    @Autowired
    private AzioneRepository azioneRepository;

    public List<AzioneDTO> getAllAzioni(String user) {
        List<AzioneEntity> azioni = azioneRepository.findByAnnoAndUser(LocalDate.now().getYear(), user);
        return azioni.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Calcolo totale entrate, uscite e saldo
    public TotaliDTO calcolaTotali(String user) {
        BigDecimal totaleEntrate = BigDecimal.ZERO;
        BigDecimal totaleUscite = BigDecimal.ZERO;

        List<AzioneEntity> azioni = azioneRepository.findByAnnoAndUser(LocalDate.now().getYear(), user);

        for (AzioneEntity azione : azioni) {
            if (azione.getEntrata() != null) {
                totaleEntrate = totaleEntrate.add(azione.getEntrata());
            }
            if (azione.getUscita() != null) {
                totaleUscite = totaleUscite.add(azione.getUscita());
            }
        }

        BigDecimal saldo = totaleEntrate.subtract(totaleUscite);

        return new TotaliDTO(totaleEntrate, totaleUscite, saldo);
    }

    // Ottiene tutte le azioni di un mese e anno, ordinate per giorno
    @Transactional(readOnly = true)
    public List<AzioneDTO> getAzioniByMeseAnnoOrdinate(String mese, Integer anno, String user) {
        List<AzioneEntity> azioni = azioneRepository.findByMeseAndAnnoAndUser(mese, anno, user);

        return azioni.stream()
            .sorted(Comparator.comparing(AzioneEntity::getNumeroGiorno, Comparator.reverseOrder())
                .thenComparing(a -> a.getEntrata() != null ? 0 : 1)) // Entrate prima delle uscite
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Ottiene tutte le azioni di un anno, ordinate per giorno
    @Transactional(readOnly = true)
    public List<AzioneDTO> getAzioniByAnnoOrdinate(Integer anno, String user) {
        List<AzioneEntity> azioni = azioneRepository.findByAnnoAndUser(anno, user);

        return azioni.stream()
            .sorted(Comparator.comparing(AzioneEntity::getNumeroGiorno, Comparator.reverseOrder())
                .thenComparing(a -> a.getEntrata() != null ? 0 : 1))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public AzioneDTO getAzioneById(Long id, String user) {
        AzioneEntity azione = azioneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Azione non trovata"));

        if (!azione.getUser().equals(user)) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        return convertToDTO(azione);
    }

    @Transactional(readOnly = true)
    public List<AzioneDTO> searchByFilter(AzioneDTO azioneSearchDTO, String user) {
        Specification<AzioneEntity> spec = Specification.where(AzioneSpecifications.hasUser(user));

        if (azioneSearchDTO.getAzione() != null) {
            spec = spec.and(AzioneSpecifications.hasAzione(azioneSearchDTO.getAzione()));
        }
        if (azioneSearchDTO.getCategoria() != null) {
            spec = spec.and(AzioneSpecifications.hasCategoria(azioneSearchDTO.getCategoria()));
        }
        if (azioneSearchDTO.getNomeGiorno() != null) {
            spec = spec.and(AzioneSpecifications.hasNomeGiorno(azioneSearchDTO.getNomeGiorno()));
        }
        if (azioneSearchDTO.getNumeroGiorno() != null) {
            spec = spec.and(AzioneSpecifications.hasNumeroGiorno(azioneSearchDTO.getNumeroGiorno()));
        }
        if (azioneSearchDTO.getMese() != null) {
            spec = spec.and(AzioneSpecifications.hasMese(azioneSearchDTO.getMese()));
        }
        if (azioneSearchDTO.getAnno() != null) {
            spec = spec.and(AzioneSpecifications.hasAnno(azioneSearchDTO.getAnno()));
        }
        if (azioneSearchDTO.getEntrata() != null) {
            spec = spec.and(AzioneSpecifications.hasEntrata(azioneSearchDTO.getEntrata()));
        }
        if (azioneSearchDTO.getUscita() != null) {
            spec = spec.and(AzioneSpecifications.hasUscita(azioneSearchDTO.getUscita()));
        }

        List<AzioneEntity> azioniEntities = azioneRepository.findAll(spec);
        return azioniEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AzioneDTO createAzione(AzioneDTO azioneDTO, String user) {
        LocalDate oggi = LocalDate.now();

        azioneDTO.setNumeroGiorno(oggi.getDayOfMonth());
        azioneDTO.setMese(String.valueOf(oggi.getMonthValue()));
        azioneDTO.setAnno(oggi.getYear());
        azioneDTO.setNomeGiorno(oggi.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ITALIAN).toUpperCase());

        AzioneEntity azioneEntity = convertToEntity(azioneDTO);
        azioneEntity.setUser(user);

        azioneEntity = azioneRepository.save(azioneEntity);
        return convertToDTO(azioneEntity);
    }

    public AzioneDTO updateAzione(Long id, AzioneDTO azioneDTO, String user) {
        AzioneEntity azioneEntity = azioneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Azione non trovata"));

        if (!azioneEntity.getUser().equals(user)) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        if (azioneEntity.getNumeroGiorno() != null) {
            azioneDTO.setNumeroGiorno(azioneEntity.getNumeroGiorno());
        }
        if (azioneEntity.getAnno() != null) {
            azioneDTO.setAnno(azioneEntity.getAnno());
        }
        if (azioneEntity.getNomeGiorno() != null) {
            azioneDTO.setNomeGiorno(azioneEntity.getNomeGiorno());
        }
        if (azioneEntity.getMese() != null) {
            azioneDTO.setMese(azioneEntity.getMese());
        }

        AzioneEntity updatedEntity = convertToEntity(azioneDTO);
        updatedEntity.setId(id);
        updatedEntity.setUser(user);

        updatedEntity = azioneRepository.save(updatedEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteAzione(Long id, String user) {
        AzioneEntity azione = azioneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Azione non trovata"));

        if (!azione.getUser().equals(user)) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        azioneRepository.deleteById(id);
    }

    private AzioneDTO convertToDTO(AzioneEntity azioneEntity) {
        AzioneDTO azioneDTO = new AzioneDTO();
        azioneDTO.setId(azioneEntity.getId());
        azioneDTO.setAzione(azioneEntity.getAzione());
        azioneDTO.setCategoria(azioneEntity.getCategoria());
        azioneDTO.setNomeGiorno(azioneEntity.getNomeGiorno());
        azioneDTO.setNumeroGiorno(azioneEntity.getNumeroGiorno());
        azioneDTO.setMese(azioneEntity.getMese());
        azioneDTO.setAnno(azioneEntity.getAnno());
        azioneDTO.setEntrata(azioneEntity.getEntrata());
        azioneDTO.setUscita(azioneEntity.getUscita());
        return azioneDTO;
    }

    private AzioneEntity convertToEntity(AzioneDTO azioneDTO) {
        AzioneEntity azioneEntity = new AzioneEntity();
        azioneEntity.setAzione(azioneDTO.getAzione());
        azioneEntity.setCategoria(azioneDTO.getCategoria());
        azioneEntity.setNomeGiorno(azioneDTO.getNomeGiorno());
        azioneEntity.setNumeroGiorno(azioneDTO.getNumeroGiorno());
        azioneEntity.setMese(azioneDTO.getMese());
        azioneEntity.setAnno(azioneDTO.getAnno());
        azioneEntity.setEntrata(azioneDTO.getEntrata());
        azioneEntity.setUscita(azioneDTO.getUscita());
        return azioneEntity;
    }
}
