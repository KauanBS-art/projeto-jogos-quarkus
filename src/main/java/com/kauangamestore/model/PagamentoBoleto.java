package com.kauangamestore.model;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class PagamentoBoleto extends Pagamento {

    private String numeroBoleto;
    private LocalDate dataVencimento;

    // --- GETTERS E SETTERS ---

    public String getNumeroBoleto() { return numeroBoleto; }
    public void setNumeroBoleto(String numeroBoleto) { this.numeroBoleto = numeroBoleto; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
}