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
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Espacio;

/**
 *
 * @author roger
 */
@Stateless
@LocalBean
public class EspacioBean extends AbstractDataAccess<Espacio> implements Serializable{
    @PersistenceContext(name = "ParqueoPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public EspacioBean() {
        super(Espacio.class);
    }

    public List<Espacio> findByIdArea(Integer idArea, int first, int pageSize) {

        if (idArea != null && first >= 0 && pageSize > 0) {
            if (em != null) {
                Query q = em.createNamedQuery("Espacio.findByIdArea");
                q.setParameter("IdArea", idArea);
                q.setFirstResult(first);
                q.setMaxResults(pageSize);
                return q.getResultList();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public int coutByIdArea(Integer idArea) {
        if (idArea != null) {
            if (em != null) {
                Query q = em.createNamedQuery("Espacio.coutByIdArea");
                q.setParameter("IdArea", idArea);
                return ((Long) q.getSingleResult()).intValue();
            }
        }
        return 0;
    }
}
