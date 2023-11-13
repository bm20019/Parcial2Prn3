/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AbstractDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.EspacioBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Area;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Espacio;


/**
 *
 * @author roger
 */
@Named
@Dependent
public class FrmEspacio extends AbstractFrm<Espacio> implements Serializable{
    @Inject
    EspacioBean eBean;
    
    @Inject
    FrmEspacioCaracteristica frmEspacioCaracteristica;


    @Inject
    FacesContext fc;
    Integer idArea;

    @Override
    public AbstractDataAccess<Espacio> getDataAccess() {
        return eBean;
    }

    @Override
    public FacesContext getFacesContext() {
        return fc;
    }

    @Override
    public String getIdPorObjeto(Espacio object) {

        if (object != null && object.getIdEspacio() != null) {
            return object.getIdEspacio().toString();
        }
        return null;

    }

    @Override
    public Espacio getObjetoPorId(String id) {

        if (id != null && this.modelo != null && this.modelo.getWrappedData() != null) {
            return this.modelo.getWrappedData().stream().filter(r -> r.getIdEspacio().toString().equals(id))
                    .collect(Collectors.toList()).get(0);
        }
        return null;
    }

    @Override
    public void instanciarRegistro() {
        this.registro = new Espacio();
        if (this.idArea != null) {
            this.registro.setIdArea(new Area(idArea));
        }
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    @Override
    public List<Espacio> cargarDatos(int primero, int tamanio) {
        if (this.idArea != null) {

            return this.eBean.findByIdArea(idArea, primero, tamanio);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public int contar() {
        if (this.idArea != null) {
            return this.eBean.coutByIdArea(idArea);
        }
        return 0;
    }

    @Override
    public void btnNuevoHandler(ActionEvent ae) {
        super.btnNuevoHandler(ae);
        frmEspacioCaracteristica.setIdEspacio(null);
        frmEspacioCaracteristica.generarListECByIdEspacio();
        frmEspacioCaracteristica.setStepTabView(0);
    }

    
    
    @Override
    public void seleccionarRegistro() {
        super.seleccionarRegistro();
        frmEspacioCaracteristica.setIdEspacio(registro);
        frmEspacioCaracteristica.generarListECByIdEspacio();
        frmEspacioCaracteristica.setStepTabView(0);
    }

    public FrmEspacioCaracteristica getFrmEspacioCaracteristica() {
        return frmEspacioCaracteristica;
    }

    @Override
    public void btnGuardarHandler(ActionEvent ae) {
    
            FacesMessage mensaje = null;
        try {
            AbstractDataAccess<Espacio> trBean = getDataAccess();
            trBean.create(registro);
            frmEspacioCaracteristica.setIdEspacio(registro);
            frmEspacioCaracteristica.colocarIdtoListEC(ae);
            this.estado = EstadosCRUD.NINGUNO;
            this.registro = null;
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro guardado con exito", "se creo el registro");
            getFacesContext().addMessage(null, mensaje);

        } catch (Exception ex) {
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo guardar el registro", "no se guardo el registro");
            getFacesContext().addMessage(null, mensaje);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    
    }
}
