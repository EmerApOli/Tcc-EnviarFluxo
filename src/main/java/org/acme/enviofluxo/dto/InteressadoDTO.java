package org.acme.enviofluxo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;
import org.acme.enviofluxo.entity.Selo;

import java.io.Serializable;
import java.util.List;

@Data

public class InteressadoDTO  {

    private Long cpf;

    private String nome;

    private String descricao;

    private String cargo;

    private String idenviofluxo;

    private List<Documentos> documentos;
    @JsonProperty("selo")
    private SeloDTO  seloDTO;




}
