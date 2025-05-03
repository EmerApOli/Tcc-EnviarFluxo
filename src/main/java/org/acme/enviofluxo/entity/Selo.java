package org.acme.enviofluxo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor // Gera o construtor padrão
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "selo")
public class Selo extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = false, nullable = false)
    private Long id;
    @Column(name = "pagina", unique = false, nullable = false)
    private Long pagina;
    @Column(name = "x", unique = false, nullable = false)
    private Long x;
    @Column(name = "y", unique = false, nullable = false)
    private Long y;
    @Column(name = "largura", unique = false, nullable = false)
    private Long largura;

    @Column(name = "altura", unique = false, nullable = false)
    private Long altura;


}
