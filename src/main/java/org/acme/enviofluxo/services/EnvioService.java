package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.EnvioFluxo;
import org.acme.enviofluxo.entity.Interessado;
import org.acme.enviofluxo.entity.Selo;
import org.acme.enviofluxo.repository.EnvioRepositry;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EnvioService {

    @Inject
    EnvioRepositry envioFluxoRepository; // Repositório para EnvioFluxo
    @Transactional
    public EnvioFluxo saveEnvio(EnvioDTO envioDTO) {
        // Cria uma nova instância de EnvioFluxo
        EnvioFluxo envioFluxo = new EnvioFluxo();
        envioFluxo.setDocumenthash("hash-do-documento"); // Defina o hash do documento conforme necessário
        envioFluxo.setStatus("Pendente"); // Defina o status conforme necessário

        List<Documentos> documentosList = new ArrayList<>();

        // Percorre cada documento no EnvioDTO
        for (EnvioDTO.DocumentoDTO documentoDTO : envioDTO.getDocumentoDTOS()) {
            Documentos documento = new Documentos();
            documento.setNomearquivo(documentoDTO.getNomeDocumento());

            List<Interessado> interessadosList = new ArrayList<>();
            for (EnvioDTO.InteressadoDTO interessadoDTO : documentoDTO.getInteressadoDTO()) {
                Interessado interessado = new Interessado();
                interessado.setCpf(interessadoDTO.getCpf());
                interessado.setNome(interessadoDTO.getNome());
                interessado.setDescricao(interessadoDTO.getDescricao());
                interessado.setCargo(interessadoDTO.getCargo());


                // Cria o selo e associa ao interessado
                Selo selo = new Selo();
                selo.setPagina(interessadoDTO.getSeloDTO().getPagina());
                selo.setX(interessadoDTO.getSeloDTO().getX());
                selo.setY(interessadoDTO.getSeloDTO().getY());
                selo.setLargura(interessadoDTO.getSeloDTO().getLargura());
                selo.setAltura(interessadoDTO.getSeloDTO().getAltura());
                interessado.setSelo(selo);
                interessado.setDocumentos(documento);

                // Adiciona o interessado à lista
                interessadosList.add(interessado);
            }

            // Associa a lista de interessados ao documento
            documento.setInteressados(interessadosList);
            documento.setEnviofluxo(envioFluxo); // Associa o documento ao EnvioFluxo

            // Adiciona o documento à lista de documentos
            documentosList.add(documento);
        }

        // Associa a lista de documentos ao EnvioFluxo
        envioFluxo.setListadocumento(documentosList);

        // Persiste o EnvioFluxo
        envioFluxoRepository.persist(envioFluxo);

        return envioFluxo; // Retorna o objeto persistido
    }
}