package org.acme.enviofluxo.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import org.acme.KafkaConfig.KafkaConfig;
import org.acme.enviofluxo.dto.*;
import org.acme.enviofluxo.blockchainservice.Blockchain;
import org.acme.enviofluxo.entity.*;
import org.acme.enviofluxo.external.DTO.EnvioPandasDTO;
import org.acme.enviofluxo.rsaassinarservice.PDFService;
import org.acme.enviofluxo.rsaassinarservice.RSAService;
import org.acme.enviofluxo.rsaassinarservice.SignatureResponse;
import org.acme.enviofluxo.services.*;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.*;
import java.util.*;

@Path("/document")
@ApplicationScoped
public class DocumentSignatureResource {

    private static final Logger LOG = Logger.getLogger(DocumentSignatureResource.class);

    @Inject
    RSAService rsaService;
    @Inject
    Blockchain blockchain;

    @Inject
    DadosBasicosService dadosBasicosService;

    @Inject
    KafkaConfig kafkaConfig;


    @Inject
    InteressadoService interessadoService;

    @Inject
    PDFService pdfService;

    @Inject
    EnvioService envioService;

    @Inject
    DocumentoService documentoService;

    @Inject
    SeloService seloService;


    @POST
    @Path("/sign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signDocument(EnvioDTO envioDTO) throws Exception {

        EnvioFluxo envioFluxo = envioService.saveEnvio(envioDTO);
        return Response.ok(envioFluxo).build(); // Retorna o objeto persistido

    }}




   


