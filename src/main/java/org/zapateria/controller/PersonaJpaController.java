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
import org.zapateria.logica.TipoIdentificacion;
import org.zapateria.logica.Reparacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.logica.Persona;
import org.zapateria.logica.Usuario;

/**
 *
 * @author jose_
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws PreexistingEntityException, Exception {
        if (persona.getReparacions1() == null) {
            persona.setReparacions1(new ArrayList<Reparacion>());
        }
        if (persona.getReparacions2() == null) {
            persona.setReparacions2(new ArrayList<Reparacion>());
        }
        if (persona.getUsuarios() == null) {
            persona.setUsuarios(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoIdentificacion tipoIdentificacionBean = persona.getTipoIdentificacionBean();
            if (tipoIdentificacionBean != null) {
                tipoIdentificacionBean = em.getReference(tipoIdentificacionBean.getClass(), tipoIdentificacionBean.getId());
                persona.setTipoIdentificacionBean(tipoIdentificacionBean);
            }
            List<Reparacion> attachedReparacions1 = new ArrayList<Reparacion>();
            for (Reparacion reparacions1ReparacionToAttach : persona.getReparacions1()) {
                reparacions1ReparacionToAttach = em.getReference(reparacions1ReparacionToAttach.getClass(), reparacions1ReparacionToAttach.getId());
                attachedReparacions1.add(reparacions1ReparacionToAttach);
            }
            persona.setReparacions1(attachedReparacions1);
            List<Reparacion> attachedReparacions2 = new ArrayList<Reparacion>();
            for (Reparacion reparacions2ReparacionToAttach : persona.getReparacions2()) {
                reparacions2ReparacionToAttach = em.getReference(reparacions2ReparacionToAttach.getClass(), reparacions2ReparacionToAttach.getId());
                attachedReparacions2.add(reparacions2ReparacionToAttach);
            }
            persona.setReparacions2(attachedReparacions2);
            List<Usuario> attachedUsuarios = new ArrayList<Usuario>();
            for (Usuario usuariosUsuarioToAttach : persona.getUsuarios()) {
                usuariosUsuarioToAttach = em.getReference(usuariosUsuarioToAttach.getClass(), usuariosUsuarioToAttach.getId());
                attachedUsuarios.add(usuariosUsuarioToAttach);
            }
            persona.setUsuarios(attachedUsuarios);
            em.persist(persona);
            if (tipoIdentificacionBean != null) {
                tipoIdentificacionBean.getPersonas().add(persona);
                tipoIdentificacionBean = em.merge(tipoIdentificacionBean);
            }
            for (Reparacion reparacions1Reparacion : persona.getReparacions1()) {
                Persona oldPersona1OfReparacions1Reparacion = reparacions1Reparacion.getPersona1();
                reparacions1Reparacion.setPersona1(persona);
                reparacions1Reparacion = em.merge(reparacions1Reparacion);
                if (oldPersona1OfReparacions1Reparacion != null) {
                    oldPersona1OfReparacions1Reparacion.getReparacions1().remove(reparacions1Reparacion);
                    oldPersona1OfReparacions1Reparacion = em.merge(oldPersona1OfReparacions1Reparacion);
                }
            }
            for (Reparacion reparacions2Reparacion : persona.getReparacions2()) {
                Persona oldPersona2OfReparacions2Reparacion = reparacions2Reparacion.getPersona2();
                reparacions2Reparacion.setPersona2(persona);
                reparacions2Reparacion = em.merge(reparacions2Reparacion);
                if (oldPersona2OfReparacions2Reparacion != null) {
                    oldPersona2OfReparacions2Reparacion.getReparacions2().remove(reparacions2Reparacion);
                    oldPersona2OfReparacions2Reparacion = em.merge(oldPersona2OfReparacions2Reparacion);
                }
            }
           /* for (Usuario usuariosUsuario : persona.getUsuarios()) {
                Persona oldPersonaBeanOfUsuariosUsuario = usuariosUsuario.getPersonaBean();
                usuariosUsuario.setPersonaBean(persona);
                usuariosUsuario = em.merge(usuariosUsuario);
                if (oldPersonaBeanOfUsuariosUsuario != null) {
                    oldPersonaBeanOfUsuariosUsuario.getUsuarios().remove(usuariosUsuario);
                    oldPersonaBeanOfUsuariosUsuario = em.merge(oldPersonaBeanOfUsuariosUsuario);
                }
            }*/
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersona(persona.getId()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getId());
            TipoIdentificacion tipoIdentificacionBeanOld = persistentPersona.getTipoIdentificacionBean();
            TipoIdentificacion tipoIdentificacionBeanNew = persona.getTipoIdentificacionBean();
            List<Reparacion> reparacions1Old = persistentPersona.getReparacions1();
            List<Reparacion> reparacions1New = persona.getReparacions1();
            List<Reparacion> reparacions2Old = persistentPersona.getReparacions2();
            List<Reparacion> reparacions2New = persona.getReparacions2();
            List<Usuario> usuariosOld = persistentPersona.getUsuarios();
            List<Usuario> usuariosNew = persona.getUsuarios();
            if (tipoIdentificacionBeanNew != null) {
                tipoIdentificacionBeanNew = em.getReference(tipoIdentificacionBeanNew.getClass(), tipoIdentificacionBeanNew.getId());
                persona.setTipoIdentificacionBean(tipoIdentificacionBeanNew);
            }
            List<Reparacion> attachedReparacions1New = new ArrayList<Reparacion>();
            for (Reparacion reparacions1NewReparacionToAttach : reparacions1New) {
                reparacions1NewReparacionToAttach = em.getReference(reparacions1NewReparacionToAttach.getClass(), reparacions1NewReparacionToAttach.getId());
                attachedReparacions1New.add(reparacions1NewReparacionToAttach);
            }
            reparacions1New = attachedReparacions1New;
            persona.setReparacions1(reparacions1New);
            List<Reparacion> attachedReparacions2New = new ArrayList<Reparacion>();
            for (Reparacion reparacions2NewReparacionToAttach : reparacions2New) {
                reparacions2NewReparacionToAttach = em.getReference(reparacions2NewReparacionToAttach.getClass(), reparacions2NewReparacionToAttach.getId());
                attachedReparacions2New.add(reparacions2NewReparacionToAttach);
            }
            reparacions2New = attachedReparacions2New;
            persona.setReparacions2(reparacions2New);
            List<Usuario> attachedUsuariosNew = new ArrayList<Usuario>();
            for (Usuario usuariosNewUsuarioToAttach : usuariosNew) {
                usuariosNewUsuarioToAttach = em.getReference(usuariosNewUsuarioToAttach.getClass(), usuariosNewUsuarioToAttach.getId());
                attachedUsuariosNew.add(usuariosNewUsuarioToAttach);
            }
            usuariosNew = attachedUsuariosNew;
            persona.setUsuarios(usuariosNew);
            persona = em.merge(persona);
            if (tipoIdentificacionBeanOld != null && !tipoIdentificacionBeanOld.equals(tipoIdentificacionBeanNew)) {
                tipoIdentificacionBeanOld.getPersonas().remove(persona);
                tipoIdentificacionBeanOld = em.merge(tipoIdentificacionBeanOld);
            }
            if (tipoIdentificacionBeanNew != null && !tipoIdentificacionBeanNew.equals(tipoIdentificacionBeanOld)) {
                tipoIdentificacionBeanNew.getPersonas().add(persona);
                tipoIdentificacionBeanNew = em.merge(tipoIdentificacionBeanNew);
            }
            for (Reparacion reparacions1OldReparacion : reparacions1Old) {
                if (!reparacions1New.contains(reparacions1OldReparacion)) {
                    reparacions1OldReparacion.setPersona1(null);
                    reparacions1OldReparacion = em.merge(reparacions1OldReparacion);
                }
            }
            for (Reparacion reparacions1NewReparacion : reparacions1New) {
                if (!reparacions1Old.contains(reparacions1NewReparacion)) {
                    Persona oldPersona1OfReparacions1NewReparacion = reparacions1NewReparacion.getPersona1();
                    reparacions1NewReparacion.setPersona1(persona);
                    reparacions1NewReparacion = em.merge(reparacions1NewReparacion);
                    if (oldPersona1OfReparacions1NewReparacion != null && !oldPersona1OfReparacions1NewReparacion.equals(persona)) {
                        oldPersona1OfReparacions1NewReparacion.getReparacions1().remove(reparacions1NewReparacion);
                        oldPersona1OfReparacions1NewReparacion = em.merge(oldPersona1OfReparacions1NewReparacion);
                    }
                }
            }
            for (Reparacion reparacions2OldReparacion : reparacions2Old) {
                if (!reparacions2New.contains(reparacions2OldReparacion)) {
                    reparacions2OldReparacion.setPersona2(null);
                    reparacions2OldReparacion = em.merge(reparacions2OldReparacion);
                }
            }
            for (Reparacion reparacions2NewReparacion : reparacions2New) {
                if (!reparacions2Old.contains(reparacions2NewReparacion)) {
                    Persona oldPersona2OfReparacions2NewReparacion = reparacions2NewReparacion.getPersona2();
                    reparacions2NewReparacion.setPersona2(persona);
                    reparacions2NewReparacion = em.merge(reparacions2NewReparacion);
                    if (oldPersona2OfReparacions2NewReparacion != null && !oldPersona2OfReparacions2NewReparacion.equals(persona)) {
                        oldPersona2OfReparacions2NewReparacion.getReparacions2().remove(reparacions2NewReparacion);
                        oldPersona2OfReparacions2NewReparacion = em.merge(oldPersona2OfReparacions2NewReparacion);
                    }
                }
            }
            for (Usuario usuariosOldUsuario : usuariosOld) {
                if (!usuariosNew.contains(usuariosOldUsuario)) {
                    usuariosOldUsuario.setPersonaBean(null);
                    usuariosOldUsuario = em.merge(usuariosOldUsuario);
                }
            }
           /* for (Usuario usuariosNewUsuario : usuariosNew) {
                if (!usuariosOld.contains(usuariosNewUsuario)) {
                    Persona oldPersonaBeanOfUsuariosNewUsuario = usuariosNewUsuario.getPersonaBean();
                    usuariosNewUsuario.setPersonaBean(persona);
                    usuariosNewUsuario = em.merge(usuariosNewUsuario);
                    if (oldPersonaBeanOfUsuariosNewUsuario != null && !oldPersonaBeanOfUsuariosNewUsuario.equals(persona)) {
                        oldPersonaBeanOfUsuariosNewUsuario.getUsuarios().remove(usuariosNewUsuario);
                        oldPersonaBeanOfUsuariosNewUsuario = em.merge(oldPersonaBeanOfUsuariosNewUsuario);
                    }
                }
            }*/
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = persona.getId();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
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
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            TipoIdentificacion tipoIdentificacionBean = persona.getTipoIdentificacionBean();
            if (tipoIdentificacionBean != null) {
                tipoIdentificacionBean.getPersonas().remove(persona);
                tipoIdentificacionBean = em.merge(tipoIdentificacionBean);
            }
            List<Reparacion> reparacions1 = persona.getReparacions1();
            for (Reparacion reparacions1Reparacion : reparacions1) {
                reparacions1Reparacion.setPersona1(null);
                reparacions1Reparacion = em.merge(reparacions1Reparacion);
            }
            List<Reparacion> reparacions2 = persona.getReparacions2();
            for (Reparacion reparacions2Reparacion : reparacions2) {
                reparacions2Reparacion.setPersona2(null);
                reparacions2Reparacion = em.merge(reparacions2Reparacion);
            }
            List<Usuario> usuarios = persona.getUsuarios();
            for (Usuario usuariosUsuario : usuarios) {
                usuariosUsuario.setPersonaBean(null);
                usuariosUsuario = em.merge(usuariosUsuario);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
