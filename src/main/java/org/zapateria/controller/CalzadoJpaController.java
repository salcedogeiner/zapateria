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
import org.zapateria.entidad.Reparacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.entidad.Calzado;

/**
 *
 * @author jose_
 */
public class CalzadoJpaController implements Serializable {

    public CalzadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Calzado calzado) throws PreexistingEntityException, Exception {
        if (calzado.getReparacions() == null) {
            calzado.setReparacions(new ArrayList<Reparacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reparacion> attachedReparacions = new ArrayList<Reparacion>();
            for (Reparacion reparacionsReparacionToAttach : calzado.getReparacions()) {
                reparacionsReparacionToAttach = em.getReference(reparacionsReparacionToAttach.getClass(), reparacionsReparacionToAttach.getId());
                attachedReparacions.add(reparacionsReparacionToAttach);
            }
            calzado.setReparacions(attachedReparacions);
            em.persist(calzado);
            for (Reparacion reparacionsReparacion : calzado.getReparacions()) {
                Calzado oldCalzadoBeanOfReparacionsReparacion = reparacionsReparacion.getCalzadoBean();
                reparacionsReparacion.setCalzadoBean(calzado);
                reparacionsReparacion = em.merge(reparacionsReparacion);
                if (oldCalzadoBeanOfReparacionsReparacion != null) {
                    oldCalzadoBeanOfReparacionsReparacion.getReparacions().remove(reparacionsReparacion);
                    oldCalzadoBeanOfReparacionsReparacion = em.merge(oldCalzadoBeanOfReparacionsReparacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCalzado(calzado.getId()) != null) {
                throw new PreexistingEntityException("Calzado " + calzado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Calzado calzado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Calzado persistentCalzado = em.find(Calzado.class, calzado.getId());
            List<Reparacion> reparacionsOld = persistentCalzado.getReparacions();
            List<Reparacion> reparacionsNew = calzado.getReparacions();
            List<Reparacion> attachedReparacionsNew = new ArrayList<Reparacion>();
            for (Reparacion reparacionsNewReparacionToAttach : reparacionsNew) {
                reparacionsNewReparacionToAttach = em.getReference(reparacionsNewReparacionToAttach.getClass(), reparacionsNewReparacionToAttach.getId());
                attachedReparacionsNew.add(reparacionsNewReparacionToAttach);
            }
            reparacionsNew = attachedReparacionsNew;
            calzado.setReparacions(reparacionsNew);
            calzado = em.merge(calzado);
            for (Reparacion reparacionsOldReparacion : reparacionsOld) {
                if (!reparacionsNew.contains(reparacionsOldReparacion)) {
                    reparacionsOldReparacion.setCalzadoBean(null);
                    reparacionsOldReparacion = em.merge(reparacionsOldReparacion);
                }
            }
            for (Reparacion reparacionsNewReparacion : reparacionsNew) {
                if (!reparacionsOld.contains(reparacionsNewReparacion)) {
                    Calzado oldCalzadoBeanOfReparacionsNewReparacion = reparacionsNewReparacion.getCalzadoBean();
                    reparacionsNewReparacion.setCalzadoBean(calzado);
                    reparacionsNewReparacion = em.merge(reparacionsNewReparacion);
                    if (oldCalzadoBeanOfReparacionsNewReparacion != null && !oldCalzadoBeanOfReparacionsNewReparacion.equals(calzado)) {
                        oldCalzadoBeanOfReparacionsNewReparacion.getReparacions().remove(reparacionsNewReparacion);
                        oldCalzadoBeanOfReparacionsNewReparacion = em.merge(oldCalzadoBeanOfReparacionsNewReparacion);
                    }
                }
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
            List<Reparacion> reparacions = calzado.getReparacions();
            for (Reparacion reparacionsReparacion : reparacions) {
                reparacionsReparacion.setCalzadoBean(null);
                reparacionsReparacion = em.merge(reparacionsReparacion);
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
