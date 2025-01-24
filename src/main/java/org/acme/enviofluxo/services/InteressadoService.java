package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.enviofluxo.entity.Interessado;


@ApplicationScoped
public class InteressadoService {


    public Interessado buscarPorCpf(Long cpf) {
        return Interessado.findByCpf(cpf);
    }

}
