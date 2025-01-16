package org.acme.enviofluxo.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.KafkaConfig.KafkaConfig;
import org.acme.enviofluxo.DTO.DadosBasicos;
import org.acme.enviofluxo.blockchainservice.Blockchain;
import org.acme.enviofluxo.rsaassinarservice.PDFService;
import org.acme.enviofluxo.rsaassinarservice.RSAService;
import org.acme.enviofluxo.rsaassinarservice.SignatureResponse;
import org.acme.enviofluxo.services.DadosBasicosService;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hibernate.sql.results.LoadingLogger.LOGGER;

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
    PDFService pdfService;

    @POST
    @Path("/sign")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signDocument(MultipartFormDataInput input) {
        try {
            LOG.info("Iniciando processo de assinatura");

            String idfluxo = UUID.randomUUID().toString();


            // Obter o arquivo do input multipart
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");

            if (inputParts == null || inputParts.isEmpty()) {
                LOG.error("Nenhum arquivo encontrado no request");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("No file found in request"))
                        .build();
            }

            InputPart inputPart = inputParts.get(0);
            InputStream inputStream = inputPart.getBody(InputStream.class, null);

            if (inputStream == null) {
                LOG.error("InputStream é nulo");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Failed to read file"))
                        .build();
            }

            // Ler o conteúdo do arquivo
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] pdfBytes = baos.toByteArray();

            LOG.info("Arquivo lido com sucesso. Tamanho: " + pdfBytes.length + " bytes");

            // Processar o documento
            byte[] documentHash = pdfService.getDocumentHash(pdfBytes);
            String signature = rsaService.signDocument(documentHash);
            byte[] signedPdf = pdfService.addSignatureToDocument(pdfBytes, signature);

            // Registrar a assinatura na blockchain
            String dataToStore = "Hash: " + Base64.getEncoder().encodeToString(documentHash) + ", Signature: " + signature;
            blockchain.addBlock(dataToStore);


            // Processar o payload JSON
           // InputPart payloadPart = uploadForm.get("dadosbasicos").get(0);
         //   String payloadJson = payloadPart.getBodyAsString();
         //   ObjectMapper objectMapper = new ObjectMapper();
            DadosBasicos dadosBasicos = dadosBasicosService.getDadosBasicos();
           //  sendKafkaMessage(dadosBasicos);

            // Criar resposta
            SignatureResponse response = new SignatureResponse();
            response.setSignature(signature);
            response.setSignedPdf(Base64.getEncoder().encodeToString(signedPdf));
            response.setPublicKey(Base64.getEncoder().encodeToString(rsaService.getPublicKey().getEncoded()));
            response.setMessage("Document signed successfully and recorded in blockchain.");

            return Response.ok(response).build();

        } catch (Exception e) {
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

    private void sendKafkaMessage(DadosBasicos dadosBasicos) {
        // Geração da mensagem
        String message = String.format("id: %d, nome: %s, descrição: %s, status: %s, Tipo: %s",
                dadosBasicos.getId(),  // Incluindo o ID aqui
                dadosBasicos.getNome(),
                dadosBasicos.getDescricao(),
                dadosBasicos.getStatus(),
                dadosBasicos.getTipoassinatura());
        try {
            // Enviando a mensagem para Kafka
            kafkaConfig.consume(message);
        } catch (Exception e) {
            LOGGER.error("Failed to send message to Kafka: {}", message, e);
            // Aqui você pode optar por lançar uma exceção ou registrar o erro conforme necessário
        }
    }
}
