package org.acme.enviofluxo.services;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.dto.DadosBasicosDTO;
import org.acme.enviofluxo.entity.DadosBasicos;


@ApplicationScoped
public class DadosBasicosService {



    @Transactional
    public DadosBasicos create(DadosBasicosDTO dadosBasicosDTO) throws Exception {

        try {
            DadosBasicos dadosBasicos = new DadosBasicos(dadosBasicosDTO);
            dadosBasicos.persist();

           return  dadosBasicos;
        } catch (Exception e) {
            // Tratar e registrar o erro
            throw new RuntimeException("Error creating DadosBasicos", e);
        }
    }

    public DadosBasicos findById(Long id) {
        return DadosBasicos.findById(id);  // Método estático fornecido pelo PanacheEntityBase
    }

    public void delete(Long id) {
        DadosBasicos.deleteById(id);  // Método estático fornecido pelo PanacheEntityBase
    }



    // Outros métodos de negócio podem ser adicionados aqui
}
