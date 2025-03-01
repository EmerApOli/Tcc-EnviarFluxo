package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.dto.DocumentoDTO;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;


@ApplicationScoped
public class DocumentoService {


    @Transactional
    public Documentos SalvarDocumento(DocumentoDTO documentoDTO){

          Documentos documentos = new Documentos(documentoDTO);
          documentos.setNomearquivo(documentoDTO.getNomearquivo());
          documentos.setArquivopdf(documentoDTO.getArquivopdf());
          documentos.persistAndFlush();

 return  documentos;
    }


    public Documentos buscarId(Long id) {
        return Documentos.findById(id);
    }

}
