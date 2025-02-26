package com.zetaxii.speseMensili.dto;

import java.math.BigDecimal;

public class TotaliDTO {

    private BigDecimal totaleEntrate;
    private BigDecimal totaleUscite;
    private BigDecimal saldo;

    // Costruttore con parametri
    public TotaliDTO(BigDecimal totaleEntrate, BigDecimal totaleUscite, BigDecimal saldo) {
        this.totaleEntrate = totaleEntrate;
        this.totaleUscite = totaleUscite;
        this.saldo = saldo;
    }

    // Costruttore vuoto (necessario per framework come Spring)
    public TotaliDTO() {}    
    
    // Getters e Setters
    public BigDecimal getTotaleEntrate() {
        return totaleEntrate;
    }

    public void setTotaleEntrate(BigDecimal totaleEntrate) {
        this.totaleEntrate = totaleEntrate;
    }

    public BigDecimal getTotaleUscite() {
        return totaleUscite;
    }

    public void setTotaleUscite(BigDecimal totaleUscite) {
        this.totaleUscite = totaleUscite;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}

