/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coltrol_Base;

import Coltrol_Base.exceptions.NonexistentEntityException;
import Coltrol_Base.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades_Base.Jugador;
import Entidades_Base.Partido;
import Entidades_Base.Tarjeta;
import Entidades_Base.TarjetaPK;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class TarjetaJpaController implements Serializable {

    public TarjetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjeta tarjeta) throws PreexistingEntityException, Exception {
        if (tarjeta.getTarjetaPK() == null) {
            tarjeta.setTarjetaPK(new TarjetaPK());
        }
        
        BigDecimal idPar = tarjeta.getPartido().getIdPartido(); //---------------------------------------------------modifiqué esto -- dhas
        tarjeta.getTarjetaPK().setPartidoIdPartido(idPar.toBigInteger());
        tarjeta.getTarjetaPK().setJugadorNumeroJugador(tarjeta.getJugador().getJugadorPK().getNumeroJugador());
        tarjeta.getTarjetaPK().setJugadorEquipoIdEquipo(tarjeta.getJugador().getJugadorPK().getEquipoIdEquipo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador = tarjeta.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getJugadorPK());
                tarjeta.setJugador(jugador);
            }
            Partido partido = tarjeta.getPartido();
            if (partido != null) {
                partido = em.getReference(partido.getClass(), partido.getIdPartido());
                tarjeta.setPartido(partido);
            }
            em.persist(tarjeta);
            if (jugador != null) {
                jugador.getTarjetaCollection().add(tarjeta);
                jugador = em.merge(jugador);
            }
            if (partido != null) {
                partido.getTarjetaCollection().add(tarjeta);
                partido = em.merge(partido);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTarjeta(tarjeta.getTarjetaPK()) != null) {
                throw new PreexistingEntityException("Tarjeta " + tarjeta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarjeta tarjeta) throws NonexistentEntityException, Exception {
        
        BigDecimal idPar = tarjeta.getPartido().getIdPartido(); //---------------------------------------------------modifiqué esto -- dhas
        tarjeta.getTarjetaPK().setPartidoIdPartido(idPar.toBigInteger());
        tarjeta.getTarjetaPK().setJugadorNumeroJugador(tarjeta.getJugador().getJugadorPK().getNumeroJugador());
        tarjeta.getTarjetaPK().setJugadorEquipoIdEquipo(tarjeta.getJugador().getJugadorPK().getEquipoIdEquipo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjeta persistentTarjeta = em.find(Tarjeta.class, tarjeta.getTarjetaPK());
            Jugador jugadorOld = persistentTarjeta.getJugador();
            Jugador jugadorNew = tarjeta.getJugador();
            Partido partidoOld = persistentTarjeta.getPartido();
            Partido partidoNew = tarjeta.getPartido();
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getJugadorPK());
                tarjeta.setJugador(jugadorNew);
            }
            if (partidoNew != null) {
                partidoNew = em.getReference(partidoNew.getClass(), partidoNew.getIdPartido());
                tarjeta.setPartido(partidoNew);
            }
            tarjeta = em.merge(tarjeta);
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.getTarjetaCollection().remove(tarjeta);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.getTarjetaCollection().add(tarjeta);
                jugadorNew = em.merge(jugadorNew);
            }
            if (partidoOld != null && !partidoOld.equals(partidoNew)) {
                partidoOld.getTarjetaCollection().remove(tarjeta);
                partidoOld = em.merge(partidoOld);
            }
            if (partidoNew != null && !partidoNew.equals(partidoOld)) {
                partidoNew.getTarjetaCollection().add(tarjeta);
                partidoNew = em.merge(partidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TarjetaPK id = tarjeta.getTarjetaPK();
                if (findTarjeta(id) == null) {
                    throw new NonexistentEntityException("The tarjeta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TarjetaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjeta tarjeta;
            try {
                tarjeta = em.getReference(Tarjeta.class, id);
                tarjeta.getTarjetaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjeta with id " + id + " no longer exists.", enfe);
            }
            Jugador jugador = tarjeta.getJugador();
            if (jugador != null) {
                jugador.getTarjetaCollection().remove(tarjeta);
                jugador = em.merge(jugador);
            }
            Partido partido = tarjeta.getPartido();
            if (partido != null) {
                partido.getTarjetaCollection().remove(tarjeta);
                partido = em.merge(partido);
            }
            em.remove(tarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarjeta> findTarjetaEntities() {
        return findTarjetaEntities(true, -1, -1);
    }

    public List<Tarjeta> findTarjetaEntities(int maxResults, int firstResult) {
        return findTarjetaEntities(false, maxResults, firstResult);
    }

    private List<Tarjeta> findTarjetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjeta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tarjeta findTarjeta(TarjetaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjeta> rt = cq.from(Tarjeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
