package org.acme.enviofluxo.external;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.acme.enviofluxo.external.DTO.DadosBasicos;


@AllArgsConstructor
@Data
@ApplicationScoped
public class DadosBasicosService {
    private DadosBasicos dadosBasicos;



    DadosBasicosService (){}

}