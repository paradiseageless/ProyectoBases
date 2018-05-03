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
@Table(name = "BOLETA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Boleta.findAll", query = "SELECT b FROM Boleta b")
    , @NamedQuery(name = "Boleta.findByValor", query = "SELECT b FROM Boleta b WHERE b.valor = :valor")
    , @NamedQuery(name = "Boleta.findBySillasNumero", query = "SELECT b FROM Boleta b WHERE b.boletaPK.sillasNumero = :sillasNumero")
    , @NamedQuery(name = "Boleta.findBySillasCategoria", query = "SELECT b FROM Boleta b WHERE b.boletaPK.sillasCategoria = :sillasCategoria")
    , @NamedQuery(name = "Boleta.findBySillasUbicacion", query = "SELECT b FROM Boleta b WHERE b.boletaPK.sillasUbicacion = :sillasUbicacion")
    , @NamedQuery(name = "Boleta.findByPartidoIdPartido", query = "SELECT b FROM Boleta b WHERE b.boletaPK.partidoIdPartido = :partidoIdPartido")
    , @NamedQuery(name = "Boleta.findByUsuariosIdUsuario", query = "SELECT b FROM Boleta b WHERE b.boletaPK.usuariosIdUsuario = :usuariosIdUsuario")})
public class Boleta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BoletaPK boletaPK;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private double valor;
    @JoinColumn(name = "PARTIDO_ID_PARTIDO", referencedColumnName = "ID_PARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partido partido;
    @JoinColumns({
        @JoinColumn(name = "SILLAS_NUMERO", referencedColumnName = "NUMERO", insertable = false, updatable = false)
        , @JoinColumn(name = "SILLAS_UBICACION", referencedColumnName = "UBICACION", insertable = false, updatable = false)
        , @JoinColumn(name = "SILLAS_CATEGORIA", referencedColumnName = "CATEGORIA", insertable = false, updatable = false)
        , @JoinColumn(name = "SILLAS_ESTADIO_ID_ESTADIO", referencedColumnName = "ESTADIO_ID_ESTADIO")})
    @ManyToOne(optional = false)
    private Sillas sillas;
    @JoinColumn(name = "USUARIOS_ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarios usuarios;

    public Boleta() {
    }

    public Boleta(BoletaPK boletaPK) {
        this.boletaPK = boletaPK;
    }

    public Boleta(BoletaPK boletaPK, double valor) {
        this.boletaPK = boletaPK;
        this.valor = valor;
    }

    public Boleta(BigInteger sillasNumero, String sillasCategoria, String sillasUbicacion, BigInteger partidoIdPartido, BigInteger usuariosIdUsuario) {
        this.boletaPK = new BoletaPK(sillasNumero, sillasCategoria, sillasUbicacion, partidoIdPartido, usuariosIdUsuario);
    }

    public BoletaPK getBoletaPK() {
        return boletaPK;
    }

    public void setBoletaPK(BoletaPK boletaPK) {
        this.boletaPK = boletaPK;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Sillas getSillas() {
        return sillas;
    }

    public void setSillas(Sillas sillas) {
        this.sillas = sillas;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (boletaPK != null ? boletaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Boleta)) {
            return false;
        }
        Boleta other = (Boleta) object;
        if ((this.boletaPK == null && other.boletaPK != null) || (this.boletaPK != null && !this.boletaPK.equals(other.boletaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Boleta[ boletaPK=" + boletaPK + " ]";
    }
    
}
