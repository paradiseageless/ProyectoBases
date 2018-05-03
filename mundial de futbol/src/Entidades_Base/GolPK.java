/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades_Base;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author danih
 */
@Embeddable
public class GolPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MINUTO")
    private BigInteger minuto;
    @Basic(optional = false)
    @Column(name = "PARTIDO_ID_PARTIDO")
    private BigInteger partidoIdPartido;
    @Basic(optional = false)
    @Column(name = "JUGADOR_NUMERO_JUGADOR")
    private BigInteger jugadorNumeroJugador;
    @Basic(optional = false)
    @Column(name = "JUGADOR_EQUIPO_ID_EQUIPO")
    private BigInteger jugadorEquipoIdEquipo;

    public GolPK() {
    }

    public GolPK(BigInteger minuto, BigInteger partidoIdPartido, BigInteger jugadorNumeroJugador, BigInteger jugadorEquipoIdEquipo) {
        this.minuto = minuto;
        this.partidoIdPartido = partidoIdPartido;
        this.jugadorNumeroJugador = jugadorNumeroJugador;
        this.jugadorEquipoIdEquipo = jugadorEquipoIdEquipo;
    }

    public BigInteger getMinuto() {
        return minuto;
    }

    public void setMinuto(BigInteger minuto) {
        this.minuto = minuto;
    }

    public BigInteger getPartidoIdPartido() {
        return partidoIdPartido;
    }

    public void setPartidoIdPartido(BigInteger partidoIdPartido) {
        this.partidoIdPartido = partidoIdPartido;
    }

    public BigInteger getJugadorNumeroJugador() {
        return jugadorNumeroJugador;
    }

    public void setJugadorNumeroJugador(BigInteger jugadorNumeroJugador) {
        this.jugadorNumeroJugador = jugadorNumeroJugador;
    }

    public BigInteger getJugadorEquipoIdEquipo() {
        return jugadorEquipoIdEquipo;
    }

    public void setJugadorEquipoIdEquipo(BigInteger jugadorEquipoIdEquipo) {
        this.jugadorEquipoIdEquipo = jugadorEquipoIdEquipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (minuto != null ? minuto.hashCode() : 0);
        hash += (partidoIdPartido != null ? partidoIdPartido.hashCode() : 0);
        hash += (jugadorNumeroJugador != null ? jugadorNumeroJugador.hashCode() : 0);
        hash += (jugadorEquipoIdEquipo != null ? jugadorEquipoIdEquipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GolPK)) {
            return false;
        }
        GolPK other = (GolPK) object;
        if ((this.minuto == null && other.minuto != null) || (this.minuto != null && !this.minuto.equals(other.minuto))) {
            return false;
        }
        if ((this.partidoIdPartido == null && other.partidoIdPartido != null) || (this.partidoIdPartido != null && !this.partidoIdPartido.equals(other.partidoIdPartido))) {
            return false;
        }
        if ((this.jugadorNumeroJugador == null && other.jugadorNumeroJugador != null) || (this.jugadorNumeroJugador != null && !this.jugadorNumeroJugador.equals(other.jugadorNumeroJugador))) {
            return false;
        }
        if ((this.jugadorEquipoIdEquipo == null && other.jugadorEquipoIdEquipo != null) || (this.jugadorEquipoIdEquipo != null && !this.jugadorEquipoIdEquipo.equals(other.jugadorEquipoIdEquipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.GolPK[ minuto=" + minuto + ", partidoIdPartido=" + partidoIdPartido + ", jugadorNumeroJugador=" + jugadorNumeroJugador + ", jugadorEquipoIdEquipo=" + jugadorEquipoIdEquipo + " ]";
    }
    
}
