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

import org.acme.enviofluxo.dto.EnvioDTO;
import org.acme.enviofluxo.dto.InteressadoDTO;
import org.acme.enviofluxo.entity.Interessado;
import org.acme.enviofluxo.external.DTO.DadosBasicos;
import org.acme.enviofluxo.blockchainservice.Blockchain;
import org.acme.enviofluxo.rsaassinarservice.PDFService;
import org.acme.enviofluxo.rsaassinarservice.RSAService;
import org.acme.enviofluxo.rsaassinarservice.SignatureResponse;
import org.acme.enviofluxo.external.DadosBasicosService;
import org.acme.enviofluxo.services.DocumentoService;
import org.acme.enviofluxo.services.EnvioService;
import org.acme.enviofluxo.services.InteressadoService;
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
    PDFService pdfService;

    @Inject
    EnvioService envioService;

    @Inject
    private InteressadoService interessadoService;

    @Inject
    private DocumentoService documentoService;

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

            try (FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\DELL\\OneDrive\\Documentos\\documento_assinado.pdf"))) {
                fos.write(signedPdf);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Registrar a assinatura na blockchain
            String dataToStore = "Hash: " + Base64.getEncoder().encodeToString(documentHash) + ", Signature: " + signature;
            blockchain.addBlock(dataToStore);


            // Processar o payload JSONInputPart payloadPart = uploadForm.get("dadosbasicos").get(0);
         //   String payloadJson = payloadPart.getBodyAsString();
         //   ObjectMapper objectMapper = new ObjectMapper();

            blockchain.addBlock(dataToStore);
            List<InputPart> interessadosParts = uploadForm.get("interessados");


        //   if (interessadosParts != null && !interessadosParts.isEmpty()) {
               InputPart interessadosPart = interessadosParts.get(0);
               String interessadosJson = interessadosPart.getBodyAsString();

              ObjectMapper objectMapper = new ObjectMapper();
                InteressadoDTO interessadoDTO = objectMapper.readValue(interessadosJson, InteressadoDTO.class);
     //      }



            DadosBasicos dadosBasicos = dadosBasicosService.getDadosBasicos();

           if( dadosBasicos == null){

               LOG.info("Não existe informação  de Dados para gravar" + dadosBasicos);

                throw  new Exception("Erro ao tentar gravar os dados");

            }
            //sendKafkaMessage(dadosBasicos);
          LOG.info("Dados para gravar" + dadosBasicos);

            Interessado interessado = interessadoService.buscarPorCpf(interessadoDTO.getCpf());

            EnvioDTO envioDTO =  new EnvioDTO();

            envioDTO.setDocumenthash(Arrays.toString(documentHash));
            envioDTO.setInteressado(interessado);
            envioDTO.setIdiniciofluxo(dadosBasicos.getId());




            envioService.InseerirEnvio(envioDTO);



            // Criar resposta
            SignatureResponse response = new SignatureResponse();
            response.setSignature(signature);
            response.setSignedPdf(Base64.getEncoder().encodeToString(signedPdf));
            response.setPublicKey(Base64.getEncoder().encodeToString(rsaService.getPublicKey().getEncoded()));
            response.setMessage(" Sua solicitação foi gerada, favor aguardar um email para fazer assinatura.");


            // to-do fazer a inserção na base



            return Response.ok(response.getMessage()).build();

        } catch (Exception e) {
            LOG.error("Erro no processo de assinatura", e);
            LOG.info("Enviando solicitação de cancelamento para o Orquestrador");


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
