package org.acme.enviofluxo.external.DTO;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Embeddable
@AllArgsConstructor
@Data
public class DadosBasicos  implements Serializable {

    private  static  final Long serialVersionUID = 1L;


    private Long id;
    private  String nome;
    private  String descricao;
    private  String status;
    private  String tipoassinatura;


    public DadosBasicos() {


    }

}
