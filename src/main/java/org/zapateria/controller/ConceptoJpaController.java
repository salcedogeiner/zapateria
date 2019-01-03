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
import org.zapateria.entidad.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.entidad.Concepto;

/**
 *
 * @author jose_
 */
public class ConceptoJpaController implements Serializable {

    public ConceptoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Concepto concepto) throws PreexistingEntityException, Exception {
        if (concepto.getPagos() == null) {
            concepto.setPagos(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pago> attachedPagos = new ArrayList<Pago>();
            for (Pago pagosPagoToAttach : concepto.getPagos()) {
                pagosPagoToAttach = em.getReference(pagosPagoToAttach.getClass(), pagosPagoToAttach.getId());
                attachedPagos.add(pagosPagoToAttach);
            }
            concepto.setPagos(attachedPagos);
            em.persist(concepto);
            for (Pago pagosPago : concepto.getPagos()) {
                Concepto oldConceptoBeanOfPagosPago = pagosPago.getConceptoBean();
                pagosPago.setConceptoBean(concepto);
                pagosPago = em.merge(pagosPago);
                if (oldConceptoBeanOfPagosPago != null) {
                    oldConceptoBeanOfPagosPago.getPagos().remove(pagosPago);
                    oldConceptoBeanOfPagosPago = em.merge(oldConceptoBeanOfPagosPago);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConcepto(concepto.getId()) != null) {
                throw new PreexistingEntityException("Concepto " + concepto + " already exists.", ex);
            }
            throw ex;
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
            List<Pago> pagosOld = persistentConcepto.getPagos();
            List<Pago> pagosNew = concepto.getPagos();
            List<Pago> attachedPagosNew = new ArrayList<Pago>();
            for (Pago pagosNewPagoToAttach : pagosNew) {
                pagosNewPagoToAttach = em.getReference(pagosNewPagoToAttach.getClass(), pagosNewPagoToAttach.getId());
                attachedPagosNew.add(pagosNewPagoToAttach);
            }
            pagosNew = attachedPagosNew;
            concepto.setPagos(pagosNew);
            concepto = em.merge(concepto);
            for (Pago pagosOldPago : pagosOld) {
                if (!pagosNew.contains(pagosOldPago)) {
                    pagosOldPago.setConceptoBean(null);
                    pagosOldPago = em.merge(pagosOldPago);
                }
            }
            for (Pago pagosNewPago : pagosNew) {
                if (!pagosOld.contains(pagosNewPago)) {
                    Concepto oldConceptoBeanOfPagosNewPago = pagosNewPago.getConceptoBean();
                    pagosNewPago.setConceptoBean(concepto);
                    pagosNewPago = em.merge(pagosNewPago);
                    if (oldConceptoBeanOfPagosNewPago != null && !oldConceptoBeanOfPagosNewPago.equals(concepto)) {
                        oldConceptoBeanOfPagosNewPago.getPagos().remove(pagosNewPago);
                        oldConceptoBeanOfPagosNewPago = em.merge(oldConceptoBeanOfPagosNewPago);
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
            List<Pago> pagos = concepto.getPagos();
            for (Pago pagosPago : pagos) {
                pagosPago.setConceptoBean(null);
                pagosPago = em.merge(pagosPago);
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
