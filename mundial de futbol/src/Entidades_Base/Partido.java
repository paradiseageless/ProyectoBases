/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades_Base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author danih
 */
@Entity
@Table(name = "PARTIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partido.findAll", query = "SELECT p FROM Partido p")
    , @NamedQuery(name = "Partido.findByIdPartido", query = "SELECT p FROM Partido p WHERE p.idPartido = :idPartido")
    , @NamedQuery(name = "Partido.findByFechaPartido", query = "SELECT p FROM Partido p WHERE p.fechaPartido = :fechaPartido")
    , @NamedQuery(name = "Partido.findByFase", query = "SELECT p FROM Partido p WHERE p.fase = :fase")})
public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PARTIDO")
    private BigDecimal idPartido;
    @Basic(optional = false)
    @Column(name = "FECHA_PARTIDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPartido;
    @Basic(optional = false)
    @Column(name = "FASE")
    private String fase;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private Collection<Gol> golCollection;
    @JoinColumn(name = "EQUIPO_ID_EQUIPO", referencedColumnName = "ID_EQUIPO")
    @ManyToOne(optional = false)
    private Equipo equipoIdEquipo;
    @JoinColumn(name = "EQUIPO_ID_EQUIPO1", referencedColumnName = "ID_EQUIPO")
    @ManyToOne(optional = false)
    private Equipo equipoIdEquipo1;
    @JoinColumn(name = "ESTADIO_ID_ESTADIO", referencedColumnName = "ID_ESTADIO")
    @ManyToOne(optional = false)
    private Estadio estadioIdEstadio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private Collection<Arbitra> arbitraCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private Collection<Boleta> boletaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private Collection<Tarjeta> tarjetaCollection;

    public Partido() {
    }

    public Partido(BigDecimal idPartido) {
        this.idPartido = idPartido;
    }

    public Partido(BigDecimal idPartido, Date fechaPartido, String fase) {
        this.idPartido = idPartido;
        this.fechaPartido = fechaPartido;
        this.fase = fase;
    }

    public BigDecimal getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(BigDecimal idPartido) {
        this.idPartido = idPartido;
    }

    public Date getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(Date fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    @XmlTransient
    public Collection<Gol> getGolCollection() {
        return golCollection;
    }

    public void setGolCollection(Collection<Gol> golCollection) {
        this.golCollection = golCollection;
    }

    public Equipo getEquipoIdEquipo() {
        return equipoIdEquipo;
    }

    public void setEquipoIdEquipo(Equipo equipoIdEquipo) {
        this.equipoIdEquipo = equipoIdEquipo;
    }

    public Equipo getEquipoIdEquipo1() {
        return equipoIdEquipo1;
    }

    public void setEquipoIdEquipo1(Equipo equipoIdEquipo1) {
        this.equipoIdEquipo1 = equipoIdEquipo1;
    }

    public Estadio getEstadioIdEstadio() {
        return estadioIdEstadio;
    }

    public void setEstadioIdEstadio(Estadio estadioIdEstadio) {
        this.estadioIdEstadio = estadioIdEstadio;
    }

    @XmlTransient
    public Collection<Arbitra> getArbitraCollection() {
        return arbitraCollection;
    }

    public void setArbitraCollection(Collection<Arbitra> arbitraCollection) {
        this.arbitraCollection = arbitraCollection;
    }

    @XmlTransient
    public Collection<Boleta> getBoletaCollection() {
        return boletaCollection;
    }

    public void setBoletaCollection(Collection<Boleta> boletaCollection) {
        this.boletaCollection = boletaCollection;
    }

    @XmlTransient
    public Collection<Tarjeta> getTarjetaCollection() {
        return tarjetaCollection;
    }

    public void setTarjetaCollection(Collection<Tarjeta> tarjetaCollection) {
        this.tarjetaCollection = tarjetaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPartido != null ? idPartido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partido)) {
            return false;
        }
        Partido other = (Partido) object;
        if ((this.idPartido == null && other.idPartido != null) || (this.idPartido != null && !this.idPartido.equals(other.idPartido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Partido[ idPartido=" + idPartido + " ]";
    }
    
}
