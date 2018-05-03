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
public class SillasPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NUMERO")
    private BigInteger numero;
    @Basic(optional = false)
    @Column(name = "CATEGORIA")
    private String categoria;
    @Basic(optional = false)
    @Column(name = "UBICACION")
    private String ubicacion;
    @Basic(optional = false)
    @Column(name = "ESTADIO_ID_ESTADIO")
    private BigInteger estadioIdEstadio;

    public SillasPK() {
    }

    public SillasPK(BigInteger numero, String categoria, String ubicacion, BigInteger estadioIdEstadio) {
        this.numero = numero;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.estadioIdEstadio = estadioIdEstadio;
    }

    public BigInteger getNumero() {
        return numero;
    }

    public void setNumero(BigInteger numero) {
        this.numero = numero;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public BigInteger getEstadioIdEstadio() {
        return estadioIdEstadio;
    }

    public void setEstadioIdEstadio(BigInteger estadioIdEstadio) {
        this.estadioIdEstadio = estadioIdEstadio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        hash += (categoria != null ? categoria.hashCode() : 0);
        hash += (ubicacion != null ? ubicacion.hashCode() : 0);
        hash += (estadioIdEstadio != null ? estadioIdEstadio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SillasPK)) {
            return false;
        }
        SillasPK other = (SillasPK) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        if ((this.categoria == null && other.categoria != null) || (this.categoria != null && !this.categoria.equals(other.categoria))) {
            return false;
        }
        if ((this.ubicacion == null && other.ubicacion != null) || (this.ubicacion != null && !this.ubicacion.equals(other.ubicacion))) {
            return false;
        }
        if ((this.estadioIdEstadio == null && other.estadioIdEstadio != null) || (this.estadioIdEstadio != null && !this.estadioIdEstadio.equals(other.estadioIdEstadio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "conexion.SillasPK[ numero=" + numero + ", categoria=" + categoria + ", ubicacion=" + ubicacion + ", estadioIdEstadio=" + estadioIdEstadio + " ]";
    }
    
}
