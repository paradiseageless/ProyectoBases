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
@Table(name = "ARBITRA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Arbitra.findAll", query = "SELECT a FROM Arbitra a")
    , @NamedQuery(name = "Arbitra.findByPartidoIdPartido", query = "SELECT a FROM Arbitra a WHERE a.arbitraPK.partidoIdPartido = :partidoIdPartido")
    , @NamedQuery(name = "Arbitra.findByJuezIdJuez", query = "SELECT a FROM Arbitra a WHERE a.arbitraPK.juezIdJuez = :juezIdJuez")
    , @NamedQuery(name = "Arbitra.findByTipo", query = "SELECT a FROM Arbitra a WHERE a.tipo = :tipo")})
public class Arbitra implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ArbitraPK arbitraPK;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumn(name = "JUEZ_ID_JUEZ", referencedColumnName = "ID_JUEZ", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Juez juez;
    @JoinColumn(name = "PARTIDO_ID_PARTIDO", referencedColumnName = "ID_PARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partido partido;

    public Arbitra() {
    }

    public Arbitra(ArbitraPK arbitraPK) {
        this.arbitraPK = arbitraPK;
    }

    public Arbitra(ArbitraPK arbitraPK, String tipo) {
        this.arbitraPK = arbitraPK;
        this.tipo = tipo;
    }

    public Arbitra(BigInteger partidoIdPartido, BigInteger juezIdJuez) {
        this.arbitraPK = new ArbitraPK(partidoIdPartido, juezIdJuez);
    }

    public ArbitraPK getArbitraPK() {
        return arbitraPK;
    }

    public void setArbitraPK(ArbitraPK arbitraPK) {
        this.arbitraPK = arbitraPK;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Juez getJuez() {
        return juez;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
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
        hash += (arbitraPK != null ? arbitraPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Arbitra)) {
            return false;
        }
        Arbitra other = (Arbitra) object;
        if ((this.arbitraPK == null && other.arbitraPK != null) || (this.arbitraPK != null && !this.arbitraPK.equals(other.arbitraPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Arbitra[ arbitraPK=" + arbitraPK + " ]";
    }
    
}
