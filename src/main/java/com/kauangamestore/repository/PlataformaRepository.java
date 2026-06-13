package com.kauangamestore.repository;

import com.kauangamestore.model.Plataforma;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlataformaRepository implements PanacheRepository<Plataforma> {

    public PanacheQuery<Plataforma> findByNome(String nome) {
        return find("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%");
    }

    public long countNome(String nome) {
        return count("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%");
    }
}
