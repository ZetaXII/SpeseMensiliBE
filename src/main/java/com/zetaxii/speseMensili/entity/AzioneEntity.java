package com.zetaxii.speseMensili.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class AzioneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "azione", nullable = false)
    private String azione;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "nomeGiorno", nullable = false)
    private String nomeGiorno;

    @Column(name = "numeroGiorno", nullable = false)
    private Integer numeroGiorno;

    @Column(name = "mese", nullable = false)
    private String mese;

    @Column(name = "anno", nullable = false)
    private Integer anno;

    @Column(name = "entrata", nullable = true)
    private BigDecimal entrata;

    @Column(name = "uscita", nullable = true)
    private BigDecimal uscita;

    @Column(name = "\"user\"", nullable=false)
    private String user; // Aggiunto il campo user

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAzione() {
        return azione;
    }

    public void setAzione(String azione) {
        this.azione = azione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNomeGiorno() {
        return nomeGiorno;
    }

    public void setNomeGiorno(String nomeGiorno) {
        this.nomeGiorno = nomeGiorno;
    }

    public Integer getNumeroGiorno() {
        return numeroGiorno;
    }

    public void setNumeroGiorno(Integer numeroGiorno) {
        this.numeroGiorno = numeroGiorno;
    }

    public String getMese() {
        return mese;
    }

    public void setMese(String mese) {
        this.mese = mese;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public BigDecimal getEntrata() {
        return entrata;
    }

    public void setEntrata(BigDecimal entrata) {
        this.entrata = entrata;
    }

    public BigDecimal getUscita() {
        return uscita;
    }

    public void setUscita(BigDecimal uscita) {
        this.uscita = uscita;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    // Logica di validazione per entrata/uscita
    @PrePersist
    @PreUpdate
    public void validateEntrataUscita() {
        if (entrata != null && uscita != null) {
            throw new IllegalStateException("Non è possibile impostare contemporaneamente un'entrata e un'uscita.");
        }
        if (entrata == null && uscita == null) {
            throw new IllegalStateException("Inserire un'entrata o un'uscita.");
        }
    }
}
