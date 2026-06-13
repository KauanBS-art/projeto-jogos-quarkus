package com.kauangamestore.model;

import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class PagamentoPix extends Pagamento {

    private String chavePix;
    private String codigoTransacao;
    private LocalDateTime dataExpiracao;

    // --- GETTERS E SETTERS ---

    public String getChavePix() { return chavePix; }
    public void setChavePix(String chavePix) { this.chavePix = chavePix; }

    public String getCodigoTransacao() { return codigoTransacao; }
    public void setCodigoTransacao(String codigoTransacao) { this.codigoTransacao = codigoTransacao; }

    public LocalDateTime getDataExpiracao() { return dataExpiracao; }
    public void setDataExpiracao(LocalDateTime dataExpiracao) { this.dataExpiracao = dataExpiracao; }
}