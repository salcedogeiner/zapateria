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
import org.zapateria.entidad.Rol;
import org.zapateria.entidad.RolUsuario;
import org.zapateria.entidad.Usuario;

/**
 *
 * @author jose_
 */
public class RolUsuarioJpaController implements Serializable {

    public RolUsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RolUsuario rolUsuario) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol rolBean = rolUsuario.getRolBean();
            if (rolBean != null) {
                rolBean = em.getReference(rolBean.getClass(), rolBean.getId());
                rolUsuario.setRolBean(rolBean);
            }
            Usuario usuarioBean = rolUsuario.getUsuarioBean();
            if (usuarioBean != null) {
                usuarioBean = em.getReference(usuarioBean.getClass(), usuarioBean.getId());
                rolUsuario.setUsuarioBean(usuarioBean);
            }
            em.persist(rolUsuario);
            if (rolBean != null) {
                rolBean.getRolUsuarios().add(rolUsuario);
                rolBean = em.merge(rolBean);
            }
            if (usuarioBean != null) {
                usuarioBean.getRolUsuarios().add(rolUsuario);
                usuarioBean = em.merge(usuarioBean);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRolUsuario(rolUsuario.getId()) != null) {
                throw new PreexistingEntityException("RolUsuario " + rolUsuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RolUsuario rolUsuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RolUsuario persistentRolUsuario = em.find(RolUsuario.class, rolUsuario.getId());
            Rol rolBeanOld = persistentRolUsuario.getRolBean();
            Rol rolBeanNew = rolUsuario.getRolBean();
            Usuario usuarioBeanOld = persistentRolUsuario.getUsuarioBean();
            Usuario usuarioBeanNew = rolUsuario.getUsuarioBean();
            if (rolBeanNew != null) {
                rolBeanNew = em.getReference(rolBeanNew.getClass(), rolBeanNew.getId());
                rolUsuario.setRolBean(rolBeanNew);
            }
            if (usuarioBeanNew != null) {
                usuarioBeanNew = em.getReference(usuarioBeanNew.getClass(), usuarioBeanNew.getId());
                rolUsuario.setUsuarioBean(usuarioBeanNew);
            }
            rolUsuario = em.merge(rolUsuario);
            if (rolBeanOld != null && !rolBeanOld.equals(rolBeanNew)) {
                rolBeanOld.getRolUsuarios().remove(rolUsuario);
                rolBeanOld = em.merge(rolBeanOld);
            }
            if (rolBeanNew != null && !rolBeanNew.equals(rolBeanOld)) {
                rolBeanNew.getRolUsuarios().add(rolUsuario);
                rolBeanNew = em.merge(rolBeanNew);
            }
            if (usuarioBeanOld != null && !usuarioBeanOld.equals(usuarioBeanNew)) {
                usuarioBeanOld.getRolUsuarios().remove(rolUsuario);
                usuarioBeanOld = em.merge(usuarioBeanOld);
            }
            if (usuarioBeanNew != null && !usuarioBeanNew.equals(usuarioBeanOld)) {
                usuarioBeanNew.getRolUsuarios().add(rolUsuario);
                usuarioBeanNew = em.merge(usuarioBeanNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rolUsuario.getId();
                if (findRolUsuario(id) == null) {
                    throw new NonexistentEntityException("The rolUsuario with id " + id + " no longer exists.");
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
            RolUsuario rolUsuario;
            try {
                rolUsuario = em.getReference(RolUsuario.class, id);
                rolUsuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rolUsuario with id " + id + " no longer exists.", enfe);
            }
            Rol rolBean = rolUsuario.getRolBean();
            if (rolBean != null) {
                rolBean.getRolUsuarios().remove(rolUsuario);
                rolBean = em.merge(rolBean);
            }
            Usuario usuarioBean = rolUsuario.getUsuarioBean();
            if (usuarioBean != null) {
                usuarioBean.getRolUsuarios().remove(rolUsuario);
                usuarioBean = em.merge(usuarioBean);
            }
            em.remove(rolUsuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RolUsuario> findRolUsuarioEntities() {
        return findRolUsuarioEntities(true, -1, -1);
    }

    public List<RolUsuario> findRolUsuarioEntities(int maxResults, int firstResult) {
        return findRolUsuarioEntities(false, maxResults, firstResult);
    }

    private List<RolUsuario> findRolUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RolUsuario.class));
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

    public RolUsuario findRolUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RolUsuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RolUsuario> rt = cq.from(RolUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
