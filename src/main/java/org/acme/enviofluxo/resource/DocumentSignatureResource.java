package org.acme.enviofluxo.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.KafkaConfig.KafkaConfig;
import org.acme.enviofluxo.dto.*;
import org.acme.enviofluxo.blockchainservice.Blockchain;
import org.acme.enviofluxo.entity.DadosBasicos;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.EnvioFluxo;
import org.acme.enviofluxo.entity.Interessado;
import org.acme.enviofluxo.external.DTO.EnvioPandasDTO;
import org.acme.enviofluxo.rsaassinarservice.PDFService;
import org.acme.enviofluxo.rsaassinarservice.RSAService;
import org.acme.enviofluxo.rsaassinarservice.SignatureResponse;
import org.acme.enviofluxo.services.DadosBasicosService;
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
    KafkaConfig kafkaConfig;


    @Inject
    InteressadoService interessadoService;

    @Inject
    PDFService pdfService;

    @Inject
    EnvioService envioService;

     @Inject
     DocumentoService documentoService;




    @POST
    @Path("/sign")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signDocument(MultipartFormDataInput input) {
        try {
            LOG.info("Iniciando processo de armazenamento do documento");

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

            // Processar o documento e gerar o hash
            byte[] documentHash = pdfService.getDocumentHash(pdfBytes);
            LOG.info("Hash do documento gerado com sucesso.");

            // Salvar o PDF em um diretório específico
            String outputDirectory = "C:\\Users\\DELL\\OneDrive\\Documentos\\DocumentoDestino\\";
            String outputFilePath = outputDirectory + "documento_extraido.pdf";



            // Criar um DocumentoDTO
            DocumentoDTO documentoDTO = new DocumentoDTO();
            documentoDTO.setNomearquivo("documento_extraido.pdf"); // ou qualquer nome apropriado
            documentoDTO.setArquivopdf(pdfBytes);

            Documentos documentSaved = documentoService.SalvarDocumento(documentoDTO);


            // Processar o documento

         //   String signature = rsaService.signDocument(documentHash);
         //   byte[] signedPdf = pdfService.addSignatureToDocument(pdfBytes, signature);

            try (FileOutputStream fos = new FileOutputStream(new File(outputFilePath))) {
                fos.write(pdfBytes);
            } catch (IOException e) {
                LOG.info("Arquivo PDF salvo com sucesso em: " +documentSaved);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(createErrorResponse("Error saving file"))
                        .build();
            }

            // Registrar a assinatura na blockchain
          //  String dataToStore = "Hash: " + Base64.getEncoder().encodeToString(documentHash) + ", Signature: " + signature;
          //  blockchain.addBlock(dataToStore);


            // Processar o payload JSONInputPart payloadPart = uploadForm.get("dadosbasicos").get(0);
         //   String payloadJson = payloadPart.getBodyAsString();
         //   ObjectMapper objectMapper = new ObjectMapper();

          //  blockchain.addBlock(dataToStore);
            List<InputPart> dadosenviogeralParts = uploadForm.get("dadosenviogeral");




        //   if (interessadosParts != null && !interessadosParts.isEmpty()) {
               InputPart dadosenviogeralPart = dadosenviogeralParts.get(0);
               String interessadosJson = dadosenviogeralPart.getBodyAsString();

              ObjectMapper objectMapper = new ObjectMapper();
              DadosEnvioGeralDTO  dadosEnvioGeralDTO = objectMapper.readValue(interessadosJson, DadosEnvioGeralDTO.class);
     //      }


            String idAleatorio = UUID.randomUUID().toString();

              InteressadoDTO  interessadobandoDTO = new InteressadoDTO();

            interessadobandoDTO.setCpf(dadosEnvioGeralDTO.getInteressadoDTO().getCpf());
            interessadobandoDTO.setNome(dadosEnvioGeralDTO.getInteressadoDTO().getNome());
            interessadobandoDTO.setDescricao(dadosEnvioGeralDTO.getInteressadoDTO().getDescricao());
            interessadobandoDTO.setCargo(dadosEnvioGeralDTO.getInteressadoDTO().getCargo());
            interessadobandoDTO.setIdenviofluxo(idAleatorio);
            interessadobandoDTO.setDocumentos(documentSaved);
            Interessado interessado =  interessadoService.SalvarInteressado(interessadobandoDTO);

            DadosBasicosDTO dadosBasicosDTO  = new DadosBasicosDTO();

            dadosBasicosDTO.setNome(dadosEnvioGeralDTO.getDadosBasicosDTO().getNome());
            dadosBasicosDTO.setDescricao(dadosEnvioGeralDTO.getDadosBasicosDTO().getDescricao());
            dadosBasicosDTO.setStatus(dadosEnvioGeralDTO.getDadosBasicosDTO().getStatus());
            dadosBasicosDTO.setTipoassinatura(dadosEnvioGeralDTO.getDadosBasicosDTO().getTipoassinatura());

            DadosBasicos dadosBasicosGravar =  dadosBasicosService.create(dadosBasicosDTO);




          //  Interessado interessado = interessadoService.buscarPorCpf(interessadoDTO.getCpf());

            EnvioDTO envioDTO =  new EnvioDTO();

            envioDTO.setDocumenthash(Arrays.toString(documentHash));
            envioDTO.setInteressado(interessado);
            envioDTO.setDadosBasicos(dadosBasicosGravar);
            envioDTO.setIdfluxo(idAleatorio);

            LOG.info("Arquivo PDF salvo com sucesso em: " + Arrays.toString(documentHash));

            // Enviar dados para Kafka
          //  String kafkaMessage = String.format("CPF: %s, Document Hash: %s",
          //          interessadobandoDTO.getCpf(),
           //         Arrays.toString(documentHash)); // Enviando o hash original como string
           //      kafkaConfig.sendMessage(  Arrays.toString(documentHash));

            EnvioPandasDTO envioPandasDTO = new EnvioPandasDTO();
              envioPandasDTO.setCpf(Long.valueOf(interessadobandoDTO.getCpf()));
              envioPandasDTO.setDocumentHash(Arrays.toString(documentHash));
              interessadobandoDTO.setIdenviofluxo(envioDTO.getIdfluxo());
              kafkaConfig.sendMessage(envioPandasDTO);
            envioService.InseerirEnvio(envioDTO);





            // Criar resposta
            SignatureResponse response = new SignatureResponse();
          //  response.setSignature(signature);
          //  response.setSignedPdf(Base64.getEncoder().encodeToString(signedPdf));
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

    @GET
    @Path("/download/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadDocument(@PathParam("id") Long documentId) {
        try {
            // Recuperar o documento do banco de dados usando o id
            Documentos document = documentoService.buscarId(documentId);

            if (document == null || document.getArquivopdf() == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("File not found")
                        .build();
            }

            byte[] pdfBytes = document.getArquivopdf(); // Obtendo o byte[]

            // Retornar o byte[] como um InputStream
            InputStream inputStream = new ByteArrayInputStream(pdfBytes);

            // Retornar a resposta com o arquivo PDF
            return Response.ok(inputStream)
                    .header("Content-Disposition", "attachment; filename=\"documento_" + documentId + ".pdf\"")
                    .build();
        } catch (Exception e) {
            LOG.error("Erro ao tentar baixar o arquivo", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while downloading the file")
                    .build();
        }
    }


    @GET
    @Path("/download/{id}")
    @Produces(MediaType.APPLICATION_JSON)
   public Documentos BuscarDocumentos (Long id ){

        Documentos document = documentoService.buscarId(id);
        return  document;
    }


}
