/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.boundary.jsf;



import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.control.AbstractDataAccess;

/**
 *
 * @author home
 */
public abstract class AbstractFrm<T> implements Serializable {

public abstract AbstractDataAccess<T> getDataAccess();

    LazyDataModel<T> modelo;

    EstadosCRUD estado = EstadosCRUD.NINGUNO;

    T registro = null;

    public abstract FacesContext getFacesContext();

    public int contar() {
        int resultado = 0;
        AbstractDataAccess<T> trBean = null;
        try {
            trBean = getDataAccess();
            resultado = trBean.count();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return resultado;
    }

    public List<T> cargarDatos(int primero, int tamanio) {
        List<T> resultado = null;
        AbstractDataAccess<T> trBean = null;
        try {
            trBean = getDataAccess();
            resultado = trBean.FindRange(primero, tamanio);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (resultado == null) {
                resultado = Collections.EMPTY_LIST;
            }
        }
        return resultado;
    }

    @PostConstruct
    public void inicializar() {
        inicializarRegistros();
    }

    public void inicializarRegistros() {
        this.modelo = new LazyDataModel<T>() {
            @Override
            public int count(Map<String, FilterMeta> map) {
                try {
                    return contar();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
                return 0;
            }

            @Override
            public List<T> load(int primero, int tamanio, Map<String, SortMeta> map, Map<String, FilterMeta> map1) {
                List<T> resultado = null;
                try {
                    return cargarDatos(primero, tamanio);
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
                return resultado;
            }

            @Override
            public String getRowKey(T object) {
                if (object != null) {
                    return getIdPorObjeto(object);
                }
                return null;
            }

            @Override
            public T getRowData(String rowKey) {
                if (rowKey != null) {
                    return getObjetoPorId(rowKey);
                }
                return null;
            }
        };
    }

    public abstract String getIdPorObjeto(T object);

    public abstract T getObjetoPorId(String id);

    public abstract void instanciarRegistro();

    public void seleccionarRegistro() {
        this.estado = EstadosCRUD.MODIFICAR;
    }

    public void btnNuevoHandler(ActionEvent ae) {
        this.instanciarRegistro();
        this.estado = EstadosCRUD.NUEVO;
    }

    public void btnCancelarHandler(ActionEvent ae) {
        this.registro = null;
        this.estado = EstadosCRUD.NINGUNO;
    }

    public void btnModificarHandler(ActionEvent ae) {
        T modify = null;
        FacesMessage mensaje = null;
        try {
            AbstractDataAccess<T> trBean = getDataAccess();
            modify = trBean.modify(registro);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El registro no se pudo modificar", "el registro no se modifico");
            getFacesContext().addMessage(null, mensaje);
        }
        if (modify != null) {
            //TODO:Notificar que se modifico
            this.estado = EstadosCRUD.NINGUNO;
            this.registro = null;
            mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "El registro se a modificado exitosamente", "el registro se modifico");
            getFacesContext().addMessage(null, mensaje);
        }
        //TODO:notificar que no se modifico
    }

    public void btnEliminarHandler(ActionEvent ae) {
        FacesMessage mensaje = null;
        try {
            AbstractDataAccess<T> trBean = getDataAccess();
            trBean.delete(registro);
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
        //TODO:notificar que no se elimino
    }

    public void btnGuardarHandler(ActionEvent ae) {
        FacesMessage mensaje = null;
        try {
            AbstractDataAccess<T> trBean = getDataAccess();
            trBean.create(registro);
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

    public T getRegistro() {
        return registro;
    }

    public void setRegistro(T registro) {
        this.registro = registro;
    }

    public LazyDataModel<T> getModelo() {
        return this.modelo;
    }

    public EstadosCRUD getEstado() {
        return estado;
    }

 }