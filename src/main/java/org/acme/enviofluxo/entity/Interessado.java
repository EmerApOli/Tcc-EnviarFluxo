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


    public  Interessado(InteressadoDTO interessadoDTO){

        this.cpf = interessadoDTO.getCpf();
        this.nome = interessadoDTO.getNome();
        this.descricao = interessadoDTO.getDescricao();
        this.cargo = interessadoDTO.getCargo();



    }

    public static Interessado findByCpf(Long cpf) {
        return find("cpf", cpf).firstResult();
    }

}
