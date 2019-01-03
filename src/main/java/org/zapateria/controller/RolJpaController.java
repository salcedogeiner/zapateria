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
import org.zapateria.entidad.RolPrivilegio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.entidad.Rol;
import org.zapateria.entidad.RolUsuario;

/**
 *
 * @author jose_
 */
public class RolJpaController implements Serializable {

    public RolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws PreexistingEntityException, Exception {
        if (rol.getRolPrivilegios() == null) {
            rol.setRolPrivilegios(new ArrayList<RolPrivilegio>());
        }
        if (rol.getRolUsuarios() == null) {
            rol.setRolUsuarios(new ArrayList<RolUsuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RolPrivilegio> attachedRolPrivilegios = new ArrayList<RolPrivilegio>();
            for (RolPrivilegio rolPrivilegiosRolPrivilegioToAttach : rol.getRolPrivilegios()) {
                rolPrivilegiosRolPrivilegioToAttach = em.getReference(rolPrivilegiosRolPrivilegioToAttach.getClass(), rolPrivilegiosRolPrivilegioToAttach.getId());
                attachedRolPrivilegios.add(rolPrivilegiosRolPrivilegioToAttach);
            }
            rol.setRolPrivilegios(attachedRolPrivilegios);
            List<RolUsuario> attachedRolUsuarios = new ArrayList<RolUsuario>();
            for (RolUsuario rolUsuariosRolUsuarioToAttach : rol.getRolUsuarios()) {
                rolUsuariosRolUsuarioToAttach = em.getReference(rolUsuariosRolUsuarioToAttach.getClass(), rolUsuariosRolUsuarioToAttach.getId());
                attachedRolUsuarios.add(rolUsuariosRolUsuarioToAttach);
            }
            rol.setRolUsuarios(attachedRolUsuarios);
            em.persist(rol);
            for (RolPrivilegio rolPrivilegiosRolPrivilegio : rol.getRolPrivilegios()) {
                Rol oldRolBeanOfRolPrivilegiosRolPrivilegio = rolPrivilegiosRolPrivilegio.getRolBean();
                rolPrivilegiosRolPrivilegio.setRolBean(rol);
                rolPrivilegiosRolPrivilegio = em.merge(rolPrivilegiosRolPrivilegio);
                if (oldRolBeanOfRolPrivilegiosRolPrivilegio != null) {
                    oldRolBeanOfRolPrivilegiosRolPrivilegio.getRolPrivilegios().remove(rolPrivilegiosRolPrivilegio);
                    oldRolBeanOfRolPrivilegiosRolPrivilegio = em.merge(oldRolBeanOfRolPrivilegiosRolPrivilegio);
                }
            }
            for (RolUsuario rolUsuariosRolUsuario : rol.getRolUsuarios()) {
                Rol oldRolBeanOfRolUsuariosRolUsuario = rolUsuariosRolUsuario.getRolBean();
                rolUsuariosRolUsuario.setRolBean(rol);
                rolUsuariosRolUsuario = em.merge(rolUsuariosRolUsuario);
                if (oldRolBeanOfRolUsuariosRolUsuario != null) {
                    oldRolBeanOfRolUsuariosRolUsuario.getRolUsuarios().remove(rolUsuariosRolUsuario);
                    oldRolBeanOfRolUsuariosRolUsuario = em.merge(oldRolBeanOfRolUsuariosRolUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRol(rol.getId()) != null) {
                throw new PreexistingEntityException("Rol " + rol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol persistentRol = em.find(Rol.class, rol.getId());
            List<RolPrivilegio> rolPrivilegiosOld = persistentRol.getRolPrivilegios();
            List<RolPrivilegio> rolPrivilegiosNew = rol.getRolPrivilegios();
            List<RolUsuario> rolUsuariosOld = persistentRol.getRolUsuarios();
            List<RolUsuario> rolUsuariosNew = rol.getRolUsuarios();
            List<RolPrivilegio> attachedRolPrivilegiosNew = new ArrayList<RolPrivilegio>();
            for (RolPrivilegio rolPrivilegiosNewRolPrivilegioToAttach : rolPrivilegiosNew) {
                rolPrivilegiosNewRolPrivilegioToAttach = em.getReference(rolPrivilegiosNewRolPrivilegioToAttach.getClass(), rolPrivilegiosNewRolPrivilegioToAttach.getId());
                attachedRolPrivilegiosNew.add(rolPrivilegiosNewRolPrivilegioToAttach);
            }
            rolPrivilegiosNew = attachedRolPrivilegiosNew;
            rol.setRolPrivilegios(rolPrivilegiosNew);
            List<RolUsuario> attachedRolUsuariosNew = new ArrayList<RolUsuario>();
            for (RolUsuario rolUsuariosNewRolUsuarioToAttach : rolUsuariosNew) {
                rolUsuariosNewRolUsuarioToAttach = em.getReference(rolUsuariosNewRolUsuarioToAttach.getClass(), rolUsuariosNewRolUsuarioToAttach.getId());
                attachedRolUsuariosNew.add(rolUsuariosNewRolUsuarioToAttach);
            }
            rolUsuariosNew = attachedRolUsuariosNew;
            rol.setRolUsuarios(rolUsuariosNew);
            rol = em.merge(rol);
            for (RolPrivilegio rolPrivilegiosOldRolPrivilegio : rolPrivilegiosOld) {
                if (!rolPrivilegiosNew.contains(rolPrivilegiosOldRolPrivilegio)) {
                    rolPrivilegiosOldRolPrivilegio.setRolBean(null);
                    rolPrivilegiosOldRolPrivilegio = em.merge(rolPrivilegiosOldRolPrivilegio);
                }
            }
            for (RolPrivilegio rolPrivilegiosNewRolPrivilegio : rolPrivilegiosNew) {
                if (!rolPrivilegiosOld.contains(rolPrivilegiosNewRolPrivilegio)) {
                    Rol oldRolBeanOfRolPrivilegiosNewRolPrivilegio = rolPrivilegiosNewRolPrivilegio.getRolBean();
                    rolPrivilegiosNewRolPrivilegio.setRolBean(rol);
                    rolPrivilegiosNewRolPrivilegio = em.merge(rolPrivilegiosNewRolPrivilegio);
                    if (oldRolBeanOfRolPrivilegiosNewRolPrivilegio != null && !oldRolBeanOfRolPrivilegiosNewRolPrivilegio.equals(rol)) {
                        oldRolBeanOfRolPrivilegiosNewRolPrivilegio.getRolPrivilegios().remove(rolPrivilegiosNewRolPrivilegio);
                        oldRolBeanOfRolPrivilegiosNewRolPrivilegio = em.merge(oldRolBeanOfRolPrivilegiosNewRolPrivilegio);
                    }
                }
            }
            for (RolUsuario rolUsuariosOldRolUsuario : rolUsuariosOld) {
                if (!rolUsuariosNew.contains(rolUsuariosOldRolUsuario)) {
                    rolUsuariosOldRolUsuario.setRolBean(null);
                    rolUsuariosOldRolUsuario = em.merge(rolUsuariosOldRolUsuario);
                }
            }
            for (RolUsuario rolUsuariosNewRolUsuario : rolUsuariosNew) {
                if (!rolUsuariosOld.contains(rolUsuariosNewRolUsuario)) {
                    Rol oldRolBeanOfRolUsuariosNewRolUsuario = rolUsuariosNewRolUsuario.getRolBean();
                    rolUsuariosNewRolUsuario.setRolBean(rol);
                    rolUsuariosNewRolUsuario = em.merge(rolUsuariosNewRolUsuario);
                    if (oldRolBeanOfRolUsuariosNewRolUsuario != null && !oldRolBeanOfRolUsuariosNewRolUsuario.equals(rol)) {
                        oldRolBeanOfRolUsuariosNewRolUsuario.getRolUsuarios().remove(rolUsuariosNewRolUsuario);
                        oldRolBeanOfRolUsuariosNewRolUsuario = em.merge(oldRolBeanOfRolUsuariosNewRolUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rol.getId();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            List<RolPrivilegio> rolPrivilegios = rol.getRolPrivilegios();
            for (RolPrivilegio rolPrivilegiosRolPrivilegio : rolPrivilegios) {
                rolPrivilegiosRolPrivilegio.setRolBean(null);
                rolPrivilegiosRolPrivilegio = em.merge(rolPrivilegiosRolPrivilegio);
            }
            List<RolUsuario> rolUsuarios = rol.getRolUsuarios();
            for (RolUsuario rolUsuariosRolUsuario : rolUsuarios) {
                rolUsuariosRolUsuario.setRolBean(null);
                rolUsuariosRolUsuario = em.merge(rolUsuariosRolUsuario);
            }
            em.remove(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
