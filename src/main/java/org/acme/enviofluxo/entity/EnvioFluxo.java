package org.acme.enviofluxo.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheEntity_;
import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.external.DTO.DadosBasicos;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
 @EqualsAndHashCode(callSuper=false)
public class EnvioFluxo extends PanacheEntityBase implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  long id;

  //  private DadosBasicos dadosBasicos;
    @ManyToOne
    @JoinColumn(name = "documento_id", nullable = false)
    private  Documentos documentos;

    @ManyToOne
    @JoinColumn(name = "interessado_cpf", nullable = false)
    private  Interessado interessado;



}
