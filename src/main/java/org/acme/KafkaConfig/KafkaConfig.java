package org.acme.KafkaConfig;
import com.google.gson.Gson;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.acme.enviofluxo.dto.DocumentosResumoDTO;
import org.acme.enviofluxo.external.DTO.EnvioPandasDTO;

@ApplicationScoped
public class KafkaConfig {

    @Inject
    @Channel("envio-fluxo")
    Emitter<String> dadosBasicosEmitter;

    private final Gson gson = new Gson(); // Inicializa o Gson

    public void sendMessage(DocumentosResumoDTO documentosResumoDTO) {
        String message = gson.toJson(documentosResumoDTO); // Converte o objeto em JSON
        dadosBasicosEmitter.send(message); // Envia a mensagem JSON
      System.out.print(message);
    }
}
