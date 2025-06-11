package org.acme.enviofluxo.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.acme.enviofluxo.entity.Documentos;
import org.acme.enviofluxo.entity.Interessado;

import java.util.List;

@ApplicationScoped
public class DocumentosRepositoty  implements PanacheRepository<Documentos> {



        public List<Documentos> findDocumentsByCpfAndProvider(Long cpf, String provider) {
            // Se o provider não for especificado, usa "govbr" como padrão

            return find("SELECT d FROM Documentos d JOIN d.interessados i WHERE i.cpf = ?1 AND d.provedor = ?2", cpf, provider).list();
        }


        public  List<Documentos> buscar(){

            return findAll().list();
        }


        public  Long buscarIdFluxoDocumento(Long id){

          Documentos  documentos = findById(id);

            return documentos.getEnvioFluxoId();

        }



}
