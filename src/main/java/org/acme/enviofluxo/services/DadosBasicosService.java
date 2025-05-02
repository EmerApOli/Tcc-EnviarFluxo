package org.acme.enviofluxo.services;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.dto.DadosBasicosDTO;
import org.acme.enviofluxo.entity.DadosBasicos;


@ApplicationScoped
public class DadosBasicosService {



    @Transactional
    public DadosBasicos create(DadosBasicos dadosBasicos) throws Exception {

        try {
           // DadosBasicos dadosBasicos = new DadosBasicos(new DadosBasicosDTO());
            dadosBasicos.persist();

           return  dadosBasicos;
        } catch (Exception e) {
            // Tratar e registrar o erro
            throw new RuntimeException("Error creating DadosBasicos", e);
        }
    }

    public DadosBasicos findById(Long id) {
        DadosBasicos dadosBasicos = DadosBasicos.findById(id);  // Método estático fornecido pelo PanacheEntityBase
        if (dadosBasicos == null) {
            throw new EntityNotFoundException("DadosBasicos not found for ID: " + id);
        }
        return dadosBasicos;
    }

    public void delete(Long id) {
        boolean deleted = DadosBasicos.deleteById(id);  // Método estático fornecido pelo PanacheEntityBase
        if (!deleted) {
            throw new EntityNotFoundException("DadosBasicos not found for ID: " + id);
        }
    }



    // Outros métodos de negócio podem ser adicionados aqui
}
