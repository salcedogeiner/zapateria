/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.zapateria.logica.Persona;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.IllegalOrphanException;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.logica.TipoIdentificacion;

/**
 *
 * @author g.salcedo
 */
public class TipoIdentificacionJpaController implements Serializable {

    public TipoIdentificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoIdentificacion tipoIdentificacion) {
        if (tipoIdentificacion.getPersonaSet() == null) {
            tipoIdentificacion.setPersonaSet(new HashSet<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Persona> attachedPersonaSet = new HashSet<Persona>();
            for (Persona personaSetPersonaToAttach : tipoIdentificacion.getPersonaSet()) {
                personaSetPersonaToAttach = em.getReference(personaSetPersonaToAttach.getClass(), personaSetPersonaToAttach.getId());
                attachedPersonaSet.add(personaSetPersonaToAttach);
            }
            tipoIdentificacion.setPersonaSet(attachedPersonaSet);
            em.persist(tipoIdentificacion);
            for (Persona personaSetPersona : tipoIdentificacion.getPersonaSet()) {
                TipoIdentificacion oldTipoIdentificacionOfPersonaSetPersona = personaSetPersona.getTipoIdentificacion();
                personaSetPersona.setTipoIdentificacion(tipoIdentificacion);
                personaSetPersona = em.merge(personaSetPersona);
                if (oldTipoIdentificacionOfPersonaSetPersona != null) {
                    oldTipoIdentificacionOfPersonaSetPersona.getPersonaSet().remove(personaSetPersona);
                    oldTipoIdentificacionOfPersonaSetPersona = em.merge(oldTipoIdentificacionOfPersonaSetPersona);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoIdentificacion tipoIdentificacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoIdentificacion persistentTipoIdentificacion = em.find(TipoIdentificacion.class, tipoIdentificacion.getId());
            Set<Persona> personaSetOld = persistentTipoIdentificacion.getPersonaSet();
            Set<Persona> personaSetNew = tipoIdentificacion.getPersonaSet();
            List<String> illegalOrphanMessages = null;
            for (Persona personaSetOldPersona : personaSetOld) {
                if (!personaSetNew.contains(personaSetOldPersona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Persona " + personaSetOldPersona + " since its tipoIdentificacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<Persona> attachedPersonaSetNew = new HashSet<Persona>();
            for (Persona personaSetNewPersonaToAttach : personaSetNew) {
                personaSetNewPersonaToAttach = em.getReference(personaSetNewPersonaToAttach.getClass(), personaSetNewPersonaToAttach.getId());
                attachedPersonaSetNew.add(personaSetNewPersonaToAttach);
            }
            personaSetNew = attachedPersonaSetNew;
            tipoIdentificacion.setPersonaSet(personaSetNew);
            tipoIdentificacion = em.merge(tipoIdentificacion);
            for (Persona personaSetNewPersona : personaSetNew) {
                if (!personaSetOld.contains(personaSetNewPersona)) {
                    TipoIdentificacion oldTipoIdentificacionOfPersonaSetNewPersona = personaSetNewPersona.getTipoIdentificacion();
                    personaSetNewPersona.setTipoIdentificacion(tipoIdentificacion);
                    personaSetNewPersona = em.merge(personaSetNewPersona);
                    if (oldTipoIdentificacionOfPersonaSetNewPersona != null && !oldTipoIdentificacionOfPersonaSetNewPersona.equals(tipoIdentificacion)) {
                        oldTipoIdentificacionOfPersonaSetNewPersona.getPersonaSet().remove(personaSetNewPersona);
                        oldTipoIdentificacionOfPersonaSetNewPersona = em.merge(oldTipoIdentificacionOfPersonaSetNewPersona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoIdentificacion.getId();
                if (findTipoIdentificacion(id) == null) {
                    throw new NonexistentEntityException("The tipoIdentificacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoIdentificacion tipoIdentificacion;
            try {
                tipoIdentificacion = em.getReference(TipoIdentificacion.class, id);
                tipoIdentificacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoIdentificacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<Persona> personaSetOrphanCheck = tipoIdentificacion.getPersonaSet();
            for (Persona personaSetOrphanCheckPersona : personaSetOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoIdentificacion (" + tipoIdentificacion + ") cannot be destroyed since the Persona " + personaSetOrphanCheckPersona + " in its personaSet field has a non-nullable tipoIdentificacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoIdentificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoIdentificacion> findTipoIdentificacionEntities() {
        return findTipoIdentificacionEntities(true, -1, -1);
    }

    public List<TipoIdentificacion> findTipoIdentificacionEntities(int maxResults, int firstResult) {
        return findTipoIdentificacionEntities(false, maxResults, firstResult);
    }

    private List<TipoIdentificacion> findTipoIdentificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoIdentificacion.class));
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

    public TipoIdentificacion findTipoIdentificacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoIdentificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoIdentificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoIdentificacion> rt = cq.from(TipoIdentificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
