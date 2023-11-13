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
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Area;

/**
 *
 * @author roger
 */
@Stateless
@LocalBean
public class AreaBean extends AbstractDataAccess<Area> implements Serializable{

        @PersistenceContext(name = "ParqueoPU")
    EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public AreaBean() {
        super(Area.class);
    }

    public List<Area> findByIdPadre(Integer idAreaPadre, int first, int pageSize) {
        if (idAreaPadre != null && first >= 0 && pageSize > 0) {
            if (em != null) {
                Query q = em.createNamedQuery("Area.findByIdPadre");
                q.setParameter("IdAreaPadre", idAreaPadre);
                q.setFirstResult(first);
                q.setMaxResults(pageSize);
                return q.getResultList();
            }

        }
        return Collections.EMPTY_LIST;
    }

    public List<Area> findRoot(int first, int pageSize) {
        if (first >= 0 && pageSize > 0) {

            if (em != null) {
                Query q = em.createNamedQuery("Area.findRaices");
                q.setFirstResult(first);
                q.setMaxResults(pageSize);
                return q.getResultList();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public int countByIdPadre(Integer idAreaPadre) {
        if (idAreaPadre != null) {
            if (em != null) {
                Query q = em.createNamedQuery("Area.countByIdArea");
                q.setParameter("IdAreaPadre", idAreaPadre);
                return ((Long) q.getSingleResult()).intValue();
            }
        }
        return 0;
    }
    
    
}
