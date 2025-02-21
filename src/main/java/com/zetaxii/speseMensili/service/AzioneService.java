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

	public List<AzioneDTO> getAllAzioni() {
		List<AzioneEntity> azioni = azioneRepository.findAll();
		return azioni.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
    // Calcolare il totale delle entrate, uscite e il saldo
    public TotaliDTO calcolaTotali() {
        BigDecimal totaleEntrate = BigDecimal.ZERO;
        BigDecimal totaleUscite = BigDecimal.ZERO;

        List<AzioneEntity> azioni = azioneRepository.findAll(); // Ottieni tutte le azioni

        // Somma delle entrate e delle uscite
        for (AzioneEntity azione : azioni) {
            if (azione.getEntrata() != null) {
                totaleEntrate = totaleEntrate.add(azione.getEntrata());
            }
            if (azione.getUscita() != null) {
                totaleUscite = totaleUscite.add(azione.getUscita());
            }
        }

        // Calcola il saldo (Entrate - Uscite)
        BigDecimal saldo = totaleEntrate.subtract(totaleUscite);

        // Restituisci i totali in un DTO
        TotaliDTO totaliDTO = new TotaliDTO();
        totaliDTO.setTotaleEntrate(totaleEntrate);
        totaliDTO.setTotaleUscite(totaleUscite);
        totaliDTO.setSaldo(saldo);

        return totaliDTO;
    }	

	// Ottiene tutte le azioni di un mese e anno, ordinate per giorno
	@Transactional(readOnly = true)
	public List<AzioneDTO> getAzioniByMeseAnnoOrdinate(String mese, Integer anno) {
		List<AzioneEntity> azioni = azioneRepository.findByMeseAndAnno(mese, anno);

		return azioni.stream().sorted(Comparator.comparing(AzioneEntity::getNumeroGiorno, Comparator.reverseOrder())
				.thenComparing(a -> a.getEntrata() != null ? 0 : 1) // Entrate prima delle uscite
		).map(this::convertToDTO).collect(Collectors.toList());
	}
	
	// Ottiene tutte le azioni di un anno, ordinate per giorno
	@Transactional(readOnly = true)
	public List<AzioneDTO> getAzioniByAnnoOrdinate(Integer anno) {
		List<AzioneEntity> azioni = azioneRepository.findByAnno(anno);

		return azioni.stream().sorted(Comparator.comparing(AzioneEntity::getNumeroGiorno, Comparator.reverseOrder())
				.thenComparing(a -> a.getEntrata() != null ? 0 : 1) // Entrate prima delle uscite
		).map(this::convertToDTO).collect(Collectors.toList());
	}	

	public AzioneDTO getAzioneById(Long id) {
		AzioneEntity azione = azioneRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Azione non trovata"));
		return convertToDTO(azione);
	}

	@Transactional(readOnly = true)
	public List<AzioneDTO> searchByFilter(AzioneDTO azioneSearchDTO) {
		Specification<AzioneEntity> spec = Specification.where(null);

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

	public AzioneDTO createAzione(AzioneDTO azioneDTO) {
		// Ottenere la data odierna
		LocalDate oggi = LocalDate.now();

		// Impostare i valori della data nel DTO
		azioneDTO.setNumeroGiorno(oggi.getDayOfMonth()); // Giorno del mese (1-31)
		azioneDTO.setMese(String.valueOf(oggi.getMonthValue())); // Mese come numero (1-12)
		azioneDTO.setAnno(oggi.getYear()); // Anno (es: 2024)
		azioneDTO.setNomeGiorno(oggi.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ITALIAN).toUpperCase());
		// Convertire in Entity e salvare
		AzioneEntity azioneEntity = convertToEntity(azioneDTO);
		azioneEntity = azioneRepository.save(azioneEntity);

		// Convertire nuovamente in DTO e restituire
		return convertToDTO(azioneEntity);
	}

	public AzioneDTO updateAzione(Long id, AzioneDTO azioneDTO) {
		AzioneEntity azioneEntity = azioneRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Azione non trovata"));

		// Impostare i valori esistenti per i campi che non devono essere modificati
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

		// Convertire il DTO aggiornato in entità
		azioneEntity = convertToEntity(azioneDTO);
		azioneEntity.setId(id);

		// Salvare l'entità aggiornata nel repository
		azioneEntity = azioneRepository.save(azioneEntity);

		return convertToDTO(azioneEntity);
	}

	public void deleteAzione(Long id) {
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
