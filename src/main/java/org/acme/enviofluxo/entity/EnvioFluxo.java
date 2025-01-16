package org.acme.enviofluxo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.acme.enviofluxo.DTO.DadosBasicos;

@AllArgsConstructor
@Data
public class EnvioFluxo {

    private  long id;
    private  Documentos documentos;
    private  Interessado interessado;
    private DadosBasicos dadosBasicos;


}
