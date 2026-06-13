package com.kauangamestore.dto;

import com.kauangamestore.model.Categoria;
import com.kauangamestore.model.Jogo;
import com.kauangamestore.model.Plataforma;
import java.time.LocalDate;
import java.util.List;

public record JogoResponseDTO(
    Long id,
    String titulo,
    String descricao,
    Double preco,
    Integer estoque,
    Double percentualDesconto,
    Double precoComDesconto,
    String nomeImagem,
    LocalDate dataLancamento,
    Long idEmpresa,
    List<Long> idPlataformas,
    List<Long> idCategorias,
    String nomeEmpresa,
    List<String> plataformas,
    List<String> categorias
) {
    public static JogoResponseDTO valueOf(Jogo jogo) {
        List<Long> idsPlataformas = jogo.getPlataformas() == null
            ? List.of()
            : jogo.getPlataformas().stream().map(Plataforma::getId).toList();

        List<Long> idsCategorias = jogo.getCategorias() == null
            ? List.of()
            : jogo.getCategorias().stream().map(Categoria::getId).toList();

        List<String> listaPlataformas = jogo.getPlataformas() == null
            ? List.of()
            : jogo.getPlataformas().stream().map(Plataforma::getNome).toList();

        List<String> listaCategorias = jogo.getCategorias() == null
            ? List.of()
            : jogo.getCategorias().stream().map(Categoria::getNome).toList();

        Double desconto = jogo.getPercentualDesconto() == null ? 0.0 : jogo.getPercentualDesconto();
        Double precoFinal = jogo.getPreco() != null ? jogo.getPreco() * (1 - (desconto / 100.0)) : 0.0;
        // Arredondar para 2 casas decimais
        precoFinal = Math.round(precoFinal * 100.0) / 100.0;

        return new JogoResponseDTO(
            jogo.getId(),
            jogo.getTitulo(),
            jogo.getDescricao(),
            jogo.getPreco(),
            jogo.getEstoque(),
            desconto,
            precoFinal,
            jogo.getNomeImagem(),
            jogo.getDataLancamento(),
            jogo.getDesenvolvedora() == null ? null : jogo.getDesenvolvedora().getId(),
            idsPlataformas,
            idsCategorias,
            jogo.getDesenvolvedora() == null ? null : jogo.getDesenvolvedora().getNome(),
            listaPlataformas,
            listaCategorias
        );
    }
}
