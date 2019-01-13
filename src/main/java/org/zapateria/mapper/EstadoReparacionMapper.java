/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.mapper;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.zapateria.logica.Reparacion;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.mapper.exceptions.IllegalOrphanException;
import org.zapateria.mapper.exceptions.NonexistentEntityException;
import org.zapateria.logica.EstadoReparacion;

/**
 *
 * @author g.salcedo
 */
public class EstadoReparacionMapper implements Serializable {

    public EstadoReparacionMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoReparacion estadoReparacion) {
        if (estadoReparacion.getReparacionSet() == null) {
            estadoReparacion.setReparacionSet(new HashSet<Reparacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Reparacion> attachedReparacionSet = new HashSet<Reparacion>();
            for (Reparacion reparacionSetReparacionToAttach : estadoReparacion.getReparacionSet()) {
                reparacionSetReparacionToAttach = em.getReference(reparacionSetReparacionToAttach.getClass(), reparacionSetReparacionToAttach.getId());
                attachedReparacionSet.add(reparacionSetReparacionToAttach);
            }
            estadoReparacion.setReparacionSet(attachedReparacionSet);
            em.persist(estadoReparacion);
            for (Reparacion reparacionSetReparacion : estadoReparacion.getReparacionSet()) {
                EstadoReparacion oldEstadoReparacionOfReparacionSetReparacion = reparacionSetReparacion.getEstadoReparacion();
                reparacionSetReparacion.setEstadoReparacion(estadoReparacion);
                reparacionSetReparacion = em.merge(reparacionSetReparacion);
                if (oldEstadoReparacionOfReparacionSetReparacion != null) {
                    oldEstadoReparacionOfReparacionSetReparacion.getReparacionSet().remove(reparacionSetReparacion);
                    oldEstadoReparacionOfReparacionSetReparacion = em.merge(oldEstadoReparacionOfReparacionSetReparacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoReparacion estadoReparacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoReparacion persistentEstadoReparacion = em.find(EstadoReparacion.class, estadoReparacion.getId());
            Set<Reparacion> reparacionSetOld = persistentEstadoReparacion.getReparacionSet();
            Set<Reparacion> reparacionSetNew = estadoReparacion.getReparacionSet();
            List<String> illegalOrphanMessages = null;
            for (Reparacion reparacionSetOldReparacion : reparacionSetOld) {
                if (!reparacionSetNew.contains(reparacionSetOldReparacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reparacion " + reparacionSetOldReparacion + " since its estadoReparacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<Reparacion> attachedReparacionSetNew = new HashSet<Reparacion>();
            for (Reparacion reparacionSetNewReparacionToAttach : reparacionSetNew) {
                reparacionSetNewReparacionToAttach = em.getReference(reparacionSetNewReparacionToAttach.getClass(), reparacionSetNewReparacionToAttach.getId());
                attachedReparacionSetNew.add(reparacionSetNewReparacionToAttach);
            }
            reparacionSetNew = attachedReparacionSetNew;
            estadoReparacion.setReparacionSet(reparacionSetNew);
            estadoReparacion = em.merge(estadoReparacion);
            for (Reparacion reparacionSetNewReparacion : reparacionSetNew) {
                if (!reparacionSetOld.contains(reparacionSetNewReparacion)) {
                    EstadoReparacion oldEstadoReparacionOfReparacionSetNewReparacion = reparacionSetNewReparacion.getEstadoReparacion();
                    reparacionSetNewReparacion.setEstadoReparacion(estadoReparacion);
                    reparacionSetNewReparacion = em.merge(reparacionSetNewReparacion);
                    if (oldEstadoReparacionOfReparacionSetNewReparacion != null && !oldEstadoReparacionOfReparacionSetNewReparacion.equals(estadoReparacion)) {
                        oldEstadoReparacionOfReparacionSetNewReparacion.getReparacionSet().remove(reparacionSetNewReparacion);
                        oldEstadoReparacionOfReparacionSetNewReparacion = em.merge(oldEstadoReparacionOfReparacionSetNewReparacion);
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Set<Reparacion> reparacionSetOrphanCheck = estadoReparacion.getReparacionSet();
            for (Reparacion reparacionSetOrphanCheckReparacion : reparacionSetOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoReparacion (" + estadoReparacion + ") cannot be destroyed since the Reparacion " + reparacionSetOrphanCheckReparacion + " in its reparacionSet field has a non-nullable estadoReparacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
