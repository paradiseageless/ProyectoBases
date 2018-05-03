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
import Entidades_Base.Boleta;
import Entidades_Base.Usuarios;
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
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getBoletaCollection() == null) {
            usuarios.setBoletaCollection(new ArrayList<Boleta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paises paisesIdPais = usuarios.getPaisesIdPais();
            if (paisesIdPais != null) {
                paisesIdPais = em.getReference(paisesIdPais.getClass(), paisesIdPais.getIdPais());
                usuarios.setPaisesIdPais(paisesIdPais);
            }
            Collection<Boleta> attachedBoletaCollection = new ArrayList<Boleta>();
            for (Boleta boletaCollectionBoletaToAttach : usuarios.getBoletaCollection()) {
                boletaCollectionBoletaToAttach = em.getReference(boletaCollectionBoletaToAttach.getClass(), boletaCollectionBoletaToAttach.getBoletaPK());
                attachedBoletaCollection.add(boletaCollectionBoletaToAttach);
            }
            usuarios.setBoletaCollection(attachedBoletaCollection);
            em.persist(usuarios);
            if (paisesIdPais != null) {
                paisesIdPais.getUsuariosCollection().add(usuarios);
                paisesIdPais = em.merge(paisesIdPais);
            }
            for (Boleta boletaCollectionBoleta : usuarios.getBoletaCollection()) {
                Usuarios oldUsuariosOfBoletaCollectionBoleta = boletaCollectionBoleta.getUsuarios();
                boletaCollectionBoleta.setUsuarios(usuarios);
                boletaCollectionBoleta = em.merge(boletaCollectionBoleta);
                if (oldUsuariosOfBoletaCollectionBoleta != null) {
                    oldUsuariosOfBoletaCollectionBoleta.getBoletaCollection().remove(boletaCollectionBoleta);
                    oldUsuariosOfBoletaCollectionBoleta = em.merge(oldUsuariosOfBoletaCollectionBoleta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuario());
            Paises paisesIdPaisOld = persistentUsuarios.getPaisesIdPais();
            Paises paisesIdPaisNew = usuarios.getPaisesIdPais();
            Collection<Boleta> boletaCollectionOld = persistentUsuarios.getBoletaCollection();
            Collection<Boleta> boletaCollectionNew = usuarios.getBoletaCollection();
            List<String> illegalOrphanMessages = null;
            for (Boleta boletaCollectionOldBoleta : boletaCollectionOld) {
                if (!boletaCollectionNew.contains(boletaCollectionOldBoleta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boleta " + boletaCollectionOldBoleta + " since its usuarios field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (paisesIdPaisNew != null) {
                paisesIdPaisNew = em.getReference(paisesIdPaisNew.getClass(), paisesIdPaisNew.getIdPais());
                usuarios.setPaisesIdPais(paisesIdPaisNew);
            }
            Collection<Boleta> attachedBoletaCollectionNew = new ArrayList<Boleta>();
            for (Boleta boletaCollectionNewBoletaToAttach : boletaCollectionNew) {
                boletaCollectionNewBoletaToAttach = em.getReference(boletaCollectionNewBoletaToAttach.getClass(), boletaCollectionNewBoletaToAttach.getBoletaPK());
                attachedBoletaCollectionNew.add(boletaCollectionNewBoletaToAttach);
            }
            boletaCollectionNew = attachedBoletaCollectionNew;
            usuarios.setBoletaCollection(boletaCollectionNew);
            usuarios = em.merge(usuarios);
            if (paisesIdPaisOld != null && !paisesIdPaisOld.equals(paisesIdPaisNew)) {
                paisesIdPaisOld.getUsuariosCollection().remove(usuarios);
                paisesIdPaisOld = em.merge(paisesIdPaisOld);
            }
            if (paisesIdPaisNew != null && !paisesIdPaisNew.equals(paisesIdPaisOld)) {
                paisesIdPaisNew.getUsuariosCollection().add(usuarios);
                paisesIdPaisNew = em.merge(paisesIdPaisNew);
            }
            for (Boleta boletaCollectionNewBoleta : boletaCollectionNew) {
                if (!boletaCollectionOld.contains(boletaCollectionNewBoleta)) {
                    Usuarios oldUsuariosOfBoletaCollectionNewBoleta = boletaCollectionNewBoleta.getUsuarios();
                    boletaCollectionNewBoleta.setUsuarios(usuarios);
                    boletaCollectionNewBoleta = em.merge(boletaCollectionNewBoleta);
                    if (oldUsuariosOfBoletaCollectionNewBoleta != null && !oldUsuariosOfBoletaCollectionNewBoleta.equals(usuarios)) {
                        oldUsuariosOfBoletaCollectionNewBoleta.getBoletaCollection().remove(boletaCollectionNewBoleta);
                        oldUsuariosOfBoletaCollectionNewBoleta = em.merge(oldUsuariosOfBoletaCollectionNewBoleta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuarios.getIdUsuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Boleta> boletaCollectionOrphanCheck = usuarios.getBoletaCollection();
            for (Boleta boletaCollectionOrphanCheckBoleta : boletaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Boleta " + boletaCollectionOrphanCheckBoleta + " in its boletaCollection field has a non-nullable usuarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paises paisesIdPais = usuarios.getPaisesIdPais();
            if (paisesIdPais != null) {
                paisesIdPais.getUsuariosCollection().remove(usuarios);
                paisesIdPais = em.merge(paisesIdPais);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
