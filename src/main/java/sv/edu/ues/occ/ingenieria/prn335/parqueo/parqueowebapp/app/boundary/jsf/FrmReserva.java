package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.stream.Collectors;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AbstractDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.ReservaBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Reserva;

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
    @Override
    public AbstractDataAccess<Reserva> getDataAccess() {
        return this.rBean;
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
