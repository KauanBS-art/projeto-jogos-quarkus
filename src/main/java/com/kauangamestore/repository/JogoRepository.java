package com.kauangamestore.repository;

import com.kauangamestore.model.Jogo;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JogoRepository implements PanacheRepository<Jogo> {

    public PanacheQuery<Jogo> findByNome(String nome) {
        return find("LOWER(titulo) LIKE LOWER(?1)", "%" + nome + "%");
    }

    public long countNome(String nome) {
        return count("LOWER(titulo) LIKE LOWER(?1)", "%" + nome + "%");
    }

    public PanacheQuery<Jogo> findByPromocao(String nome) {
        String query = "percentualDesconto > 0";
        if (nome != null && !nome.isBlank()) {
            return find(query + " AND LOWER(titulo) LIKE LOWER(?1)", "%" + nome + "%");
        }
        return find(query);
    }

    public long countPromocoes(String nome) {
        String query = "percentualDesconto > 0";
        if (nome != null && !nome.isBlank()) {
            return count(query + " AND LOWER(titulo) LIKE LOWER(?1)", "%" + nome + "%");
        }
        return count(query);
    }
}
