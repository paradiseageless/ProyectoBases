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
public class ArbitraPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "PARTIDO_ID_PARTIDO")
    private BigInteger partidoIdPartido;
    @Basic(optional = false)
    @Column(name = "JUEZ_ID_JUEZ")
    private BigInteger juezIdJuez;

    public ArbitraPK() {
    }

    public ArbitraPK(BigInteger partidoIdPartido, BigInteger juezIdJuez) {
        this.partidoIdPartido = partidoIdPartido;
        this.juezIdJuez = juezIdJuez;
    }

    public BigInteger getPartidoIdPartido() {
        return partidoIdPartido;
    }

    public void setPartidoIdPartido(BigInteger partidoIdPartido) {
        this.partidoIdPartido = partidoIdPartido;
    }

    public BigInteger getJuezIdJuez() {
        return juezIdJuez;
    }

    public void setJuezIdJuez(BigInteger juezIdJuez) {
        this.juezIdJuez = juezIdJuez;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partidoIdPartido != null ? partidoIdPartido.hashCode() : 0);
        hash += (juezIdJuez != null ? juezIdJuez.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArbitraPK)) {
            return false;
        }
        ArbitraPK other = (ArbitraPK) object;
        if ((this.partidoIdPartido == null && other.partidoIdPartido != null) || (this.partidoIdPartido != null && !this.partidoIdPartido.equals(other.partidoIdPartido))) {
            return false;
        }
        if ((this.juezIdJuez == null && other.juezIdJuez != null) || (this.juezIdJuez != null && !this.juezIdJuez.equals(other.juezIdJuez))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.ArbitraPK[ partidoIdPartido=" + partidoIdPartido + ", juezIdJuez=" + juezIdJuez + " ]";
    }
    
}
