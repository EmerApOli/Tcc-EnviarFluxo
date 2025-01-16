package org.acme.KafkaConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.acme.enviofluxo.DTO.DadosBasicos;
import org.acme.enviofluxo.services.DadosBasicosService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class KafkaConfig {


     @Inject
    DadosBasicosService dadosBasicosService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Incoming("envia-fluxo")
    public void consume(String message) {
        try {
            // Deserializando a mensagem JSON para o DTO

            // Chama o método que processa a mensagem
            DadosBasicos dadosBasicos = processMessage(message);
            // Armazena os dados no serviço
            dadosBasicosService.setDadosBasicos(dadosBasicos);

            // Processar a mensagem recebida
            System.out.println("Mensagem recebida: " + dadosBasicos);
            // Aqui você pode implementar a lógica que deseja aplicar ao objeto dadosBasicos


        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private DadosBasicos processMessage(String message) throws JsonMappingException, JsonProcessingException {
        // Deserializando a mensagem JSON para o DTO
        return objectMapper.readValue(message, DadosBasicos.class);
    }


}