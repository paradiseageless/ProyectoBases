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
import Entidades_Base.Estadio;
import Entidades_Base.Boleta;
import Entidades_Base.Sillas;
import Entidades_Base.SillasPK;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class SillasJpaController implements Serializable {

    public SillasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sillas sillas) throws PreexistingEntityException, Exception {
        if (sillas.getSillasPK() == null) {
            sillas.setSillasPK(new SillasPK());
        }
        if (sillas.getBoletaCollection() == null) {
            sillas.setBoletaCollection(new ArrayList<Boleta>());
        }
        
        BigDecimal idEst = sillas.getEstadio().getIdEstadio(); //---------------------------------------------------modifiqué esto -- dhas
        
        sillas.getSillasPK().setEstadioIdEstadio(idEst.toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estadio estadio = sillas.getEstadio();
            if (estadio != null) {
                estadio = em.getReference(estadio.getClass(), estadio.getIdEstadio());
                sillas.setEstadio(estadio);
            }
            Collection<Boleta> attachedBoletaCollection = new ArrayList<Boleta>();
            for (Boleta boletaCollectionBoletaToAttach : sillas.getBoletaCollection()) {
                boletaCollectionBoletaToAttach = em.getReference(boletaCollectionBoletaToAttach.getClass(), boletaCollectionBoletaToAttach.getBoletaPK());
                attachedBoletaCollection.add(boletaCollectionBoletaToAttach);
            }
            sillas.setBoletaCollection(attachedBoletaCollection);
            em.persist(sillas);
            if (estadio != null) {
                estadio.getSillasCollection().add(sillas);
                estadio = em.merge(estadio);
            }
            for (Boleta boletaCollectionBoleta : sillas.getBoletaCollection()) {
                Sillas oldSillasOfBoletaCollectionBoleta = boletaCollectionBoleta.getSillas();
                boletaCollectionBoleta.setSillas(sillas);
                boletaCollectionBoleta = em.merge(boletaCollectionBoleta);
                if (oldSillasOfBoletaCollectionBoleta != null) {
                    oldSillasOfBoletaCollectionBoleta.getBoletaCollection().remove(boletaCollectionBoleta);
                    oldSillasOfBoletaCollectionBoleta = em.merge(oldSillasOfBoletaCollectionBoleta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSillas(sillas.getSillasPK()) != null) {
                throw new PreexistingEntityException("Sillas " + sillas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sillas sillas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        
        BigDecimal idEst = sillas.getEstadio().getIdEstadio(); //---------------------------------------------------modifiqué esto -- dhas
        sillas.getSillasPK().setEstadioIdEstadio(idEst.toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sillas persistentSillas = em.find(Sillas.class, sillas.getSillasPK());
            Estadio estadioOld = persistentSillas.getEstadio();
            Estadio estadioNew = sillas.getEstadio();
            Collection<Boleta> boletaCollectionOld = persistentSillas.getBoletaCollection();
            Collection<Boleta> boletaCollectionNew = sillas.getBoletaCollection();
            List<String> illegalOrphanMessages = null;
            for (Boleta boletaCollectionOldBoleta : boletaCollectionOld) {
                if (!boletaCollectionNew.contains(boletaCollectionOldBoleta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boleta " + boletaCollectionOldBoleta + " since its sillas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estadioNew != null) {
                estadioNew = em.getReference(estadioNew.getClass(), estadioNew.getIdEstadio());
                sillas.setEstadio(estadioNew);
            }
            Collection<Boleta> attachedBoletaCollectionNew = new ArrayList<Boleta>();
            for (Boleta boletaCollectionNewBoletaToAttach : boletaCollectionNew) {
                boletaCollectionNewBoletaToAttach = em.getReference(boletaCollectionNewBoletaToAttach.getClass(), boletaCollectionNewBoletaToAttach.getBoletaPK());
                attachedBoletaCollectionNew.add(boletaCollectionNewBoletaToAttach);
            }
            boletaCollectionNew = attachedBoletaCollectionNew;
            sillas.setBoletaCollection(boletaCollectionNew);
            sillas = em.merge(sillas);
            if (estadioOld != null && !estadioOld.equals(estadioNew)) {
                estadioOld.getSillasCollection().remove(sillas);
                estadioOld = em.merge(estadioOld);
            }
            if (estadioNew != null && !estadioNew.equals(estadioOld)) {
                estadioNew.getSillasCollection().add(sillas);
                estadioNew = em.merge(estadioNew);
            }
            for (Boleta boletaCollectionNewBoleta : boletaCollectionNew) {
                if (!boletaCollectionOld.contains(boletaCollectionNewBoleta)) {
                    Sillas oldSillasOfBoletaCollectionNewBoleta = boletaCollectionNewBoleta.getSillas();
                    boletaCollectionNewBoleta.setSillas(sillas);
                    boletaCollectionNewBoleta = em.merge(boletaCollectionNewBoleta);
                    if (oldSillasOfBoletaCollectionNewBoleta != null && !oldSillasOfBoletaCollectionNewBoleta.equals(sillas)) {
                        oldSillasOfBoletaCollectionNewBoleta.getBoletaCollection().remove(boletaCollectionNewBoleta);
                        oldSillasOfBoletaCollectionNewBoleta = em.merge(oldSillasOfBoletaCollectionNewBoleta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SillasPK id = sillas.getSillasPK();
                if (findSillas(id) == null) {
                    throw new NonexistentEntityException("The sillas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SillasPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sillas sillas;
            try {
                sillas = em.getReference(Sillas.class, id);
                sillas.getSillasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sillas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Boleta> boletaCollectionOrphanCheck = sillas.getBoletaCollection();
            for (Boleta boletaCollectionOrphanCheckBoleta : boletaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sillas (" + sillas + ") cannot be destroyed since the Boleta " + boletaCollectionOrphanCheckBoleta + " in its boletaCollection field has a non-nullable sillas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estadio estadio = sillas.getEstadio();
            if (estadio != null) {
                estadio.getSillasCollection().remove(sillas);
                estadio = em.merge(estadio);
            }
            em.remove(sillas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sillas> findSillasEntities() {
        return findSillasEntities(true, -1, -1);
    }

    public List<Sillas> findSillasEntities(int maxResults, int firstResult) {
        return findSillasEntities(false, maxResults, firstResult);
    }

    private List<Sillas> findSillasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sillas.class));
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

    public Sillas findSillas(SillasPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sillas.class, id);
        } finally {
            em.close();
        }
    }

    public int getSillasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sillas> rt = cq.from(Sillas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
