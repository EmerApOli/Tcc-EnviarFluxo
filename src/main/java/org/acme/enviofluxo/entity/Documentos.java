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
public class Documentos  extends PanacheEntityBase implements Serializable {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long  id;

    @JoinColumn(name = "tipo")
    private String tipo;
    @JoinColumn(name = "descricao")
    private String descricao;
}
