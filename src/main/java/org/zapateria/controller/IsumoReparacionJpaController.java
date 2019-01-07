/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.logica.Insumo;
import org.zapateria.logica.IsumoReparacion;
import org.zapateria.logica.Reparacion;

/**
 *
 * @author g.salcedo
 */
public class IsumoReparacionJpaController implements Serializable {

    public IsumoReparacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(IsumoReparacion isumoReparacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo insumo = isumoReparacion.getInsumo();
            if (insumo != null) {
                insumo = em.getReference(insumo.getClass(), insumo.getId());
                isumoReparacion.setInsumo(insumo);
            }
            Reparacion reparacion = isumoReparacion.getReparacion();
            if (reparacion != null) {
                reparacion = em.getReference(reparacion.getClass(), reparacion.getId());
                isumoReparacion.setReparacion(reparacion);
            }
            em.persist(isumoReparacion);
            if (insumo != null) {
                insumo.getIsumoReparacionSet().add(isumoReparacion);
                insumo = em.merge(insumo);
            }
            if (reparacion != null) {
                reparacion.getIsumoReparacionSet().add(isumoReparacion);
                reparacion = em.merge(reparacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(IsumoReparacion isumoReparacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IsumoReparacion persistentIsumoReparacion = em.find(IsumoReparacion.class, isumoReparacion.getId());
            Insumo insumoOld = persistentIsumoReparacion.getInsumo();
            Insumo insumoNew = isumoReparacion.getInsumo();
            Reparacion reparacionOld = persistentIsumoReparacion.getReparacion();
            Reparacion reparacionNew = isumoReparacion.getReparacion();
            if (insumoNew != null) {
                insumoNew = em.getReference(insumoNew.getClass(), insumoNew.getId());
                isumoReparacion.setInsumo(insumoNew);
            }
            if (reparacionNew != null) {
                reparacionNew = em.getReference(reparacionNew.getClass(), reparacionNew.getId());
                isumoReparacion.setReparacion(reparacionNew);
            }
            isumoReparacion = em.merge(isumoReparacion);
            if (insumoOld != null && !insumoOld.equals(insumoNew)) {
                insumoOld.getIsumoReparacionSet().remove(isumoReparacion);
                insumoOld = em.merge(insumoOld);
            }
            if (insumoNew != null && !insumoNew.equals(insumoOld)) {
                insumoNew.getIsumoReparacionSet().add(isumoReparacion);
                insumoNew = em.merge(insumoNew);
            }
            if (reparacionOld != null && !reparacionOld.equals(reparacionNew)) {
                reparacionOld.getIsumoReparacionSet().remove(isumoReparacion);
                reparacionOld = em.merge(reparacionOld);
            }
            if (reparacionNew != null && !reparacionNew.equals(reparacionOld)) {
                reparacionNew.getIsumoReparacionSet().add(isumoReparacion);
                reparacionNew = em.merge(reparacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = isumoReparacion.getId();
                if (findIsumoReparacion(id) == null) {
                    throw new NonexistentEntityException("The isumoReparacion with id " + id + " no longer exists.");
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
            IsumoReparacion isumoReparacion;
            try {
                isumoReparacion = em.getReference(IsumoReparacion.class, id);
                isumoReparacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The isumoReparacion with id " + id + " no longer exists.", enfe);
            }
            Insumo insumo = isumoReparacion.getInsumo();
            if (insumo != null) {
                insumo.getIsumoReparacionSet().remove(isumoReparacion);
                insumo = em.merge(insumo);
            }
            Reparacion reparacion = isumoReparacion.getReparacion();
            if (reparacion != null) {
                reparacion.getIsumoReparacionSet().remove(isumoReparacion);
                reparacion = em.merge(reparacion);
            }
            em.remove(isumoReparacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<IsumoReparacion> findIsumoReparacionEntities() {
        return findIsumoReparacionEntities(true, -1, -1);
    }

    public List<IsumoReparacion> findIsumoReparacionEntities(int maxResults, int firstResult) {
        return findIsumoReparacionEntities(false, maxResults, firstResult);
    }

    private List<IsumoReparacion> findIsumoReparacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(IsumoReparacion.class));
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

    public IsumoReparacion findIsumoReparacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(IsumoReparacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getIsumoReparacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<IsumoReparacion> rt = cq.from(IsumoReparacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
