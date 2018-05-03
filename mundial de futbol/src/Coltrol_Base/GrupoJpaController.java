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
import Entidades_Base.Grupo;
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
public class GrupoJpaController implements Serializable {

    public GrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupo grupo) throws PreexistingEntityException, Exception {
        if (grupo.getEquipoCollection() == null) {
            grupo.setEquipoCollection(new ArrayList<Equipo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Equipo> attachedEquipoCollection = new ArrayList<Equipo>();
            for (Equipo equipoCollectionEquipoToAttach : grupo.getEquipoCollection()) {
                equipoCollectionEquipoToAttach = em.getReference(equipoCollectionEquipoToAttach.getClass(), equipoCollectionEquipoToAttach.getIdEquipo());
                attachedEquipoCollection.add(equipoCollectionEquipoToAttach);
            }
            grupo.setEquipoCollection(attachedEquipoCollection);
            em.persist(grupo);
            for (Equipo equipoCollectionEquipo : grupo.getEquipoCollection()) {
                Grupo oldGrupoIdGrupoOfEquipoCollectionEquipo = equipoCollectionEquipo.getGrupoIdGrupo();
                equipoCollectionEquipo.setGrupoIdGrupo(grupo);
                equipoCollectionEquipo = em.merge(equipoCollectionEquipo);
                if (oldGrupoIdGrupoOfEquipoCollectionEquipo != null) {
                    oldGrupoIdGrupoOfEquipoCollectionEquipo.getEquipoCollection().remove(equipoCollectionEquipo);
                    oldGrupoIdGrupoOfEquipoCollectionEquipo = em.merge(oldGrupoIdGrupoOfEquipoCollectionEquipo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupo(grupo.getIdGrupo()) != null) {
                throw new PreexistingEntityException("Grupo " + grupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getIdGrupo());
            Collection<Equipo> equipoCollectionOld = persistentGrupo.getEquipoCollection();
            Collection<Equipo> equipoCollectionNew = grupo.getEquipoCollection();
            List<String> illegalOrphanMessages = null;
            for (Equipo equipoCollectionOldEquipo : equipoCollectionOld) {
                if (!equipoCollectionNew.contains(equipoCollectionOldEquipo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Equipo " + equipoCollectionOldEquipo + " since its grupoIdGrupo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Equipo> attachedEquipoCollectionNew = new ArrayList<Equipo>();
            for (Equipo equipoCollectionNewEquipoToAttach : equipoCollectionNew) {
                equipoCollectionNewEquipoToAttach = em.getReference(equipoCollectionNewEquipoToAttach.getClass(), equipoCollectionNewEquipoToAttach.getIdEquipo());
                attachedEquipoCollectionNew.add(equipoCollectionNewEquipoToAttach);
            }
            equipoCollectionNew = attachedEquipoCollectionNew;
            grupo.setEquipoCollection(equipoCollectionNew);
            grupo = em.merge(grupo);
            for (Equipo equipoCollectionNewEquipo : equipoCollectionNew) {
                if (!equipoCollectionOld.contains(equipoCollectionNewEquipo)) {
                    Grupo oldGrupoIdGrupoOfEquipoCollectionNewEquipo = equipoCollectionNewEquipo.getGrupoIdGrupo();
                    equipoCollectionNewEquipo.setGrupoIdGrupo(grupo);
                    equipoCollectionNewEquipo = em.merge(equipoCollectionNewEquipo);
                    if (oldGrupoIdGrupoOfEquipoCollectionNewEquipo != null && !oldGrupoIdGrupoOfEquipoCollectionNewEquipo.equals(grupo)) {
                        oldGrupoIdGrupoOfEquipoCollectionNewEquipo.getEquipoCollection().remove(equipoCollectionNewEquipo);
                        oldGrupoIdGrupoOfEquipoCollectionNewEquipo = em.merge(oldGrupoIdGrupoOfEquipoCollectionNewEquipo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = grupo.getIdGrupo();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
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
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getIdGrupo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Equipo> equipoCollectionOrphanCheck = grupo.getEquipoCollection();
            for (Equipo equipoCollectionOrphanCheckEquipo : equipoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Equipo " + equipoCollectionOrphanCheckEquipo + " in its equipoCollection field has a non-nullable grupoIdGrupo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
