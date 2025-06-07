package org.acme.enviofluxo.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DadosEnvioGeralDTO {


    private DadosBasicosDTO  dadosBasicosDTO;

}
