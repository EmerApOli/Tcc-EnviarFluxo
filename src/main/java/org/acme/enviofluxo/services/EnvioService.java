package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.KafkaConfig.KafkaConfig;
import org.acme.enviofluxo.dto.DocumentosResumoDTO;
import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.EnvioFluxo;
import org.acme.enviofluxo.entity.Interessado;
import org.acme.enviofluxo.entity.Selo;
import org.acme.enviofluxo.repository.EnvioRepositry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;





@ApplicationScoped
public class EnvioService {


    @Inject
    KafkaConfig kafkaConfig;
    @Inject
    EnvioRepositry envioFluxoRepository; // Repositório para EnvioFluxo

    @Inject
    DocumentoService documentoService;
    @Transactional
    public EnvioFluxo saveEnvio(EnvioDTO envioDTO) {
        EnvioFluxo envioFluxo = new EnvioFluxo();
        envioFluxo.setDocumenthash("hash-do-documento");
        envioFluxo.setStatus("Pendente");
        //envioFluxo.setProvedor(envioDTO.getProvedor());
        String baseUrl = "/app/pdfs";
        List<Documentos> documentosList = new ArrayList<>();
        Set<Long> cpfJaAdicionados = new HashSet<>(); // Para evitar duplicação de CPFs
        envioFluxoRepository.persist(envioFluxo);
        for (EnvioDTO.DocumentoDTO documentoDTO : envioDTO.getDocumentoDTOS()) {
            Documentos documento = new Documentos();
            documento.setNomearquivo(documentoDTO.getNomeDocumento());
            documento.setProvedor(documentoDTO.getProvedor());
            documento.setEnviofluxo(envioFluxo);
            documento.setUrldocumento(baseUrl);

            List<Interessado> interessadosList = new ArrayList<>();
            for (EnvioDTO.InteressadoDTO interessadoDTO : documentoDTO.getInteressadoDTO()) {
                // Verifica se o interessado já foi adicionado pelo CPF
                if (!cpfJaAdicionados.contains(interessadoDTO.getCpf())) {
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

                    // Associa o interessado ao documento
                    interessado.setDocumentos(documento);

                    // Adiciona o interessado à lista
                    interessadosList.add(interessado);
                    cpfJaAdicionados.add(interessadoDTO.getCpf()); // Marca o CPF como adicionado

                    // Envia a mensagem para Kafka
                    DocumentosResumoDTO documentosResumoDTO = new DocumentosResumoDTO();
                    documentosResumoDTO.setEnvioFluxoId(envioFluxo.getId()); // Supondo que você tenha um método getId()
                    documentosResumoDTO.setCpf(interessadoDTO.getCpf());
                    documentosResumoDTO.setUrl(baseUrl + "/" + documento.getNomearquivo()); // URL completa
                    documentosResumoDTO.setProvedor(documento.getProvedor());
                    documentosResumoDTO.setNomeInteressado(interessado.getNome());
                    documentosResumoDTO.setNomedoc(documento.getNomearquivo().replace("\"", "").replace("'", ""));
                    documentosResumoDTO.setStatuspedido(envioFluxo.getStatus());
                    kafkaConfig.sendMessage(documentosResumoDTO);



                }
            }

            // Associa a lista de interessados ao documento
            documento.setInteressados(interessadosList);
            documentosList.add(documento);
        }

        // Associa a lista de documentos ao EnvioFluxo
        envioFluxo.setListadocumento(documentosList);

        // Persiste o EnvioFluxo






        return envioFluxo;
}}