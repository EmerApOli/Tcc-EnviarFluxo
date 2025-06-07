package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;
import org.acme.enviofluxo.repository.DocumentosRepositoty;

import java.util.List;


@ApplicationScoped
public class DocumentoService {


    @Inject
    DocumentosRepositoty documentosRepositoty;
    public List<Documentos> findDocumentsByCpfAndProvider(String cpf, String provider) {
        return   documentosRepositoty.findDocumentsByCpfAndProvider(cpf,provider);
    }

    public Documentos buscarId(Long id) {
        return Documentos.findById(id);
    }

}
