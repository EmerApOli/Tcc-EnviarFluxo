package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;


@ApplicationScoped
public class DocumentoService {





    public Documentos buscarId(Long id) {
        return Documentos.findById(id);
    }

}
