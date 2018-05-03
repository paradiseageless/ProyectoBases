/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coltrol_Base;

import Coltrol_Base.exceptions.IllegalOrphanException;
import Coltrol_Base.exceptions.NonexistentEntityException;
import Coltrol_Base.exceptions.PreexistingEntityException;
import Entidades_Base.Equipo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades_Base.Grupo;
import Entidades_Base.Jugador;
import java.util.ArrayList;
import java.util.Collection;
import Entidades_Base.Partido;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class EquipoJpaController implements Serializable {

    public EquipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Equipo equipo) throws PreexistingEntityException, Exception {
        if (equipo.getJugadorCollection() == null) {
            equipo.setJugadorCollection(new ArrayList<Jugador>());
        }
        if (equipo.getPartidoCollection() == null) {
            equipo.setPartidoCollection(new ArrayList<Partido>());
        }
        if (equipo.getPartidoCollection1() == null) {
            equipo.setPartidoCollection1(new ArrayList<Partido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupoIdGrupo = equipo.getGrupoIdGrupo();
            if (grupoIdGrupo != null) {
                grupoIdGrupo = em.getReference(grupoIdGrupo.getClass(), grupoIdGrupo.getIdGrupo());
                equipo.setGrupoIdGrupo(grupoIdGrupo);
            }
            Collection<Jugador> attachedJugadorCollection = new ArrayList<Jugador>();
            for (Jugador jugadorCollectionJugadorToAttach : equipo.getJugadorCollection()) {
                jugadorCollectionJugadorToAttach = em.getReference(jugadorCollectionJugadorToAttach.getClass(), jugadorCollectionJugadorToAttach.getJugadorPK());
                attachedJugadorCollection.add(jugadorCollectionJugadorToAttach);
            }
            equipo.setJugadorCollection(attachedJugadorCollection);
            Collection<Partido> attachedPartidoCollection = new ArrayList<Partido>();
            for (Partido partidoCollectionPartidoToAttach : equipo.getPartidoCollection()) {
                partidoCollectionPartidoToAttach = em.getReference(partidoCollectionPartidoToAttach.getClass(), partidoCollectionPartidoToAttach.getIdPartido());
                attachedPartidoCollection.add(partidoCollectionPartidoToAttach);
            }
            equipo.setPartidoCollection(attachedPartidoCollection);
            Collection<Partido> attachedPartidoCollection1 = new ArrayList<Partido>();
            for (Partido partidoCollection1PartidoToAttach : equipo.getPartidoCollection1()) {
                partidoCollection1PartidoToAttach = em.getReference(partidoCollection1PartidoToAttach.getClass(), partidoCollection1PartidoToAttach.getIdPartido());
                attachedPartidoCollection1.add(partidoCollection1PartidoToAttach);
            }
            equipo.setPartidoCollection1(attachedPartidoCollection1);
            em.persist(equipo);
            if (grupoIdGrupo != null) {
                grupoIdGrupo.getEquipoCollection().add(equipo);
                grupoIdGrupo = em.merge(grupoIdGrupo);
            }
            for (Jugador jugadorCollectionJugador : equipo.getJugadorCollection()) {
                Equipo oldEquipoOfJugadorCollectionJugador = jugadorCollectionJugador.getEquipo();
                jugadorCollectionJugador.setEquipo(equipo);
                jugadorCollectionJugador = em.merge(jugadorCollectionJugador);
                if (oldEquipoOfJugadorCollectionJugador != null) {
                    oldEquipoOfJugadorCollectionJugador.getJugadorCollection().remove(jugadorCollectionJugador);
                    oldEquipoOfJugadorCollectionJugador = em.merge(oldEquipoOfJugadorCollectionJugador);
                }
            }
            for (Partido partidoCollectionPartido : equipo.getPartidoCollection()) {
                Equipo oldEquipoIdEquipoOfPartidoCollectionPartido = partidoCollectionPartido.getEquipoIdEquipo();
                partidoCollectionPartido.setEquipoIdEquipo(equipo);
                partidoCollectionPartido = em.merge(partidoCollectionPartido);
                if (oldEquipoIdEquipoOfPartidoCollectionPartido != null) {
                    oldEquipoIdEquipoOfPartidoCollectionPartido.getPartidoCollection().remove(partidoCollectionPartido);
                    oldEquipoIdEquipoOfPartidoCollectionPartido = em.merge(oldEquipoIdEquipoOfPartidoCollectionPartido);
                }
            }
            for (Partido partidoCollection1Partido : equipo.getPartidoCollection1()) {
                Equipo oldEquipoIdEquipo1OfPartidoCollection1Partido = partidoCollection1Partido.getEquipoIdEquipo1();
                partidoCollection1Partido.setEquipoIdEquipo1(equipo);
                partidoCollection1Partido = em.merge(partidoCollection1Partido);
                if (oldEquipoIdEquipo1OfPartidoCollection1Partido != null) {
                    oldEquipoIdEquipo1OfPartidoCollection1Partido.getPartidoCollection1().remove(partidoCollection1Partido);
                    oldEquipoIdEquipo1OfPartidoCollection1Partido = em.merge(oldEquipoIdEquipo1OfPartidoCollection1Partido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEquipo(equipo.getIdEquipo()) != null) {
                throw new PreexistingEntityException("Equipo " + equipo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Equipo equipo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo persistentEquipo = em.find(Equipo.class, equipo.getIdEquipo());
            Grupo grupoIdGrupoOld = persistentEquipo.getGrupoIdGrupo();
            Grupo grupoIdGrupoNew = equipo.getGrupoIdGrupo();
            Collection<Jugador> jugadorCollectionOld = persistentEquipo.getJugadorCollection();
            Collection<Jugador> jugadorCollectionNew = equipo.getJugadorCollection();
            Collection<Partido> partidoCollectionOld = persistentEquipo.getPartidoCollection();
            Collection<Partido> partidoCollectionNew = equipo.getPartidoCollection();
            Collection<Partido> partidoCollection1Old = persistentEquipo.getPartidoCollection1();
            Collection<Partido> partidoCollection1New = equipo.getPartidoCollection1();
            List<String> illegalOrphanMessages = null;
            for (Jugador jugadorCollectionOldJugador : jugadorCollectionOld) {
                if (!jugadorCollectionNew.contains(jugadorCollectionOldJugador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jugador " + jugadorCollectionOldJugador + " since its equipo field is not nullable.");
                }
            }
            for (Partido partidoCollectionOldPartido : partidoCollectionOld) {
                if (!partidoCollectionNew.contains(partidoCollectionOldPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Partido " + partidoCollectionOldPartido + " since its equipoIdEquipo field is not nullable.");
                }
            }
            for (Partido partidoCollection1OldPartido : partidoCollection1Old) {
                if (!partidoCollection1New.contains(partidoCollection1OldPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Partido " + partidoCollection1OldPartido + " since its equipoIdEquipo1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (grupoIdGrupoNew != null) {
                grupoIdGrupoNew = em.getReference(grupoIdGrupoNew.getClass(), grupoIdGrupoNew.getIdGrupo());
                equipo.setGrupoIdGrupo(grupoIdGrupoNew);
            }
            Collection<Jugador> attachedJugadorCollectionNew = new ArrayList<Jugador>();
            for (Jugador jugadorCollectionNewJugadorToAttach : jugadorCollectionNew) {
                jugadorCollectionNewJugadorToAttach = em.getReference(jugadorCollectionNewJugadorToAttach.getClass(), jugadorCollectionNewJugadorToAttach.getJugadorPK());
                attachedJugadorCollectionNew.add(jugadorCollectionNewJugadorToAttach);
            }
            jugadorCollectionNew = attachedJugadorCollectionNew;
            equipo.setJugadorCollection(jugadorCollectionNew);
            Collection<Partido> attachedPartidoCollectionNew = new ArrayList<Partido>();
            for (Partido partidoCollectionNewPartidoToAttach : partidoCollectionNew) {
                partidoCollectionNewPartidoToAttach = em.getReference(partidoCollectionNewPartidoToAttach.getClass(), partidoCollectionNewPartidoToAttach.getIdPartido());
                attachedPartidoCollectionNew.add(partidoCollectionNewPartidoToAttach);
            }
            partidoCollectionNew = attachedPartidoCollectionNew;
            equipo.setPartidoCollection(partidoCollectionNew);
            Collection<Partido> attachedPartidoCollection1New = new ArrayList<Partido>();
            for (Partido partidoCollection1NewPartidoToAttach : partidoCollection1New) {
                partidoCollection1NewPartidoToAttach = em.getReference(partidoCollection1NewPartidoToAttach.getClass(), partidoCollection1NewPartidoToAttach.getIdPartido());
                attachedPartidoCollection1New.add(partidoCollection1NewPartidoToAttach);
            }
            partidoCollection1New = attachedPartidoCollection1New;
            equipo.setPartidoCollection1(partidoCollection1New);
            equipo = em.merge(equipo);
            if (grupoIdGrupoOld != null && !grupoIdGrupoOld.equals(grupoIdGrupoNew)) {
                grupoIdGrupoOld.getEquipoCollection().remove(equipo);
                grupoIdGrupoOld = em.merge(grupoIdGrupoOld);
            }
            if (grupoIdGrupoNew != null && !grupoIdGrupoNew.equals(grupoIdGrupoOld)) {
                grupoIdGrupoNew.getEquipoCollection().add(equipo);
                grupoIdGrupoNew = em.merge(grupoIdGrupoNew);
            }
            for (Jugador jugadorCollectionNewJugador : jugadorCollectionNew) {
                if (!jugadorCollectionOld.contains(jugadorCollectionNewJugador)) {
                    Equipo oldEquipoOfJugadorCollectionNewJugador = jugadorCollectionNewJugador.getEquipo();
                    jugadorCollectionNewJugador.setEquipo(equipo);
                    jugadorCollectionNewJugador = em.merge(jugadorCollectionNewJugador);
                    if (oldEquipoOfJugadorCollectionNewJugador != null && !oldEquipoOfJugadorCollectionNewJugador.equals(equipo)) {
                        oldEquipoOfJugadorCollectionNewJugador.getJugadorCollection().remove(jugadorCollectionNewJugador);
                        oldEquipoOfJugadorCollectionNewJugador = em.merge(oldEquipoOfJugadorCollectionNewJugador);
                    }
                }
            }
            for (Partido partidoCollectionNewPartido : partidoCollectionNew) {
                if (!partidoCollectionOld.contains(partidoCollectionNewPartido)) {
                    Equipo oldEquipoIdEquipoOfPartidoCollectionNewPartido = partidoCollectionNewPartido.getEquipoIdEquipo();
                    partidoCollectionNewPartido.setEquipoIdEquipo(equipo);
                    partidoCollectionNewPartido = em.merge(partidoCollectionNewPartido);
                    if (oldEquipoIdEquipoOfPartidoCollectionNewPartido != null && !oldEquipoIdEquipoOfPartidoCollectionNewPartido.equals(equipo)) {
                        oldEquipoIdEquipoOfPartidoCollectionNewPartido.getPartidoCollection().remove(partidoCollectionNewPartido);
                        oldEquipoIdEquipoOfPartidoCollectionNewPartido = em.merge(oldEquipoIdEquipoOfPartidoCollectionNewPartido);
                    }
                }
            }
            for (Partido partidoCollection1NewPartido : partidoCollection1New) {
                if (!partidoCollection1Old.contains(partidoCollection1NewPartido)) {
                    Equipo oldEquipoIdEquipo1OfPartidoCollection1NewPartido = partidoCollection1NewPartido.getEquipoIdEquipo1();
                    partidoCollection1NewPartido.setEquipoIdEquipo1(equipo);
                    partidoCollection1NewPartido = em.merge(partidoCollection1NewPartido);
                    if (oldEquipoIdEquipo1OfPartidoCollection1NewPartido != null && !oldEquipoIdEquipo1OfPartidoCollection1NewPartido.equals(equipo)) {
                        oldEquipoIdEquipo1OfPartidoCollection1NewPartido.getPartidoCollection1().remove(partidoCollection1NewPartido);
                        oldEquipoIdEquipo1OfPartidoCollection1NewPartido = em.merge(oldEquipoIdEquipo1OfPartidoCollection1NewPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = equipo.getIdEquipo();
                if (findEquipo(id) == null) {
                    throw new NonexistentEntityException("The equipo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipo;
            try {
                equipo = em.getReference(Equipo.class, id);
                equipo.getIdEquipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Jugador> jugadorCollectionOrphanCheck = equipo.getJugadorCollection();
            for (Jugador jugadorCollectionOrphanCheckJugador : jugadorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the Jugador " + jugadorCollectionOrphanCheckJugador + " in its jugadorCollection field has a non-nullable equipo field.");
            }
            Collection<Partido> partidoCollectionOrphanCheck = equipo.getPartidoCollection();
            for (Partido partidoCollectionOrphanCheckPartido : partidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the Partido " + partidoCollectionOrphanCheckPartido + " in its partidoCollection field has a non-nullable equipoIdEquipo field.");
            }
            Collection<Partido> partidoCollection1OrphanCheck = equipo.getPartidoCollection1();
            for (Partido partidoCollection1OrphanCheckPartido : partidoCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the Partido " + partidoCollection1OrphanCheckPartido + " in its partidoCollection1 field has a non-nullable equipoIdEquipo1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grupo grupoIdGrupo = equipo.getGrupoIdGrupo();
            if (grupoIdGrupo != null) {
                grupoIdGrupo.getEquipoCollection().remove(equipo);
                grupoIdGrupo = em.merge(grupoIdGrupo);
            }
            em.remove(equipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Equipo> findEquipoEntities() {
        return findEquipoEntities(true, -1, -1);
    }

    public List<Equipo> findEquipoEntities(int maxResults, int firstResult) {
        return findEquipoEntities(false, maxResults, firstResult);
    }

    private List<Equipo> findEquipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Equipo.class));
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

    public Equipo findEquipo(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Equipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Equipo> rt = cq.from(Equipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
