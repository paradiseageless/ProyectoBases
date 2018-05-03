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
@Table(name = "GOL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gol.findAll", query = "SELECT g FROM Gol g")
    , @NamedQuery(name = "Gol.findByMinuto", query = "SELECT g FROM Gol g WHERE g.golPK.minuto = :minuto")
    , @NamedQuery(name = "Gol.findByTiempo", query = "SELECT g FROM Gol g WHERE g.tiempo = :tiempo")
    , @NamedQuery(name = "Gol.findByTipoTiro", query = "SELECT g FROM Gol g WHERE g.tipoTiro = :tipoTiro")
    , @NamedQuery(name = "Gol.findByPartidoIdPartido", query = "SELECT g FROM Gol g WHERE g.golPK.partidoIdPartido = :partidoIdPartido")
    , @NamedQuery(name = "Gol.findByVar", query = "SELECT g FROM Gol g WHERE g.var = :var")
    , @NamedQuery(name = "Gol.findByJugadorNumeroJugador", query = "SELECT g FROM Gol g WHERE g.golPK.jugadorNumeroJugador = :jugadorNumeroJugador")
    , @NamedQuery(name = "Gol.findByJugadorEquipoIdEquipo", query = "SELECT g FROM Gol g WHERE g.golPK.jugadorEquipoIdEquipo = :jugadorEquipoIdEquipo")})
public class Gol implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GolPK golPK;
    @Basic(optional = false)
    @Column(name = "TIEMPO")
    private String tiempo;
    @Basic(optional = false)
    @Column(name = "TIPO_TIRO")
    private String tipoTiro;
    @Basic(optional = false)
    @Column(name = "VAR")
    private BigInteger var;
    @JoinColumns({
        @JoinColumn(name = "JUGADOR_NUMERO_JUGADOR", referencedColumnName = "NUMERO_JUGADOR", insertable = false, updatable = false)
        , @JoinColumn(name = "JUGADOR_EQUIPO_ID_EQUIPO", referencedColumnName = "EQUIPO_ID_EQUIPO", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Jugador jugador;
    @JoinColumn(name = "PARTIDO_ID_PARTIDO", referencedColumnName = "ID_PARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partido partido;

    public Gol() {
    }

    public Gol(GolPK golPK) {
        this.golPK = golPK;
    }

    public Gol(GolPK golPK, String tiempo, String tipoTiro, BigInteger var) {
        this.golPK = golPK;
        this.tiempo = tiempo;
        this.tipoTiro = tipoTiro;
        this.var = var;
    }

    public Gol(BigInteger minuto, BigInteger partidoIdPartido, BigInteger jugadorNumeroJugador, BigInteger jugadorEquipoIdEquipo) {
        this.golPK = new GolPK(minuto, partidoIdPartido, jugadorNumeroJugador, jugadorEquipoIdEquipo);
    }

    public GolPK getGolPK() {
        return golPK;
    }

    public void setGolPK(GolPK golPK) {
        this.golPK = golPK;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getTipoTiro() {
        return tipoTiro;
    }

    public void setTipoTiro(String tipoTiro) {
        this.tipoTiro = tipoTiro;
    }

    public BigInteger getVar() {
        return var;
    }

    public void setVar(BigInteger var) {
        this.var = var;
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
        hash += (golPK != null ? golPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gol)) {
            return false;
        }
        Gol other = (Gol) object;
        if ((this.golPK == null && other.golPK != null) || (this.golPK != null && !this.golPK.equals(other.golPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.Gol[ golPK=" + golPK + " ]";
    }
    
}
