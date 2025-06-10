package org.acme.enviofluxo.repository;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.enviofluxo.entity.Selo;

import java.util.List;

@ApplicationScoped
public class SeloRepository implements PanacheRepository<Selo> {


    public List<Selo> buscarTodos() {
        return listAll(); // Método fornecido pelo Panache para listar todos os registros
    }

}
