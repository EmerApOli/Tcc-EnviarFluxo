package org.acme.enviofluxo.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;

public class DocumentosRepositoty  implements PanacheRepository<Interessado> {
}
