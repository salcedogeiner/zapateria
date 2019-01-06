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
import org.zapateria.logica.RolPrivilegio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.logica.Privilegio;

/**
 *
 * @author jose_
 */
public class PrivilegioJpaController implements Serializable {

    public PrivilegioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Privilegio privilegio) throws PreexistingEntityException, Exception {
        if (privilegio.getRolPrivilegios() == null) {
            privilegio.setRolPrivilegios(new ArrayList<RolPrivilegio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RolPrivilegio> attachedRolPrivilegios = new ArrayList<RolPrivilegio>();
            for (RolPrivilegio rolPrivilegiosRolPrivilegioToAttach : privilegio.getRolPrivilegios()) {
                rolPrivilegiosRolPrivilegioToAttach = em.getReference(rolPrivilegiosRolPrivilegioToAttach.getClass(), rolPrivilegiosRolPrivilegioToAttach.getId());
                attachedRolPrivilegios.add(rolPrivilegiosRolPrivilegioToAttach);
            }
            privilegio.setRolPrivilegios(attachedRolPrivilegios);
            em.persist(privilegio);
            for (RolPrivilegio rolPrivilegiosRolPrivilegio : privilegio.getRolPrivilegios()) {
                Privilegio oldPrivilegioBeanOfRolPrivilegiosRolPrivilegio = rolPrivilegiosRolPrivilegio.getPrivilegioBean();
                rolPrivilegiosRolPrivilegio.setPrivilegioBean(privilegio);
                rolPrivilegiosRolPrivilegio = em.merge(rolPrivilegiosRolPrivilegio);
                if (oldPrivilegioBeanOfRolPrivilegiosRolPrivilegio != null) {
                    oldPrivilegioBeanOfRolPrivilegiosRolPrivilegio.getRolPrivilegios().remove(rolPrivilegiosRolPrivilegio);
                    oldPrivilegioBeanOfRolPrivilegiosRolPrivilegio = em.merge(oldPrivilegioBeanOfRolPrivilegiosRolPrivilegio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPrivilegio(privilegio.getId()) != null) {
                throw new PreexistingEntityException("Privilegio " + privilegio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Privilegio privilegio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Privilegio persistentPrivilegio = em.find(Privilegio.class, privilegio.getId());
            List<RolPrivilegio> rolPrivilegiosOld = persistentPrivilegio.getRolPrivilegios();
            List<RolPrivilegio> rolPrivilegiosNew = privilegio.getRolPrivilegios();
            List<RolPrivilegio> attachedRolPrivilegiosNew = new ArrayList<RolPrivilegio>();
            for (RolPrivilegio rolPrivilegiosNewRolPrivilegioToAttach : rolPrivilegiosNew) {
                rolPrivilegiosNewRolPrivilegioToAttach = em.getReference(rolPrivilegiosNewRolPrivilegioToAttach.getClass(), rolPrivilegiosNewRolPrivilegioToAttach.getId());
                attachedRolPrivilegiosNew.add(rolPrivilegiosNewRolPrivilegioToAttach);
            }
            rolPrivilegiosNew = attachedRolPrivilegiosNew;
            privilegio.setRolPrivilegios(rolPrivilegiosNew);
            privilegio = em.merge(privilegio);
            for (RolPrivilegio rolPrivilegiosOldRolPrivilegio : rolPrivilegiosOld) {
                if (!rolPrivilegiosNew.contains(rolPrivilegiosOldRolPrivilegio)) {
                    rolPrivilegiosOldRolPrivilegio.setPrivilegioBean(null);
                    rolPrivilegiosOldRolPrivilegio = em.merge(rolPrivilegiosOldRolPrivilegio);
                }
            }
            for (RolPrivilegio rolPrivilegiosNewRolPrivilegio : rolPrivilegiosNew) {
                if (!rolPrivilegiosOld.contains(rolPrivilegiosNewRolPrivilegio)) {
                    Privilegio oldPrivilegioBeanOfRolPrivilegiosNewRolPrivilegio = rolPrivilegiosNewRolPrivilegio.getPrivilegioBean();
                    rolPrivilegiosNewRolPrivilegio.setPrivilegioBean(privilegio);
                    rolPrivilegiosNewRolPrivilegio = em.merge(rolPrivilegiosNewRolPrivilegio);
                    if (oldPrivilegioBeanOfRolPrivilegiosNewRolPrivilegio != null && !oldPrivilegioBeanOfRolPrivilegiosNewRolPrivilegio.equals(privilegio)) {
                        oldPrivilegioBeanOfRolPrivilegiosNewRolPrivilegio.getRolPrivilegios().remove(rolPrivilegiosNewRolPrivilegio);
                        oldPrivilegioBeanOfRolPrivilegiosNewRolPrivilegio = em.merge(oldPrivilegioBeanOfRolPrivilegiosNewRolPrivilegio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = privilegio.getId();
                if (findPrivilegio(id) == null) {
                    throw new NonexistentEntityException("The privilegio with id " + id + " no longer exists.");
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
            Privilegio privilegio;
            try {
                privilegio = em.getReference(Privilegio.class, id);
                privilegio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The privilegio with id " + id + " no longer exists.", enfe);
            }
            List<RolPrivilegio> rolPrivilegios = privilegio.getRolPrivilegios();
            for (RolPrivilegio rolPrivilegiosRolPrivilegio : rolPrivilegios) {
                rolPrivilegiosRolPrivilegio.setPrivilegioBean(null);
                rolPrivilegiosRolPrivilegio = em.merge(rolPrivilegiosRolPrivilegio);
            }
            em.remove(privilegio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Privilegio> findPrivilegioEntities() {
        return findPrivilegioEntities(true, -1, -1);
    }

    public List<Privilegio> findPrivilegioEntities(int maxResults, int firstResult) {
        return findPrivilegioEntities(false, maxResults, firstResult);
    }

    private List<Privilegio> findPrivilegioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Privilegio.class));
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

    public Privilegio findPrivilegio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Privilegio.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrivilegioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Privilegio> rt = cq.from(Privilegio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
