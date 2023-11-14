package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AbstractDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.EspacioBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.ReservaBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.TipoReservaBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Area;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Espacio;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.EspacioCaracteristica;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Reserva;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.TipoEspacio;
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
    @Inject
    EspacioBean eBean;
    @Inject
    FrmArea frmArea;

    private TreeNode raiz;
    private TreeNode nodoSeleccionado;

    private List<TipoReserva> listaTipoReserva;
    //private Integer IdTipoReserva = null;

    private List<String> caracteristicasSeleccionadas;
    private List<String> caractaristicasDisponibles;
    private List<String> caracteristicasDisponiblesAsItems;

    private Area areaPadre;

    private List<Espacio> espaciosDisponibles;

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
        if (object != null && object.getIdReserva() != null) {
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

    public List<TipoReserva> getListaTipoReserva() {
        listaTipoReserva = trBean.FindRange(0, 999);
        return listaTipoReserva;
    }

    public void setListaTipoReserva(List<TipoReserva> listaTipoReserva) {
        this.listaTipoReserva = listaTipoReserva;
    }

    public String generarPathArea(long idEspacio) {

        Espacio espacio = eBean.findById(idEspacio);

        if (espacio != null) {
            Area areaPadre = espacio.getIdArea().getIdAreaPadre();
            Area area = espacio.getIdArea();
            if (areaPadre != null) {
                return "Espacio: " + espacio.getNombre() + " Areas:" + areaPadre.getNombre() + "/" + area.getNombre();
            } else if (area != null) {
                return "Espacio: " + espacio.getNombre() + " Areas: " + area.getNombre();
            }
        }
        return "";
    }

    public void cambiarFechaDesde() {
        this.registro.setDesde(new Date());
    }

    public boolean validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Obtiene la fecha seleccionada
        Date fechaSeleccionada = (Date) value;

        if (fechaSeleccionada != null && !this.registro.getDesde().after(fechaSeleccionada)) {
            //System.out.print(fechaSeleccionada + "/" + this.registro.getDesde());
            return true;
        }
        throw new ValidatorException(new FacesMessage("La fecha debe ser posterior a la fecha actual"));
    }

    public void seleccionarNodoListener(NodeSelectEvent nse) {
        this.caractaristicasDisponibles = new ArrayList<>();
        nse.getTreeNode().setExpanded(true);
        Area area = (Area) nse.getTreeNode().getData();
        areaPadre = area;

        List<Espacio> esList = areaPadre.getEspacioList();
        for (int i = 0; i < esList.size(); i++) {
            Espacio es = esList.get(i);
            List<EspacioCaracteristica> esCList = es.getEspacioCaracteristicaList();
            for (int j = 0; j < esCList.size(); j++) {
                EspacioCaracteristica ec = esCList.get(j);
                TipoEspacio tp = ec.getIdTipoEspacio();

                this.caractaristicasDisponibles.add(tp.getNombre() + ": " + ec.getValor());
            }
        }

        for (int i = 0; i < this.caractaristicasDisponibles.size(); i++) {
            System.out.print(this.caractaristicasDisponibles.get(i));
        }

        this.caracteristicasSeleccionadas = this.caractaristicasDisponibles;
        this.setCaracteristicasDisponiblesAsItems(this.caracteristicasSeleccionadas);
    }

    public List<String> getCaractaristicasDisponibles() {
        return this.caractaristicasDisponibles;
    }

    public void setCaractaristicasDisponibles(List<String> caractaristicasDisponibles) {
        this.caractaristicasDisponibles = caractaristicasDisponibles;
    }

    public List<String> getCaracteristicasDisponiblesAsItems() {
        return caracteristicasDisponiblesAsItems;
    }

    public void setCaracteristicasDisponiblesAsItems(List<String> caracteristicasDisponiblesAsItems) {
        this.caracteristicasDisponiblesAsItems = caracteristicasDisponiblesAsItems;
    }

    public List<String> getCaracteristicasSeleccionadas() {
        return this.caracteristicasSeleccionadas;
    }

    public void setCaracteristicasSeleccionadas(List<String> caracteristicasSeleccionadas) {
        this.caracteristicasSeleccionadas = caracteristicasSeleccionadas;
    }

    public TreeNode getRaiz() {
        frmArea.generarArbol();
        raiz = frmArea.getRaiz();
        return raiz;
    }

    public void setRaiz(TreeNode raiz) {
        this.raiz = raiz;
    }

    public TreeNode getNodoSeleccionado() {
        return nodoSeleccionado;
    }

    public void setNodoSeleccionado(TreeNode nodoSeleccionado) {
        this.nodoSeleccionado = nodoSeleccionado;
    }

    public void refinarBusquedaNodo() {

    }

    public List<Espacio> getEspaciosDisponibles() {
        if (this.areaPadre != null) {
            if (!this.areaPadre.getAreaList().equals(null)) {
                List<Area> ar = this.areaPadre.getAreaList();

                for (int i = 0; i < ar.size(); i++) {
                    Area ar1 = ar.get(i);
                    List<Espacio> es = ar1.getEspacioList();
                    for (int j = 0; j < es.size(); j++) {
                        Espacio esp = es.get(j);
                        espaciosDisponibles.add(esp);
                    }
                }
            } else {
                List<Espacio> es = this.areaPadre.getEspacioList();
                for (int j = 0; j < es.size(); j++) {
                    Espacio esp = es.get(j);
                    espaciosDisponibles.add(esp);
                }
            }
        }

        return espaciosDisponibles;
    }

    public void setEspaciosDisponibles(List<Espacio> espaciosDisponibles) {
        this.espaciosDisponibles = espaciosDisponibles;
    }
}
