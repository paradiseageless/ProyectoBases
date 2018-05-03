/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades_Base;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "JUGADOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jugador.findAll", query = "SELECT j FROM Jugador j")
    , @NamedQuery(name = "Jugador.findByNumeroJugador", query = "SELECT j FROM Jugador j WHERE j.jugadorPK.numeroJugador = :numeroJugador")
    , @NamedQuery(name = "Jugador.findByNombreJugador", query = "SELECT j FROM Jugador j WHERE j.nombreJugador = :nombreJugador")
    , @NamedQuery(name = "Jugador.findByApellidoJugador", query = "SELECT j FROM Jugador j WHERE j.apellidoJugador = :apellidoJugador")
    , @NamedQuery(name = "Jugador.findByFechaNacimiento", query = "SELECT j FROM Jugador j WHERE j.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Jugador.findByEstatura", query = "SELECT j FROM Jugador j WHERE j.estatura = :estatura")
    , @NamedQuery(name = "Jugador.findByPeso", query = "SELECT j FROM Jugador j WHERE j.peso = :peso")
    , @NamedQuery(name = "Jugador.findByEquipoIdEquipo", query = "SELECT j FROM Jugador j WHERE j.jugadorPK.equipoIdEquipo = :equipoIdEquipo")})
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JugadorPK jugadorPK;
    @Basic(optional = false)
    @Column(name = "NOMBRE_JUGADOR")
    private String nombreJugador;
    @Basic(optional = false)
    @Column(name = "APELLIDO_JUGADOR")
    private String apellidoJugador;
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ESTATURA")
    private Double estatura;
    @Column(name = "PESO")
    private Double peso;
    @JoinColumn(name = "EQUIPO_ID_EQUIPO", referencedColumnName = "ID_EQUIPO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Equipo equipo;
    @JoinColumn(name = "LUGAR_NACIMIENTO", referencedColumnName = "ID_PAIS")
    @ManyToOne(optional = false)
    private Paises lugarNacimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugador")
    private Collection<Gol> golCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugador")
    private Collection<Tarjeta> tarjetaCollection;

    public Jugador() {
    }

    public Jugador(JugadorPK jugadorPK) {
        this.jugadorPK = jugadorPK;
    }

    public Jugador(JugadorPK jugadorPK, String nombreJugador, String apellidoJugador) {
        this.jugadorPK = jugadorPK;
        this.nombreJugador = nombreJugador;
        this.apellidoJugador = apellidoJugador;
    }

    public Jugador(BigInteger numeroJugador, BigInteger equipoIdEquipo) {
        this.jugadorPK = new JugadorPK(numeroJugador, equipoIdEquipo);
    }

    public JugadorPK getJugadorPK() {
        return jugadorPK;
    }

    public void setJugadorPK(JugadorPK jugadorPK) {
        this.jugadorPK = jugadorPK;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getApellidoJugador() {
        return apellidoJugador;
    }

    public void setApellidoJugador(String apellidoJugador) {
        this.apellidoJugador = apellidoJugador;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Double getEstatura() {
        return estatura;
    }

    public void setEstatura(Double estatura) {
        this.estatura = estatura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Paises getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(Paises lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    @XmlTransient
    public Collection<Gol> getGolCollection() {
        return golCollection;
    }

    public void setGolCollection(Collection<Gol> golCollection) {
        this.golCollection = golCollection;
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
        hash += (jugadorPK != null ? jugadorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jugador)) {
            return false;
        }
        Jugador other = (Jugador) object;
        if ((this.jugadorPK == null && other.jugadorPK != null) || (this.jugadorPK != null && !this.jugadorPK.equals(other.jugadorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Jugador[ jugadorPK=" + jugadorPK + " ]";
    }
    
}
