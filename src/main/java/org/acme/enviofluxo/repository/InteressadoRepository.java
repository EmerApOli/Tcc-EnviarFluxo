package org.acme.enviofluxo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.enviofluxo.entity.Interessado;

@ApplicationScoped
public class InteressadoRepository implements PanacheRepository<Interessado> {
}
