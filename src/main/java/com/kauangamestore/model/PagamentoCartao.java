package com.kauangamestore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PagamentoCartao extends Pagamento {

    @ManyToOne
    @JoinColumn(name = "id_cartao", nullable = false)
    private CartaoCredito cartao;

    private Integer parcelas;

    public CartaoCredito getCartao() { return cartao; }
    public void setCartao(CartaoCredito cartao) { this.cartao = cartao; }

    public Integer getParcelas() { return parcelas; }
    public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }
}
