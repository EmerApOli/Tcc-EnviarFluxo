package org.acme.KafkaConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import org.acme.enviofluxo.DTO.DadosBasicos;
import org.acme.enviofluxo.services.DadosBasicosService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class KafkaConfig {


     @Inject
    DadosBasicosService dadosBasicosService;

    @Inject
    @Channel("envia-fluxo-compensation")
    Emitter<DadosBasicos> compensationEmitter;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Incoming("envia-fluxo")
    public void consume(String message) {
        try {
            // Deserializando a mensagem JSON para o DTO
            DadosBasicos dadosBasicos = processMessage(message);
            // Armazena os dados no serviço
            dadosBasicosService.setDadosBasicos(dadosBasicos);

            // Processar a mensagem recebida
            System.out.println("Mensagem recebida: " + dadosBasicos);
            handleCompensation(dadosBasicos,"Simulando enviar erro ao iniciar");

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

    private void handleCompensation(DadosBasicos dadosBasicos, String errorMessage) {
        System.err.println(errorMessage);
        // Enviando a mensagem original para o tópico de compensação
        compensationEmitter.send(dadosBasicos);
    }



    }


