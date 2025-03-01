package org.acme.enviofluxo.dto;


import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.entity.Interessado;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class InteressadoDTO implements Serializable {

    private  String cpf;

    private  String nome;

    private  String descricao;

    private String  cargo;

    private  String idenviofluxo;




    public  InteressadoDTO(Interessado interessado){

        this.cpf = interessado.getCpf();
        this.nome = interessado.getNome();
        this.descricao = interessado.getDescricao();
        this.cargo = interessado.getCargo();
        this.idenviofluxo = interessado.getIdenviofluxo();



    }

    public InteressadoDTO(){}

}
