package com.zetaxii.speseMensili.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.zetaxii.speseMensili.entity.AzioneEntity;

@Repository
public interface AzioneRepository extends JpaRepository<AzioneEntity, Long>, JpaSpecificationExecutor<AzioneEntity> {

	List<AzioneEntity> findByMeseAndAnnoOrderByNumeroGiornoDesc(String mese, Integer anno);

	List<AzioneEntity> findByMeseAndAnno(String mese, Integer anno);

	List<AzioneEntity> findByAnno(Integer anno);
}
