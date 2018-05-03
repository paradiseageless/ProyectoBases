/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coltrol_Base;

import Coltrol_Base.exceptions.NonexistentEntityException;
import Coltrol_Base.exceptions.PreexistingEntityException;
import Entidades_Base.Boleta;
import Entidades_Base.BoletaPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades_Base.Partido;
import Entidades_Base.Sillas;
import Entidades_Base.Usuarios;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class BoletaJpaController implements Serializable {

    public BoletaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Boleta boleta) throws PreexistingEntityException, Exception {
        if (boleta.getBoletaPK() == null) {
            boleta.setBoletaPK(new BoletaPK());
        }
        
        BigDecimal idUs = boleta.getUsuarios().getIdUsuario();  //---------------------------------------------------modifiqué esto -- dhas
        boleta.getBoletaPK().setUsuariosIdUsuario(idUs.toBigInteger());
        boleta.getBoletaPK().setSillasUbicacion(boleta.getSillas().getSillasPK().getUbicacion());
        boleta.getBoletaPK().setSillasCategoria(boleta.getSillas().getSillasPK().getCategoria());
        
        BigDecimal idPar = boleta.getPartido().getIdPartido();  //---------------------------------------------------modifiqué esto -- dhas
        boleta.getBoletaPK().setPartidoIdPartido(idPar.toBigInteger());
        boleta.getBoletaPK().setSillasNumero(boleta.getSillas().getSillasPK().getNumero());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partido partido = boleta.getPartido();
            if (partido != null) {
                partido = em.getReference(partido.getClass(), partido.getIdPartido());
                boleta.setPartido(partido);
            }
            Sillas sillas = boleta.getSillas();
            if (sillas != null) {
                sillas = em.getReference(sillas.getClass(), sillas.getSillasPK());
                boleta.setSillas(sillas);
            }
            Usuarios usuarios = boleta.getUsuarios();
            if (usuarios != null) {
                usuarios = em.getReference(usuarios.getClass(), usuarios.getIdUsuario());
                boleta.setUsuarios(usuarios);
            }
            em.persist(boleta);
            if (partido != null) {
                partido.getBoletaCollection().add(boleta);
                partido = em.merge(partido);
            }
            if (sillas != null) {
                sillas.getBoletaCollection().add(boleta);
                sillas = em.merge(sillas);
            }
            if (usuarios != null) {
                usuarios.getBoletaCollection().add(boleta);
                usuarios = em.merge(usuarios);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBoleta(boleta.getBoletaPK()) != null) {
                throw new PreexistingEntityException("Boleta " + boleta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Boleta boleta) throws NonexistentEntityException, Exception {
        
        BigDecimal idUs = boleta.getUsuarios().getIdUsuario();//---------------------------------------------------modifiqué esto -- dhas
        
        boleta.getBoletaPK().setUsuariosIdUsuario(idUs.toBigInteger());
        boleta.getBoletaPK().setSillasUbicacion(boleta.getSillas().getSillasPK().getUbicacion());
        boleta.getBoletaPK().setSillasCategoria(boleta.getSillas().getSillasPK().getCategoria());
        
        BigDecimal idPar = boleta.getPartido().getIdPartido();  //---------------------------------------------------modifiqué esto -- dhas
                
        boleta.getBoletaPK().setPartidoIdPartido(idPar.toBigInteger());
        boleta.getBoletaPK().setSillasNumero(boleta.getSillas().getSillasPK().getNumero());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Boleta persistentBoleta = em.find(Boleta.class, boleta.getBoletaPK());
            Partido partidoOld = persistentBoleta.getPartido();
            Partido partidoNew = boleta.getPartido();
            Sillas sillasOld = persistentBoleta.getSillas();
            Sillas sillasNew = boleta.getSillas();
            Usuarios usuariosOld = persistentBoleta.getUsuarios();
            Usuarios usuariosNew = boleta.getUsuarios();
            if (partidoNew != null) {
                partidoNew = em.getReference(partidoNew.getClass(), partidoNew.getIdPartido());
                boleta.setPartido(partidoNew);
            }
            if (sillasNew != null) {
                sillasNew = em.getReference(sillasNew.getClass(), sillasNew.getSillasPK());
                boleta.setSillas(sillasNew);
            }
            if (usuariosNew != null) {
                usuariosNew = em.getReference(usuariosNew.getClass(), usuariosNew.getIdUsuario());
                boleta.setUsuarios(usuariosNew);
            }
            boleta = em.merge(boleta);
            if (partidoOld != null && !partidoOld.equals(partidoNew)) {
                partidoOld.getBoletaCollection().remove(boleta);
                partidoOld = em.merge(partidoOld);
            }
            if (partidoNew != null && !partidoNew.equals(partidoOld)) {
                partidoNew.getBoletaCollection().add(boleta);
                partidoNew = em.merge(partidoNew);
            }
            if (sillasOld != null && !sillasOld.equals(sillasNew)) {
                sillasOld.getBoletaCollection().remove(boleta);
                sillasOld = em.merge(sillasOld);
            }
            if (sillasNew != null && !sillasNew.equals(sillasOld)) {
                sillasNew.getBoletaCollection().add(boleta);
                sillasNew = em.merge(sillasNew);
            }
            if (usuariosOld != null && !usuariosOld.equals(usuariosNew)) {
                usuariosOld.getBoletaCollection().remove(boleta);
                usuariosOld = em.merge(usuariosOld);
            }
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                usuariosNew.getBoletaCollection().add(boleta);
                usuariosNew = em.merge(usuariosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BoletaPK id = boleta.getBoletaPK();
                if (findBoleta(id) == null) {
                    throw new NonexistentEntityException("The boleta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BoletaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Boleta boleta;
            try {
                boleta = em.getReference(Boleta.class, id);
                boleta.getBoletaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boleta with id " + id + " no longer exists.", enfe);
            }
            Partido partido = boleta.getPartido();
            if (partido != null) {
                partido.getBoletaCollection().remove(boleta);
                partido = em.merge(partido);
            }
            Sillas sillas = boleta.getSillas();
            if (sillas != null) {
                sillas.getBoletaCollection().remove(boleta);
                sillas = em.merge(sillas);
            }
            Usuarios usuarios = boleta.getUsuarios();
            if (usuarios != null) {
                usuarios.getBoletaCollection().remove(boleta);
                usuarios = em.merge(usuarios);
            }
            em.remove(boleta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Boleta> findBoletaEntities() {
        return findBoletaEntities(true, -1, -1);
    }

    public List<Boleta> findBoletaEntities(int maxResults, int firstResult) {
        return findBoletaEntities(false, maxResults, firstResult);
    }

    private List<Boleta> findBoletaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Boleta.class));
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

    public Boleta findBoleta(BoletaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Boleta.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoletaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Boleta> rt = cq.from(Boleta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
