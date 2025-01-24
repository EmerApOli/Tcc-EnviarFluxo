package org.acme.enviofluxo.dto;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.acme.enviofluxo.entity.Interessado;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class InteressadoDTO implements Serializable {

    private  Long cpf;

    private  String nome;

    private  String descricao;

    private String  cargo;

    private  Long idiniciofluxo;


    public  InteressadoDTO(Interessado interessado){

        this.cpf = interessado.getCpf();
        this.nome = interessado.getNome();
        this.descricao = interessado.getDescricao();
        this.cargo = interessado.getCargo();



    }

    InteressadoDTO(){}

}
