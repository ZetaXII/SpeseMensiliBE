package com.zetaxii.speseMensili.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.zetaxii.speseMensili.entity.AzioneEntity;

public class AzioneSpecifications {

    public static Specification<AzioneEntity> hasAzione(String azione) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("azione"), azione);
    }

    public static Specification<AzioneEntity> hasCategoria(String categoria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoria"), categoria);
    }

    public static Specification<AzioneEntity> hasNomeGiorno(String nomeGiorno) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nomeGiorno"), nomeGiorno);
    }

    public static Specification<AzioneEntity> hasNumeroGiorno(Integer numeroGiorno) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("numeroGiorno"), numeroGiorno);
    }

    public static Specification<AzioneEntity> hasMese(String mese) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("mese"), mese);
    }

    public static Specification<AzioneEntity> hasAnno(Integer anno) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("anno"), anno);
    }

    public static Specification<AzioneEntity> hasEntrata(BigDecimal entrata) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("entrata"), entrata);
    }

    public static Specification<AzioneEntity> hasUscita(BigDecimal uscita) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("uscita"), uscita);
    }
}
