/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coltrol_Base;

import Coltrol_Base.exceptions.IllegalOrphanException;
import Coltrol_Base.exceptions.NonexistentEntityException;
import Coltrol_Base.exceptions.PreexistingEntityException;
import Entidades_Base.Continentes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades_Base.Paises;
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
public class ContinentesJpaController implements Serializable {

    public ContinentesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Continentes continentes) throws PreexistingEntityException, Exception {
        if (continentes.getPaisesCollection() == null) {
            continentes.setPaisesCollection(new ArrayList<Paises>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Paises> attachedPaisesCollection = new ArrayList<Paises>();
            for (Paises paisesCollectionPaisesToAttach : continentes.getPaisesCollection()) {
                paisesCollectionPaisesToAttach = em.getReference(paisesCollectionPaisesToAttach.getClass(), paisesCollectionPaisesToAttach.getIdPais());
                attachedPaisesCollection.add(paisesCollectionPaisesToAttach);
            }
            continentes.setPaisesCollection(attachedPaisesCollection);
            em.persist(continentes);
            for (Paises paisesCollectionPaises : continentes.getPaisesCollection()) {
                Continentes oldContinentesIdContinenteOfPaisesCollectionPaises = paisesCollectionPaises.getContinentesIdContinente();
                paisesCollectionPaises.setContinentesIdContinente(continentes);
                paisesCollectionPaises = em.merge(paisesCollectionPaises);
                if (oldContinentesIdContinenteOfPaisesCollectionPaises != null) {
                    oldContinentesIdContinenteOfPaisesCollectionPaises.getPaisesCollection().remove(paisesCollectionPaises);
                    oldContinentesIdContinenteOfPaisesCollectionPaises = em.merge(oldContinentesIdContinenteOfPaisesCollectionPaises);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findContinentes(continentes.getIdContinente()) != null) {
                throw new PreexistingEntityException("Continentes " + continentes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Continentes continentes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Continentes persistentContinentes = em.find(Continentes.class, continentes.getIdContinente());
            Collection<Paises> paisesCollectionOld = persistentContinentes.getPaisesCollection();
            Collection<Paises> paisesCollectionNew = continentes.getPaisesCollection();
            List<String> illegalOrphanMessages = null;
            for (Paises paisesCollectionOldPaises : paisesCollectionOld) {
                if (!paisesCollectionNew.contains(paisesCollectionOldPaises)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paises " + paisesCollectionOldPaises + " since its continentesIdContinente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Paises> attachedPaisesCollectionNew = new ArrayList<Paises>();
            for (Paises paisesCollectionNewPaisesToAttach : paisesCollectionNew) {
                paisesCollectionNewPaisesToAttach = em.getReference(paisesCollectionNewPaisesToAttach.getClass(), paisesCollectionNewPaisesToAttach.getIdPais());
                attachedPaisesCollectionNew.add(paisesCollectionNewPaisesToAttach);
            }
            paisesCollectionNew = attachedPaisesCollectionNew;
            continentes.setPaisesCollection(paisesCollectionNew);
            continentes = em.merge(continentes);
            for (Paises paisesCollectionNewPaises : paisesCollectionNew) {
                if (!paisesCollectionOld.contains(paisesCollectionNewPaises)) {
                    Continentes oldContinentesIdContinenteOfPaisesCollectionNewPaises = paisesCollectionNewPaises.getContinentesIdContinente();
                    paisesCollectionNewPaises.setContinentesIdContinente(continentes);
                    paisesCollectionNewPaises = em.merge(paisesCollectionNewPaises);
                    if (oldContinentesIdContinenteOfPaisesCollectionNewPaises != null && !oldContinentesIdContinenteOfPaisesCollectionNewPaises.equals(continentes)) {
                        oldContinentesIdContinenteOfPaisesCollectionNewPaises.getPaisesCollection().remove(paisesCollectionNewPaises);
                        oldContinentesIdContinenteOfPaisesCollectionNewPaises = em.merge(oldContinentesIdContinenteOfPaisesCollectionNewPaises);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = continentes.getIdContinente();
                if (findContinentes(id) == null) {
                    throw new NonexistentEntityException("The continentes with id " + id + " no longer exists.");
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
            Continentes continentes;
            try {
                continentes = em.getReference(Continentes.class, id);
                continentes.getIdContinente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The continentes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Paises> paisesCollectionOrphanCheck = continentes.getPaisesCollection();
            for (Paises paisesCollectionOrphanCheckPaises : paisesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Continentes (" + continentes + ") cannot be destroyed since the Paises " + paisesCollectionOrphanCheckPaises + " in its paisesCollection field has a non-nullable continentesIdContinente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(continentes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Continentes> findContinentesEntities() {
        return findContinentesEntities(true, -1, -1);
    }

    public List<Continentes> findContinentesEntities(int maxResults, int firstResult) {
        return findContinentesEntities(false, maxResults, firstResult);
    }

    private List<Continentes> findContinentesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Continentes.class));
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

    public Continentes findContinentes(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Continentes.class, id);
        } finally {
            em.close();
        }
    }

    public int getContinentesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Continentes> rt = cq.from(Continentes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
