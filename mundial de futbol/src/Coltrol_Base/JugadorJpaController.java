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
import Entidades_Base.Paises;
import Entidades_Base.Gol;
import Entidades_Base.Jugador;
import Entidades_Base.JugadorPK;
import java.util.ArrayList;
import java.util.Collection;
import Entidades_Base.Tarjeta;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class JugadorJpaController implements Serializable {

    public JugadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jugador jugador) throws PreexistingEntityException, Exception {
        if (jugador.getJugadorPK() == null) {
            jugador.setJugadorPK(new JugadorPK());
        }
        if (jugador.getGolCollection() == null) {
            jugador.setGolCollection(new ArrayList<Gol>());
        }
        if (jugador.getTarjetaCollection() == null) {
            jugador.setTarjetaCollection(new ArrayList<Tarjeta>());
        }
        
        BigDecimal idEq = jugador.getEquipo().getIdEquipo(); //---------------------------------------------------modifiqué esto -- dhas
        jugador.getJugadorPK().setEquipoIdEquipo(idEq.toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipo = jugador.getEquipo();
            if (equipo != null) {
                equipo = em.getReference(equipo.getClass(), equipo.getIdEquipo());
                jugador.setEquipo(equipo);
            }
            Paises lugarNacimiento = jugador.getLugarNacimiento();
            if (lugarNacimiento != null) {
                lugarNacimiento = em.getReference(lugarNacimiento.getClass(), lugarNacimiento.getIdPais());
                jugador.setLugarNacimiento(lugarNacimiento);
            }
            Collection<Gol> attachedGolCollection = new ArrayList<Gol>();
            for (Gol golCollectionGolToAttach : jugador.getGolCollection()) {
                golCollectionGolToAttach = em.getReference(golCollectionGolToAttach.getClass(), golCollectionGolToAttach.getGolPK());
                attachedGolCollection.add(golCollectionGolToAttach);
            }
            jugador.setGolCollection(attachedGolCollection);
            Collection<Tarjeta> attachedTarjetaCollection = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaCollectionTarjetaToAttach : jugador.getTarjetaCollection()) {
                tarjetaCollectionTarjetaToAttach = em.getReference(tarjetaCollectionTarjetaToAttach.getClass(), tarjetaCollectionTarjetaToAttach.getTarjetaPK());
                attachedTarjetaCollection.add(tarjetaCollectionTarjetaToAttach);
            }
            jugador.setTarjetaCollection(attachedTarjetaCollection);
            em.persist(jugador);
            if (equipo != null) {
                equipo.getJugadorCollection().add(jugador);
                equipo = em.merge(equipo);
            }
            if (lugarNacimiento != null) {
                lugarNacimiento.getJugadorCollection().add(jugador);
                lugarNacimiento = em.merge(lugarNacimiento);
            }
            for (Gol golCollectionGol : jugador.getGolCollection()) {
                Jugador oldJugadorOfGolCollectionGol = golCollectionGol.getJugador();
                golCollectionGol.setJugador(jugador);
                golCollectionGol = em.merge(golCollectionGol);
                if (oldJugadorOfGolCollectionGol != null) {
                    oldJugadorOfGolCollectionGol.getGolCollection().remove(golCollectionGol);
                    oldJugadorOfGolCollectionGol = em.merge(oldJugadorOfGolCollectionGol);
                }
            }
            for (Tarjeta tarjetaCollectionTarjeta : jugador.getTarjetaCollection()) {
                Jugador oldJugadorOfTarjetaCollectionTarjeta = tarjetaCollectionTarjeta.getJugador();
                tarjetaCollectionTarjeta.setJugador(jugador);
                tarjetaCollectionTarjeta = em.merge(tarjetaCollectionTarjeta);
                if (oldJugadorOfTarjetaCollectionTarjeta != null) {
                    oldJugadorOfTarjetaCollectionTarjeta.getTarjetaCollection().remove(tarjetaCollectionTarjeta);
                    oldJugadorOfTarjetaCollectionTarjeta = em.merge(oldJugadorOfTarjetaCollectionTarjeta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJugador(jugador.getJugadorPK()) != null) {
                throw new PreexistingEntityException("Jugador " + jugador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jugador jugador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        
        BigDecimal idEq = jugador.getEquipo().getIdEquipo(); //---------------------------------------------------modifiqué esto -- dhas
        jugador.getJugadorPK().setEquipoIdEquipo(idEq.toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador persistentJugador = em.find(Jugador.class, jugador.getJugadorPK());
            Equipo equipoOld = persistentJugador.getEquipo();
            Equipo equipoNew = jugador.getEquipo();
            Paises lugarNacimientoOld = persistentJugador.getLugarNacimiento();
            Paises lugarNacimientoNew = jugador.getLugarNacimiento();
            Collection<Gol> golCollectionOld = persistentJugador.getGolCollection();
            Collection<Gol> golCollectionNew = jugador.getGolCollection();
            Collection<Tarjeta> tarjetaCollectionOld = persistentJugador.getTarjetaCollection();
            Collection<Tarjeta> tarjetaCollectionNew = jugador.getTarjetaCollection();
            List<String> illegalOrphanMessages = null;
            for (Gol golCollectionOldGol : golCollectionOld) {
                if (!golCollectionNew.contains(golCollectionOldGol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Gol " + golCollectionOldGol + " since its jugador field is not nullable.");
                }
            }
            for (Tarjeta tarjetaCollectionOldTarjeta : tarjetaCollectionOld) {
                if (!tarjetaCollectionNew.contains(tarjetaCollectionOldTarjeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjeta " + tarjetaCollectionOldTarjeta + " since its jugador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (equipoNew != null) {
                equipoNew = em.getReference(equipoNew.getClass(), equipoNew.getIdEquipo());
                jugador.setEquipo(equipoNew);
            }
            if (lugarNacimientoNew != null) {
                lugarNacimientoNew = em.getReference(lugarNacimientoNew.getClass(), lugarNacimientoNew.getIdPais());
                jugador.setLugarNacimiento(lugarNacimientoNew);
            }
            Collection<Gol> attachedGolCollectionNew = new ArrayList<Gol>();
            for (Gol golCollectionNewGolToAttach : golCollectionNew) {
                golCollectionNewGolToAttach = em.getReference(golCollectionNewGolToAttach.getClass(), golCollectionNewGolToAttach.getGolPK());
                attachedGolCollectionNew.add(golCollectionNewGolToAttach);
            }
            golCollectionNew = attachedGolCollectionNew;
            jugador.setGolCollection(golCollectionNew);
            Collection<Tarjeta> attachedTarjetaCollectionNew = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaCollectionNewTarjetaToAttach : tarjetaCollectionNew) {
                tarjetaCollectionNewTarjetaToAttach = em.getReference(tarjetaCollectionNewTarjetaToAttach.getClass(), tarjetaCollectionNewTarjetaToAttach.getTarjetaPK());
                attachedTarjetaCollectionNew.add(tarjetaCollectionNewTarjetaToAttach);
            }
            tarjetaCollectionNew = attachedTarjetaCollectionNew;
            jugador.setTarjetaCollection(tarjetaCollectionNew);
            jugador = em.merge(jugador);
            if (equipoOld != null && !equipoOld.equals(equipoNew)) {
                equipoOld.getJugadorCollection().remove(jugador);
                equipoOld = em.merge(equipoOld);
            }
            if (equipoNew != null && !equipoNew.equals(equipoOld)) {
                equipoNew.getJugadorCollection().add(jugador);
                equipoNew = em.merge(equipoNew);
            }
            if (lugarNacimientoOld != null && !lugarNacimientoOld.equals(lugarNacimientoNew)) {
                lugarNacimientoOld.getJugadorCollection().remove(jugador);
                lugarNacimientoOld = em.merge(lugarNacimientoOld);
            }
            if (lugarNacimientoNew != null && !lugarNacimientoNew.equals(lugarNacimientoOld)) {
                lugarNacimientoNew.getJugadorCollection().add(jugador);
                lugarNacimientoNew = em.merge(lugarNacimientoNew);
            }
            for (Gol golCollectionNewGol : golCollectionNew) {
                if (!golCollectionOld.contains(golCollectionNewGol)) {
                    Jugador oldJugadorOfGolCollectionNewGol = golCollectionNewGol.getJugador();
                    golCollectionNewGol.setJugador(jugador);
                    golCollectionNewGol = em.merge(golCollectionNewGol);
                    if (oldJugadorOfGolCollectionNewGol != null && !oldJugadorOfGolCollectionNewGol.equals(jugador)) {
                        oldJugadorOfGolCollectionNewGol.getGolCollection().remove(golCollectionNewGol);
                        oldJugadorOfGolCollectionNewGol = em.merge(oldJugadorOfGolCollectionNewGol);
                    }
                }
            }
            for (Tarjeta tarjetaCollectionNewTarjeta : tarjetaCollectionNew) {
                if (!tarjetaCollectionOld.contains(tarjetaCollectionNewTarjeta)) {
                    Jugador oldJugadorOfTarjetaCollectionNewTarjeta = tarjetaCollectionNewTarjeta.getJugador();
                    tarjetaCollectionNewTarjeta.setJugador(jugador);
                    tarjetaCollectionNewTarjeta = em.merge(tarjetaCollectionNewTarjeta);
                    if (oldJugadorOfTarjetaCollectionNewTarjeta != null && !oldJugadorOfTarjetaCollectionNewTarjeta.equals(jugador)) {
                        oldJugadorOfTarjetaCollectionNewTarjeta.getTarjetaCollection().remove(tarjetaCollectionNewTarjeta);
                        oldJugadorOfTarjetaCollectionNewTarjeta = em.merge(oldJugadorOfTarjetaCollectionNewTarjeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JugadorPK id = jugador.getJugadorPK();
                if (findJugador(id) == null) {
                    throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JugadorPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador;
            try {
                jugador = em.getReference(Jugador.class, id);
                jugador.getJugadorPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Gol> golCollectionOrphanCheck = jugador.getGolCollection();
            for (Gol golCollectionOrphanCheckGol : golCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Gol " + golCollectionOrphanCheckGol + " in its golCollection field has a non-nullable jugador field.");
            }
            Collection<Tarjeta> tarjetaCollectionOrphanCheck = jugador.getTarjetaCollection();
            for (Tarjeta tarjetaCollectionOrphanCheckTarjeta : tarjetaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Tarjeta " + tarjetaCollectionOrphanCheckTarjeta + " in its tarjetaCollection field has a non-nullable jugador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Equipo equipo = jugador.getEquipo();
            if (equipo != null) {
                equipo.getJugadorCollection().remove(jugador);
                equipo = em.merge(equipo);
            }
            Paises lugarNacimiento = jugador.getLugarNacimiento();
            if (lugarNacimiento != null) {
                lugarNacimiento.getJugadorCollection().remove(jugador);
                lugarNacimiento = em.merge(lugarNacimiento);
            }
            em.remove(jugador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jugador> findJugadorEntities() {
        return findJugadorEntities(true, -1, -1);
    }

    public List<Jugador> findJugadorEntities(int maxResults, int firstResult) {
        return findJugadorEntities(false, maxResults, firstResult);
    }

    private List<Jugador> findJugadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jugador.class));
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

    public Jugador findJugador(JugadorPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jugador.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jugador> rt = cq.from(Jugador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
