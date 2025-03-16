package org.acme.enviofluxo.external.DTO;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@Data
@NoArgsConstructor

public class EnvioPandasDTO {
    private Long cpf;
    private String documentHash;
    private byte[]  arquivopdf;
    private String idFluxo;
    private  String status;
}
