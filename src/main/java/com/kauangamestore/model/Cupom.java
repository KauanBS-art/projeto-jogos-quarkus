package com.kauangamestore.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Cupom extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Column(nullable = false, unique = true)
    private String codigo;

    private Double percentualDesconto;

    private Boolean ativo = true;

    // Restrições Opcionais
    private String restricaoCategoria;
    private String restricaoPlataforma;
    private String restricaoEmpresa;
    private Boolean primeiraCompra;
    private Double restricaoPrecoMinimo;
    private Double restricaoPrecoMaximo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Double getPercentualDesconto() { return percentualDesconto; }
    public void setPercentualDesconto(Double percentualDesconto) { this.percentualDesconto = percentualDesconto; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public String getRestricaoCategoria() { return restricaoCategoria; }
    public void setRestricaoCategoria(String restricaoCategoria) { this.restricaoCategoria = restricaoCategoria; }

    public String getRestricaoPlataforma() { return restricaoPlataforma; }
    public void setRestricaoPlataforma(String restricaoPlataforma) { this.restricaoPlataforma = restricaoPlataforma; }

    public String getRestricaoEmpresa() { return restricaoEmpresa; }
    public void setRestricaoEmpresa(String restricaoEmpresa) { this.restricaoEmpresa = restricaoEmpresa; }

    public Boolean getPrimeiraCompra() { return primeiraCompra; }
    public void setPrimeiraCompra(Boolean primeiraCompra) { this.primeiraCompra = primeiraCompra; }

    public Double getRestricaoPrecoMinimo() { return restricaoPrecoMinimo; }
    public void setRestricaoPrecoMinimo(Double restricaoPrecoMinimo) { this.restricaoPrecoMinimo = restricaoPrecoMinimo; }

    public Double getRestricaoPrecoMaximo() { return restricaoPrecoMaximo; }
    public void setRestricaoPrecoMaximo(Double restricaoPrecoMaximo) { this.restricaoPrecoMaximo = restricaoPrecoMaximo; }
}
