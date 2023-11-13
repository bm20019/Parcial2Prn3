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
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.TipoEspacio;

/**
 *
 * @author roger
 */
@Stateless
@LocalBean
public class TipoEspacioBean extends AbstractDataAccess<TipoEspacio> implements Serializable {
    
    @PersistenceContext(unitName = "ParqueoPU")
    EntityManager em;
    
    /*
    * Almacena un registro de la base de datos
    * @param registro de Entidad a guardar
    * @throws IllegalStateException Si ocurre un error en el repositorio
    * @throws IllegalArgumentException Si el parametro es nulo
    */
   

//    public List<TipoEspacio> findRange(int first, int pageSize){
//        if (first >= 0 && pageSize > 0) {
//            if (em != null) {
//                Query q = em.createNamedQuery("TipoEspacio.findAll");
//                q.setFirstResult(first);
//                q.setMaxResults(pageSize);
//                return q.getResultList();
//            }
//        }
//        return Collections.EMPTY_LIST;
//    }
//
//    public TipoEspacio modify(TipoEspacio registro) {
//        if (registro != null) {
//            if (em != null){
//                try {
//                     return em.merge(registro);
//                } catch (Exception ex) {
//                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
//                }                               
//            }
//            throw new IllegalStateException();
//        }  
//        return null;
//    }    

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public TipoEspacioBean() {
        super(TipoEspacio.class);
    }
    
    public List<TipoEspacio> findRangeSlow(int first, int pageSize){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return this.FindRange(first, pageSize);
        
    }
}
