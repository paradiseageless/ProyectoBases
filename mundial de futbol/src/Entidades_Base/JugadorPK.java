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
public class JugadorPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NUMERO_JUGADOR")
    private BigInteger numeroJugador;
    @Basic(optional = false)
    @Column(name = "EQUIPO_ID_EQUIPO")
    private BigInteger equipoIdEquipo;

    public JugadorPK() {
    }

    public JugadorPK(BigInteger numeroJugador, BigInteger equipoIdEquipo) {
        this.numeroJugador = numeroJugador;
        this.equipoIdEquipo = equipoIdEquipo;
    }

    public BigInteger getNumeroJugador() {
        return numeroJugador;
    }

    public void setNumeroJugador(BigInteger numeroJugador) {
        this.numeroJugador = numeroJugador;
    }

    public BigInteger getEquipoIdEquipo() {
        return equipoIdEquipo;
    }

    public void setEquipoIdEquipo(BigInteger equipoIdEquipo) {
        this.equipoIdEquipo = equipoIdEquipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroJugador != null ? numeroJugador.hashCode() : 0);
        hash += (equipoIdEquipo != null ? equipoIdEquipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JugadorPK)) {
            return false;
        }
        JugadorPK other = (JugadorPK) object;
        if ((this.numeroJugador == null && other.numeroJugador != null) || (this.numeroJugador != null && !this.numeroJugador.equals(other.numeroJugador))) {
            return false;
        }
        if ((this.equipoIdEquipo == null && other.equipoIdEquipo != null) || (this.equipoIdEquipo != null && !this.equipoIdEquipo.equals(other.equipoIdEquipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.JugadorPK[ numeroJugador=" + numeroJugador + ", equipoIdEquipo=" + equipoIdEquipo + " ]";
    }
    
}
