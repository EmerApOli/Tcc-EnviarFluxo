package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.entity.Interessado;
import org.acme.enviofluxo.repository.InteressadoRepository;


@ApplicationScoped
public class InteressadoService {

    @Inject
InteressadoRepository interessadoRepository;

  @Transactional
     public  Interessado  SalvarInteressado(Interessado interessado){

         try {
            // Interessado interessado = new Interessado(interessadoDTO);
             interessado.persist();

             return interessado;
         } catch (Exception e) {
             // Tratar e registrar o erro
             throw new RuntimeException("Erro ao cadastrar Interessado", e);
         }

     }

    public Interessado buscarPorCpf(Long cpf) {
        return Interessado.findByCpf(cpf);
    }

}
