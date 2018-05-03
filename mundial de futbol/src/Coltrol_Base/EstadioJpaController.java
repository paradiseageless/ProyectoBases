/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coltrol_Base;

import Coltrol_Base.exceptions.IllegalOrphanException;
import Coltrol_Base.exceptions.NonexistentEntityException;
import Coltrol_Base.exceptions.PreexistingEntityException;
import Entidades_Base.Estadio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades_Base.Sillas;
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
public class EstadioJpaController implements Serializable {

    public EstadioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estadio estadio) throws PreexistingEntityException, Exception {
        if (estadio.getSillasCollection() == null) {
            estadio.setSillasCollection(new ArrayList<Sillas>());
        }
        if (estadio.getPartidoCollection() == null) {
            estadio.setPartidoCollection(new ArrayList<Partido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Sillas> attachedSillasCollection = new ArrayList<Sillas>();
            for (Sillas sillasCollectionSillasToAttach : estadio.getSillasCollection()) {
                sillasCollectionSillasToAttach = em.getReference(sillasCollectionSillasToAttach.getClass(), sillasCollectionSillasToAttach.getSillasPK());
                attachedSillasCollection.add(sillasCollectionSillasToAttach);
            }
            estadio.setSillasCollection(attachedSillasCollection);
            Collection<Partido> attachedPartidoCollection = new ArrayList<Partido>();
            for (Partido partidoCollectionPartidoToAttach : estadio.getPartidoCollection()) {
                partidoCollectionPartidoToAttach = em.getReference(partidoCollectionPartidoToAttach.getClass(), partidoCollectionPartidoToAttach.getIdPartido());
                attachedPartidoCollection.add(partidoCollectionPartidoToAttach);
            }
            estadio.setPartidoCollection(attachedPartidoCollection);
            em.persist(estadio);
            for (Sillas sillasCollectionSillas : estadio.getSillasCollection()) {
                Estadio oldEstadioOfSillasCollectionSillas = sillasCollectionSillas.getEstadio();
                sillasCollectionSillas.setEstadio(estadio);
                sillasCollectionSillas = em.merge(sillasCollectionSillas);
                if (oldEstadioOfSillasCollectionSillas != null) {
                    oldEstadioOfSillasCollectionSillas.getSillasCollection().remove(sillasCollectionSillas);
                    oldEstadioOfSillasCollectionSillas = em.merge(oldEstadioOfSillasCollectionSillas);
                }
            }
            for (Partido partidoCollectionPartido : estadio.getPartidoCollection()) {
                Estadio oldEstadioIdEstadioOfPartidoCollectionPartido = partidoCollectionPartido.getEstadioIdEstadio();
                partidoCollectionPartido.setEstadioIdEstadio(estadio);
                partidoCollectionPartido = em.merge(partidoCollectionPartido);
                if (oldEstadioIdEstadioOfPartidoCollectionPartido != null) {
                    oldEstadioIdEstadioOfPartidoCollectionPartido.getPartidoCollection().remove(partidoCollectionPartido);
                    oldEstadioIdEstadioOfPartidoCollectionPartido = em.merge(oldEstadioIdEstadioOfPartidoCollectionPartido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadio(estadio.getIdEstadio()) != null) {
                throw new PreexistingEntityException("Estadio " + estadio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estadio estadio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estadio persistentEstadio = em.find(Estadio.class, estadio.getIdEstadio());
            Collection<Sillas> sillasCollectionOld = persistentEstadio.getSillasCollection();
            Collection<Sillas> sillasCollectionNew = estadio.getSillasCollection();
            Collection<Partido> partidoCollectionOld = persistentEstadio.getPartidoCollection();
            Collection<Partido> partidoCollectionNew = estadio.getPartidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Sillas sillasCollectionOldSillas : sillasCollectionOld) {
                if (!sillasCollectionNew.contains(sillasCollectionOldSillas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sillas " + sillasCollectionOldSillas + " since its estadio field is not nullable.");
                }
            }
            for (Partido partidoCollectionOldPartido : partidoCollectionOld) {
                if (!partidoCollectionNew.contains(partidoCollectionOldPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Partido " + partidoCollectionOldPartido + " since its estadioIdEstadio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Sillas> attachedSillasCollectionNew = new ArrayList<Sillas>();
            for (Sillas sillasCollectionNewSillasToAttach : sillasCollectionNew) {
                sillasCollectionNewSillasToAttach = em.getReference(sillasCollectionNewSillasToAttach.getClass(), sillasCollectionNewSillasToAttach.getSillasPK());
                attachedSillasCollectionNew.add(sillasCollectionNewSillasToAttach);
            }
            sillasCollectionNew = attachedSillasCollectionNew;
            estadio.setSillasCollection(sillasCollectionNew);
            Collection<Partido> attachedPartidoCollectionNew = new ArrayList<Partido>();
            for (Partido partidoCollectionNewPartidoToAttach : partidoCollectionNew) {
                partidoCollectionNewPartidoToAttach = em.getReference(partidoCollectionNewPartidoToAttach.getClass(), partidoCollectionNewPartidoToAttach.getIdPartido());
                attachedPartidoCollectionNew.add(partidoCollectionNewPartidoToAttach);
            }
            partidoCollectionNew = attachedPartidoCollectionNew;
            estadio.setPartidoCollection(partidoCollectionNew);
            estadio = em.merge(estadio);
            for (Sillas sillasCollectionNewSillas : sillasCollectionNew) {
                if (!sillasCollectionOld.contains(sillasCollectionNewSillas)) {
                    Estadio oldEstadioOfSillasCollectionNewSillas = sillasCollectionNewSillas.getEstadio();
                    sillasCollectionNewSillas.setEstadio(estadio);
                    sillasCollectionNewSillas = em.merge(sillasCollectionNewSillas);
                    if (oldEstadioOfSillasCollectionNewSillas != null && !oldEstadioOfSillasCollectionNewSillas.equals(estadio)) {
                        oldEstadioOfSillasCollectionNewSillas.getSillasCollection().remove(sillasCollectionNewSillas);
                        oldEstadioOfSillasCollectionNewSillas = em.merge(oldEstadioOfSillasCollectionNewSillas);
                    }
                }
            }
            for (Partido partidoCollectionNewPartido : partidoCollectionNew) {
                if (!partidoCollectionOld.contains(partidoCollectionNewPartido)) {
                    Estadio oldEstadioIdEstadioOfPartidoCollectionNewPartido = partidoCollectionNewPartido.getEstadioIdEstadio();
                    partidoCollectionNewPartido.setEstadioIdEstadio(estadio);
                    partidoCollectionNewPartido = em.merge(partidoCollectionNewPartido);
                    if (oldEstadioIdEstadioOfPartidoCollectionNewPartido != null && !oldEstadioIdEstadioOfPartidoCollectionNewPartido.equals(estadio)) {
                        oldEstadioIdEstadioOfPartidoCollectionNewPartido.getPartidoCollection().remove(partidoCollectionNewPartido);
                        oldEstadioIdEstadioOfPartidoCollectionNewPartido = em.merge(oldEstadioIdEstadioOfPartidoCollectionNewPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = estadio.getIdEstadio();
                if (findEstadio(id) == null) {
                    throw new NonexistentEntityException("The estadio with id " + id + " no longer exists.");
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
            Estadio estadio;
            try {
                estadio = em.getReference(Estadio.class, id);
                estadio.getIdEstadio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Sillas> sillasCollectionOrphanCheck = estadio.getSillasCollection();
            for (Sillas sillasCollectionOrphanCheckSillas : sillasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estadio (" + estadio + ") cannot be destroyed since the Sillas " + sillasCollectionOrphanCheckSillas + " in its sillasCollection field has a non-nullable estadio field.");
            }
            Collection<Partido> partidoCollectionOrphanCheck = estadio.getPartidoCollection();
            for (Partido partidoCollectionOrphanCheckPartido : partidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estadio (" + estadio + ") cannot be destroyed since the Partido " + partidoCollectionOrphanCheckPartido + " in its partidoCollection field has a non-nullable estadioIdEstadio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estadio> findEstadioEntities() {
        return findEstadioEntities(true, -1, -1);
    }

    public List<Estadio> findEstadioEntities(int maxResults, int firstResult) {
        return findEstadioEntities(false, maxResults, firstResult);
    }

    private List<Estadio> findEstadioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estadio.class));
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

    public Estadio findEstadio(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estadio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estadio> rt = cq.from(Estadio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
