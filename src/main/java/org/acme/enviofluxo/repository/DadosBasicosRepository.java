package org.acme.enviofluxo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.enviofluxo.entity.DadosBasicos;


@ApplicationScoped
public class DadosBasicosRepository implements PanacheRepository<DadosBasicos> {
    // Aqui você pode adicionar métodos personalizados, se necessário
}
