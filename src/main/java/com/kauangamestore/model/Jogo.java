//Classe de Kauan Batista

package com.kauangamestore.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Jogo extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    private Double preco;
    private Integer estoque;

    @Column(name = "percentual_desconto")
    private Double percentualDesconto;

    @Column(name = "nome_imagem")
    private String nomeImagem;

    @Column(name = "data_lancamento")
    private LocalDate dataLancamento;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa desenvolvedora;

    @ManyToMany
    @JoinTable(name = "jogo_plataforma",
        joinColumns = @JoinColumn(name = "id_jogo"),
        inverseJoinColumns = @JoinColumn(name = "id_plataforma"))
    private List<Plataforma> plataformas;

    @ManyToMany
    @JoinTable(name = "jogo_categoria",
        joinColumns = @JoinColumn(name = "id_jogo"),
        inverseJoinColumns = @JoinColumn(name = "id_categoria"))
    private List<Categoria> categorias;

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public Double getPercentualDesconto() { return percentualDesconto; }
    public void setPercentualDesconto(Double percentualDesconto) { this.percentualDesconto = percentualDesconto; }

    public String getNomeImagem() { return nomeImagem; }
    public void setNomeImagem(String nomeImagem) { this.nomeImagem = nomeImagem; }

    public LocalDate getDataLancamento() { return dataLancamento; }
    public void setDataLancamento(LocalDate dataLancamento) { this.dataLancamento = dataLancamento; }

    public Empresa getDesenvolvedora() { return desenvolvedora; }
    public void setDesenvolvedora(Empresa desenvolvedora) { this.desenvolvedora = desenvolvedora; }

    public List<Plataforma> getPlataformas() { return plataformas; }
    public void setPlataformas(List<Plataforma> plataformas) { this.plataformas = plataformas; }

    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }
}
