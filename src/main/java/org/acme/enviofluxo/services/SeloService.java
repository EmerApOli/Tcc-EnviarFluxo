package org.acme.enviofluxo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.entity.Selo;
import org.acme.enviofluxo.repository.SeloRepository;
import org.modelmapper.ModelMapper;

@ApplicationScoped
public class SeloService {
    // Supondo que você tenha um repositório para selos
    @Inject
    SeloRepository seloRepository;

    private final ModelMapper modelMapper = new ModelMapper();
   @Transactional
    public Selo salvarSelo(Selo selo) {
      // Selo selo = modelMapper.map(seloDTO, Selo.class);
           seloRepository.persist(selo);
         return  selo;
    }


  //  public SeloDTO PegarSelo(EnvioDTO envioDTO){

        // Processar e salvar os selos
    ///    if (envioDTO.getItens() != null) {
     ///       for (ItemDTO item : envioDTO.getItens()) {
     ///           SeloDTO selo = item.getInteressadoDTO().getSeloDTO(); // Suponha que você tenha um método getSelo() em InteressadoDTO
    ///            if (selo != null)
     //             // salvarSelo(selo);
     ///            return  selo;
      ///          }
      //      }



       // return  null;
   // }
}