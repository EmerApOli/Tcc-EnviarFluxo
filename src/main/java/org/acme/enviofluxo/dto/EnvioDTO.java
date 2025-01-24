package org.acme.enviofluxo.dto;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.enviofluxo.entity.Interessado;

@AllArgsConstructor
//@NoArgsConstructor
@Data
public class EnvioDTO {

    private  Long   id;
    private  String documenthash;

    private Long idiniciofluxo;
    private  Interessado interessado;


    public  EnvioDTO( ){

    }
}
