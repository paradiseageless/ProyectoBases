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
import Entidades_Base.Paises;
import Entidades_Base.Arbitra;
import Entidades_Base.Juez;
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
public class JuezJpaController implements Serializable {

    public JuezJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Juez juez) throws PreexistingEntityException, Exception {
        if (juez.getArbitraCollection() == null) {
            juez.setArbitraCollection(new ArrayList<Arbitra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paises nacionalidad = juez.getNacionalidad();
            if (nacionalidad != null) {
                nacionalidad = em.getReference(nacionalidad.getClass(), nacionalidad.getIdPais());
                juez.setNacionalidad(nacionalidad);
            }
            Collection<Arbitra> attachedArbitraCollection = new ArrayList<Arbitra>();
            for (Arbitra arbitraCollectionArbitraToAttach : juez.getArbitraCollection()) {
                arbitraCollectionArbitraToAttach = em.getReference(arbitraCollectionArbitraToAttach.getClass(), arbitraCollectionArbitraToAttach.getArbitraPK());
                attachedArbitraCollection.add(arbitraCollectionArbitraToAttach);
            }
            juez.setArbitraCollection(attachedArbitraCollection);
            em.persist(juez);
            if (nacionalidad != null) {
                nacionalidad.getJuezCollection().add(juez);
                nacionalidad = em.merge(nacionalidad);
            }
            for (Arbitra arbitraCollectionArbitra : juez.getArbitraCollection()) {
                Juez oldJuezOfArbitraCollectionArbitra = arbitraCollectionArbitra.getJuez();
                arbitraCollectionArbitra.setJuez(juez);
                arbitraCollectionArbitra = em.merge(arbitraCollectionArbitra);
                if (oldJuezOfArbitraCollectionArbitra != null) {
                    oldJuezOfArbitraCollectionArbitra.getArbitraCollection().remove(arbitraCollectionArbitra);
                    oldJuezOfArbitraCollectionArbitra = em.merge(oldJuezOfArbitraCollectionArbitra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJuez(juez.getIdJuez()) != null) {
                throw new PreexistingEntityException("Juez " + juez + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Juez juez) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juez persistentJuez = em.find(Juez.class, juez.getIdJuez());
            Paises nacionalidadOld = persistentJuez.getNacionalidad();
            Paises nacionalidadNew = juez.getNacionalidad();
            Collection<Arbitra> arbitraCollectionOld = persistentJuez.getArbitraCollection();
            Collection<Arbitra> arbitraCollectionNew = juez.getArbitraCollection();
            List<String> illegalOrphanMessages = null;
            for (Arbitra arbitraCollectionOldArbitra : arbitraCollectionOld) {
                if (!arbitraCollectionNew.contains(arbitraCollectionOldArbitra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Arbitra " + arbitraCollectionOldArbitra + " since its juez field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nacionalidadNew != null) {
                nacionalidadNew = em.getReference(nacionalidadNew.getClass(), nacionalidadNew.getIdPais());
                juez.setNacionalidad(nacionalidadNew);
            }
            Collection<Arbitra> attachedArbitraCollectionNew = new ArrayList<Arbitra>();
            for (Arbitra arbitraCollectionNewArbitraToAttach : arbitraCollectionNew) {
                arbitraCollectionNewArbitraToAttach = em.getReference(arbitraCollectionNewArbitraToAttach.getClass(), arbitraCollectionNewArbitraToAttach.getArbitraPK());
                attachedArbitraCollectionNew.add(arbitraCollectionNewArbitraToAttach);
            }
            arbitraCollectionNew = attachedArbitraCollectionNew;
            juez.setArbitraCollection(arbitraCollectionNew);
            juez = em.merge(juez);
            if (nacionalidadOld != null && !nacionalidadOld.equals(nacionalidadNew)) {
                nacionalidadOld.getJuezCollection().remove(juez);
                nacionalidadOld = em.merge(nacionalidadOld);
            }
            if (nacionalidadNew != null && !nacionalidadNew.equals(nacionalidadOld)) {
                nacionalidadNew.getJuezCollection().add(juez);
                nacionalidadNew = em.merge(nacionalidadNew);
            }
            for (Arbitra arbitraCollectionNewArbitra : arbitraCollectionNew) {
                if (!arbitraCollectionOld.contains(arbitraCollectionNewArbitra)) {
                    Juez oldJuezOfArbitraCollectionNewArbitra = arbitraCollectionNewArbitra.getJuez();
                    arbitraCollectionNewArbitra.setJuez(juez);
                    arbitraCollectionNewArbitra = em.merge(arbitraCollectionNewArbitra);
                    if (oldJuezOfArbitraCollectionNewArbitra != null && !oldJuezOfArbitraCollectionNewArbitra.equals(juez)) {
                        oldJuezOfArbitraCollectionNewArbitra.getArbitraCollection().remove(arbitraCollectionNewArbitra);
                        oldJuezOfArbitraCollectionNewArbitra = em.merge(oldJuezOfArbitraCollectionNewArbitra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = juez.getIdJuez();
                if (findJuez(id) == null) {
                    throw new NonexistentEntityException("The juez with id " + id + " no longer exists.");
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
            Juez juez;
            try {
                juez = em.getReference(Juez.class, id);
                juez.getIdJuez();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The juez with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Arbitra> arbitraCollectionOrphanCheck = juez.getArbitraCollection();
            for (Arbitra arbitraCollectionOrphanCheckArbitra : arbitraCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Juez (" + juez + ") cannot be destroyed since the Arbitra " + arbitraCollectionOrphanCheckArbitra + " in its arbitraCollection field has a non-nullable juez field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paises nacionalidad = juez.getNacionalidad();
            if (nacionalidad != null) {
                nacionalidad.getJuezCollection().remove(juez);
                nacionalidad = em.merge(nacionalidad);
            }
            em.remove(juez);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Juez> findJuezEntities() {
        return findJuezEntities(true, -1, -1);
    }

    public List<Juez> findJuezEntities(int maxResults, int firstResult) {
        return findJuezEntities(false, maxResults, firstResult);
    }

    private List<Juez> findJuezEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Juez.class));
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

    public Juez findJuez(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Juez.class, id);
        } finally {
            em.close();
        }
    }

    public int getJuezCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Juez> rt = cq.from(Juez.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
