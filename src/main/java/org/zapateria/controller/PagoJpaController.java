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
import org.zapateria.entidad.Concepto;
import org.zapateria.entidad.Pago;
import org.zapateria.entidad.Reparacion;

/**
 *
 * @author jose_
 */
public class PagoJpaController implements Serializable {

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Concepto conceptoBean = pago.getConceptoBean();
            if (conceptoBean != null) {
                conceptoBean = em.getReference(conceptoBean.getClass(), conceptoBean.getId());
                pago.setConceptoBean(conceptoBean);
            }
            Reparacion reparacionBean = pago.getReparacionBean();
            if (reparacionBean != null) {
                reparacionBean = em.getReference(reparacionBean.getClass(), reparacionBean.getId());
                pago.setReparacionBean(reparacionBean);
            }
            em.persist(pago);
            if (conceptoBean != null) {
                conceptoBean.getPagos().add(pago);
                conceptoBean = em.merge(conceptoBean);
            }
            if (reparacionBean != null) {
                reparacionBean.getPagos().add(pago);
                reparacionBean = em.merge(reparacionBean);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPago(pago.getId()) != null) {
                throw new PreexistingEntityException("Pago " + pago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getId());
            Concepto conceptoBeanOld = persistentPago.getConceptoBean();
            Concepto conceptoBeanNew = pago.getConceptoBean();
            Reparacion reparacionBeanOld = persistentPago.getReparacionBean();
            Reparacion reparacionBeanNew = pago.getReparacionBean();
            if (conceptoBeanNew != null) {
                conceptoBeanNew = em.getReference(conceptoBeanNew.getClass(), conceptoBeanNew.getId());
                pago.setConceptoBean(conceptoBeanNew);
            }
            if (reparacionBeanNew != null) {
                reparacionBeanNew = em.getReference(reparacionBeanNew.getClass(), reparacionBeanNew.getId());
                pago.setReparacionBean(reparacionBeanNew);
            }
            pago = em.merge(pago);
            if (conceptoBeanOld != null && !conceptoBeanOld.equals(conceptoBeanNew)) {
                conceptoBeanOld.getPagos().remove(pago);
                conceptoBeanOld = em.merge(conceptoBeanOld);
            }
            if (conceptoBeanNew != null && !conceptoBeanNew.equals(conceptoBeanOld)) {
                conceptoBeanNew.getPagos().add(pago);
                conceptoBeanNew = em.merge(conceptoBeanNew);
            }
            if (reparacionBeanOld != null && !reparacionBeanOld.equals(reparacionBeanNew)) {
                reparacionBeanOld.getPagos().remove(pago);
                reparacionBeanOld = em.merge(reparacionBeanOld);
            }
            if (reparacionBeanNew != null && !reparacionBeanNew.equals(reparacionBeanOld)) {
                reparacionBeanNew.getPagos().add(pago);
                reparacionBeanNew = em.merge(reparacionBeanNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getId();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
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
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            Concepto conceptoBean = pago.getConceptoBean();
            if (conceptoBean != null) {
                conceptoBean.getPagos().remove(pago);
                conceptoBean = em.merge(conceptoBean);
            }
            Reparacion reparacionBean = pago.getReparacionBean();
            if (reparacionBean != null) {
                reparacionBean.getPagos().remove(pago);
                reparacionBean = em.merge(reparacionBean);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
