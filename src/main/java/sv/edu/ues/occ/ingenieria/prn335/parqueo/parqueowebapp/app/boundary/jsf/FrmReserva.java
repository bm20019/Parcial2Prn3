package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AbstractDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.ReservaBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.TipoReservaBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Reserva;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.TipoReserva;

/**
 *
 * @author adalberto
 */
@Named
@ViewScoped
public class FrmReserva extends AbstractFrm<Reserva> implements Serializable {
    @Inject
    ReservaBean rBean;
    @Inject
    FacesContext fc;
    
    @Inject
    TipoReservaBean trBean;
    
    private List<TipoReserva> listaTipoReserva;
    private Integer IdTipoReserva = null;
    
    @Override
    public AbstractDataAccess<Reserva> getDataAccess() {
        return this.rBean;
    }

    public List<TipoReserva> getListaTipoReserva() {
        List<TipoReserva> newArray = new ArrayList<>();
        
        TipoReserva tr1 = new TipoReserva();
        tr1.setIdTipoReserva(1);
        tr1.setNombre("larga duracion");
        newArray.add(tr1);
        
        listaTipoReserva = newArray;
        return newArray;
        
    }

    public void setListaTipoReserva(List<TipoReserva> listaTipoReserva) {
        this.listaTipoReserva = listaTipoReserva;
    }
    

    @Override
    public FacesContext getFacesContext() {
        return this.fc;
    }

    @Override
    public String getIdPorObjeto(Reserva object) {
         if (object != null && object.getIdReserva()!= null) {
            return object.getIdReserva().toString();
        }
        return null;
    }

    @Override
    public Reserva getObjetoPorId(String id) {
        if (id != null && this.modelo != null && this.modelo.getWrappedData() != null) {
            return this.modelo.getWrappedData().stream().filter(r -> r.getIdReserva().toString().equals(id))
                    .collect(Collectors.toList()).get(0);
        }
        return null;
    }

    @Override
    public void instanciarRegistro() {
        this.registro = new Reserva();
    }
    
    
}
