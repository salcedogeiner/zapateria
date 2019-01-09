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
import org.zapateria.logica.Pago;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.logica.Concepto;

/**
 *
 * @author g.salcedo
 */
public class ConceptoMapper implements Serializable {

    public ConceptoMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Concepto concepto) {
        if (concepto.getPagoSet() == null) {
            concepto.setPagoSet(new HashSet<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Pago> attachedPagoSet = new HashSet<Pago>();
            for (Pago pagoSetPagoToAttach : concepto.getPagoSet()) {
                pagoSetPagoToAttach = em.getReference(pagoSetPagoToAttach.getClass(), pagoSetPagoToAttach.getId());
                attachedPagoSet.add(pagoSetPagoToAttach);
            }
            concepto.setPagoSet(attachedPagoSet);
            em.persist(concepto);
            for (Pago pagoSetPago : concepto.getPagoSet()) {
                Concepto oldConceptoOfPagoSetPago = pagoSetPago.getConcepto();
                pagoSetPago.setConcepto(concepto);
                pagoSetPago = em.merge(pagoSetPago);
                if (oldConceptoOfPagoSetPago != null) {
                    oldConceptoOfPagoSetPago.getPagoSet().remove(pagoSetPago);
                    oldConceptoOfPagoSetPago = em.merge(oldConceptoOfPagoSetPago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Concepto concepto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Concepto persistentConcepto = em.find(Concepto.class, concepto.getId());
            Set<Pago> pagoSetOld = persistentConcepto.getPagoSet();
            Set<Pago> pagoSetNew = concepto.getPagoSet();
            Set<Pago> attachedPagoSetNew = new HashSet<Pago>();
            for (Pago pagoSetNewPagoToAttach : pagoSetNew) {
                pagoSetNewPagoToAttach = em.getReference(pagoSetNewPagoToAttach.getClass(), pagoSetNewPagoToAttach.getId());
                attachedPagoSetNew.add(pagoSetNewPagoToAttach);
            }
            pagoSetNew = attachedPagoSetNew;
            concepto.setPagoSet(pagoSetNew);
            concepto = em.merge(concepto);
            for (Pago pagoSetOldPago : pagoSetOld) {
                if (!pagoSetNew.contains(pagoSetOldPago)) {
                    pagoSetOldPago.setConcepto(null);
                    pagoSetOldPago = em.merge(pagoSetOldPago);
                }
            }
            for (Pago pagoSetNewPago : pagoSetNew) {
                if (!pagoSetOld.contains(pagoSetNewPago)) {
                    Concepto oldConceptoOfPagoSetNewPago = pagoSetNewPago.getConcepto();
                    pagoSetNewPago.setConcepto(concepto);
                    pagoSetNewPago = em.merge(pagoSetNewPago);
                    if (oldConceptoOfPagoSetNewPago != null && !oldConceptoOfPagoSetNewPago.equals(concepto)) {
                        oldConceptoOfPagoSetNewPago.getPagoSet().remove(pagoSetNewPago);
                        oldConceptoOfPagoSetNewPago = em.merge(oldConceptoOfPagoSetNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = concepto.getId();
                if (findConcepto(id) == null) {
                    throw new NonexistentEntityException("The concepto with id " + id + " no longer exists.");
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
            Concepto concepto;
            try {
                concepto = em.getReference(Concepto.class, id);
                concepto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The concepto with id " + id + " no longer exists.", enfe);
            }
            Set<Pago> pagoSet = concepto.getPagoSet();
            for (Pago pagoSetPago : pagoSet) {
                pagoSetPago.setConcepto(null);
                pagoSetPago = em.merge(pagoSetPago);
            }
            em.remove(concepto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Concepto> findConceptoEntities() {
        return findConceptoEntities(true, -1, -1);
    }

    public List<Concepto> findConceptoEntities(int maxResults, int firstResult) {
        return findConceptoEntities(false, maxResults, firstResult);
    }

    private List<Concepto> findConceptoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Concepto.class));
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

    public Concepto findConcepto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Concepto.class, id);
        } finally {
            em.close();
        }
    }

    public int getConceptoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Concepto> rt = cq.from(Concepto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
