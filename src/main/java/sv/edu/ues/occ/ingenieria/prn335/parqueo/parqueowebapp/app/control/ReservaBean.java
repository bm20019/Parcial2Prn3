/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Reserva;

/**
 *
 * @author roger
 */
@Stateless
@LocalBean
public class ReservaBean extends AbstractDataAccess<Reserva> implements Serializable{

    @PersistenceContext(unitName = "ParqueoPU")
    EntityManager em;
    
    public ReservaBean() {
        super(Reserva.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    } 
}