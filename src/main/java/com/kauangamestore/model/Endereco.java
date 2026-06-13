//Classe de Kauan Batista

package com.kauangamestore.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
public class Endereco extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String logradouro;
    public String numero;
    public String cep;
    public String cidade;
    public String bairro;
    public String complemento;
    public String estado;
}
