package com.zetaxii.speseMensili.dto;

import java.math.BigDecimal;

public class AzioneDTO {

    private Long id;
    private String azione;
    private String categoria;
    private String nomeGiorno;
    private Integer numeroGiorno;
    private String mese;
    private Integer anno;
    private BigDecimal entrata;
    private BigDecimal uscita;
    private String user;

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
}
