package org.acme.enviofluxo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentosResumoDTO {
    private Long envioFluxoId;
    private Long cpf;
    private String url;
    private String provedor;
    private String nomeInteressado;
}
