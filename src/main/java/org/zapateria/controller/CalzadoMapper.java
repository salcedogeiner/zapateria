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
import org.zapateria.logica.Reparacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.IllegalOrphanException;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.logica.Calzado;

/**
 *
 * @author g.salcedo
 */
public class CalzadoMapper implements Serializable {

    public CalzadoMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Calzado calzado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reparacion reparacion = calzado.getReparacion();
            if (reparacion != null) {
                reparacion = em.getReference(reparacion.getClass(), reparacion.getId());
                calzado.setReparacion(reparacion);
            }
            em.persist(calzado);
            if (reparacion != null) {
                Calzado oldCalzadoOfReparacion = reparacion.getCalzado();
                if (oldCalzadoOfReparacion != null) {
                    oldCalzadoOfReparacion.setReparacion(null);
                    oldCalzadoOfReparacion = em.merge(oldCalzadoOfReparacion);
                }
                reparacion.setCalzado(calzado);
                reparacion = em.merge(reparacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Calzado calzado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Calzado persistentCalzado = em.find(Calzado.class, calzado.getId());
            Reparacion reparacionOld = persistentCalzado.getReparacion();
            Reparacion reparacionNew = calzado.getReparacion();
            List<String> illegalOrphanMessages = null;
            if (reparacionOld != null && !reparacionOld.equals(reparacionNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Reparacion " + reparacionOld + " since its calzado field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (reparacionNew != null) {
                reparacionNew = em.getReference(reparacionNew.getClass(), reparacionNew.getId());
                calzado.setReparacion(reparacionNew);
            }
            calzado = em.merge(calzado);
            if (reparacionNew != null && !reparacionNew.equals(reparacionOld)) {
                Calzado oldCalzadoOfReparacion = reparacionNew.getCalzado();
                if (oldCalzadoOfReparacion != null) {
                    oldCalzadoOfReparacion.setReparacion(null);
                    oldCalzadoOfReparacion = em.merge(oldCalzadoOfReparacion);
                }
                reparacionNew.setCalzado(calzado);
                reparacionNew = em.merge(reparacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = calzado.getId();
                if (findCalzado(id) == null) {
                    throw new NonexistentEntityException("The calzado with id " + id + " no longer exists.");
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
            Calzado calzado;
            try {
                calzado = em.getReference(Calzado.class, id);
                calzado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The calzado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Reparacion reparacionOrphanCheck = calzado.getReparacion();
            if (reparacionOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Calzado (" + calzado + ") cannot be destroyed since the Reparacion " + reparacionOrphanCheck + " in its reparacion field has a non-nullable calzado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(calzado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Calzado> findCalzadoEntities() {
        return findCalzadoEntities(true, -1, -1);
    }

    public List<Calzado> findCalzadoEntities(int maxResults, int firstResult) {
        return findCalzadoEntities(false, maxResults, firstResult);
    }

    private List<Calzado> findCalzadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Calzado.class));
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

    public Calzado findCalzado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Calzado.class, id);
        } finally {
            em.close();
        }
    }

    public int getCalzadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Calzado> rt = cq.from(Calzado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
