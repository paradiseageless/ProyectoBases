/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coltrol_Base;

import Coltrol_Base.exceptions.IllegalOrphanException;
import Coltrol_Base.exceptions.NonexistentEntityException;
import Coltrol_Base.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades_Base.Equipo;
import Entidades_Base.Estadio;
import Entidades_Base.Gol;
import java.util.ArrayList;
import java.util.Collection;
import Entidades_Base.Arbitra;
import Entidades_Base.Boleta;
import Entidades_Base.Partido;
import Entidades_Base.Tarjeta;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class PartidoJpaController implements Serializable {

    public PartidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Partido partido) throws PreexistingEntityException, Exception {
        if (partido.getGolCollection() == null) {
            partido.setGolCollection(new ArrayList<Gol>());
        }
        if (partido.getArbitraCollection() == null) {
            partido.setArbitraCollection(new ArrayList<Arbitra>());
        }
        if (partido.getBoletaCollection() == null) {
            partido.setBoletaCollection(new ArrayList<Boleta>());
        }
        if (partido.getTarjetaCollection() == null) {
            partido.setTarjetaCollection(new ArrayList<Tarjeta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipoIdEquipo = partido.getEquipoIdEquipo();
            if (equipoIdEquipo != null) {
                equipoIdEquipo = em.getReference(equipoIdEquipo.getClass(), equipoIdEquipo.getIdEquipo());
                partido.setEquipoIdEquipo(equipoIdEquipo);
            }
            Equipo equipoIdEquipo1 = partido.getEquipoIdEquipo1();
            if (equipoIdEquipo1 != null) {
                equipoIdEquipo1 = em.getReference(equipoIdEquipo1.getClass(), equipoIdEquipo1.getIdEquipo());
                partido.setEquipoIdEquipo1(equipoIdEquipo1);
            }
            Estadio estadioIdEstadio = partido.getEstadioIdEstadio();
            if (estadioIdEstadio != null) {
                estadioIdEstadio = em.getReference(estadioIdEstadio.getClass(), estadioIdEstadio.getIdEstadio());
                partido.setEstadioIdEstadio(estadioIdEstadio);
            }
            Collection<Gol> attachedGolCollection = new ArrayList<Gol>();
            for (Gol golCollectionGolToAttach : partido.getGolCollection()) {
                golCollectionGolToAttach = em.getReference(golCollectionGolToAttach.getClass(), golCollectionGolToAttach.getGolPK());
                attachedGolCollection.add(golCollectionGolToAttach);
            }
            partido.setGolCollection(attachedGolCollection);
            Collection<Arbitra> attachedArbitraCollection = new ArrayList<Arbitra>();
            for (Arbitra arbitraCollectionArbitraToAttach : partido.getArbitraCollection()) {
                arbitraCollectionArbitraToAttach = em.getReference(arbitraCollectionArbitraToAttach.getClass(), arbitraCollectionArbitraToAttach.getArbitraPK());
                attachedArbitraCollection.add(arbitraCollectionArbitraToAttach);
            }
            partido.setArbitraCollection(attachedArbitraCollection);
            Collection<Boleta> attachedBoletaCollection = new ArrayList<Boleta>();
            for (Boleta boletaCollectionBoletaToAttach : partido.getBoletaCollection()) {
                boletaCollectionBoletaToAttach = em.getReference(boletaCollectionBoletaToAttach.getClass(), boletaCollectionBoletaToAttach.getBoletaPK());
                attachedBoletaCollection.add(boletaCollectionBoletaToAttach);
            }
            partido.setBoletaCollection(attachedBoletaCollection);
            Collection<Tarjeta> attachedTarjetaCollection = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaCollectionTarjetaToAttach : partido.getTarjetaCollection()) {
                tarjetaCollectionTarjetaToAttach = em.getReference(tarjetaCollectionTarjetaToAttach.getClass(), tarjetaCollectionTarjetaToAttach.getTarjetaPK());
                attachedTarjetaCollection.add(tarjetaCollectionTarjetaToAttach);
            }
            partido.setTarjetaCollection(attachedTarjetaCollection);
            em.persist(partido);
            if (equipoIdEquipo != null) {
                equipoIdEquipo.getPartidoCollection().add(partido);
                equipoIdEquipo = em.merge(equipoIdEquipo);
            }
            if (equipoIdEquipo1 != null) {
                equipoIdEquipo1.getPartidoCollection().add(partido);
                equipoIdEquipo1 = em.merge(equipoIdEquipo1);
            }
            if (estadioIdEstadio != null) {
                estadioIdEstadio.getPartidoCollection().add(partido);
                estadioIdEstadio = em.merge(estadioIdEstadio);
            }
            for (Gol golCollectionGol : partido.getGolCollection()) {
                Partido oldPartidoOfGolCollectionGol = golCollectionGol.getPartido();
                golCollectionGol.setPartido(partido);
                golCollectionGol = em.merge(golCollectionGol);
                if (oldPartidoOfGolCollectionGol != null) {
                    oldPartidoOfGolCollectionGol.getGolCollection().remove(golCollectionGol);
                    oldPartidoOfGolCollectionGol = em.merge(oldPartidoOfGolCollectionGol);
                }
            }
            for (Arbitra arbitraCollectionArbitra : partido.getArbitraCollection()) {
                Partido oldPartidoOfArbitraCollectionArbitra = arbitraCollectionArbitra.getPartido();
                arbitraCollectionArbitra.setPartido(partido);
                arbitraCollectionArbitra = em.merge(arbitraCollectionArbitra);
                if (oldPartidoOfArbitraCollectionArbitra != null) {
                    oldPartidoOfArbitraCollectionArbitra.getArbitraCollection().remove(arbitraCollectionArbitra);
                    oldPartidoOfArbitraCollectionArbitra = em.merge(oldPartidoOfArbitraCollectionArbitra);
                }
            }
            for (Boleta boletaCollectionBoleta : partido.getBoletaCollection()) {
                Partido oldPartidoOfBoletaCollectionBoleta = boletaCollectionBoleta.getPartido();
                boletaCollectionBoleta.setPartido(partido);
                boletaCollectionBoleta = em.merge(boletaCollectionBoleta);
                if (oldPartidoOfBoletaCollectionBoleta != null) {
                    oldPartidoOfBoletaCollectionBoleta.getBoletaCollection().remove(boletaCollectionBoleta);
                    oldPartidoOfBoletaCollectionBoleta = em.merge(oldPartidoOfBoletaCollectionBoleta);
                }
            }
            for (Tarjeta tarjetaCollectionTarjeta : partido.getTarjetaCollection()) {
                Partido oldPartidoOfTarjetaCollectionTarjeta = tarjetaCollectionTarjeta.getPartido();
                tarjetaCollectionTarjeta.setPartido(partido);
                tarjetaCollectionTarjeta = em.merge(tarjetaCollectionTarjeta);
                if (oldPartidoOfTarjetaCollectionTarjeta != null) {
                    oldPartidoOfTarjetaCollectionTarjeta.getTarjetaCollection().remove(tarjetaCollectionTarjeta);
                    oldPartidoOfTarjetaCollectionTarjeta = em.merge(oldPartidoOfTarjetaCollectionTarjeta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPartido(partido.getIdPartido()) != null) {
                throw new PreexistingEntityException("Partido " + partido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Partido partido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partido persistentPartido = em.find(Partido.class, partido.getIdPartido());
            Equipo equipoIdEquipoOld = persistentPartido.getEquipoIdEquipo();
            Equipo equipoIdEquipoNew = partido.getEquipoIdEquipo();
            Equipo equipoIdEquipo1Old = persistentPartido.getEquipoIdEquipo1();
            Equipo equipoIdEquipo1New = partido.getEquipoIdEquipo1();
            Estadio estadioIdEstadioOld = persistentPartido.getEstadioIdEstadio();
            Estadio estadioIdEstadioNew = partido.getEstadioIdEstadio();
            Collection<Gol> golCollectionOld = persistentPartido.getGolCollection();
            Collection<Gol> golCollectionNew = partido.getGolCollection();
            Collection<Arbitra> arbitraCollectionOld = persistentPartido.getArbitraCollection();
            Collection<Arbitra> arbitraCollectionNew = partido.getArbitraCollection();
            Collection<Boleta> boletaCollectionOld = persistentPartido.getBoletaCollection();
            Collection<Boleta> boletaCollectionNew = partido.getBoletaCollection();
            Collection<Tarjeta> tarjetaCollectionOld = persistentPartido.getTarjetaCollection();
            Collection<Tarjeta> tarjetaCollectionNew = partido.getTarjetaCollection();
            List<String> illegalOrphanMessages = null;
            for (Gol golCollectionOldGol : golCollectionOld) {
                if (!golCollectionNew.contains(golCollectionOldGol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Gol " + golCollectionOldGol + " since its partido field is not nullable.");
                }
            }
            for (Arbitra arbitraCollectionOldArbitra : arbitraCollectionOld) {
                if (!arbitraCollectionNew.contains(arbitraCollectionOldArbitra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Arbitra " + arbitraCollectionOldArbitra + " since its partido field is not nullable.");
                }
            }
            for (Boleta boletaCollectionOldBoleta : boletaCollectionOld) {
                if (!boletaCollectionNew.contains(boletaCollectionOldBoleta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boleta " + boletaCollectionOldBoleta + " since its partido field is not nullable.");
                }
            }
            for (Tarjeta tarjetaCollectionOldTarjeta : tarjetaCollectionOld) {
                if (!tarjetaCollectionNew.contains(tarjetaCollectionOldTarjeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjeta " + tarjetaCollectionOldTarjeta + " since its partido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (equipoIdEquipoNew != null) {
                equipoIdEquipoNew = em.getReference(equipoIdEquipoNew.getClass(), equipoIdEquipoNew.getIdEquipo());
                partido.setEquipoIdEquipo(equipoIdEquipoNew);
            }
            if (equipoIdEquipo1New != null) {
                equipoIdEquipo1New = em.getReference(equipoIdEquipo1New.getClass(), equipoIdEquipo1New.getIdEquipo());
                partido.setEquipoIdEquipo1(equipoIdEquipo1New);
            }
            if (estadioIdEstadioNew != null) {
                estadioIdEstadioNew = em.getReference(estadioIdEstadioNew.getClass(), estadioIdEstadioNew.getIdEstadio());
                partido.setEstadioIdEstadio(estadioIdEstadioNew);
            }
            Collection<Gol> attachedGolCollectionNew = new ArrayList<Gol>();
            for (Gol golCollectionNewGolToAttach : golCollectionNew) {
                golCollectionNewGolToAttach = em.getReference(golCollectionNewGolToAttach.getClass(), golCollectionNewGolToAttach.getGolPK());
                attachedGolCollectionNew.add(golCollectionNewGolToAttach);
            }
            golCollectionNew = attachedGolCollectionNew;
            partido.setGolCollection(golCollectionNew);
            Collection<Arbitra> attachedArbitraCollectionNew = new ArrayList<Arbitra>();
            for (Arbitra arbitraCollectionNewArbitraToAttach : arbitraCollectionNew) {
                arbitraCollectionNewArbitraToAttach = em.getReference(arbitraCollectionNewArbitraToAttach.getClass(), arbitraCollectionNewArbitraToAttach.getArbitraPK());
                attachedArbitraCollectionNew.add(arbitraCollectionNewArbitraToAttach);
            }
            arbitraCollectionNew = attachedArbitraCollectionNew;
            partido.setArbitraCollection(arbitraCollectionNew);
            Collection<Boleta> attachedBoletaCollectionNew = new ArrayList<Boleta>();
            for (Boleta boletaCollectionNewBoletaToAttach : boletaCollectionNew) {
                boletaCollectionNewBoletaToAttach = em.getReference(boletaCollectionNewBoletaToAttach.getClass(), boletaCollectionNewBoletaToAttach.getBoletaPK());
                attachedBoletaCollectionNew.add(boletaCollectionNewBoletaToAttach);
            }
            boletaCollectionNew = attachedBoletaCollectionNew;
            partido.setBoletaCollection(boletaCollectionNew);
            Collection<Tarjeta> attachedTarjetaCollectionNew = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaCollectionNewTarjetaToAttach : tarjetaCollectionNew) {
                tarjetaCollectionNewTarjetaToAttach = em.getReference(tarjetaCollectionNewTarjetaToAttach.getClass(), tarjetaCollectionNewTarjetaToAttach.getTarjetaPK());
                attachedTarjetaCollectionNew.add(tarjetaCollectionNewTarjetaToAttach);
            }
            tarjetaCollectionNew = attachedTarjetaCollectionNew;
            partido.setTarjetaCollection(tarjetaCollectionNew);
            partido = em.merge(partido);
            if (equipoIdEquipoOld != null && !equipoIdEquipoOld.equals(equipoIdEquipoNew)) {
                equipoIdEquipoOld.getPartidoCollection().remove(partido);
                equipoIdEquipoOld = em.merge(equipoIdEquipoOld);
            }
            if (equipoIdEquipoNew != null && !equipoIdEquipoNew.equals(equipoIdEquipoOld)) {
                equipoIdEquipoNew.getPartidoCollection().add(partido);
                equipoIdEquipoNew = em.merge(equipoIdEquipoNew);
            }
            if (equipoIdEquipo1Old != null && !equipoIdEquipo1Old.equals(equipoIdEquipo1New)) {
                equipoIdEquipo1Old.getPartidoCollection().remove(partido);
                equipoIdEquipo1Old = em.merge(equipoIdEquipo1Old);
            }
            if (equipoIdEquipo1New != null && !equipoIdEquipo1New.equals(equipoIdEquipo1Old)) {
                equipoIdEquipo1New.getPartidoCollection().add(partido);
                equipoIdEquipo1New = em.merge(equipoIdEquipo1New);
            }
            if (estadioIdEstadioOld != null && !estadioIdEstadioOld.equals(estadioIdEstadioNew)) {
                estadioIdEstadioOld.getPartidoCollection().remove(partido);
                estadioIdEstadioOld = em.merge(estadioIdEstadioOld);
            }
            if (estadioIdEstadioNew != null && !estadioIdEstadioNew.equals(estadioIdEstadioOld)) {
                estadioIdEstadioNew.getPartidoCollection().add(partido);
                estadioIdEstadioNew = em.merge(estadioIdEstadioNew);
            }
            for (Gol golCollectionNewGol : golCollectionNew) {
                if (!golCollectionOld.contains(golCollectionNewGol)) {
                    Partido oldPartidoOfGolCollectionNewGol = golCollectionNewGol.getPartido();
                    golCollectionNewGol.setPartido(partido);
                    golCollectionNewGol = em.merge(golCollectionNewGol);
                    if (oldPartidoOfGolCollectionNewGol != null && !oldPartidoOfGolCollectionNewGol.equals(partido)) {
                        oldPartidoOfGolCollectionNewGol.getGolCollection().remove(golCollectionNewGol);
                        oldPartidoOfGolCollectionNewGol = em.merge(oldPartidoOfGolCollectionNewGol);
                    }
                }
            }
            for (Arbitra arbitraCollectionNewArbitra : arbitraCollectionNew) {
                if (!arbitraCollectionOld.contains(arbitraCollectionNewArbitra)) {
                    Partido oldPartidoOfArbitraCollectionNewArbitra = arbitraCollectionNewArbitra.getPartido();
                    arbitraCollectionNewArbitra.setPartido(partido);
                    arbitraCollectionNewArbitra = em.merge(arbitraCollectionNewArbitra);
                    if (oldPartidoOfArbitraCollectionNewArbitra != null && !oldPartidoOfArbitraCollectionNewArbitra.equals(partido)) {
                        oldPartidoOfArbitraCollectionNewArbitra.getArbitraCollection().remove(arbitraCollectionNewArbitra);
                        oldPartidoOfArbitraCollectionNewArbitra = em.merge(oldPartidoOfArbitraCollectionNewArbitra);
                    }
                }
            }
            for (Boleta boletaCollectionNewBoleta : boletaCollectionNew) {
                if (!boletaCollectionOld.contains(boletaCollectionNewBoleta)) {
                    Partido oldPartidoOfBoletaCollectionNewBoleta = boletaCollectionNewBoleta.getPartido();
                    boletaCollectionNewBoleta.setPartido(partido);
                    boletaCollectionNewBoleta = em.merge(boletaCollectionNewBoleta);
                    if (oldPartidoOfBoletaCollectionNewBoleta != null && !oldPartidoOfBoletaCollectionNewBoleta.equals(partido)) {
                        oldPartidoOfBoletaCollectionNewBoleta.getBoletaCollection().remove(boletaCollectionNewBoleta);
                        oldPartidoOfBoletaCollectionNewBoleta = em.merge(oldPartidoOfBoletaCollectionNewBoleta);
                    }
                }
            }
            for (Tarjeta tarjetaCollectionNewTarjeta : tarjetaCollectionNew) {
                if (!tarjetaCollectionOld.contains(tarjetaCollectionNewTarjeta)) {
                    Partido oldPartidoOfTarjetaCollectionNewTarjeta = tarjetaCollectionNewTarjeta.getPartido();
                    tarjetaCollectionNewTarjeta.setPartido(partido);
                    tarjetaCollectionNewTarjeta = em.merge(tarjetaCollectionNewTarjeta);
                    if (oldPartidoOfTarjetaCollectionNewTarjeta != null && !oldPartidoOfTarjetaCollectionNewTarjeta.equals(partido)) {
                        oldPartidoOfTarjetaCollectionNewTarjeta.getTarjetaCollection().remove(tarjetaCollectionNewTarjeta);
                        oldPartidoOfTarjetaCollectionNewTarjeta = em.merge(oldPartidoOfTarjetaCollectionNewTarjeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = partido.getIdPartido();
                if (findPartido(id) == null) {
                    throw new NonexistentEntityException("The partido with id " + id + " no longer exists.");
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
            Partido partido;
            try {
                partido = em.getReference(Partido.class, id);
                partido.getIdPartido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The partido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Gol> golCollectionOrphanCheck = partido.getGolCollection();
            for (Gol golCollectionOrphanCheckGol : golCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Gol " + golCollectionOrphanCheckGol + " in its golCollection field has a non-nullable partido field.");
            }
            Collection<Arbitra> arbitraCollectionOrphanCheck = partido.getArbitraCollection();
            for (Arbitra arbitraCollectionOrphanCheckArbitra : arbitraCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Arbitra " + arbitraCollectionOrphanCheckArbitra + " in its arbitraCollection field has a non-nullable partido field.");
            }
            Collection<Boleta> boletaCollectionOrphanCheck = partido.getBoletaCollection();
            for (Boleta boletaCollectionOrphanCheckBoleta : boletaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Boleta " + boletaCollectionOrphanCheckBoleta + " in its boletaCollection field has a non-nullable partido field.");
            }
            Collection<Tarjeta> tarjetaCollectionOrphanCheck = partido.getTarjetaCollection();
            for (Tarjeta tarjetaCollectionOrphanCheckTarjeta : tarjetaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Tarjeta " + tarjetaCollectionOrphanCheckTarjeta + " in its tarjetaCollection field has a non-nullable partido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Equipo equipoIdEquipo = partido.getEquipoIdEquipo();
            if (equipoIdEquipo != null) {
                equipoIdEquipo.getPartidoCollection().remove(partido);
                equipoIdEquipo = em.merge(equipoIdEquipo);
            }
            Equipo equipoIdEquipo1 = partido.getEquipoIdEquipo1();
            if (equipoIdEquipo1 != null) {
                equipoIdEquipo1.getPartidoCollection().remove(partido);
                equipoIdEquipo1 = em.merge(equipoIdEquipo1);
            }
            Estadio estadioIdEstadio = partido.getEstadioIdEstadio();
            if (estadioIdEstadio != null) {
                estadioIdEstadio.getPartidoCollection().remove(partido);
                estadioIdEstadio = em.merge(estadioIdEstadio);
            }
            em.remove(partido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Partido> findPartidoEntities() {
        return findPartidoEntities(true, -1, -1);
    }

    public List<Partido> findPartidoEntities(int maxResults, int firstResult) {
        return findPartidoEntities(false, maxResults, firstResult);
    }

    private List<Partido> findPartidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Partido.class));
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

    public Partido findPartido(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Partido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPartidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Partido> rt = cq.from(Partido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
