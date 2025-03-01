package org.acme.enviofluxo.dto;


import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DocumentoDTO {

    private String  nomearquivo;


    private byte[]  arquivopdf;


}
