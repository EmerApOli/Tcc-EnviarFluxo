package org.acme.enviofluxo.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "documenthash")
    private  String documenthash;


    @Column(name = "status")
    private  String status;





    @OneToMany(mappedBy = "enviofluxo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documentos> listadocumento;




    public static DadosBasicos findByCpf(Long id) {
        return find("id", id).firstResult();
    }


}
