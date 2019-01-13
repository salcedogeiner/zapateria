/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.mapper;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.zapateria.mapper.exceptions.NonexistentEntityException;
import org.zapateria.logica.Concepto;
import org.zapateria.logica.Pago;
import org.zapateria.logica.Reparacion;

/**
 *
 * @author g.salcedo
 */
public class PagoMapper implements Serializable {

    public PagoMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Concepto concepto = pago.getConcepto();
            if (concepto != null) {
                concepto = em.getReference(concepto.getClass(), concepto.getId());
                pago.setConcepto(concepto);
            }
            Reparacion reparacion = pago.getReparacion();
            if (reparacion != null) {
                reparacion = em.getReference(reparacion.getClass(), reparacion.getId());
                pago.setReparacion(reparacion);
            }
            em.persist(pago);
            if (concepto != null) {
                concepto.getPagoSet().add(pago);
                concepto = em.merge(concepto);
            }
            if (reparacion != null) {
                reparacion.getPagoSet().add(pago);
                reparacion = em.merge(reparacion);
            }
            em.getTransaction().commit();
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
            Concepto conceptoOld = persistentPago.getConcepto();
            Concepto conceptoNew = pago.getConcepto();
            Reparacion reparacionOld = persistentPago.getReparacion();
            Reparacion reparacionNew = pago.getReparacion();
            if (conceptoNew != null) {
                conceptoNew = em.getReference(conceptoNew.getClass(), conceptoNew.getId());
                pago.setConcepto(conceptoNew);
            }
            if (reparacionNew != null) {
                reparacionNew = em.getReference(reparacionNew.getClass(), reparacionNew.getId());
                pago.setReparacion(reparacionNew);
            }
            pago = em.merge(pago);
            if (conceptoOld != null && !conceptoOld.equals(conceptoNew)) {
                conceptoOld.getPagoSet().remove(pago);
                conceptoOld = em.merge(conceptoOld);
            }
            if (conceptoNew != null && !conceptoNew.equals(conceptoOld)) {
                conceptoNew.getPagoSet().add(pago);
                conceptoNew = em.merge(conceptoNew);
            }
            if (reparacionOld != null && !reparacionOld.equals(reparacionNew)) {
                reparacionOld.getPagoSet().remove(pago);
                reparacionOld = em.merge(reparacionOld);
            }
            if (reparacionNew != null && !reparacionNew.equals(reparacionOld)) {
                reparacionNew.getPagoSet().add(pago);
                reparacionNew = em.merge(reparacionNew);
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
            Concepto concepto = pago.getConcepto();
            if (concepto != null) {
                concepto.getPagoSet().remove(pago);
                concepto = em.merge(concepto);
            }
            Reparacion reparacion = pago.getReparacion();
            if (reparacion != null) {
                reparacion.getPagoSet().remove(pago);
                reparacion = em.merge(reparacion);
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
