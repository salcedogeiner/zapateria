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
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.entidad.Insumo;
import org.zapateria.entidad.IsumoReparacion;
import org.zapateria.entidad.Reparacion;

/**
 *
 * @author jose_
 */
public class IsumoReparacionJpaController implements Serializable {

    public IsumoReparacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(IsumoReparacion isumoReparacion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo insumoBean = isumoReparacion.getInsumoBean();
            if (insumoBean != null) {
                insumoBean = em.getReference(insumoBean.getClass(), insumoBean.getId());
                isumoReparacion.setInsumoBean(insumoBean);
            }
            Reparacion reparacionBean = isumoReparacion.getReparacionBean();
            if (reparacionBean != null) {
                reparacionBean = em.getReference(reparacionBean.getClass(), reparacionBean.getId());
                isumoReparacion.setReparacionBean(reparacionBean);
            }
            em.persist(isumoReparacion);
            if (insumoBean != null) {
                insumoBean.getIsumoReparacions().add(isumoReparacion);
                insumoBean = em.merge(insumoBean);
            }
            if (reparacionBean != null) {
                reparacionBean.getIsumoReparacions().add(isumoReparacion);
                reparacionBean = em.merge(reparacionBean);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIsumoReparacion(isumoReparacion.getId()) != null) {
                throw new PreexistingEntityException("IsumoReparacion " + isumoReparacion + " already exists.", ex);
            }
            throw ex;
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
            Insumo insumoBeanOld = persistentIsumoReparacion.getInsumoBean();
            Insumo insumoBeanNew = isumoReparacion.getInsumoBean();
            Reparacion reparacionBeanOld = persistentIsumoReparacion.getReparacionBean();
            Reparacion reparacionBeanNew = isumoReparacion.getReparacionBean();
            if (insumoBeanNew != null) {
                insumoBeanNew = em.getReference(insumoBeanNew.getClass(), insumoBeanNew.getId());
                isumoReparacion.setInsumoBean(insumoBeanNew);
            }
            if (reparacionBeanNew != null) {
                reparacionBeanNew = em.getReference(reparacionBeanNew.getClass(), reparacionBeanNew.getId());
                isumoReparacion.setReparacionBean(reparacionBeanNew);
            }
            isumoReparacion = em.merge(isumoReparacion);
            if (insumoBeanOld != null && !insumoBeanOld.equals(insumoBeanNew)) {
                insumoBeanOld.getIsumoReparacions().remove(isumoReparacion);
                insumoBeanOld = em.merge(insumoBeanOld);
            }
            if (insumoBeanNew != null && !insumoBeanNew.equals(insumoBeanOld)) {
                insumoBeanNew.getIsumoReparacions().add(isumoReparacion);
                insumoBeanNew = em.merge(insumoBeanNew);
            }
            if (reparacionBeanOld != null && !reparacionBeanOld.equals(reparacionBeanNew)) {
                reparacionBeanOld.getIsumoReparacions().remove(isumoReparacion);
                reparacionBeanOld = em.merge(reparacionBeanOld);
            }
            if (reparacionBeanNew != null && !reparacionBeanNew.equals(reparacionBeanOld)) {
                reparacionBeanNew.getIsumoReparacions().add(isumoReparacion);
                reparacionBeanNew = em.merge(reparacionBeanNew);
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
            Insumo insumoBean = isumoReparacion.getInsumoBean();
            if (insumoBean != null) {
                insumoBean.getIsumoReparacions().remove(isumoReparacion);
                insumoBean = em.merge(insumoBean);
            }
            Reparacion reparacionBean = isumoReparacion.getReparacionBean();
            if (reparacionBean != null) {
                reparacionBean.getIsumoReparacions().remove(isumoReparacion);
                reparacionBean = em.merge(reparacionBean);
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
