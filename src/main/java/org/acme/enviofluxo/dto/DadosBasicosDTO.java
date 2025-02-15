package org.acme.enviofluxo.dto;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DadosBasicosDTO {

     private Long id;

    private  String nome;
    private  String descricao;
    private  String status;
    private  String tipoassinatura;





}
