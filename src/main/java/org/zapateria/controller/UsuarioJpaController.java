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
import org.zapateria.logica.RolUsuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.logica.Usuario;

/**
 *
 * @author jose_
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getRolUsuarios() == null) {
            usuario.setRolUsuarios(new ArrayList<RolUsuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
           /* Persona personaBean = usuario.getPersonaBean();
            if (personaBean != null) {
                personaBean = em.getReference(personaBean.getClass(), personaBean.getId());
                usuario.setPersonaBean(personaBean);
            }*/
            List<RolUsuario> attachedRolUsuarios = new ArrayList<RolUsuario>();
            for (RolUsuario rolUsuariosRolUsuarioToAttach : usuario.getRolUsuarios()) {
                rolUsuariosRolUsuarioToAttach = em.getReference(rolUsuariosRolUsuarioToAttach.getClass(), rolUsuariosRolUsuarioToAttach.getId());
                attachedRolUsuarios.add(rolUsuariosRolUsuarioToAttach);
            }
            usuario.setRolUsuarios(attachedRolUsuarios);
            em.persist(usuario);
           /* if (personaBean != null) {
                personaBean.getUsuarios().add(usuario);
                personaBean = em.merge(personaBean);
            }
            for (RolUsuario rolUsuariosRolUsuario : usuario.getRolUsuarios()) {
                Usuario oldUsuarioBeanOfRolUsuariosRolUsuario = rolUsuariosRolUsuario.getUsuarioBean();
                rolUsuariosRolUsuario.setUsuarioBean(usuario);
                rolUsuariosRolUsuario = em.merge(rolUsuariosRolUsuario);
                if (oldUsuarioBeanOfRolUsuariosRolUsuario != null) {
                    oldUsuarioBeanOfRolUsuariosRolUsuario.getRolUsuarios().remove(rolUsuariosRolUsuario);
                    oldUsuarioBeanOfRolUsuariosRolUsuario = em.merge(oldUsuarioBeanOfRolUsuariosRolUsuario);
                }
            }*/
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getId()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

 /*   public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Persona personaBeanOld = persistentUsuario.getPersonaBean();
            Persona personaBeanNew = usuario.getPersonaBean();
            List<RolUsuario> rolUsuariosOld = persistentUsuario.getRolUsuarios();
            List<RolUsuario> rolUsuariosNew = usuario.getRolUsuarios();
            if (personaBeanNew != null) {
                personaBeanNew = em.getReference(personaBeanNew.getClass(), personaBeanNew.getId());
                usuario.setPersonaBean(personaBeanNew);
            }
            List<RolUsuario> attachedRolUsuariosNew = new ArrayList<RolUsuario>();
            for (RolUsuario rolUsuariosNewRolUsuarioToAttach : rolUsuariosNew) {
                rolUsuariosNewRolUsuarioToAttach = em.getReference(rolUsuariosNewRolUsuarioToAttach.getClass(), rolUsuariosNewRolUsuarioToAttach.getId());
                attachedRolUsuariosNew.add(rolUsuariosNewRolUsuarioToAttach);
            }
            rolUsuariosNew = attachedRolUsuariosNew;
            usuario.setRolUsuarios(rolUsuariosNew);
            usuario = em.merge(usuario);
            if (personaBeanOld != null && !personaBeanOld.equals(personaBeanNew)) {
                personaBeanOld.getUsuarios().remove(usuario);
                personaBeanOld = em.merge(personaBeanOld);
            }
            if (personaBeanNew != null && !personaBeanNew.equals(personaBeanOld)) {
                personaBeanNew.getUsuarios().add(usuario);
                personaBeanNew = em.merge(personaBeanNew);
            }
            for (RolUsuario rolUsuariosOldRolUsuario : rolUsuariosOld) {
                if (!rolUsuariosNew.contains(rolUsuariosOldRolUsuario)) {
                    rolUsuariosOldRolUsuario.setUsuarioBean(null);
                    rolUsuariosOldRolUsuario = em.merge(rolUsuariosOldRolUsuario);
                }
            }
           // for (RolUsuario rolUsuariosNewRolUsuario : rolUsuariosNew) {
                //if (!rolUsuariosOld.contains(rolUsuariosNewRolUsuario)) {
                   // Usuario oldUsuarioBeanOfRolUsuariosNewRolUsuario = rolUsuariosNewRolUsuario.getUsuarioBean();
                   // rolUsuariosNewRolUsuario.setUsuarioBean(usuario);
                   // rolUsuariosNewRolUsuario = em.merge(rolUsuariosNewRolUsuario);
                   // if (oldUsuarioBeanOfRolUsuariosNewRolUsuario != null && !oldUsuarioBeanOfRolUsuariosNewRolUsuario.equals(usuario)) {
                   //     oldUsuarioBeanOfRolUsuariosNewRolUsuario.getRolUsuarios().remove(rolUsuariosNewRolUsuario);
                   //     oldUsuarioBeanOfRolUsuariosNewRolUsuario = em.merge(oldUsuarioBeanOfRolUsuariosNewRolUsuario);
                //    }
            //    }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }*/

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
           // Persona personaBean = usuario.getPersonaBean();
           // if (personaBean != null) {
           //     personaBean.getUsuarios().remove(usuario);
            //    personaBean = em.merge(personaBean);
           // }
            List<RolUsuario> rolUsuarios = usuario.getRolUsuarios();
            for (RolUsuario rolUsuariosRolUsuario : rolUsuarios) {
               // rolUsuariosRolUsuario.setUsuarioBean(null);
                rolUsuariosRolUsuario = em.merge(rolUsuariosRolUsuario);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /**
     * Consulta el usuario por medio de nombre_usuario y clave
     * @param usuario
     * @return usuario
     */
    public Usuario consultarUsuario(Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            Query query;
            query = em.createQuery
                ("SELECT usuario FROM Usuario usuario WHERE usuario.nombreUsuario = :nombre and usuario.clave = :clave");
            query.setParameter("nombre", usuario.getNombreUsuario());
            query.setParameter("clave", usuario.getClave());
            
            usuario = (Usuario) query.getSingleResult();
            
            return usuario;
        } finally {
            em.close();
        }
    }
    
}
