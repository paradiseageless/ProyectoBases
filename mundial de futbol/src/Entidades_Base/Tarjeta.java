/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades_Base;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author danih
 */
@Entity
@Table(name = "TARJETA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarjeta.findAll", query = "SELECT t FROM Tarjeta t")
    , @NamedQuery(name = "Tarjeta.findByColor", query = "SELECT t FROM Tarjeta t WHERE t.color = :color")
    , @NamedQuery(name = "Tarjeta.findByPartidoIdPartido", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.partidoIdPartido = :partidoIdPartido")
    , @NamedQuery(name = "Tarjeta.findByJugadorNumeroJugador", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.jugadorNumeroJugador = :jugadorNumeroJugador")
    , @NamedQuery(name = "Tarjeta.findByMinuto", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.minuto = :minuto")
    , @NamedQuery(name = "Tarjeta.findByJugadorEquipoIdEquipo", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.jugadorEquipoIdEquipo = :jugadorEquipoIdEquipo")})
public class Tarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TarjetaPK tarjetaPK;
    @Column(name = "COLOR")
    private String color;
    @JoinColumns({
        @JoinColumn(name = "JUGADOR_NUMERO_JUGADOR", referencedColumnName = "NUMERO_JUGADOR", insertable = false, updatable = false)
        , @JoinColumn(name = "JUGADOR_EQUIPO_ID_EQUIPO", referencedColumnName = "EQUIPO_ID_EQUIPO", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Jugador jugador;
    @JoinColumn(name = "PARTIDO_ID_PARTIDO", referencedColumnName = "ID_PARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partido partido;

    public Tarjeta() {
    }

    public Tarjeta(TarjetaPK tarjetaPK) {
        this.tarjetaPK = tarjetaPK;
    }

    public Tarjeta(BigInteger partidoIdPartido, BigInteger jugadorNumeroJugador, double minuto, BigInteger jugadorEquipoIdEquipo) {
        this.tarjetaPK = new TarjetaPK(partidoIdPartido, jugadorNumeroJugador, minuto, jugadorEquipoIdEquipo);
    }

    public TarjetaPK getTarjetaPK() {
        return tarjetaPK;
    }

    public void setTarjetaPK(TarjetaPK tarjetaPK) {
        this.tarjetaPK = tarjetaPK;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarjetaPK != null ? tarjetaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjeta)) {
            return false;
        }
        Tarjeta other = (Tarjeta) object;
        if ((this.tarjetaPK == null && other.tarjetaPK != null) || (this.tarjetaPK != null && !this.tarjetaPK.equals(other.tarjetaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Tarjeta[ tarjetaPK=" + tarjetaPK + " ]";
    }
    
}
