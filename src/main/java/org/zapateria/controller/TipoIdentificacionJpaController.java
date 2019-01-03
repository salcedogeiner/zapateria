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
import org.zapateria.entidad.Persona;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.entidad.TipoIdentificacion;

/**
 *
 * @author jose_
 */
public class TipoIdentificacionJpaController implements Serializable {

    public TipoIdentificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoIdentificacion tipoIdentificacion) throws PreexistingEntityException, Exception {
        if (tipoIdentificacion.getPersonas() == null) {
            tipoIdentificacion.setPersonas(new ArrayList<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Persona> attachedPersonas = new ArrayList<Persona>();
            for (Persona personasPersonaToAttach : tipoIdentificacion.getPersonas()) {
                personasPersonaToAttach = em.getReference(personasPersonaToAttach.getClass(), personasPersonaToAttach.getId());
                attachedPersonas.add(personasPersonaToAttach);
            }
            tipoIdentificacion.setPersonas(attachedPersonas);
            em.persist(tipoIdentificacion);
            for (Persona personasPersona : tipoIdentificacion.getPersonas()) {
                TipoIdentificacion oldTipoIdentificacionBeanOfPersonasPersona = personasPersona.getTipoIdentificacionBean();
                personasPersona.setTipoIdentificacionBean(tipoIdentificacion);
                personasPersona = em.merge(personasPersona);
                if (oldTipoIdentificacionBeanOfPersonasPersona != null) {
                    oldTipoIdentificacionBeanOfPersonasPersona.getPersonas().remove(personasPersona);
                    oldTipoIdentificacionBeanOfPersonasPersona = em.merge(oldTipoIdentificacionBeanOfPersonasPersona);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoIdentificacion(tipoIdentificacion.getId()) != null) {
                throw new PreexistingEntityException("TipoIdentificacion " + tipoIdentificacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoIdentificacion tipoIdentificacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoIdentificacion persistentTipoIdentificacion = em.find(TipoIdentificacion.class, tipoIdentificacion.getId());
            List<Persona> personasOld = persistentTipoIdentificacion.getPersonas();
            List<Persona> personasNew = tipoIdentificacion.getPersonas();
            List<Persona> attachedPersonasNew = new ArrayList<Persona>();
            for (Persona personasNewPersonaToAttach : personasNew) {
                personasNewPersonaToAttach = em.getReference(personasNewPersonaToAttach.getClass(), personasNewPersonaToAttach.getId());
                attachedPersonasNew.add(personasNewPersonaToAttach);
            }
            personasNew = attachedPersonasNew;
            tipoIdentificacion.setPersonas(personasNew);
            tipoIdentificacion = em.merge(tipoIdentificacion);
            for (Persona personasOldPersona : personasOld) {
                if (!personasNew.contains(personasOldPersona)) {
                    personasOldPersona.setTipoIdentificacionBean(null);
                    personasOldPersona = em.merge(personasOldPersona);
                }
            }
            for (Persona personasNewPersona : personasNew) {
                if (!personasOld.contains(personasNewPersona)) {
                    TipoIdentificacion oldTipoIdentificacionBeanOfPersonasNewPersona = personasNewPersona.getTipoIdentificacionBean();
                    personasNewPersona.setTipoIdentificacionBean(tipoIdentificacion);
                    personasNewPersona = em.merge(personasNewPersona);
                    if (oldTipoIdentificacionBeanOfPersonasNewPersona != null && !oldTipoIdentificacionBeanOfPersonasNewPersona.equals(tipoIdentificacion)) {
                        oldTipoIdentificacionBeanOfPersonasNewPersona.getPersonas().remove(personasNewPersona);
                        oldTipoIdentificacionBeanOfPersonasNewPersona = em.merge(oldTipoIdentificacionBeanOfPersonasNewPersona);
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
            List<Persona> personas = tipoIdentificacion.getPersonas();
            for (Persona personasPersona : personas) {
                personasPersona.setTipoIdentificacionBean(null);
                personasPersona = em.merge(personasPersona);
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
