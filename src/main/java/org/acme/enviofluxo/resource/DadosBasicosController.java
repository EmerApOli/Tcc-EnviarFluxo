package org.acme.enviofluxo.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.enviofluxo.dto.DadosBasicosDTO;
import org.acme.enviofluxo.entity.DadosBasicos;

import org.acme.enviofluxo.services.DadosBasicosService;
import org.acme.enviofluxo.services.InteressadoService;


import static org.hibernate.sql.results.LoadingLogger.LOGGER;

@Path("/dados-basicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DadosBasicosController {

    @Inject
    DadosBasicosService dadosBasicosService;




    @POST
    public Response createDadosBasicos( DadosBasicosDTO dadosBasicosDTO) throws Exception {
        DadosBasicos created = dadosBasicosService.create(dadosBasicosDTO);


        // Obter o ID gerado
        Long idGerado = created.getId();

        String message = String.format("Nome: %s, Descrição: %s, Status: %s, TipoAssinatura: %s, Id: %d",
                dadosBasicosDTO.getNome(),
                dadosBasicosDTO.getDescricao(),
                dadosBasicosDTO.getStatus(),
                dadosBasicosDTO.getTipoassinatura(),
                idGerado); // Aqui estamos passando o idGerado corretamente
      //  kafkaConfig.sendMessage(message);

        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Response getDadosBasicos(@PathParam("id") Long id) {
        DadosBasicos dadosBasicos = dadosBasicosService.findById(id);
        if (dadosBasicos == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(dadosBasicos).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDadosBasicos(@PathParam("id") Long id) {

            return Response.status(Response.Status.NOT_FOUND).build();

      //  dadosBasicosService.delete(id);

    }

    private void sendKafkaMessage(DadosBasicos dadosBasicos) {
        // Geração da mensagem
        String message = String.format("Nome: %s, Descrição: %s, Status: %s, Tipo: %s",
                dadosBasicos.getNome(),
                dadosBasicos.getDescricao(),
              //  dadosBasicos.getStatus(),
                dadosBasicos.getTipoassinatura());

        try {
            // Enviando a mensagem para Kafka
          //  kafkaConfig.sendMessage(message);
        } catch (Exception e) {
            LOGGER.error("Failed to send message to Kafka: {}", message, e);
            // Aqui você pode optar por lançar uma exceção ou registrar o erro conforme necessário
        }
    }
    // Outros endpoints podem ser adicionados aqui
}
