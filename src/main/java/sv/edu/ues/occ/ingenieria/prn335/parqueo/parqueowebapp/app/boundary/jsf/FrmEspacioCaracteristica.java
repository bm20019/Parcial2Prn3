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
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AbstractDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.EspacioCaracteristicaBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Espacio;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.EspacioCaracteristica;

/**
 *
 * @author roger
 */
@Named
@Dependent
public class FrmEspacioCaracteristica extends AbstractFrm<EspacioCaracteristica> implements Serializable{
    @Inject
    EspacioCaracteristicaBean ecBean;

    @Inject
    FacesContext fc;

    @Inject
    FrmTipoEspacio frmTipoEspacio;
    Espacio idEspacio;
    Long longIdEspacio;

    List<EspacioCaracteristica> listaEC;
    List<EspacioCaracteristica> listByIdEspacio;

    int stepTabView = 0;

    @Override
    public AbstractDataAccess<EspacioCaracteristica> getDataAccess() {
        return ecBean;
    }

    @Override
    public FacesContext getFacesContext() {
        return fc;
    }

    @Override
    public String getIdPorObjeto(EspacioCaracteristica object) {

        if (object != null && object.getIdEspacio() != null) {
            return object.getIdEspacio().toString();
        }
        return null;
    }

    @Override
    public EspacioCaracteristica getObjetoPorId(String id) {
        if (id != null && this.modelo != null && this.modelo.getWrappedData() != null) {
            return this.modelo.getWrappedData().stream().filter(r -> r.getIdEspacio().toString().equals(id))
                    .collect(Collectors.toList()).get(0);
        }
        return null;
    }

    @Override
    public void instanciarRegistro() {
        this.registro = new EspacioCaracteristica();
    }

    public List<EspacioCaracteristica> getListaEC() {
        this.listaEC = ecBean.FindRange(0, 999999);
        return listaEC;
    }

    public FrmTipoEspacio getFrmTipoEspacio() {
        return frmTipoEspacio;
    }

    public void setListaEC(List<EspacioCaracteristica> listaEC) {
        this.listaEC = listaEC;
    }

    public void generarListECByIdEspacio() {
        if (idEspacio != null) {
            this.listByIdEspacio = ecBean.findByIdEspacio(idEspacio.getIdEspacio(), 0, 99999);
        } else {
            this.listByIdEspacio = ecBean.findByIdEspacio(null, 0, 9999999);
        }

    }

    public List<EspacioCaracteristica> getListByIdEspacio() {
        return this.listByIdEspacio;
    }

    public void setListByIdEspacio(List<EspacioCaracteristica> listByIdEspacio) {
        this.listByIdEspacio = listByIdEspacio;
    }

    public EspacioCaracteristica getECFromECList(Long idEC) {
        if (idEC != null) {
            for (int i = 0; i < listByIdEspacio.size(); i++) {
                if (Objects.equals(listByIdEspacio.get(i).getIdEspacioCaracteristica(), idEC)) {
                    return listByIdEspacio.get(i);
                }
            }
        }
        return null;
    }

    public Espacio getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(Espacio idEspacio) {
        this.idEspacio = idEspacio;
    }

    public Long getLongIdEspacio() {
        return longIdEspacio;
    }

    public void setLongIdEspacio(Long longIdEspacio) {
        this.longIdEspacio = longIdEspacio;
    }

    public void selectECListListener(ActionEvent ae) {
        this.estado = EstadosCRUD.MODIFICAR;
        this.registro = getECFromECList(longIdEspacio);
        this.frmTipoEspacio.setIdTipoEspacio(registro.getIdTipoEspacio().getIdTipoEspacio());

    }

    public void selectTEListLisetener(ActionEvent ae) {
        this.registro.setIdTipoEspacio(frmTipoEspacio.teOfListTE(frmTipoEspacio.getIdTipoEspacio())
        );
    }

    public void colocarIdtoListEC(ActionEvent ae) {
        if (listByIdEspacio.get(0).getIdEspacio() == null) {
            for (int i = 0; i < listByIdEspacio.size(); i++) {
                registro = listByIdEspacio.get(i);
                registro.setIdEspacio(idEspacio);
                btnModificarHandler(ae);
            }
        }
    }

    public void eliminarListEC(ActionEvent ae) {
        if (listByIdEspacio.size()>0) {
            if (listByIdEspacio.get(0).getIdEspacio() == null) {
                for (int i = 0; i < listByIdEspacio.size(); i++) {
                    registro = listByIdEspacio.get(i);
                    btnEliminarHandler(ae);
                }
            }
        }

    }

    @Override
    public void btnNuevoHandler(ActionEvent ae) {
        super.btnNuevoHandler(ae);
        this.registro.setIdEspacio(this.idEspacio);
        this.registro.setIdTipoEspacio(frmTipoEspacio.getListaTE().get(0));
    }

    @Override
    public void btnCancelarHandler(ActionEvent ae) {
        super.btnCancelarHandler(ae);
        this.frmTipoEspacio.setIdTipoEspacio(null);
    }

    @Override
    public void btnGuardarHandler(ActionEvent ae) {
        FacesMessage mensaje = null;
        try {
            AbstractDataAccess<EspacioCaracteristica> trBean = getDataAccess();
            trBean.create(registro);
            this.listByIdEspacio.add(registro);
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

    @Override
    public void btnEliminarHandler(ActionEvent ae) {
        FacesMessage mensaje = null;
        try {
            AbstractDataAccess<EspacioCaracteristica> trBean = getDataAccess();
            trBean.delete(registro);
            this.listByIdEspacio.remove(registro);
            this.estado = EstadosCRUD.NINGUNO;
            this.registro = null;
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro eliminado exitosamente", "se elimino el registro");
            getFacesContext().addMessage(null, mensaje);
            //TODO: enviar mensaje de exito
        } catch (Exception ex) {
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo eliminar el registro", "no se elimino el registro");
            getFacesContext().addMessage(null, mensaje);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public int getStepTabView() {
        return stepTabView;
    }

    public int tomarespacio() {
        return stepTabView;
    }

    public void setStepTabView(int stepTabView) {
        this.stepTabView = stepTabView;
    }

    public void stepTbvwNext() {
        if (stepTabView < 1) {
            stepTabView++;
        }
    }

    public void stepTbvwBack() {
        if (stepTabView > 0) {
            stepTabView--;
        }
    }

}
