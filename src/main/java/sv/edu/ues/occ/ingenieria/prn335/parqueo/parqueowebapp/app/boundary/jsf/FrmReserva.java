package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    

    public List<EspacioCaracteristica> getCaracteristicasSeleccionadas() {
        caracteristicasSeleccionadas = frmEspacioCaracteristica.getListByIdEspacio();
        return caracteristicasSeleccionadas;
    }

    public void setCaracteristicasSeleccionadas(List<EspacioCaracteristica> caracteristicasSeleccionadas) {
        this.caracteristicasSeleccionadas = caracteristicasSeleccionadas;
    }
    
    @Inject
    FrmEspacioCaracteristica frmEspacioCaracteristica;
    
    List<EspacioCaracteristica> caracteristicasSeleccionadas;
    List<String> caractaristicasDisponibles;
    List<EspacioCaracteristica> caracteristicasDisponiblesAsItems;

    public List<EspacioCaracteristica> getCaracteristicasDisponiblesAsItems() {
        caracteristicasDisponiblesAsItems = frmEspacioCaracteristica.getListaEC();
        return caracteristicasDisponiblesAsItems;
    }

    public void setCaracteristicasDisponiblesAsItems(List<EspacioCaracteristica> caracteristicasDisponiblesAsItems) {
        this.caracteristicasDisponiblesAsItems = caracteristicasDisponiblesAsItems;
    }

    public List<String> getCaractaristicasDisponibles() {
        caractaristicasDisponibles = new ArrayList();
        List<EspacioCaracteristica> cadisponibles = frmEspacioCaracteristica.getListaEC();
        for (int i = 0; i < cadisponibles.size(); i++) {
            TipoEspacio teEspacio = cadisponibles.get(i).getIdTipoEspacio();
            
            String ecNombre = cadisponibles.get(i).getValor();
            caractaristicasDisponibles.add(teEspacio.getNombre() + ":" +ecNombre  );
        }
        return caractaristicasDisponibles;
    }

    public void setCaractaristicasDisponibles(List<String> caractaristicasDisponibles) {
        this.caractaristicasDisponibles = caractaristicasDisponibles;
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
    
    TreeNode raiz;
    TreeNode nodoSeleccionado;

    private List<TipoReserva> listaTipoReserva;
    private Integer IdTipoReserva = null;

    @Override
    public AbstractDataAccess<Reserva> getDataAccess() {
        return this.rBean;
    }

    public List<TipoReserva> getListaTipoReserva() {
        List<TipoReserva> newArray = new ArrayList<>();

//        TipoReserva tr1 = new TipoReserva();
//        tr1.setIdTipoReserva(1);
//        tr1.setNombre("larga duracion");
//        newArray.add(tr1);
//        
//        listaTipoReserva = newArray;
        listaTipoReserva = trBean.FindRange(0, 999);
        return listaTipoReserva;

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

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Obtiene la fecha actual
//    Date fechaActual = new Date();
//
//    // Obtiene la fecha seleccionada
//    Date fechaSeleccionada = (Date) value;
//
//    // Verifica que la fecha seleccionada sea posterior a la fecha actual
//    if (this.registro.getHasta().before(fechaSeleccionada)) {
//        throw new ValidatorException(new FacesMessage("La fecha debe ser posterior a la fecha actual"));
//    } else{
//        throw new ValidatorException(new FacesMessage(fechaSeleccionada.toString()));
//    }

        // Obtiene la fecha seleccionada
        Date fechaSeleccionada = (Date) value;

        if (fechaSeleccionada != null && !this.registro.getDesde().after(fechaSeleccionada)) {
            System.out.print(fechaSeleccionada + "/" + this.registro.getDesde());
            return;
        }
        throw new ValidatorException(new FacesMessage("La fecha debe ser posterior a la fecha actual"));

    }

   
    public void seleccionarNodoListener(NodeSelectEvent nse) {
        frmArea.seleccionarNodoListener(nse);
    }
    
    public void refinarBusquedaNodo(){
        
    }
   
}
