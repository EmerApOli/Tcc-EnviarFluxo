package org.acme.enviofluxo.dto;


import jakarta.persistence.Entity;
import lombok.*;
import org.acme.enviofluxo.entity.DadosBasicos;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DadosEnvioGeralDTO {


    private DadosBasicosDTO  dadosBasicosDTO;
    private  InteressadoDTO interessadoDTO;
}
