/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades_Base;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "SILLAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sillas.findAll", query = "SELECT s FROM Sillas s")
    , @NamedQuery(name = "Sillas.findByNumero", query = "SELECT s FROM Sillas s WHERE s.sillasPK.numero = :numero")
    , @NamedQuery(name = "Sillas.findByCategoria", query = "SELECT s FROM Sillas s WHERE s.sillasPK.categoria = :categoria")
    , @NamedQuery(name = "Sillas.findByUbicacion", query = "SELECT s FROM Sillas s WHERE s.sillasPK.ubicacion = :ubicacion")
    , @NamedQuery(name = "Sillas.findByEstadioIdEstadio", query = "SELECT s FROM Sillas s WHERE s.sillasPK.estadioIdEstadio = :estadioIdEstadio")})
public class Sillas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SillasPK sillasPK;
    @JoinColumn(name = "ESTADIO_ID_ESTADIO", referencedColumnName = "ID_ESTADIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estadio estadio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sillas")
    private Collection<Boleta> boletaCollection;

    public Sillas() {
    }

    public Sillas(SillasPK sillasPK) {
        this.sillasPK = sillasPK;
    }

    public Sillas(BigInteger numero, String categoria, String ubicacion, BigInteger estadioIdEstadio) {
        this.sillasPK = new SillasPK(numero, categoria, ubicacion, estadioIdEstadio);
    }

    public SillasPK getSillasPK() {
        return sillasPK;
    }

    public void setSillasPK(SillasPK sillasPK) {
        this.sillasPK = sillasPK;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    @XmlTransient
    public Collection<Boleta> getBoletaCollection() {
        return boletaCollection;
    }

    public void setBoletaCollection(Collection<Boleta> boletaCollection) {
        this.boletaCollection = boletaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sillasPK != null ? sillasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sillas)) {
            return false;
        }
        Sillas other = (Sillas) object;
        if ((this.sillasPK == null && other.sillasPK != null) || (this.sillasPK != null && !this.sillasPK.equals(other.sillasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Sillas[ sillasPK=" + sillasPK + " ]";
    }
    
}
