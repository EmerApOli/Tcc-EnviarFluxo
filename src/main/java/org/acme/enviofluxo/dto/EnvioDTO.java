package org.acme.enviofluxo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EnvioDTO {
    @JsonProperty("provedor")
    private  String provedor;
    @JsonProperty("documentoDTOS")
    private List<DocumentoDTO> documentoDTOS;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DocumentoDTO {

        private  String provedor;
        private String nomeDocumento; // Corrigido para 'nomeDocumento'
        private List<InteressadoDTO> interessadoDTO;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InteressadoDTO {
        private Long cpf;
        private String nome;
        private String descricao;
        private String cargo;
        private SeloDTO seloDTO;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SeloDTO {
        private Long id;
        private Long pagina;
        private Long x;
        private Long y;
        private Long largura;
        private Long altura;
    }
}