package org.acme.KafkaConfig;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class KafkaConfig {

    @Inject
    @Channel("envio-fluxo")
    Emitter<String> dadosBasicosEmitter;

    public void sendMessage(String message) {
        dadosBasicosEmitter.send(message);
    }


}
