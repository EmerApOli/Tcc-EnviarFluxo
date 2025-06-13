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
import org.acme.enviofluxo.repository.SeloRepository;
import org.acme.enviofluxo.rsaassinarservice.PDFService;
import org.acme.enviofluxo.rsaassinarservice.RSAService;
import org.acme.enviofluxo.rsaassinarservice.SignatureResponse;
import org.acme.enviofluxo.services.*;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Path("/document/")
@ApplicationScoped
public class DocumentSignatureResource {

    private static final Logger LOG = Logger.getLogger(DocumentSignatureResource.class);




    @Inject
    KafkaConfig kafkaConfig;


    @Inject
    EnvioService envioService;

    @Inject
    DocumentoService documentoService;


    @Inject
    SeloService seloService;

    @POST
    @Path("sign")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signDocument(MultipartFormDataInput input) throws Exception {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> fileParts = uploadForm.get("file");
        List<InputPart> dadosEnvioGeralParts = uploadForm.get("dadosenviogeral");

        if (fileParts == null || fileParts.isEmpty() || dadosEnvioGeralParts == null || dadosEnvioGeralParts.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        EnvioDTO envioDTO = new EnvioDTO();
        List<EnvioDTO.DocumentoDTO> documentoDTOS = new ArrayList<>();

        // Processar o payload JSON
        InputPart dadosEnvioGeralPart = dadosEnvioGeralParts.get(0);
        String jsonPayload = dadosEnvioGeralPart.getBodyAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        EnvioDTO dadosGerais = objectMapper.readValue(jsonPayload, EnvioDTO.class);

        // Verifica se o número de arquivos e o número de interessados são compatíveis
        if (fileParts.size() != dadosGerais.getDocumentoDTOS().size()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O número de arquivos deve corresponder ao número de documentos.").build();
        }

        // Processar cada arquivo e associar ao respectivo interessado
        String outputDirectory = null;
        for (int i = 0; i < fileParts.size(); i++) {
            InputPart inputPart = fileParts.get(i);
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] pdfBytes = inputStream.readAllBytes();

            String fileName = inputPart.getHeaders().get("Content-Disposition").get(0);
            String extractedFileName = extractFileName(fileName);
            outputDirectory = "/app/pdfs/";
            // Gravar o arquivo no diretório /app/pdfs
            // Gravar o arquivo no diretório /app/pdfs
            java.nio.file.Path filePath = Paths.get(outputDirectory, extractedFileName.replace("\"", "").replace("'", "")); // Alterado para Paths.get()
            Files.write(filePath, pdfBytes);


            // Criar DocumentoDTO
            EnvioDTO.DocumentoDTO documentoDTO = dadosGerais.getDocumentoDTOS().get(i); // Obter o documento correspondente
            documentoDTO.setNomeDocumento(extractedFileName); // Atualiza o nome do documento
            documentoDTO.setUrldoc(outputDirectory);
            // Adicionar documentoDTO à lista
            documentoDTOS.add(documentoDTO);
        }

        envioDTO.setDocumentoDTOS(documentoDTOS);

        EnvioFluxo envioFluxo = envioService.saveEnvio(envioDTO);

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
        return null; // Retorna null se não encontrar o nome do arquivo
    }


    @GET
    @Path("/por-cpf")
    @Produces("application/json")
    public Response getDocumentosPorCpf(@QueryParam("cpf") Long cpf) {
        List<DocumentosResumoDTO> documentos = (List<DocumentosResumoDTO>) documentoService.findDocumentsByCpfAndProvider(cpf, "govbr");
        return Response.ok(documentos).build();

    }
    @GET
    @Path("/selo")
    @Produces("application/json")
    public  Response buscarSelo(){
        List<Selo> selo = seloService.buscarTodos();

        return  Response.ok(selo).build();

    }


}









