package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.acme.enviofluxo.dto.DocumentosResumoDTO;
import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;
import org.acme.enviofluxo.repository.DocumentosRepositoty;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class DocumentoService {


    @Inject
    DocumentosRepositoty documentosRepositoty;
    public List<DocumentosResumoDTO> findDocumentsByCpfAndProvider(String cpf, String provider) {
        List<Documentos> documentos = documentosRepositoty.findDocumentsByCpfAndProvider(cpf, provider);
        return convertToResumoDTO(documentos); // Chama o método de conversão
    }

    public Documentos buscarId(Long id) {
        return Documentos.findById(id);
    }


    public List<DocumentosResumoDTO> convertToResumoDTO(List<Documentos> documentos) {
        List<DocumentosResumoDTO> resumoList = new ArrayList<>();

        for (Documentos documento : documentos) {
            for (Interessado interessado : documento.getInteressados()) {
                DocumentosResumoDTO resumo = new DocumentosResumoDTO();
                resumo.setEnvioFluxoId(documento.getEnvioFluxoId());
                resumo.setCpf(String.valueOf(interessado.getCpf()));
                resumo.setUrl(documento.getUrldocumento());
                resumo.setProvedor(documento.getProvedor());
                resumo.setNomeInteressado(interessado.getNome());
                resumoList.add(resumo);
            }
        }

        return resumoList;
    }

}
