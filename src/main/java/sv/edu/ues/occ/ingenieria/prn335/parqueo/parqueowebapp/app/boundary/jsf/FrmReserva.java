package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIOutput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.model.SelectItem;
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
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.TipoEspacioBean;
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
    FacesContext fc;
    @Inject
    ReservaBean rBean;
    @Inject
    TipoReservaBean trBean;
    @Inject
    TipoEspacioBean teBean;
    @Inject
    EspacioBean eBean;
    @Inject
    FrmArea frmArea;
    @Inject
    FrmEspacio frmEspacio;

    private TreeNode raiz;
    private TreeNode nodoSeleccionado;

    private List<TipoReserva> listaTipoReserva;

    private List<TipoEspacio> caracteristicasSeleccionadas;
    private List<TipoEspacio> caractaristicasDisponibles;
    private List<SelectItem> caracteristicasDisponiblesAsItems;
    private List<Espacio> espaciosDisponibles;

    private Area areaLocal;

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        frmArea.generarArbol();
        raiz = frmArea.getRaiz();
    }

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

    public void cambiarFechaDesde(AjaxBehaviorEvent event) {
        this.registro.setDesde((Date) ((UIOutput) event.getSource()).getValue());
    }

    public boolean validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Obtiene la fecha seleccionada
        Date fechaSeleccionada = (Date) value;

        Date fechaAhora = new Date();

        if (this.registro.getDesde().getTime() > fechaAhora.getTime()) {
            if (fechaSeleccionada != null) {
                if (this.registro.getDesde().getTime() < fechaSeleccionada.getTime()) {
                    return true;
                }
            }
        }
        throw new ValidatorException(new FacesMessage("La fecha debe ser posterior a la fecha actual"));
    }

    public void seleccionarNodoListener(NodeSelectEvent nse) {

        Area area = (Area) nse.getTreeNode().getData();

        if (this.areaLocal != null && this.areaLocal.getIdArea() != null && this.frmEspacio != null) {
            this.frmEspacio.setIdArea(areaLocal.getIdArea());
        }

        espaciosDisponibles = eBean.findByIdArea(area.getIdArea(), 0, 10000);
        caractaristicasDisponibles = teBean.FindRange(0, 100000);
        rellenarEspaciosDisponibles();
        
        List<SelectItem> items = new ArrayList<>();

        for (TipoEspacio caracteristica : caractaristicasDisponibles) {
            for (EspacioCaracteristica esC : caracteristica.getEspacioCaracteristicaList()) {
                items.add(new SelectItem(caracteristica, caracteristica.getNombre() + ": " + esC.getValor()));
            }
        }
        setCaracteristicasDisponiblesAsItems(items);
    }

    public List<TipoEspacio> getCaractaristicasDisponibles() {
        return this.caractaristicasDisponibles;
    }

    public void setCaractaristicasDisponibles(List<TipoEspacio> caractaristicasDisponibles) {
        this.caractaristicasDisponibles = caractaristicasDisponibles;
    }

    public List<SelectItem> getCaracteristicasDisponiblesAsItems() {
        return caracteristicasDisponiblesAsItems;
    }

    public void setCaracteristicasDisponiblesAsItems(List<SelectItem> caracteristicasDisponiblesAsItems) {
        this.caracteristicasDisponiblesAsItems = caracteristicasDisponiblesAsItems;
    }

    public List<TipoEspacio> getCaracteristicasSeleccionadas() {
        return this.caracteristicasSeleccionadas;
    }

    public void setCaracteristicasSeleccionadas(List<TipoEspacio> caracteristicasSeleccionadas) {
        this.caracteristicasSeleccionadas = caracteristicasSeleccionadas;
    }

    public TreeNode getRaiz() {
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

        List<Espacio> items = new ArrayList<>();
        for (TipoEspacio caracteristica : caractaristicasDisponibles) {
            for (EspacioCaracteristica esC : caracteristica.getEspacioCaracteristicaList()) {
                if (caracteristica.getEspacioCaracteristicaList().contains(esC)) {
                    if (!items.contains(eBean.findById(caracteristica.getIdTipoEspacio()))) {
                        items.add(eBean.findById(caracteristica.getIdTipoEspacio()));
                    }
                }
            }
        }
        espaciosDisponibles = items;
    }

    public List<Espacio> getEspaciosDisponibles() {
        return espaciosDisponibles;
    }

    public void setEspaciosDisponibles(List<Espacio> espaciosDisponibles) {
        this.espaciosDisponibles = espaciosDisponibles;
    }

    private void rellenarEspaciosDisponibles() {

        if (espaciosDisponibles != null) {
            Date desde = this.registro.getDesde();
            Date hasta = this.registro.getHasta();
            List<Espacio> espacios = espaciosDisponibles;
            List<Espacio> reservado = new ArrayList<>();
            for (Espacio esp : espacios) {
                List<Reserva> rslist = esp.getReservaList();
                for (Reserva rs : rslist) {
                    if (rs.getDesde().getTime() >= desde.getTime() && rs.getHasta().getTime() <= hasta.getTime()) {
                        System.out.println("La fecha ya esta reservada para: " + esp.getNombre());
                        if (!reservado.contains(esp)) {
                            reservado.add(esp);
                        }
                    }
                }
            }

            for (Espacio es : reservado) {
                if (espaciosDisponibles.contains(es)) {
                    espaciosDisponibles.remove(es);
                }
            }

        }
    }
}
