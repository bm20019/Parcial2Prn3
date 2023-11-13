/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.prn335.parqueo.parqueowebapp.app.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author roger
 */
@Entity
@Table(name = "tipo_espacio", catalog = "parqueo", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TipoEspacio.findAll", query = "SELECT t FROM TipoEspacio t ORDER BY t.nombre ASC"),
    @NamedQuery(name = "TipoEspacio.findByIdTipoEspacio", query = "SELECT t FROM TipoEspacio t WHERE t.idTipoEspacio = :idTipoEspacio"),
    @NamedQuery(name = "TipoEspacio.findByNombre", query = "SELECT t FROM TipoEspacio t WHERE t.nombre = :nombre")})
public class TipoEspacio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_espacio")
    @NotNull
    private Integer idTipoEspacio;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idTipoEspacio", fetch = FetchType.LAZY)
    private List<EspacioCaracteristica> espacioCaracteristicaList;

    public TipoEspacio() {
    }

    public TipoEspacio(Integer idTipoEspacio) {
        this.idTipoEspacio = idTipoEspacio;
    }

    public Integer getIdTipoEspacio() {
        return idTipoEspacio;
    }

    public void setIdTipoEspacio(Integer idTipoEspacio) {
        this.idTipoEspacio = idTipoEspacio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @JsonbTransient
    public List<EspacioCaracteristica> getEspacioCaracteristicaList() {
        return espacioCaracteristicaList;
    }

    public void setEspacioCaracteristicaList(List<EspacioCaracteristica> espacioCaracteristicaList) {
        this.espacioCaracteristicaList = espacioCaracteristicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoEspacio != null ? idTipoEspacio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEspacio)) {
            return false;
        }
        TipoEspacio other = (TipoEspacio) object;
        if ((this.idTipoEspacio == null && other.idTipoEspacio != null) || (this.idTipoEspacio != null && !this.idTipoEspacio.equals(other.idTipoEspacio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.edu.ues.occ.ingenieria.prn335.mappeo1.entity.TipoEspacio[ idTipoEspacio=" + idTipoEspacio + " ]";
    }
    
}
