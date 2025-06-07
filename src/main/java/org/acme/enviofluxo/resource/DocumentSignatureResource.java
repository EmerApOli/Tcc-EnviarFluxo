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
    KafkaConfig kafkaConfig;


    @Inject
    EnvioService envioService;







    @POST
    @Path("/sign")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signDocument(MultipartFormDataInput input) throws Exception {
        // Obter o arquivo do input multipart
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> fileParts = uploadForm.get("file");
        List<InputPart> dadosEnvioGeralParts = uploadForm.get("dadosenviogeral");

        if (fileParts == null || fileParts.isEmpty() || dadosEnvioGeralParts == null || dadosEnvioGeralParts.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        EnvioDTO envioDTO = new EnvioDTO(); // Cria uma nova instância de EnvioDTO
        List<EnvioDTO.DocumentoDTO> documentoDTOS = new ArrayList<>();

        // Processar o payload JSON
        InputPart dadosEnvioGeralPart = dadosEnvioGeralParts.get(0);
        String jsonPayload = dadosEnvioGeralPart.getBodyAsString();

        // Deserializar o JSON para um objeto EnvioDTO
        ObjectMapper objectMapper = new ObjectMapper();
        EnvioDTO dadosGerais = objectMapper.readValue(jsonPayload, EnvioDTO.class);

        // Preencher envioDTO com dados gerais e documentos
        envioDTO.setDocumentoDTOS(new ArrayList<>());

// Processar cada arquivo
        for (InputPart inputPart : fileParts) {
            // Ler o arquivo
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] pdfBytes = inputStream.readAllBytes(); // Lê todos os bytes do InputStream

            // Extrair o nome do arquivo
            String fileName = inputPart.getHeaders().get("Content-Disposition").get(0);
            String extractedFileName = extractFileName(fileName);

            // Criar DocumentoDTO
            EnvioDTO.DocumentoDTO documentoDTO = new EnvioDTO.DocumentoDTO();
            documentoDTO.setNomeDocumento(extractedFileName);

            // Adicionar interessados do dadosGerais ao documentoDTO
            if (dadosGerais.getDocumentoDTOS() != null && !dadosGerais.getDocumentoDTOS().isEmpty()) {
                for (EnvioDTO.DocumentoDTO doc : dadosGerais.getDocumentoDTOS()) {
                    if (doc.getInteressadoDTO() != null) {
                        documentoDTO.setInteressadoDTO(doc.getInteressadoDTO());
                    }
                }
            }

            // Adicionar documentoDTO à lista
            documentoDTOS.add(documentoDTO);
        }

// Preencher envioDTO com os documentos processados
        envioDTO.setDocumentoDTOS(documentoDTOS);
        // Adicione outros dados gerais ao envioDTO se necessário
        // Por exemplo, se houver outros atributos no EnvioDTO que você quiser preencher:
        // envioDTO.setAlgumaInformacao(dadosGerais.getAlgumaInformacao());

        // Chamar o serviço para salvar o envio
        EnvioFluxo envioFluxo = envioService.saveEnvio(envioDTO);

        // Retornar resposta adequada
        return Response.ok().entity("Documentos processados e enviados com sucesso: " + envioFluxo.getId()).build();
    }

    // Método auxiliar para extrair o nome do arquivo
    private String extractFileName(String contentDisposition) {
        String[] parts = contentDisposition.split(";");
        for (String part : parts) {
            if (part.trim().startsWith("filename")) {
                String fileName = part.substring(part.indexOf("=") + 2, part.length() - 1); // Remove aspas
                return fileName;
            }
        }
        return null;
}}






