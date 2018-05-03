/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coltrol_Base;

import Coltrol_Base.exceptions.NonexistentEntityException;
import Coltrol_Base.exceptions.PreexistingEntityException;
import Entidades_Base.Gol;
import Entidades_Base.GolPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades_Base.Jugador;
import Entidades_Base.Partido;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class GolJpaController implements Serializable {

    public GolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Gol gol) throws PreexistingEntityException, Exception {
        if (gol.getGolPK() == null) {
            gol.setGolPK(new GolPK());
        }
        gol.getGolPK().setJugadorEquipoIdEquipo(gol.getJugador().getJugadorPK().getEquipoIdEquipo());
        gol.getGolPK().setJugadorNumeroJugador(gol.getJugador().getJugadorPK().getNumeroJugador());
        
        BigDecimal idPar = gol.getPartido().getIdPartido();  //---------------------------------------------------modifiqué esto -- dhas
        gol.getGolPK().setPartidoIdPartido(idPar.toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador = gol.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getJugadorPK());
                gol.setJugador(jugador);
            }
            Partido partido = gol.getPartido();
            if (partido != null) {
                partido = em.getReference(partido.getClass(), partido.getIdPartido());
                gol.setPartido(partido);
            }
            em.persist(gol);
            if (jugador != null) {
                jugador.getGolCollection().add(gol);
                jugador = em.merge(jugador);
            }
            if (partido != null) {
                partido.getGolCollection().add(gol);
                partido = em.merge(partido);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGol(gol.getGolPK()) != null) {
                throw new PreexistingEntityException("Gol " + gol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Gol gol) throws NonexistentEntityException, Exception {
        gol.getGolPK().setJugadorEquipoIdEquipo(gol.getJugador().getJugadorPK().getEquipoIdEquipo());
        gol.getGolPK().setJugadorNumeroJugador(gol.getJugador().getJugadorPK().getNumeroJugador());
        
        BigDecimal idPar = gol.getPartido().getIdPartido() ; //---------------------------------------------------modifiqué esto -- dhas
        
        gol.getGolPK().setPartidoIdPartido(idPar.toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gol persistentGol = em.find(Gol.class, gol.getGolPK());
            Jugador jugadorOld = persistentGol.getJugador();
            Jugador jugadorNew = gol.getJugador();
            Partido partidoOld = persistentGol.getPartido();
            Partido partidoNew = gol.getPartido();
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getJugadorPK());
                gol.setJugador(jugadorNew);
            }
            if (partidoNew != null) {
                partidoNew = em.getReference(partidoNew.getClass(), partidoNew.getIdPartido());
                gol.setPartido(partidoNew);
            }
            gol = em.merge(gol);
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.getGolCollection().remove(gol);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.getGolCollection().add(gol);
                jugadorNew = em.merge(jugadorNew);
            }
            if (partidoOld != null && !partidoOld.equals(partidoNew)) {
                partidoOld.getGolCollection().remove(gol);
                partidoOld = em.merge(partidoOld);
            }
            if (partidoNew != null && !partidoNew.equals(partidoOld)) {
                partidoNew.getGolCollection().add(gol);
                partidoNew = em.merge(partidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                GolPK id = gol.getGolPK();
                if (findGol(id) == null) {
                    throw new NonexistentEntityException("The gol with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(GolPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gol gol;
            try {
                gol = em.getReference(Gol.class, id);
                gol.getGolPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gol with id " + id + " no longer exists.", enfe);
            }
            Jugador jugador = gol.getJugador();
            if (jugador != null) {
                jugador.getGolCollection().remove(gol);
                jugador = em.merge(jugador);
            }
            Partido partido = gol.getPartido();
            if (partido != null) {
                partido.getGolCollection().remove(gol);
                partido = em.merge(partido);
            }
            em.remove(gol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Gol> findGolEntities() {
        return findGolEntities(true, -1, -1);
    }

    public List<Gol> findGolEntities(int maxResults, int firstResult) {
        return findGolEntities(false, maxResults, firstResult);
    }

    private List<Gol> findGolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gol.class));
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

    public Gol findGol(GolPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Gol.class, id);
        } finally {
            em.close();
        }
    }

    public int getGolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gol> rt = cq.from(Gol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
