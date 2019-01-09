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
import org.zapateria.logica.Usuario;
import org.zapateria.logica.Reparacion;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.IllegalOrphanException;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.logica.Persona;

/**
 *
 * @author g.salcedo
 */
public class PersonaMapper implements Serializable {

    public PersonaMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) {
        if (persona.getReparacionSet() == null) {
            persona.setReparacionSet(new HashSet<Reparacion>());
        }
        if (persona.getReparacionSet1() == null) {
            persona.setReparacionSet1(new HashSet<Reparacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoIdentificacion tipoIdentificacion = persona.getTipoIdentificacion();
            if (tipoIdentificacion != null) {
                tipoIdentificacion = em.getReference(tipoIdentificacion.getClass(), tipoIdentificacion.getId());
                persona.setTipoIdentificacion(tipoIdentificacion);
            }
            Usuario usuario = persona.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                persona.setUsuario(usuario);
            }
            Set<Reparacion> attachedReparacionSet = new HashSet<Reparacion>();
            for (Reparacion reparacionSetReparacionToAttach : persona.getReparacionSet()) {
                reparacionSetReparacionToAttach = em.getReference(reparacionSetReparacionToAttach.getClass(), reparacionSetReparacionToAttach.getId());
                attachedReparacionSet.add(reparacionSetReparacionToAttach);
            }
            persona.setReparacionSet(attachedReparacionSet);
            Set<Reparacion> attachedReparacionSet1 = new HashSet<Reparacion>();
            for (Reparacion reparacionSet1ReparacionToAttach : persona.getReparacionSet1()) {
                reparacionSet1ReparacionToAttach = em.getReference(reparacionSet1ReparacionToAttach.getClass(), reparacionSet1ReparacionToAttach.getId());
                attachedReparacionSet1.add(reparacionSet1ReparacionToAttach);
            }
            persona.setReparacionSet1(attachedReparacionSet1);
            em.persist(persona);
            if (tipoIdentificacion != null) {
                tipoIdentificacion.getPersonaSet().add(persona);
                tipoIdentificacion = em.merge(tipoIdentificacion);
            }
            if (usuario != null) {
                Persona oldPersonaOfUsuario = usuario.getPersona();
                if (oldPersonaOfUsuario != null) {
                    oldPersonaOfUsuario.setUsuario(null);
                    oldPersonaOfUsuario = em.merge(oldPersonaOfUsuario);
                }
                usuario.setPersona(persona);
                usuario = em.merge(usuario);
            }
            for (Reparacion reparacionSetReparacion : persona.getReparacionSet()) {
                Persona oldClienteOfReparacionSetReparacion = reparacionSetReparacion.getCliente();
                reparacionSetReparacion.setCliente(persona);
                reparacionSetReparacion = em.merge(reparacionSetReparacion);
                if (oldClienteOfReparacionSetReparacion != null) {
                    oldClienteOfReparacionSetReparacion.getReparacionSet().remove(reparacionSetReparacion);
                    oldClienteOfReparacionSetReparacion = em.merge(oldClienteOfReparacionSetReparacion);
                }
            }
            for (Reparacion reparacionSet1Reparacion : persona.getReparacionSet1()) {
                Persona oldZapateroEncargadoOfReparacionSet1Reparacion = reparacionSet1Reparacion.getZapateroEncargado();
                reparacionSet1Reparacion.setZapateroEncargado(persona);
                reparacionSet1Reparacion = em.merge(reparacionSet1Reparacion);
                if (oldZapateroEncargadoOfReparacionSet1Reparacion != null) {
                    oldZapateroEncargadoOfReparacionSet1Reparacion.getReparacionSet1().remove(reparacionSet1Reparacion);
                    oldZapateroEncargadoOfReparacionSet1Reparacion = em.merge(oldZapateroEncargadoOfReparacionSet1Reparacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getId());
            TipoIdentificacion tipoIdentificacionOld = persistentPersona.getTipoIdentificacion();
            TipoIdentificacion tipoIdentificacionNew = persona.getTipoIdentificacion();
            Usuario usuarioOld = persistentPersona.getUsuario();
            Usuario usuarioNew = persona.getUsuario();
            Set<Reparacion> reparacionSetOld = persistentPersona.getReparacionSet();
            Set<Reparacion> reparacionSetNew = persona.getReparacionSet();
            Set<Reparacion> reparacionSet1Old = persistentPersona.getReparacionSet1();
            Set<Reparacion> reparacionSet1New = persona.getReparacionSet1();
            List<String> illegalOrphanMessages = null;
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Usuario " + usuarioOld + " since its persona field is not nullable.");
            }
            for (Reparacion reparacionSetOldReparacion : reparacionSetOld) {
                if (!reparacionSetNew.contains(reparacionSetOldReparacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reparacion " + reparacionSetOldReparacion + " since its cliente field is not nullable.");
                }
            }
            for (Reparacion reparacionSet1OldReparacion : reparacionSet1Old) {
                if (!reparacionSet1New.contains(reparacionSet1OldReparacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reparacion " + reparacionSet1OldReparacion + " since its zapateroEncargado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoIdentificacionNew != null) {
                tipoIdentificacionNew = em.getReference(tipoIdentificacionNew.getClass(), tipoIdentificacionNew.getId());
                persona.setTipoIdentificacion(tipoIdentificacionNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                persona.setUsuario(usuarioNew);
            }
            Set<Reparacion> attachedReparacionSetNew = new HashSet<Reparacion>();
            for (Reparacion reparacionSetNewReparacionToAttach : reparacionSetNew) {
                reparacionSetNewReparacionToAttach = em.getReference(reparacionSetNewReparacionToAttach.getClass(), reparacionSetNewReparacionToAttach.getId());
                attachedReparacionSetNew.add(reparacionSetNewReparacionToAttach);
            }
            reparacionSetNew = attachedReparacionSetNew;
            persona.setReparacionSet(reparacionSetNew);
            Set<Reparacion> attachedReparacionSet1New = new HashSet<Reparacion>();
            for (Reparacion reparacionSet1NewReparacionToAttach : reparacionSet1New) {
                reparacionSet1NewReparacionToAttach = em.getReference(reparacionSet1NewReparacionToAttach.getClass(), reparacionSet1NewReparacionToAttach.getId());
                attachedReparacionSet1New.add(reparacionSet1NewReparacionToAttach);
            }
            reparacionSet1New = attachedReparacionSet1New;
            persona.setReparacionSet1(reparacionSet1New);
            persona = em.merge(persona);
            if (tipoIdentificacionOld != null && !tipoIdentificacionOld.equals(tipoIdentificacionNew)) {
                tipoIdentificacionOld.getPersonaSet().remove(persona);
                tipoIdentificacionOld = em.merge(tipoIdentificacionOld);
            }
            if (tipoIdentificacionNew != null && !tipoIdentificacionNew.equals(tipoIdentificacionOld)) {
                tipoIdentificacionNew.getPersonaSet().add(persona);
                tipoIdentificacionNew = em.merge(tipoIdentificacionNew);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                Persona oldPersonaOfUsuario = usuarioNew.getPersona();
                if (oldPersonaOfUsuario != null) {
                    oldPersonaOfUsuario.setUsuario(null);
                    oldPersonaOfUsuario = em.merge(oldPersonaOfUsuario);
                }
                usuarioNew.setPersona(persona);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Reparacion reparacionSetNewReparacion : reparacionSetNew) {
                if (!reparacionSetOld.contains(reparacionSetNewReparacion)) {
                    Persona oldClienteOfReparacionSetNewReparacion = reparacionSetNewReparacion.getCliente();
                    reparacionSetNewReparacion.setCliente(persona);
                    reparacionSetNewReparacion = em.merge(reparacionSetNewReparacion);
                    if (oldClienteOfReparacionSetNewReparacion != null && !oldClienteOfReparacionSetNewReparacion.equals(persona)) {
                        oldClienteOfReparacionSetNewReparacion.getReparacionSet().remove(reparacionSetNewReparacion);
                        oldClienteOfReparacionSetNewReparacion = em.merge(oldClienteOfReparacionSetNewReparacion);
                    }
                }
            }
            for (Reparacion reparacionSet1NewReparacion : reparacionSet1New) {
                if (!reparacionSet1Old.contains(reparacionSet1NewReparacion)) {
                    Persona oldZapateroEncargadoOfReparacionSet1NewReparacion = reparacionSet1NewReparacion.getZapateroEncargado();
                    reparacionSet1NewReparacion.setZapateroEncargado(persona);
                    reparacionSet1NewReparacion = em.merge(reparacionSet1NewReparacion);
                    if (oldZapateroEncargadoOfReparacionSet1NewReparacion != null && !oldZapateroEncargadoOfReparacionSet1NewReparacion.equals(persona)) {
                        oldZapateroEncargadoOfReparacionSet1NewReparacion.getReparacionSet1().remove(reparacionSet1NewReparacion);
                        oldZapateroEncargadoOfReparacionSet1NewReparacion = em.merge(oldZapateroEncargadoOfReparacionSet1NewReparacion);
                    }
                }
            }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Usuario usuarioOrphanCheck = persona.getUsuario();
            if (usuarioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Usuario " + usuarioOrphanCheck + " in its usuario field has a non-nullable persona field.");
            }
            Set<Reparacion> reparacionSetOrphanCheck = persona.getReparacionSet();
            for (Reparacion reparacionSetOrphanCheckReparacion : reparacionSetOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Reparacion " + reparacionSetOrphanCheckReparacion + " in its reparacionSet field has a non-nullable cliente field.");
            }
            Set<Reparacion> reparacionSet1OrphanCheck = persona.getReparacionSet1();
            for (Reparacion reparacionSet1OrphanCheckReparacion : reparacionSet1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Reparacion " + reparacionSet1OrphanCheckReparacion + " in its reparacionSet1 field has a non-nullable zapateroEncargado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoIdentificacion tipoIdentificacion = persona.getTipoIdentificacion();
            if (tipoIdentificacion != null) {
                tipoIdentificacion.getPersonaSet().remove(persona);
                tipoIdentificacion = em.merge(tipoIdentificacion);
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
