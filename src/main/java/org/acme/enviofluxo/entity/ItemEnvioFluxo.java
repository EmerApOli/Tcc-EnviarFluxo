package org.acme.enviofluxo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)

public  class ItemEnvioFluxo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  long id;

    @ManyToOne
    @JoinColumn(name = "enviofluxo_id")
    private EnvioFluxo enviofluxo;


    @ManyToOne
    @JoinColumn(name = "interessado_id")
     private Interessado interessado;


   ;



}
