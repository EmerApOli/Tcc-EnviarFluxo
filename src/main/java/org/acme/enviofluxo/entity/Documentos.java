package org.acme.enviofluxo.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.acme.enviofluxo.dto.EnvioDTO;


import java.io.Serializable;
import java.util.List;


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

    @JoinColumn(name = "provedor")
    private String  provedor;

    @JoinColumn(name = "arquivopdf")
    private byte[]  arquivopdf;

    @JoinColumn(name = "urldocumento")
    String urldocumento;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "enviofluxo_id")
    private  EnvioFluxo enviofluxo;

    @OneToMany(mappedBy = "documentos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interessado> interessados;


    public Long getEnvioFluxoId() {
        return this.enviofluxo != null ? this.enviofluxo.getId() : null;
    }

    public String getStatusFluxo() {
        return this.enviofluxo != null ? this.enviofluxo.getStatus() : null;
    }




    }





