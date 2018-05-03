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
@Table(name = "PAISES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paises.findAll", query = "SELECT p FROM Paises p")
    , @NamedQuery(name = "Paises.findByIdPais", query = "SELECT p FROM Paises p WHERE p.idPais = :idPais")
    , @NamedQuery(name = "Paises.findByNombrePais", query = "SELECT p FROM Paises p WHERE p.nombrePais = :nombrePais")
    , @NamedQuery(name = "Paises.findByNumeroHabitantes", query = "SELECT p FROM Paises p WHERE p.numeroHabitantes = :numeroHabitantes")})
public class Paises implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PAIS")
    private BigDecimal idPais;
    @Basic(optional = false)
    @Column(name = "NOMBRE_PAIS")
    private String nombrePais;
    @Column(name = "NUMERO_HABITANTES")
    private BigInteger numeroHabitantes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nacionalidad")
    private Collection<Juez> juezCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lugarNacimiento")
    private Collection<Jugador> jugadorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paisesIdPais")
    private Collection<Usuarios> usuariosCollection;
    @JoinColumn(name = "CONTINENTES_ID_CONTINENTE", referencedColumnName = "ID_CONTINENTE")
    @ManyToOne(optional = false)
    private Continentes continentesIdContinente;

    public Paises() {
    }

    public Paises(BigDecimal idPais) {
        this.idPais = idPais;
    }

    public Paises(BigDecimal idPais, String nombrePais) {
        this.idPais = idPais;
        this.nombrePais = nombrePais;
    }

    public BigDecimal getIdPais() {
        return idPais;
    }

    public void setIdPais(BigDecimal idPais) {
        this.idPais = idPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public BigInteger getNumeroHabitantes() {
        return numeroHabitantes;
    }

    public void setNumeroHabitantes(BigInteger numeroHabitantes) {
        this.numeroHabitantes = numeroHabitantes;
    }

    @XmlTransient
    public Collection<Juez> getJuezCollection() {
        return juezCollection;
    }

    public void setJuezCollection(Collection<Juez> juezCollection) {
        this.juezCollection = juezCollection;
    }

    @XmlTransient
    public Collection<Jugador> getJugadorCollection() {
        return jugadorCollection;
    }

    public void setJugadorCollection(Collection<Jugador> jugadorCollection) {
        this.jugadorCollection = jugadorCollection;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }

    public Continentes getContinentesIdContinente() {
        return continentesIdContinente;
    }

    public void setContinentesIdContinente(Continentes continentesIdContinente) {
        this.continentesIdContinente = continentesIdContinente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPais != null ? idPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paises)) {
            return false;
        }
        Paises other = (Paises) object;
        if ((this.idPais == null && other.idPais != null) || (this.idPais != null && !this.idPais.equals(other.idPais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Paises[ idPais=" + idPais + " ]";
    }
    
}
