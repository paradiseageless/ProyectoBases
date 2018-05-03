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
@Table(name = "CONTINENTES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Continentes.findAll", query = "SELECT c FROM Continentes c")
    , @NamedQuery(name = "Continentes.findByIdContinente", query = "SELECT c FROM Continentes c WHERE c.idContinente = :idContinente")
    , @NamedQuery(name = "Continentes.findByNombreContinente", query = "SELECT c FROM Continentes c WHERE c.nombreContinente = :nombreContinente")})
public class Continentes implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CONTINENTE")
    private BigDecimal idContinente;
    @Basic(optional = false)
    @Column(name = "NOMBRE_CONTINENTE")
    private String nombreContinente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "continentesIdContinente")
    private Collection<Paises> paisesCollection;

    public Continentes() {
    }

    public Continentes(BigDecimal idContinente) {
        this.idContinente = idContinente;
    }

    public Continentes(BigDecimal idContinente, String nombreContinente) {
        this.idContinente = idContinente;
        this.nombreContinente = nombreContinente;
    }

    public BigDecimal getIdContinente() {
        return idContinente;
    }

    public void setIdContinente(BigDecimal idContinente) {
        this.idContinente = idContinente;
    }

    public String getNombreContinente() {
        return nombreContinente;
    }

    public void setNombreContinente(String nombreContinente) {
        this.nombreContinente = nombreContinente;
    }

    @XmlTransient
    public Collection<Paises> getPaisesCollection() {
        return paisesCollection;
    }

    public void setPaisesCollection(Collection<Paises> paisesCollection) {
        this.paisesCollection = paisesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContinente != null ? idContinente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Continentes)) {
            return false;
        }
        Continentes other = (Continentes) object;
        if ((this.idContinente == null && other.idContinente != null) || (this.idContinente != null && !this.idContinente.equals(other.idContinente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Continentes[ idContinente=" + idContinente + " ]";
    }
    
}
