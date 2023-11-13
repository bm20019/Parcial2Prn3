/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AbstractDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.TipoEspacioBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.TipoEspacio;


/**
 *
 * @author home
 */
@Named
@ViewScoped
public class FrmTipoEspacio extends AbstractFrm<TipoEspacio> implements Serializable {
    
    @Inject
    TipoEspacioBean teBean;

    @Inject
    FacesContext fc;

    List<TipoEspacio> listaTE;

    Integer idTipoEspacio;
    
    @Override
    public AbstractDataAccess<TipoEspacio> getDataAccess() {
        return teBean;
    }

    @Override
    public FacesContext getFacesContext() {
        return fc;
    }

    @Override
    public String getIdPorObjeto(TipoEspacio object) {
        if (object != null && object.getIdTipoEspacio() != null) {
            return object.getIdTipoEspacio().toString();
        }
        return null;
    }

    @Override
    public TipoEspacio getObjetoPorId(String id) {
        if (id != null && this.modelo != null && this.modelo.getWrappedData() != null) {
            return this.modelo.getWrappedData().stream().filter(r -> r.getIdTipoEspacio().toString().equals(id))
                    .collect(Collectors.toList()).get(0);
        }
        return null;

    }

    @Override
    public void instanciarRegistro() {
        this.registro = new TipoEspacio();
    }
    

    public List<TipoEspacio> getListaTE() {
        this.listaTE = teBean.FindRange(0, 99999);
        return listaTE;
    }

    public void setListaTE(List<TipoEspacio> listaTE) {
        this.listaTE = listaTE;
    }

    public Integer getIdTipoEspacio() {
        return idTipoEspacio;
    }

    public void setIdTipoEspacio(Integer idTipoEspacio) {
        this.idTipoEspacio = idTipoEspacio;
    }
    
    public TipoEspacio teOfListTE(Integer idTE) {
        if (idTE != null) {
            for (int i = 0; i < listaTE.size(); i++) {
                if (Objects.equals(listaTE.get(i).getIdTipoEspacio(), idTE)) {
                    return listaTE.get(i);
                }
            }
        }
        return null;
    }
}
