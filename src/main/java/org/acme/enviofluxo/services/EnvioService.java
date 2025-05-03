package org.acme.enviofluxo.services;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.dto.*;
import org.acme.enviofluxo.entity.*;
import org.modelmapper.ModelMapper;

import java.util.Arrays;

@ApplicationScoped
public class EnvioService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Inject
    private InteressadoService interessadoService;

    @Inject
    private DadosBasicosService dadosBasicosService;

    @Inject
    SeloService seloService;


    @Inject
    private DocumentoService documentosService; // ou o serviço que você usar para Documentos

    @Transactional
    public void processarEnvio(EnvioDTO envioDTO,  SeloDTO seloDTO, byte[] documentHash, Documentos documentSaved, String idAleatorio) throws Exception {
        // Criar e salvar DadosBasicos
        DadosBasicos dadosBasicos = modelMapper.map(envioDTO.getDadosBasicosDTO(), DadosBasicos.class);

        dadosBasicosService.create(dadosBasicos);


        EnvioFluxo envioFluxo = new EnvioFluxo();
        envioFluxo.setIdfluxo(idAleatorio);
        envioFluxo.setDocumenthash(Arrays.toString(documentHash)); // Ajuste conforme necessário
        envioFluxo.setStatus("iniciado");
        envioFluxo.setDadosBasicos(dadosBasicos);

        // Persistir EnvioFluxo
        envioFluxo.persist();




        // Processar cada item
        for (ItemDTO item : envioDTO.getItens()) {
            Interessado interessado = modelMapper.map(item.getInteressadoDTO(), Interessado.class);
            Selo selo =  modelMapper.map(item.getInteressadoDTO().getSeloDTO(), Selo.class);
            seloService.salvarSelo(selo);
            interessado.setIdenviofluxo(idAleatorio); // Setar o ID aleatório
            interessado.setDocumento(documentSaved);
            interessado.setSelo(selo);// Associar o documento salvo
            interessadoService.SalvarInteressado(interessado);
            ItemEnvioFluxo itemEnvioFluxo = new ItemEnvioFluxo(envioFluxo, interessado);
            itemEnvioFluxo.persist();
        }





    }
}