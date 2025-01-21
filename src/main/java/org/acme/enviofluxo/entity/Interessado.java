package org.acme.enviofluxo.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
public class Interessado extends PanacheEntityBase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
   private  Long  id;
    @Column(name = "cpf")
    private  Long cpf;
    @Column(name = "nome")
    private  String nome;
    @Column(name = "descricao")
    private  String descricao;
    @Column(name = "cargo")
    private String  cargo;
    @Column(name = "idiniciofluxo")
   private  Long idiniciofluxo;

}
