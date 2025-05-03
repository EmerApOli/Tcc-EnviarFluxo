package org.acme.enviofluxo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemDTO {
    @JsonProperty("interessadoDTO")
    private InteressadoDTO interessadoDTO;
}