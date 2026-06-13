package com.kauangamestore.repository;

import com.kauangamestore.model.Cupom;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CupomRepository implements PanacheRepositoryBase<Cupom, Long> {

    public Cupom findByCodigo(String codigo) {
        return find("upper(codigo)", codigo == null ? null : codigo.trim().toUpperCase()).firstResult();
    }
}
