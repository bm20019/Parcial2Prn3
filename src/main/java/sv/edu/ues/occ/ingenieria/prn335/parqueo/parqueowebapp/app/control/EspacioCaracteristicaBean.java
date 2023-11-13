
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.EspacioCaracteristica;

/**
 *
 * @author roger
 */
@Stateless
@LocalBean
public class EspacioCaracteristicaBean extends AbstractDataAccess<EspacioCaracteristica> implements Serializable{
    @PersistenceContext(name = "ParqueoPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public EspacioCaracteristicaBean() {
        super(EspacioCaracteristica.class);
    }

    public List<EspacioCaracteristica> findByIdEspacio(Long idEspacio, int first, int pageSize) {

        if (first >= 0 && pageSize > 0) {
            if (em != null) {
                Query q = em.createNamedQuery("EspacioCaracteristica.findByIdEspacio");
                if (idEspacio != null) {
                    q.setParameter("idEspacio", idEspacio);
                } else {
                    q.setParameter("idEspacio", null);
                }
                q.setFirstResult(first);
                q.setMaxResults(pageSize);
                return q.getResultList();
            }
        }
        return Collections.EMPTY_LIST;
    }
}
