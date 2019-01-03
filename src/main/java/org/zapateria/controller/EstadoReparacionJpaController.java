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
import org.zapateria.entidad.Reparacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.entidad.EstadoReparacion;

/**
 *
 * @author jose_
 */
public class EstadoReparacionJpaController implements Serializable {

    public EstadoReparacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoReparacion estadoReparacion) throws PreexistingEntityException, Exception {
        if (estadoReparacion.getReparacions() == null) {
            estadoReparacion.setReparacions(new ArrayList<Reparacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reparacion> attachedReparacions = new ArrayList<Reparacion>();
            for (Reparacion reparacionsReparacionToAttach : estadoReparacion.getReparacions()) {
                reparacionsReparacionToAttach = em.getReference(reparacionsReparacionToAttach.getClass(), reparacionsReparacionToAttach.getId());
                attachedReparacions.add(reparacionsReparacionToAttach);
            }
            estadoReparacion.setReparacions(attachedReparacions);
            em.persist(estadoReparacion);
            for (Reparacion reparacionsReparacion : estadoReparacion.getReparacions()) {
                EstadoReparacion oldEstadoReparacionBeanOfReparacionsReparacion = reparacionsReparacion.getEstadoReparacionBean();
                reparacionsReparacion.setEstadoReparacionBean(estadoReparacion);
                reparacionsReparacion = em.merge(reparacionsReparacion);
                if (oldEstadoReparacionBeanOfReparacionsReparacion != null) {
                    oldEstadoReparacionBeanOfReparacionsReparacion.getReparacions().remove(reparacionsReparacion);
                    oldEstadoReparacionBeanOfReparacionsReparacion = em.merge(oldEstadoReparacionBeanOfReparacionsReparacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadoReparacion(estadoReparacion.getId()) != null) {
                throw new PreexistingEntityException("EstadoReparacion " + estadoReparacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoReparacion estadoReparacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoReparacion persistentEstadoReparacion = em.find(EstadoReparacion.class, estadoReparacion.getId());
            List<Reparacion> reparacionsOld = persistentEstadoReparacion.getReparacions();
            List<Reparacion> reparacionsNew = estadoReparacion.getReparacions();
            List<Reparacion> attachedReparacionsNew = new ArrayList<Reparacion>();
            for (Reparacion reparacionsNewReparacionToAttach : reparacionsNew) {
                reparacionsNewReparacionToAttach = em.getReference(reparacionsNewReparacionToAttach.getClass(), reparacionsNewReparacionToAttach.getId());
                attachedReparacionsNew.add(reparacionsNewReparacionToAttach);
            }
            reparacionsNew = attachedReparacionsNew;
            estadoReparacion.setReparacions(reparacionsNew);
            estadoReparacion = em.merge(estadoReparacion);
            for (Reparacion reparacionsOldReparacion : reparacionsOld) {
                if (!reparacionsNew.contains(reparacionsOldReparacion)) {
                    reparacionsOldReparacion.setEstadoReparacionBean(null);
                    reparacionsOldReparacion = em.merge(reparacionsOldReparacion);
                }
            }
            for (Reparacion reparacionsNewReparacion : reparacionsNew) {
                if (!reparacionsOld.contains(reparacionsNewReparacion)) {
                    EstadoReparacion oldEstadoReparacionBeanOfReparacionsNewReparacion = reparacionsNewReparacion.getEstadoReparacionBean();
                    reparacionsNewReparacion.setEstadoReparacionBean(estadoReparacion);
                    reparacionsNewReparacion = em.merge(reparacionsNewReparacion);
                    if (oldEstadoReparacionBeanOfReparacionsNewReparacion != null && !oldEstadoReparacionBeanOfReparacionsNewReparacion.equals(estadoReparacion)) {
                        oldEstadoReparacionBeanOfReparacionsNewReparacion.getReparacions().remove(reparacionsNewReparacion);
                        oldEstadoReparacionBeanOfReparacionsNewReparacion = em.merge(oldEstadoReparacionBeanOfReparacionsNewReparacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadoReparacion.getId();
                if (findEstadoReparacion(id) == null) {
                    throw new NonexistentEntityException("The estadoReparacion with id " + id + " no longer exists.");
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
            EstadoReparacion estadoReparacion;
            try {
                estadoReparacion = em.getReference(EstadoReparacion.class, id);
                estadoReparacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoReparacion with id " + id + " no longer exists.", enfe);
            }
            List<Reparacion> reparacions = estadoReparacion.getReparacions();
            for (Reparacion reparacionsReparacion : reparacions) {
                reparacionsReparacion.setEstadoReparacionBean(null);
                reparacionsReparacion = em.merge(reparacionsReparacion);
            }
            em.remove(estadoReparacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoReparacion> findEstadoReparacionEntities() {
        return findEstadoReparacionEntities(true, -1, -1);
    }

    public List<EstadoReparacion> findEstadoReparacionEntities(int maxResults, int firstResult) {
        return findEstadoReparacionEntities(false, maxResults, firstResult);
    }

    private List<EstadoReparacion> findEstadoReparacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoReparacion.class));
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

    public EstadoReparacion findEstadoReparacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoReparacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoReparacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoReparacion> rt = cq.from(EstadoReparacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
