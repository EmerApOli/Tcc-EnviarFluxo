package org.acme.enviofluxo.dto;

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

    private DadosBasicosDTO dadosBasicosDTO;
    private List<ItemDTO> itens;
}