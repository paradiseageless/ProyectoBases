/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades_Base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "JUEZ")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Juez.findAll", query = "SELECT j FROM Juez j")
    , @NamedQuery(name = "Juez.findByIdJuez", query = "SELECT j FROM Juez j WHERE j.idJuez = :idJuez")
    , @NamedQuery(name = "Juez.findByNombre", query = "SELECT j FROM Juez j WHERE j.nombre = :nombre")
    , @NamedQuery(name = "Juez.findByApellido", query = "SELECT j FROM Juez j WHERE j.apellido = :apellido")})
public class Juez implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_JUEZ")
    private BigDecimal idJuez;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "APELLIDO")
    private String apellido;
    @JoinColumn(name = "NACIONALIDAD", referencedColumnName = "ID_PAIS")
    @ManyToOne(optional = false)
    private Paises nacionalidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "juez")
    private Collection<Arbitra> arbitraCollection;

    public Juez() {
    }

    public Juez(BigDecimal idJuez) {
        this.idJuez = idJuez;
    }

    public Juez(BigDecimal idJuez, String nombre, String apellido) {
        this.idJuez = idJuez;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public BigDecimal getIdJuez() {
        return idJuez;
    }

    public void setIdJuez(BigDecimal idJuez) {
        this.idJuez = idJuez;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Paises getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Paises nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @XmlTransient
    public Collection<Arbitra> getArbitraCollection() {
        return arbitraCollection;
    }

    public void setArbitraCollection(Collection<Arbitra> arbitraCollection) {
        this.arbitraCollection = arbitraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJuez != null ? idJuez.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Juez)) {
            return false;
        }
        Juez other = (Juez) object;
        if ((this.idJuez == null && other.idJuez != null) || (this.idJuez != null && !this.idJuez.equals(other.idJuez))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Juez[ idJuez=" + idJuez + " ]";
    }
    
}
