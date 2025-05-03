package org.acme.enviofluxo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.acme.enviofluxo.entity.DadosBasicos;
import org.acme.enviofluxo.entity.ItemEnvioFluxo;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor // Gera um construtor sem parâmetros
public class EnvioDTO {

    @JsonProperty("dadosBasicosDTO")
    private DadosBasicosDTO dadosBasicosDTO;
    @JsonProperty("itens")
    private List<ItemDTO> itens;
}