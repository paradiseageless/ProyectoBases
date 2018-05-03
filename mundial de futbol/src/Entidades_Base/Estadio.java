/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades_Base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author danih
 */
@Entity
@Table(name = "ESTADIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estadio.findAll", query = "SELECT e FROM Estadio e")
    , @NamedQuery(name = "Estadio.findByIdEstadio", query = "SELECT e FROM Estadio e WHERE e.idEstadio = :idEstadio")
    , @NamedQuery(name = "Estadio.findByNombre", query = "SELECT e FROM Estadio e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Estadio.findByCapacidad", query = "SELECT e FROM Estadio e WHERE e.capacidad = :capacidad")
    , @NamedQuery(name = "Estadio.findByNombreCiudad", query = "SELECT e FROM Estadio e WHERE e.nombreCiudad = :nombreCiudad")})
public class Estadio implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ESTADIO")
    private BigDecimal idEstadio;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "CAPACIDAD")
    private BigInteger capacidad;
    @Basic(optional = false)
    @Column(name = "NOMBRE_CIUDAD")
    private String nombreCiudad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadio")
    private Collection<Sillas> sillasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadioIdEstadio")
    private Collection<Partido> partidoCollection;

    public Estadio() {
    }

    public Estadio(BigDecimal idEstadio) {
        this.idEstadio = idEstadio;
    }

    public Estadio(BigDecimal idEstadio, String nombre, String nombreCiudad) {
        this.idEstadio = idEstadio;
        this.nombre = nombre;
        this.nombreCiudad = nombreCiudad;
    }

    public BigDecimal getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(BigDecimal idEstadio) {
        this.idEstadio = idEstadio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(BigInteger capacidad) {
        this.capacidad = capacidad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    @XmlTransient
    public Collection<Sillas> getSillasCollection() {
        return sillasCollection;
    }

    public void setSillasCollection(Collection<Sillas> sillasCollection) {
        this.sillasCollection = sillasCollection;
    }

    @XmlTransient
    public Collection<Partido> getPartidoCollection() {
        return partidoCollection;
    }

    public void setPartidoCollection(Collection<Partido> partidoCollection) {
        this.partidoCollection = partidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstadio != null ? idEstadio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estadio)) {
            return false;
        }
        Estadio other = (Estadio) object;
        if ((this.idEstadio == null && other.idEstadio != null) || (this.idEstadio != null && !this.idEstadio.equals(other.idEstadio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Estadio[ idEstadio=" + idEstadio + " ]";
    }
    
}
