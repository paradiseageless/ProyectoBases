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
import Entidades_Base.Continentes;
import Entidades_Base.Juez;
import java.util.ArrayList;
import java.util.Collection;
import Entidades_Base.Jugador;
import Entidades_Base.Paises;
import Entidades_Base.Usuarios;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author danih
 */
public class PaisesJpaController implements Serializable {

    public PaisesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paises paises) throws PreexistingEntityException, Exception {
        if (paises.getJuezCollection() == null) {
            paises.setJuezCollection(new ArrayList<Juez>());
        }
        if (paises.getJugadorCollection() == null) {
            paises.setJugadorCollection(new ArrayList<Jugador>());
        }
        if (paises.getUsuariosCollection() == null) {
            paises.setUsuariosCollection(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Continentes continentesIdContinente = paises.getContinentesIdContinente();
            if (continentesIdContinente != null) {
                continentesIdContinente = em.getReference(continentesIdContinente.getClass(), continentesIdContinente.getIdContinente());
                paises.setContinentesIdContinente(continentesIdContinente);
            }
            Collection<Juez> attachedJuezCollection = new ArrayList<Juez>();
            for (Juez juezCollectionJuezToAttach : paises.getJuezCollection()) {
                juezCollectionJuezToAttach = em.getReference(juezCollectionJuezToAttach.getClass(), juezCollectionJuezToAttach.getIdJuez());
                attachedJuezCollection.add(juezCollectionJuezToAttach);
            }
            paises.setJuezCollection(attachedJuezCollection);
            Collection<Jugador> attachedJugadorCollection = new ArrayList<Jugador>();
            for (Jugador jugadorCollectionJugadorToAttach : paises.getJugadorCollection()) {
                jugadorCollectionJugadorToAttach = em.getReference(jugadorCollectionJugadorToAttach.getClass(), jugadorCollectionJugadorToAttach.getJugadorPK());
                attachedJugadorCollection.add(jugadorCollectionJugadorToAttach);
            }
            paises.setJugadorCollection(attachedJugadorCollection);
            Collection<Usuarios> attachedUsuariosCollection = new ArrayList<Usuarios>();
            for (Usuarios usuariosCollectionUsuariosToAttach : paises.getUsuariosCollection()) {
                usuariosCollectionUsuariosToAttach = em.getReference(usuariosCollectionUsuariosToAttach.getClass(), usuariosCollectionUsuariosToAttach.getIdUsuario());
                attachedUsuariosCollection.add(usuariosCollectionUsuariosToAttach);
            }
            paises.setUsuariosCollection(attachedUsuariosCollection);
            em.persist(paises);
            if (continentesIdContinente != null) {
                continentesIdContinente.getPaisesCollection().add(paises);
                continentesIdContinente = em.merge(continentesIdContinente);
            }
            for (Juez juezCollectionJuez : paises.getJuezCollection()) {
                Paises oldNacionalidadOfJuezCollectionJuez = juezCollectionJuez.getNacionalidad();
                juezCollectionJuez.setNacionalidad(paises);
                juezCollectionJuez = em.merge(juezCollectionJuez);
                if (oldNacionalidadOfJuezCollectionJuez != null) {
                    oldNacionalidadOfJuezCollectionJuez.getJuezCollection().remove(juezCollectionJuez);
                    oldNacionalidadOfJuezCollectionJuez = em.merge(oldNacionalidadOfJuezCollectionJuez);
                }
            }
            for (Jugador jugadorCollectionJugador : paises.getJugadorCollection()) {
                Paises oldLugarNacimientoOfJugadorCollectionJugador = jugadorCollectionJugador.getLugarNacimiento();
                jugadorCollectionJugador.setLugarNacimiento(paises);
                jugadorCollectionJugador = em.merge(jugadorCollectionJugador);
                if (oldLugarNacimientoOfJugadorCollectionJugador != null) {
                    oldLugarNacimientoOfJugadorCollectionJugador.getJugadorCollection().remove(jugadorCollectionJugador);
                    oldLugarNacimientoOfJugadorCollectionJugador = em.merge(oldLugarNacimientoOfJugadorCollectionJugador);
                }
            }
            for (Usuarios usuariosCollectionUsuarios : paises.getUsuariosCollection()) {
                Paises oldPaisesIdPaisOfUsuariosCollectionUsuarios = usuariosCollectionUsuarios.getPaisesIdPais();
                usuariosCollectionUsuarios.setPaisesIdPais(paises);
                usuariosCollectionUsuarios = em.merge(usuariosCollectionUsuarios);
                if (oldPaisesIdPaisOfUsuariosCollectionUsuarios != null) {
                    oldPaisesIdPaisOfUsuariosCollectionUsuarios.getUsuariosCollection().remove(usuariosCollectionUsuarios);
                    oldPaisesIdPaisOfUsuariosCollectionUsuarios = em.merge(oldPaisesIdPaisOfUsuariosCollectionUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPaises(paises.getIdPais()) != null) {
                throw new PreexistingEntityException("Paises " + paises + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paises paises) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paises persistentPaises = em.find(Paises.class, paises.getIdPais());
            Continentes continentesIdContinenteOld = persistentPaises.getContinentesIdContinente();
            Continentes continentesIdContinenteNew = paises.getContinentesIdContinente();
            Collection<Juez> juezCollectionOld = persistentPaises.getJuezCollection();
            Collection<Juez> juezCollectionNew = paises.getJuezCollection();
            Collection<Jugador> jugadorCollectionOld = persistentPaises.getJugadorCollection();
            Collection<Jugador> jugadorCollectionNew = paises.getJugadorCollection();
            Collection<Usuarios> usuariosCollectionOld = persistentPaises.getUsuariosCollection();
            Collection<Usuarios> usuariosCollectionNew = paises.getUsuariosCollection();
            List<String> illegalOrphanMessages = null;
            for (Juez juezCollectionOldJuez : juezCollectionOld) {
                if (!juezCollectionNew.contains(juezCollectionOldJuez)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Juez " + juezCollectionOldJuez + " since its nacionalidad field is not nullable.");
                }
            }
            for (Jugador jugadorCollectionOldJugador : jugadorCollectionOld) {
                if (!jugadorCollectionNew.contains(jugadorCollectionOldJugador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jugador " + jugadorCollectionOldJugador + " since its lugarNacimiento field is not nullable.");
                }
            }
            for (Usuarios usuariosCollectionOldUsuarios : usuariosCollectionOld) {
                if (!usuariosCollectionNew.contains(usuariosCollectionOldUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuarios " + usuariosCollectionOldUsuarios + " since its paisesIdPais field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (continentesIdContinenteNew != null) {
                continentesIdContinenteNew = em.getReference(continentesIdContinenteNew.getClass(), continentesIdContinenteNew.getIdContinente());
                paises.setContinentesIdContinente(continentesIdContinenteNew);
            }
            Collection<Juez> attachedJuezCollectionNew = new ArrayList<Juez>();
            for (Juez juezCollectionNewJuezToAttach : juezCollectionNew) {
                juezCollectionNewJuezToAttach = em.getReference(juezCollectionNewJuezToAttach.getClass(), juezCollectionNewJuezToAttach.getIdJuez());
                attachedJuezCollectionNew.add(juezCollectionNewJuezToAttach);
            }
            juezCollectionNew = attachedJuezCollectionNew;
            paises.setJuezCollection(juezCollectionNew);
            Collection<Jugador> attachedJugadorCollectionNew = new ArrayList<Jugador>();
            for (Jugador jugadorCollectionNewJugadorToAttach : jugadorCollectionNew) {
                jugadorCollectionNewJugadorToAttach = em.getReference(jugadorCollectionNewJugadorToAttach.getClass(), jugadorCollectionNewJugadorToAttach.getJugadorPK());
                attachedJugadorCollectionNew.add(jugadorCollectionNewJugadorToAttach);
            }
            jugadorCollectionNew = attachedJugadorCollectionNew;
            paises.setJugadorCollection(jugadorCollectionNew);
            Collection<Usuarios> attachedUsuariosCollectionNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosCollectionNewUsuariosToAttach : usuariosCollectionNew) {
                usuariosCollectionNewUsuariosToAttach = em.getReference(usuariosCollectionNewUsuariosToAttach.getClass(), usuariosCollectionNewUsuariosToAttach.getIdUsuario());
                attachedUsuariosCollectionNew.add(usuariosCollectionNewUsuariosToAttach);
            }
            usuariosCollectionNew = attachedUsuariosCollectionNew;
            paises.setUsuariosCollection(usuariosCollectionNew);
            paises = em.merge(paises);
            if (continentesIdContinenteOld != null && !continentesIdContinenteOld.equals(continentesIdContinenteNew)) {
                continentesIdContinenteOld.getPaisesCollection().remove(paises);
                continentesIdContinenteOld = em.merge(continentesIdContinenteOld);
            }
            if (continentesIdContinenteNew != null && !continentesIdContinenteNew.equals(continentesIdContinenteOld)) {
                continentesIdContinenteNew.getPaisesCollection().add(paises);
                continentesIdContinenteNew = em.merge(continentesIdContinenteNew);
            }
            for (Juez juezCollectionNewJuez : juezCollectionNew) {
                if (!juezCollectionOld.contains(juezCollectionNewJuez)) {
                    Paises oldNacionalidadOfJuezCollectionNewJuez = juezCollectionNewJuez.getNacionalidad();
                    juezCollectionNewJuez.setNacionalidad(paises);
                    juezCollectionNewJuez = em.merge(juezCollectionNewJuez);
                    if (oldNacionalidadOfJuezCollectionNewJuez != null && !oldNacionalidadOfJuezCollectionNewJuez.equals(paises)) {
                        oldNacionalidadOfJuezCollectionNewJuez.getJuezCollection().remove(juezCollectionNewJuez);
                        oldNacionalidadOfJuezCollectionNewJuez = em.merge(oldNacionalidadOfJuezCollectionNewJuez);
                    }
                }
            }
            for (Jugador jugadorCollectionNewJugador : jugadorCollectionNew) {
                if (!jugadorCollectionOld.contains(jugadorCollectionNewJugador)) {
                    Paises oldLugarNacimientoOfJugadorCollectionNewJugador = jugadorCollectionNewJugador.getLugarNacimiento();
                    jugadorCollectionNewJugador.setLugarNacimiento(paises);
                    jugadorCollectionNewJugador = em.merge(jugadorCollectionNewJugador);
                    if (oldLugarNacimientoOfJugadorCollectionNewJugador != null && !oldLugarNacimientoOfJugadorCollectionNewJugador.equals(paises)) {
                        oldLugarNacimientoOfJugadorCollectionNewJugador.getJugadorCollection().remove(jugadorCollectionNewJugador);
                        oldLugarNacimientoOfJugadorCollectionNewJugador = em.merge(oldLugarNacimientoOfJugadorCollectionNewJugador);
                    }
                }
            }
            for (Usuarios usuariosCollectionNewUsuarios : usuariosCollectionNew) {
                if (!usuariosCollectionOld.contains(usuariosCollectionNewUsuarios)) {
                    Paises oldPaisesIdPaisOfUsuariosCollectionNewUsuarios = usuariosCollectionNewUsuarios.getPaisesIdPais();
                    usuariosCollectionNewUsuarios.setPaisesIdPais(paises);
                    usuariosCollectionNewUsuarios = em.merge(usuariosCollectionNewUsuarios);
                    if (oldPaisesIdPaisOfUsuariosCollectionNewUsuarios != null && !oldPaisesIdPaisOfUsuariosCollectionNewUsuarios.equals(paises)) {
                        oldPaisesIdPaisOfUsuariosCollectionNewUsuarios.getUsuariosCollection().remove(usuariosCollectionNewUsuarios);
                        oldPaisesIdPaisOfUsuariosCollectionNewUsuarios = em.merge(oldPaisesIdPaisOfUsuariosCollectionNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = paises.getIdPais();
                if (findPaises(id) == null) {
                    throw new NonexistentEntityException("The paises with id " + id + " no longer exists.");
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
            Paises paises;
            try {
                paises = em.getReference(Paises.class, id);
                paises.getIdPais();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paises with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Juez> juezCollectionOrphanCheck = paises.getJuezCollection();
            for (Juez juezCollectionOrphanCheckJuez : juezCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paises (" + paises + ") cannot be destroyed since the Juez " + juezCollectionOrphanCheckJuez + " in its juezCollection field has a non-nullable nacionalidad field.");
            }
            Collection<Jugador> jugadorCollectionOrphanCheck = paises.getJugadorCollection();
            for (Jugador jugadorCollectionOrphanCheckJugador : jugadorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paises (" + paises + ") cannot be destroyed since the Jugador " + jugadorCollectionOrphanCheckJugador + " in its jugadorCollection field has a non-nullable lugarNacimiento field.");
            }
            Collection<Usuarios> usuariosCollectionOrphanCheck = paises.getUsuariosCollection();
            for (Usuarios usuariosCollectionOrphanCheckUsuarios : usuariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paises (" + paises + ") cannot be destroyed since the Usuarios " + usuariosCollectionOrphanCheckUsuarios + " in its usuariosCollection field has a non-nullable paisesIdPais field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Continentes continentesIdContinente = paises.getContinentesIdContinente();
            if (continentesIdContinente != null) {
                continentesIdContinente.getPaisesCollection().remove(paises);
                continentesIdContinente = em.merge(continentesIdContinente);
            }
            em.remove(paises);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paises> findPaisesEntities() {
        return findPaisesEntities(true, -1, -1);
    }

    public List<Paises> findPaisesEntities(int maxResults, int firstResult) {
        return findPaisesEntities(false, maxResults, firstResult);
    }

    private List<Paises> findPaisesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paises.class));
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

    public Paises findPaises(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paises.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paises> rt = cq.from(Paises.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
