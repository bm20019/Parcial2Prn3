/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author home
 */
public abstract class AbstractDataAccess<T> implements Serializable {

    final Class tipoDato;

    public AbstractDataAccess(Class tipoDato) {
        this.tipoDato = tipoDato;
    }

    public abstract EntityManager getEntityManager();

    public void create(T registro) throws IllegalStateException, IllegalArgumentException {

        if (registro != null) {
            try {
                EntityManager em = getEntityManager();
                if (em != null) {

                    em.persist(registro);
                    return;

                }

            } catch (Exception ex) {

                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);

            }
            throw new IllegalStateException();
        }
        throw new IllegalArgumentException();
    }

    public List<T> FindRange(int first, int pageSize) {
        EntityManager em = null;
        try {
            em = getEntityManager();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        if (first >= 0 && pageSize > 0) {
            if (em != null) {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery cq = cb.createQuery(tipoDato);
                Root<T> raiz = cq.from(tipoDato);
                cq.select(raiz);
                TypedQuery q = em.createQuery(cq);

                q.setFirstResult(first);
                q.setMaxResults(pageSize);
                return q.getResultList();
            }
            throw new IllegalStateException();
        }
        return Collections.EMPTY_LIST;
    }

    public T modify(T registro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
//      return em.merge(registro);

        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        if (registro != null) {
            ///EntityManager em = getEntityManager();
            if (em != null) {

                try {
                    return em.merge(registro);

                } catch (Exception ex) {

                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            throw new IllegalStateException();
        }
        return null;
    }

    public T findById(Object id) {
        EntityManager em = null;
        if (id != null) {
            try {
                em = getEntityManager();

            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            if (em != null) {
                try {
                    return (T) em.find(tipoDato, id);
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            throw new IllegalStateException();
        }
        throw new IllegalArgumentException();
    }

    public void delete(T registro) throws IllegalStateException, IllegalStateException {

        if (registro != null) {
            try {
                EntityManager em = getEntityManager();
                if (em != null) {
                    em.remove(em.merge(registro));
                    System.out.println("Registro eliminado correctamente.");
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);

            }
            throw new IllegalStateException();
        }
        throw new IllegalArgumentException();

    }

    public int count() throws IllegalStateException {
        EntityManager em = null;
        try {
            em = this.getEntityManager();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        if (em != null) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);

            cq.select(cb.count(cq.from(tipoDato)));

            Long result = em.createQuery(cq).getSingleResult();

            if (result != null) {
                return Math.toIntExact(result); // Convierte Long a int
            } else {
                throw new IllegalStateException("La consulta no devolvió resultados.");
            }
        }
        throw new IllegalStateException("No se pudo obtener el EntityManager.");
    }
}
