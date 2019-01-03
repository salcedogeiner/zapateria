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
import org.zapateria.entidad.Privilegio;
import org.zapateria.entidad.Rol;
import org.zapateria.entidad.RolPrivilegio;

/**
 *
 * @author jose_
 */
public class RolPrivilegioJpaController implements Serializable {

    public RolPrivilegioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RolPrivilegio rolPrivilegio) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Privilegio privilegioBean = rolPrivilegio.getPrivilegioBean();
            if (privilegioBean != null) {
                privilegioBean = em.getReference(privilegioBean.getClass(), privilegioBean.getId());
                rolPrivilegio.setPrivilegioBean(privilegioBean);
            }
            Rol rolBean = rolPrivilegio.getRolBean();
            if (rolBean != null) {
                rolBean = em.getReference(rolBean.getClass(), rolBean.getId());
                rolPrivilegio.setRolBean(rolBean);
            }
            em.persist(rolPrivilegio);
            if (privilegioBean != null) {
                privilegioBean.getRolPrivilegios().add(rolPrivilegio);
                privilegioBean = em.merge(privilegioBean);
            }
            if (rolBean != null) {
                rolBean.getRolPrivilegios().add(rolPrivilegio);
                rolBean = em.merge(rolBean);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRolPrivilegio(rolPrivilegio.getId()) != null) {
                throw new PreexistingEntityException("RolPrivilegio " + rolPrivilegio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RolPrivilegio rolPrivilegio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RolPrivilegio persistentRolPrivilegio = em.find(RolPrivilegio.class, rolPrivilegio.getId());
            Privilegio privilegioBeanOld = persistentRolPrivilegio.getPrivilegioBean();
            Privilegio privilegioBeanNew = rolPrivilegio.getPrivilegioBean();
            Rol rolBeanOld = persistentRolPrivilegio.getRolBean();
            Rol rolBeanNew = rolPrivilegio.getRolBean();
            if (privilegioBeanNew != null) {
                privilegioBeanNew = em.getReference(privilegioBeanNew.getClass(), privilegioBeanNew.getId());
                rolPrivilegio.setPrivilegioBean(privilegioBeanNew);
            }
            if (rolBeanNew != null) {
                rolBeanNew = em.getReference(rolBeanNew.getClass(), rolBeanNew.getId());
                rolPrivilegio.setRolBean(rolBeanNew);
            }
            rolPrivilegio = em.merge(rolPrivilegio);
            if (privilegioBeanOld != null && !privilegioBeanOld.equals(privilegioBeanNew)) {
                privilegioBeanOld.getRolPrivilegios().remove(rolPrivilegio);
                privilegioBeanOld = em.merge(privilegioBeanOld);
            }
            if (privilegioBeanNew != null && !privilegioBeanNew.equals(privilegioBeanOld)) {
                privilegioBeanNew.getRolPrivilegios().add(rolPrivilegio);
                privilegioBeanNew = em.merge(privilegioBeanNew);
            }
            if (rolBeanOld != null && !rolBeanOld.equals(rolBeanNew)) {
                rolBeanOld.getRolPrivilegios().remove(rolPrivilegio);
                rolBeanOld = em.merge(rolBeanOld);
            }
            if (rolBeanNew != null && !rolBeanNew.equals(rolBeanOld)) {
                rolBeanNew.getRolPrivilegios().add(rolPrivilegio);
                rolBeanNew = em.merge(rolBeanNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rolPrivilegio.getId();
                if (findRolPrivilegio(id) == null) {
                    throw new NonexistentEntityException("The rolPrivilegio with id " + id + " no longer exists.");
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
            RolPrivilegio rolPrivilegio;
            try {
                rolPrivilegio = em.getReference(RolPrivilegio.class, id);
                rolPrivilegio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rolPrivilegio with id " + id + " no longer exists.", enfe);
            }
            Privilegio privilegioBean = rolPrivilegio.getPrivilegioBean();
            if (privilegioBean != null) {
                privilegioBean.getRolPrivilegios().remove(rolPrivilegio);
                privilegioBean = em.merge(privilegioBean);
            }
            Rol rolBean = rolPrivilegio.getRolBean();
            if (rolBean != null) {
                rolBean.getRolPrivilegios().remove(rolPrivilegio);
                rolBean = em.merge(rolBean);
            }
            em.remove(rolPrivilegio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RolPrivilegio> findRolPrivilegioEntities() {
        return findRolPrivilegioEntities(true, -1, -1);
    }

    public List<RolPrivilegio> findRolPrivilegioEntities(int maxResults, int firstResult) {
        return findRolPrivilegioEntities(false, maxResults, firstResult);
    }

    private List<RolPrivilegio> findRolPrivilegioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RolPrivilegio.class));
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

    public RolPrivilegio findRolPrivilegio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RolPrivilegio.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolPrivilegioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RolPrivilegio> rt = cq.from(RolPrivilegio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
