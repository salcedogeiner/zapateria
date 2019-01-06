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
import org.zapateria.logica.IsumoReparacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.logica.Insumo;

/**
 *
 * @author jose_
 */
public class InsumoJpaController implements Serializable {

    public InsumoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Insumo insumo) throws PreexistingEntityException, Exception {
        if (insumo.getIsumoReparacions() == null) {
            insumo.setIsumoReparacions(new ArrayList<IsumoReparacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<IsumoReparacion> attachedIsumoReparacions = new ArrayList<IsumoReparacion>();
            for (IsumoReparacion isumoReparacionsIsumoReparacionToAttach : insumo.getIsumoReparacions()) {
                isumoReparacionsIsumoReparacionToAttach = em.getReference(isumoReparacionsIsumoReparacionToAttach.getClass(), isumoReparacionsIsumoReparacionToAttach.getId());
                attachedIsumoReparacions.add(isumoReparacionsIsumoReparacionToAttach);
            }
            insumo.setIsumoReparacions(attachedIsumoReparacions);
            em.persist(insumo);
            for (IsumoReparacion isumoReparacionsIsumoReparacion : insumo.getIsumoReparacions()) {
                Insumo oldInsumoBeanOfIsumoReparacionsIsumoReparacion = isumoReparacionsIsumoReparacion.getInsumoBean();
                isumoReparacionsIsumoReparacion.setInsumoBean(insumo);
                isumoReparacionsIsumoReparacion = em.merge(isumoReparacionsIsumoReparacion);
                if (oldInsumoBeanOfIsumoReparacionsIsumoReparacion != null) {
                    oldInsumoBeanOfIsumoReparacionsIsumoReparacion.getIsumoReparacions().remove(isumoReparacionsIsumoReparacion);
                    oldInsumoBeanOfIsumoReparacionsIsumoReparacion = em.merge(oldInsumoBeanOfIsumoReparacionsIsumoReparacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInsumo(insumo.getId()) != null) {
                throw new PreexistingEntityException("Insumo " + insumo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Insumo insumo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo persistentInsumo = em.find(Insumo.class, insumo.getId());
            List<IsumoReparacion> isumoReparacionsOld = persistentInsumo.getIsumoReparacions();
            List<IsumoReparacion> isumoReparacionsNew = insumo.getIsumoReparacions();
            List<IsumoReparacion> attachedIsumoReparacionsNew = new ArrayList<IsumoReparacion>();
            for (IsumoReparacion isumoReparacionsNewIsumoReparacionToAttach : isumoReparacionsNew) {
                isumoReparacionsNewIsumoReparacionToAttach = em.getReference(isumoReparacionsNewIsumoReparacionToAttach.getClass(), isumoReparacionsNewIsumoReparacionToAttach.getId());
                attachedIsumoReparacionsNew.add(isumoReparacionsNewIsumoReparacionToAttach);
            }
            isumoReparacionsNew = attachedIsumoReparacionsNew;
            insumo.setIsumoReparacions(isumoReparacionsNew);
            insumo = em.merge(insumo);
            for (IsumoReparacion isumoReparacionsOldIsumoReparacion : isumoReparacionsOld) {
                if (!isumoReparacionsNew.contains(isumoReparacionsOldIsumoReparacion)) {
                    isumoReparacionsOldIsumoReparacion.setInsumoBean(null);
                    isumoReparacionsOldIsumoReparacion = em.merge(isumoReparacionsOldIsumoReparacion);
                }
            }
            for (IsumoReparacion isumoReparacionsNewIsumoReparacion : isumoReparacionsNew) {
                if (!isumoReparacionsOld.contains(isumoReparacionsNewIsumoReparacion)) {
                    Insumo oldInsumoBeanOfIsumoReparacionsNewIsumoReparacion = isumoReparacionsNewIsumoReparacion.getInsumoBean();
                    isumoReparacionsNewIsumoReparacion.setInsumoBean(insumo);
                    isumoReparacionsNewIsumoReparacion = em.merge(isumoReparacionsNewIsumoReparacion);
                    if (oldInsumoBeanOfIsumoReparacionsNewIsumoReparacion != null && !oldInsumoBeanOfIsumoReparacionsNewIsumoReparacion.equals(insumo)) {
                        oldInsumoBeanOfIsumoReparacionsNewIsumoReparacion.getIsumoReparacions().remove(isumoReparacionsNewIsumoReparacion);
                        oldInsumoBeanOfIsumoReparacionsNewIsumoReparacion = em.merge(oldInsumoBeanOfIsumoReparacionsNewIsumoReparacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = insumo.getId();
                if (findInsumo(id) == null) {
                    throw new NonexistentEntityException("The insumo with id " + id + " no longer exists.");
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
            Insumo insumo;
            try {
                insumo = em.getReference(Insumo.class, id);
                insumo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The insumo with id " + id + " no longer exists.", enfe);
            }
            List<IsumoReparacion> isumoReparacions = insumo.getIsumoReparacions();
            for (IsumoReparacion isumoReparacionsIsumoReparacion : isumoReparacions) {
                isumoReparacionsIsumoReparacion.setInsumoBean(null);
                isumoReparacionsIsumoReparacion = em.merge(isumoReparacionsIsumoReparacion);
            }
            em.remove(insumo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Insumo> findInsumoEntities() {
        return findInsumoEntities(true, -1, -1);
    }

    public List<Insumo> findInsumoEntities(int maxResults, int firstResult) {
        return findInsumoEntities(false, maxResults, firstResult);
    }

    private List<Insumo> findInsumoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Insumo.class));
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

    public Insumo findInsumo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Insumo.class, id);
        } finally {
            em.close();
        }
    }

    public int getInsumoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Insumo> rt = cq.from(Insumo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
