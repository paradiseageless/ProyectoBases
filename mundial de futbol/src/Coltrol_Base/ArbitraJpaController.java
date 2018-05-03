/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coltrol_Base;

import Coltrol_Base.exceptions.NonexistentEntityException;
import Coltrol_Base.exceptions.PreexistingEntityException;
import Entidades_Base.Arbitra;
import Entidades_Base.ArbitraPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades_Base.Juez;
import Entidades_Base.Partido;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class ArbitraJpaController implements Serializable {

    public ArbitraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Arbitra arbitra) throws PreexistingEntityException, Exception {
        if (arbitra.getArbitraPK() == null) {
            arbitra.setArbitraPK(new ArbitraPK());
        }
        BigDecimal idPar = arbitra.getPartido().getIdPartido();  //---------------------------------------------------modifiqué esto -- dhas
        
        arbitra.getArbitraPK().setPartidoIdPartido(idPar.toBigInteger());
        
        BigDecimal idJuez = arbitra.getJuez().getIdJuez();  //---------------------------------------------------modifiqué esto -- dhas
        
        arbitra.getArbitraPK().setJuezIdJuez(idJuez.toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juez juez = arbitra.getJuez();
            if (juez != null) {
                juez = em.getReference(juez.getClass(), juez.getIdJuez());
                arbitra.setJuez(juez);
            }
            Partido partido = arbitra.getPartido();
            if (partido != null) {
                partido = em.getReference(partido.getClass(), partido.getIdPartido());
                arbitra.setPartido(partido);
            }
            em.persist(arbitra);
            if (juez != null) {
                juez.getArbitraCollection().add(arbitra);
                juez = em.merge(juez);
            }
            if (partido != null) {
                partido.getArbitraCollection().add(arbitra);
                partido = em.merge(partido);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findArbitra(arbitra.getArbitraPK()) != null) {
                throw new PreexistingEntityException("Arbitra " + arbitra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Arbitra arbitra) throws NonexistentEntityException, Exception {
        
        BigDecimal idPar = arbitra.getPartido().getIdPartido();  //---------------------------------------------------modifiqué esto -- dhas
        
        BigDecimal idJuez = arbitra.getJuez().getIdJuez();  //---------------------------------------------------modifiqué esto -- dhas
        
        arbitra.getArbitraPK().setPartidoIdPartido(idPar.toBigInteger());
        arbitra.getArbitraPK().setJuezIdJuez(idJuez.toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Arbitra persistentArbitra = em.find(Arbitra.class, arbitra.getArbitraPK());
            Juez juezOld = persistentArbitra.getJuez();
            Juez juezNew = arbitra.getJuez();
            Partido partidoOld = persistentArbitra.getPartido();
            Partido partidoNew = arbitra.getPartido();
            if (juezNew != null) {
                juezNew = em.getReference(juezNew.getClass(), juezNew.getIdJuez());
                arbitra.setJuez(juezNew);
            }
            if (partidoNew != null) {
                partidoNew = em.getReference(partidoNew.getClass(), partidoNew.getIdPartido());
                arbitra.setPartido(partidoNew);
            }
            arbitra = em.merge(arbitra);
            if (juezOld != null && !juezOld.equals(juezNew)) {
                juezOld.getArbitraCollection().remove(arbitra);
                juezOld = em.merge(juezOld);
            }
            if (juezNew != null && !juezNew.equals(juezOld)) {
                juezNew.getArbitraCollection().add(arbitra);
                juezNew = em.merge(juezNew);
            }
            if (partidoOld != null && !partidoOld.equals(partidoNew)) {
                partidoOld.getArbitraCollection().remove(arbitra);
                partidoOld = em.merge(partidoOld);
            }
            if (partidoNew != null && !partidoNew.equals(partidoOld)) {
                partidoNew.getArbitraCollection().add(arbitra);
                partidoNew = em.merge(partidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ArbitraPK id = arbitra.getArbitraPK();
                if (findArbitra(id) == null) {
                    throw new NonexistentEntityException("The arbitra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ArbitraPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Arbitra arbitra;
            try {
                arbitra = em.getReference(Arbitra.class, id);
                arbitra.getArbitraPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The arbitra with id " + id + " no longer exists.", enfe);
            }
            Juez juez = arbitra.getJuez();
            if (juez != null) {
                juez.getArbitraCollection().remove(arbitra);
                juez = em.merge(juez);
            }
            Partido partido = arbitra.getPartido();
            if (partido != null) {
                partido.getArbitraCollection().remove(arbitra);
                partido = em.merge(partido);
            }
            em.remove(arbitra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Arbitra> findArbitraEntities() {
        return findArbitraEntities(true, -1, -1);
    }

    public List<Arbitra> findArbitraEntities(int maxResults, int firstResult) {
        return findArbitraEntities(false, maxResults, firstResult);
    }

    private List<Arbitra> findArbitraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Arbitra.class));
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

    public Arbitra findArbitra(ArbitraPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Arbitra.class, id);
        } finally {
            em.close();
        }
    }

    public int getArbitraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Arbitra> rt = cq.from(Arbitra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
