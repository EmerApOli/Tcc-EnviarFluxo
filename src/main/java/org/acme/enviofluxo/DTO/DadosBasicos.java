package org.acme.enviofluxo.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DadosBasicos {


    private Long id;
    private  String nome;
    private  String descricao;
    private  String status;
    private  String tipoassinatura;


    public DadosBasicos() {


    }

}
