package org.acme.enviofluxo.dto;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.enviofluxo.entity.DadosBasicos;
import org.acme.enviofluxo.entity.Interessado;

@AllArgsConstructor
//@NoArgsConstructor
@Data
public class EnvioDTO {

    private  Long   id;
    private  String documenthash;
    private  String idfluxo;
    private DadosBasicos dadosBasicos;
    private  Interessado interessado;
    private  String status;


    public  EnvioDTO( ){

    }
}
