package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.dto.InteressadoDTO;
import org.acme.enviofluxo.entity.EnvioFluxo;
import org.acme.enviofluxo.entity.Interessado;


@ApplicationScoped
public class EnvioService {

    @Transactional
    public EnvioFluxo InseerirEnvio(EnvioDTO envioDTO) {
        try {
            EnvioFluxo envioFluxo = new EnvioFluxo(envioDTO);
            envioFluxo.persist();

            return envioFluxo;
        } catch (Exception e) {
            // Tratar e registrar o erro
            throw new RuntimeException("Erro ao cadastrar Envio", e);
        }
    }





}

