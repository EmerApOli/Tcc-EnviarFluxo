package org.acme.enviofluxo.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.dto.DadosBasicosDTO;


import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "dadosbasicos")
public class DadosBasicos extends PanacheEntityBase implements Serializable {


    private  static  final Long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "nome")
    private  String nome;
    @JoinColumn(name = "descricao")
    private  String descricao;
    @JoinColumn(name = "tipoassinatura")
    private  String tipoassinatura;



    public DadosBasicos(DadosBasicosDTO dadosBasicosDTO){


        this.nome = dadosBasicosDTO.getNome();
        this.descricao = dadosBasicosDTO.getDescricao();
      //  this.status = dadosBasicosDTO.getStatus();
        this.tipoassinatura = dadosBasicosDTO.getTipoassinatura();

    }


}
