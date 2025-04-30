package org.acme.enviofluxo.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheEntity_;
import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.dto.EnvioDTO;

import java.io.Serializable;
import java.util.List;

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

    @Column(name = "idfluxo")
    private  String idfluxo;

     @Column(name = "documenthash")
    private  String documenthash;


    @Column(name = "status")
    private  String status;

    @ManyToOne
    @JoinColumn(name = "id_dadosbasicos", nullable = false)
    private DadosBasicos  dadosBasicos;



    @OneToMany(mappedBy = "enviofluxo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEnvioFluxo> items;


    public  EnvioFluxo(EnvioDTO envioDTO){
        this.documenthash =  envioDTO.getDocumenthash();
       // this.interessado = envioDTO.getInteressado();
        this.dadosBasicos= envioDTO.getDadosBasicos();
        this.idfluxo  = envioDTO.getIdfluxo();
        this.status = envioDTO.getStatus();

    }

    public static DadosBasicos findByCpf(Long id) {
        return find("id", id).firstResult();
    }

}
