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
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.IllegalOrphanException;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.logica.Insumo;

/**
 *
 * @author g.salcedo
 */
public class InsumoJpaController implements Serializable {

    public InsumoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Insumo insumo) {
        if (insumo.getIsumoReparacionSet() == null) {
            insumo.setIsumoReparacionSet(new HashSet<IsumoReparacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<IsumoReparacion> attachedIsumoReparacionSet = new HashSet<IsumoReparacion>();
            for (IsumoReparacion isumoReparacionSetIsumoReparacionToAttach : insumo.getIsumoReparacionSet()) {
                isumoReparacionSetIsumoReparacionToAttach = em.getReference(isumoReparacionSetIsumoReparacionToAttach.getClass(), isumoReparacionSetIsumoReparacionToAttach.getId());
                attachedIsumoReparacionSet.add(isumoReparacionSetIsumoReparacionToAttach);
            }
            insumo.setIsumoReparacionSet(attachedIsumoReparacionSet);
            em.persist(insumo);
            for (IsumoReparacion isumoReparacionSetIsumoReparacion : insumo.getIsumoReparacionSet()) {
                Insumo oldInsumoOfIsumoReparacionSetIsumoReparacion = isumoReparacionSetIsumoReparacion.getInsumo();
                isumoReparacionSetIsumoReparacion.setInsumo(insumo);
                isumoReparacionSetIsumoReparacion = em.merge(isumoReparacionSetIsumoReparacion);
                if (oldInsumoOfIsumoReparacionSetIsumoReparacion != null) {
                    oldInsumoOfIsumoReparacionSetIsumoReparacion.getIsumoReparacionSet().remove(isumoReparacionSetIsumoReparacion);
                    oldInsumoOfIsumoReparacionSetIsumoReparacion = em.merge(oldInsumoOfIsumoReparacionSetIsumoReparacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Insumo insumo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo persistentInsumo = em.find(Insumo.class, insumo.getId());
            Set<IsumoReparacion> isumoReparacionSetOld = persistentInsumo.getIsumoReparacionSet();
            Set<IsumoReparacion> isumoReparacionSetNew = insumo.getIsumoReparacionSet();
            List<String> illegalOrphanMessages = null;
            for (IsumoReparacion isumoReparacionSetOldIsumoReparacion : isumoReparacionSetOld) {
                if (!isumoReparacionSetNew.contains(isumoReparacionSetOldIsumoReparacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain IsumoReparacion " + isumoReparacionSetOldIsumoReparacion + " since its insumo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<IsumoReparacion> attachedIsumoReparacionSetNew = new HashSet<IsumoReparacion>();
            for (IsumoReparacion isumoReparacionSetNewIsumoReparacionToAttach : isumoReparacionSetNew) {
                isumoReparacionSetNewIsumoReparacionToAttach = em.getReference(isumoReparacionSetNewIsumoReparacionToAttach.getClass(), isumoReparacionSetNewIsumoReparacionToAttach.getId());
                attachedIsumoReparacionSetNew.add(isumoReparacionSetNewIsumoReparacionToAttach);
            }
            isumoReparacionSetNew = attachedIsumoReparacionSetNew;
            insumo.setIsumoReparacionSet(isumoReparacionSetNew);
            insumo = em.merge(insumo);
            for (IsumoReparacion isumoReparacionSetNewIsumoReparacion : isumoReparacionSetNew) {
                if (!isumoReparacionSetOld.contains(isumoReparacionSetNewIsumoReparacion)) {
                    Insumo oldInsumoOfIsumoReparacionSetNewIsumoReparacion = isumoReparacionSetNewIsumoReparacion.getInsumo();
                    isumoReparacionSetNewIsumoReparacion.setInsumo(insumo);
                    isumoReparacionSetNewIsumoReparacion = em.merge(isumoReparacionSetNewIsumoReparacion);
                    if (oldInsumoOfIsumoReparacionSetNewIsumoReparacion != null && !oldInsumoOfIsumoReparacionSetNewIsumoReparacion.equals(insumo)) {
                        oldInsumoOfIsumoReparacionSetNewIsumoReparacion.getIsumoReparacionSet().remove(isumoReparacionSetNewIsumoReparacion);
                        oldInsumoOfIsumoReparacionSetNewIsumoReparacion = em.merge(oldInsumoOfIsumoReparacionSetNewIsumoReparacion);
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Set<IsumoReparacion> isumoReparacionSetOrphanCheck = insumo.getIsumoReparacionSet();
            for (IsumoReparacion isumoReparacionSetOrphanCheckIsumoReparacion : isumoReparacionSetOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Insumo (" + insumo + ") cannot be destroyed since the IsumoReparacion " + isumoReparacionSetOrphanCheckIsumoReparacion + " in its isumoReparacionSet field has a non-nullable insumo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
