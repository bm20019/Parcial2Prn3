/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author roger
 */
@Entity
@Table(name = "espacio", catalog = "parqueo", schema = "public")
@NamedQueries({
    @NamedQuery(name = "Espacio.findAll", query = "SELECT e FROM Espacio e"),
    @NamedQuery(name = "Espacio.findByIdEspacio", query = "SELECT e FROM Espacio e WHERE e.idEspacio = :idEspacio"),
    @NamedQuery(name = "Espacio.findByNombre", query = "SELECT e FROM Espacio e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Espacio.findByObservaciones", query = "SELECT e FROM Espacio e WHERE e.observaciones = :observaciones"),
    @NamedQuery(name = "Espacio.findByActivo", query = "SELECT e FROM Espacio e WHERE e.activo = :activo"),
    @NamedQuery(name = "Espacio.findByIdArea", query = "SELECT e FROM Espacio e WHERE e.idArea .idArea = :IdArea ORDER BY e.nombre ASC")
})
public class Espacio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_espacio")
    private Long idEspacio;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "activo")
    private Boolean activo;
    @JoinColumn(name = "id_area", referencedColumnName = "id_area")
    @ManyToOne(fetch = FetchType.LAZY)
    private Area idArea;
    @OneToMany(mappedBy = "idEspacio", fetch = FetchType.LAZY)
    private List<Reserva> reservaList;
    @OneToMany(mappedBy = "idEspacio", fetch = FetchType.LAZY)
    private List<EspacioCaracteristica> espacioCaracteristicaList;

    public Espacio() {
    }

    public Espacio(Long idEspacio) {
        this.idEspacio = idEspacio;
    }

    public Long getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(Long idEspacio) {
        this.idEspacio = idEspacio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Area getIdArea() {
        return idArea;
    }

    public void setIdArea(Area idArea) {
        this.idArea = idArea;
    }

    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    public List<EspacioCaracteristica> getEspacioCaracteristicaList() {
        return espacioCaracteristicaList;
    }

    public void setEspacioCaracteristicaList(List<EspacioCaracteristica> espacioCaracteristicaList) {
        this.espacioCaracteristicaList = espacioCaracteristicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEspacio != null ? idEspacio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Espacio)) {
            return false;
        }
        Espacio other = (Espacio) object;
        if ((this.idEspacio == null && other.idEspacio != null) || (this.idEspacio != null && !this.idEspacio.equals(other.idEspacio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.edu.ues.occ.ingenieria.prn335.mappeo1.entity.Espacio[ idEspacio=" + idEspacio + " ]";
    }
    
}
