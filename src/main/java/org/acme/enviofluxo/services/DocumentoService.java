package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;


@ApplicationScoped
public class DocumentoService {


    public Documentos buscarPorCpf(Long id) {
        return Documentos.findById(id);
    }

}
