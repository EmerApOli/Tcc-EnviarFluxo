package org.acme.enviofluxo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)

public  class ItemEnvioFluxo  extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  long id;

    @ManyToOne
    @JoinColumn(name = "enviofluxo_id")
    private EnvioFluxo enviofluxo;


    @ManyToOne
    @JoinColumn(name = "documento_id")
     private Documentos documentos;




    public ItemEnvioFluxo(EnvioFluxo enviofluxo, Documentos documentos) {
        this.enviofluxo = enviofluxo;
        this.documentos = documentos;
        // Inicializar outros atributos, se necessário
    }


}
