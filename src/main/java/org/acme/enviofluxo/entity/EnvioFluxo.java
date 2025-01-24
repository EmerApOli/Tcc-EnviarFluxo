package org.acme.enviofluxo.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheEntity_;
import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.external.DTO.DadosBasicos;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
 @EqualsAndHashCode(callSuper=false)
public class EnvioFluxo extends PanacheEntityBase implements Serializable {
    private  static  final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  long id;

    @Column(name = "idiniciofluxo")
    private Long idiniciofluxo;
    @Column(name = "documenthash")
    private  String documenthash;

    @ManyToOne
    @JoinColumn(name = "interessado_cpf", nullable = false)
    private  Interessado interessado;



    public  EnvioFluxo(EnvioDTO envioDTO){
        this.documenthash =  envioDTO.getDocumenthash();
        this.interessado = envioDTO.getInteressado();
        this.idiniciofluxo= envioDTO.getIdiniciofluxo();

    }

}
