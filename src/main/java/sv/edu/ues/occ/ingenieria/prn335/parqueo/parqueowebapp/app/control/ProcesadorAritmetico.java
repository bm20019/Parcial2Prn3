/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import java.io.Serializable;

/**
 *
 * @author roger
 */
@Stateless
@LocalBean
public class ProcesadorAritmetico implements Serializable {
    
    
    public Integer sumar(Integer i, Integer j) throws IllegalArgumentException {
        if (i != null && j != null) {
             return i + j;   
        }
        throw new IllegalArgumentException();
    } 
    
}
