package org.acme.enviofluxo.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.dto.DadosBasicosDTO;
import org.acme.enviofluxo.dto.DocumentoDTO;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
public class Documentos  extends PanacheEntityBase implements Serializable {

    private  static  final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long  id;

    @JoinColumn(name = "nomearquivo")
    private String  nomearquivo;

    @JoinColumn(name = "arquivopdf")
    private byte[]  arquivopdf;

    public Documentos(DocumentoDTO documentoDTO){
        this.arquivopdf = documentoDTO.getArquivopdf();
        this.nomearquivo = documentoDTO.getNomearquivo();

    }

    public static Interessado findByCpf(Long id) {
        return find("id", id).firstResult();
    }
}


