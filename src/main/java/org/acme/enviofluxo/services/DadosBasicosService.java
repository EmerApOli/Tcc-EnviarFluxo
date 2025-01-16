package org.acme.enviofluxo.services;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.acme.enviofluxo.DTO.DadosBasicos;


@AllArgsConstructor
@Data
@ApplicationScoped
public class DadosBasicosService {
    private DadosBasicos dadosBasicos;



    DadosBasicosService (){}

}