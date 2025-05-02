package org.acme.enviofluxo.dto;


import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;

import java.io.Serializable;

@Data

public class InteressadoDTO  {

    private String cpf;

    private String nome;

    private String descricao;

    private String cargo;

    private String idenviofluxo;

    private Documentos documentos;




}
