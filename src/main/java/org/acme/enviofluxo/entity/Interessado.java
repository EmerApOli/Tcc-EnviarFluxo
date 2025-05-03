package org.acme.enviofluxo.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.dto.InteressadoDTO;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
public class Interessado extends PanacheEntityBase implements Serializable {

    private  static  final Long serialVersionUID = 1L;


 //   @GeneratedValue(strategy = GenerationType.IDENTITY)
  //  @Column(name = "id")
  //  private  Long  id;
    @Id // Define 'cpf' como chave primária
    @Column(name = "cpf", unique = true, nullable = false)
     private  String cpf;
    @Column(name = "nome")
    private  String nome;
    @Column(name = "descricao")
    private  String descricao;
    @Column(name = "cargo")
    private String  cargo;
    @Column(name = "idenviofluxo")
   private  String idenviofluxo;

    @ManyToOne
    @JoinColumn(name = "id_documento", nullable = false)
    private Documentos  documento;

    @ManyToOne
    @JoinColumn(name = "id_selo", nullable = false)
    private Selo  selo;




    public  Interessado(InteressadoDTO interessadoDTO){

        this.cpf = interessadoDTO.getCpf();
        this.nome = interessadoDTO.getNome();
        this.descricao = interessadoDTO.getDescricao();
        this.cargo = interessadoDTO.getCargo();
        this.idenviofluxo = interessadoDTO.getIdenviofluxo();
        this.documento  = interessadoDTO.getDocumentos();




    }

    public static Interessado findByCpf(Long cpf) {
        return find("cpf", cpf).firstResult();
    }

}
