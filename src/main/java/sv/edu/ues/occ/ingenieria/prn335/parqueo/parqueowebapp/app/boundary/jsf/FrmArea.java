/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.event.ListSelectionEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AbstractDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AreaBean;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity.Area;


/**
 *
 * @author roger
 */
@Named
@ViewScoped
public class FrmArea extends AbstractFrm<Area> implements Serializable {
    @Inject
    FrmEspacio frmEspacio;

    @Inject
    AreaBean aBean;

    @Inject
    FacesContext fc;

    TreeNode raiz;
    TreeNode nodoSeleccionado;

    Area AreaPadre = null;

    private List<Area> ListaAreas;
    private Integer IdAreaEspacio = null;

    @Override
    public void instanciarRegistro() {
        this.registro = new Area();
        registro.setIdAreaPadre(AreaPadre);
    }

    @Override
    public void btnCancelarHandler(ActionEvent ae) {
        this.AreaPadre = null;
        this.registro = null;
        this.setNodoSeleccionado(null);
        this.estado = EstadosCRUD.NINGUNO;
    }

    @Override
    public AbstractDataAccess<Area> getDataAccess() {
        return this.aBean;
    }

    @Override
    public FacesContext getFacesContext() {
        return fc;
    }

    @Override
    public String getIdPorObjeto(Area object) {
        if (object != null && object.getIdArea() != null) {
            return object.getIdArea().toString();
        }
        return null;
    }

    @Override
    public Area getObjetoPorId(String id) {
        if (id != null && this.modelo != null && this.modelo.getWrappedData() != null) {
            return this.modelo.getWrappedData().stream().filter(r -> r.getIdArea().toString().equals(id))
                    .collect(Collectors.toList()).get(0);
        }
        return null;
    }

    //Metodos Para TreeArea
    public TreeNode getRaiz() {
        generarArbol();
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

    public void seleccionarNodoListener(NodeSelectEvent nse) {

        this.registro = (Area) nse.getTreeNode().getData();
        this.AreaPadre = registro;
        this.seleccionarRegistro();

        if (this.registro != null && this.registro.getIdArea() != null) {
            this.frmEspacio.setIdArea(this.registro.getIdArea());
            this.IdAreaEspacio = registro.getIdArea();
        }
    }

    public void generarArbol() {
        this.raiz = new DefaultTreeNode("Areas", null);
        List<Area> lista = aBean.findRoot(0, 10000000);
        if (lista != null && !lista.isEmpty()) {
            for (Area next : lista) {
                if (next.getIdAreaPadre() == null) {
                    this.generarRamas(raiz, next);
                }
            }
        }
    }

    public void generarRamas(TreeNode raiz, Area actual) {
        DefaultTreeNode nuevoPadre = new DefaultTreeNode(actual, raiz);
        List<Area> hijos = this.aBean.findByIdPadre(actual.getIdArea(), 0, 100000000);
        for (Area hijo : hijos) {
            generarRamas(nuevoPadre, hijo);
        }
    }

    public void generarListaAreas() {
        ListaAreas = aBean.FindRange(0, 999999);
    }

    public Area getAreaPadre() {
        return AreaPadre;
    }

    public void setAreaPadre(Area AreaPadre) {
        this.AreaPadre = AreaPadre;
    }

    //Generar Area Latinoamericana promedio sin figura paterna
    public void btnNuevoRaiz(ActionEvent ae) {

        this.AreaPadre = null;
        this.registro = new Area();
        this.setNodoSeleccionado(null);
        this.estado = EstadosCRUD.NUEVO;
    }

    //Color IdArea al Espacio por creace
    public void selectAreaListListener(ListSelectionEvent ef) {
        if (this.frmEspacio.registro != null) {
            this.frmEspacio.registro.setIdArea(aBean.findById(this.IdAreaEspacio));
        }
    }

    public void setIdAreaEspacio(Integer IdAreaEspacio) {
        if (this.frmEspacio.registro != null && this.registro != null) {
            this.frmEspacio.registro.setIdArea(aBean.findById(IdAreaEspacio));
        }
    }

    public List<Area> getListaAreas() {
        generarListaAreas();
        return ListaAreas;
    }

    public Integer getIdAreaEspacio() {
        return this.IdAreaEspacio;
    }

    public Area getAreaFromAreaList(Integer idA) {
        if (idA != null && this.registro != null) {
            for (int i = 0; i < ListaAreas.size(); i++) {
                if (Objects.equals(ListaAreas.get(i).getIdArea(), idA)) {
                    return ListaAreas.get(i);
                }
            }
        }
        return null;
    }

    public FrmEspacio getFrmEspacio() {
        return frmEspacio;
    }

    public void closeDialogEspacioListener(ActionEvent ae) {
        this.frmEspacio.frmEspacioCaracteristica.eliminarListEC(ae);
        this.frmEspacio.frmEspacioCaracteristica.btnCancelarHandler(ae);
        this.frmEspacio.estado = EstadosCRUD.NINGUNO;
        this.frmEspacio.registro = null;
    }
}
