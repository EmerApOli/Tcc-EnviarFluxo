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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signDocument(MultipartFormDataInput input) throws Exception {
        try {
            // Obter o arquivo do input multipart
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> fileParts = uploadForm.get("file");
            List<InputPart> dadosEnvioGeralParts = uploadForm.get("dadosenviogeral");

            if (fileParts == null || fileParts.isEmpty() || dadosEnvioGeralParts == null || dadosEnvioGeralParts.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("No file or data found in request"))
                        .build();
            }

            // Ler o arquivo
            InputPart inputPart = fileParts.get(0);
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] pdfBytes = inputStream.readAllBytes(); // Lê todos os bytes do InputStream

            // Processar o documento e gerar o hash
            byte[] documentHash = pdfService.getDocumentHash(pdfBytes);
            Documentos documentSaved = documentoService.SalvarDocumento(new DocumentoDTO("documento_extraido.pdf", pdfBytes));

            // Processar o payload JSON
            InputPart dadosEnvioGeralPart = dadosEnvioGeralParts.get(0);
            String jsonPayload = dadosEnvioGeralPart.getBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();
            EnvioDTO envioDTO = objectMapper.readValue(jsonPayload, EnvioDTO.class);

            SeloDTO   seloDTO = seloService.PegarSelo(envioDTO);
          // Selo selo =  seloService.salvarSelo(seloDTO);

            String idAleatorio = UUID.randomUUID().toString();

            // Chamar o serviço para processar o envio
            envioService.processarEnvio(envioDTO, seloDTO, documentHash, documentSaved, idAleatorio);

            // Criar resposta
            SignatureResponse response = new SignatureResponse();
            response.setMessage("Sua solicitação foi gerada, favor aguardar um email para fazer assinatura.");
            return Response.ok(response.getMessage()).build();

        } catch (
                Exception e) {
            LOG.error("Erro no processo de assinatura", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse(e.getMessage()))
                    .build();
        }
    }


    private Object createErrorResponse(String message) {
        SignatureResponse response = new SignatureResponse();
        response.setMessage("Error: " + message);
        return response;
    }





    }




   


