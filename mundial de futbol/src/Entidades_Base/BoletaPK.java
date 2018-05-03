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
public class BoletaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "SILLAS_NUMERO")
    private BigInteger sillasNumero;
    @Basic(optional = false)
    @Column(name = "SILLAS_CATEGORIA")
    private String sillasCategoria;
    @Basic(optional = false)
    @Column(name = "SILLAS_UBICACION")
    private String sillasUbicacion;
    @Basic(optional = false)
    @Column(name = "PARTIDO_ID_PARTIDO")
    private BigInteger partidoIdPartido;
    @Basic(optional = false)
    @Column(name = "USUARIOS_ID_USUARIO")
    private BigInteger usuariosIdUsuario;

    public BoletaPK() {
    }

    public BoletaPK(BigInteger sillasNumero, String sillasCategoria, String sillasUbicacion, BigInteger partidoIdPartido, BigInteger usuariosIdUsuario) {
        this.sillasNumero = sillasNumero;
        this.sillasCategoria = sillasCategoria;
        this.sillasUbicacion = sillasUbicacion;
        this.partidoIdPartido = partidoIdPartido;
        this.usuariosIdUsuario = usuariosIdUsuario;
    }

    public BigInteger getSillasNumero() {
        return sillasNumero;
    }

    public void setSillasNumero(BigInteger sillasNumero) {
        this.sillasNumero = sillasNumero;
    }

    public String getSillasCategoria() {
        return sillasCategoria;
    }

    public void setSillasCategoria(String sillasCategoria) {
        this.sillasCategoria = sillasCategoria;
    }

    public String getSillasUbicacion() {
        return sillasUbicacion;
    }

    public void setSillasUbicacion(String sillasUbicacion) {
        this.sillasUbicacion = sillasUbicacion;
    }

    public BigInteger getPartidoIdPartido() {
        return partidoIdPartido;
    }

    public void setPartidoIdPartido(BigInteger partidoIdPartido) {
        this.partidoIdPartido = partidoIdPartido;
    }

    public BigInteger getUsuariosIdUsuario() {
        return usuariosIdUsuario;
    }

    public void setUsuariosIdUsuario(BigInteger usuariosIdUsuario) {
        this.usuariosIdUsuario = usuariosIdUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sillasNumero != null ? sillasNumero.hashCode() : 0);
        hash += (sillasCategoria != null ? sillasCategoria.hashCode() : 0);
        hash += (sillasUbicacion != null ? sillasUbicacion.hashCode() : 0);
        hash += (partidoIdPartido != null ? partidoIdPartido.hashCode() : 0);
        hash += (usuariosIdUsuario != null ? usuariosIdUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoletaPK)) {
            return false;
        }
        BoletaPK other = (BoletaPK) object;
        if ((this.sillasNumero == null && other.sillasNumero != null) || (this.sillasNumero != null && !this.sillasNumero.equals(other.sillasNumero))) {
            return false;
        }
        if ((this.sillasCategoria == null && other.sillasCategoria != null) || (this.sillasCategoria != null && !this.sillasCategoria.equals(other.sillasCategoria))) {
            return false;
        }
        if ((this.sillasUbicacion == null && other.sillasUbicacion != null) || (this.sillasUbicacion != null && !this.sillasUbicacion.equals(other.sillasUbicacion))) {
            return false;
        }
        if ((this.partidoIdPartido == null && other.partidoIdPartido != null) || (this.partidoIdPartido != null && !this.partidoIdPartido.equals(other.partidoIdPartido))) {
            return false;
        }
        if ((this.usuariosIdUsuario == null && other.usuariosIdUsuario != null) || (this.usuariosIdUsuario != null && !this.usuariosIdUsuario.equals(other.usuariosIdUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.BoletaPK[ sillasNumero=" + sillasNumero + ", sillasCategoria=" + sillasCategoria + ", sillasUbicacion=" + sillasUbicacion + ", partidoIdPartido=" + partidoIdPartido + ", usuariosIdUsuario=" + usuariosIdUsuario + " ]";
    }
    
}
