/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.mapper;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.zapateria.logica.Calzado;
import org.zapateria.logica.EstadoReparacion;
import org.zapateria.logica.Persona;
import org.zapateria.logica.IsumoReparacion;
import java.util.HashSet;
import java.util.Set;
import org.zapateria.logica.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.mapper.exceptions.IllegalOrphanException;
import org.zapateria.mapper.exceptions.NonexistentEntityException;
import org.zapateria.logica.Reparacion;

/**
 *
 * @author g.salcedo
 */
public class ReparacionMapper implements Serializable {

    public ReparacionMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reparacion reparacion) throws RuntimeException {
        if (reparacion.getIsumoReparacionSet() == null) {
            reparacion.setIsumoReparacionSet(new HashSet<IsumoReparacion>());
        }
        if (reparacion.getPagoSet() == null) {
            reparacion.setPagoSet(new HashSet<Pago>());
        }
        List<String> illegalOrphanMessages = null;
        Calzado calzadoOrphanCheck = reparacion.getCalzado();
        if (calzadoOrphanCheck != null) {
            Reparacion oldReparacionOfCalzado = calzadoOrphanCheck.getReparacion();
            if (oldReparacionOfCalzado != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Calzado " + calzadoOrphanCheck + " already has an item of type Reparacion whose calzado column cannot be null. Please make another selection for the calzado field.");
            }
        }
        /*if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }*/
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Calzado calzado = reparacion.getCalzado();
            if (calzado != null) {
                calzado = em.getReference(calzado.getClass(), calzado.getId());
                reparacion.setCalzado(calzado);
            }
            EstadoReparacion estadoReparacion = reparacion.getEstadoReparacion();
            if (estadoReparacion != null) {
                estadoReparacion = em.getReference(estadoReparacion.getClass(), estadoReparacion.getId());
                reparacion.setEstadoReparacion(estadoReparacion);
            }
            Persona cliente = reparacion.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getId());
                reparacion.setCliente(cliente);
            }
            Persona zapateroEncargado = reparacion.getZapateroEncargado();
            if (zapateroEncargado != null) {
                zapateroEncargado = em.getReference(zapateroEncargado.getClass(), zapateroEncargado.getId());
                reparacion.setZapateroEncargado(zapateroEncargado);
            }
            Set<IsumoReparacion> attachedIsumoReparacionSet = new HashSet<IsumoReparacion>();
            for (IsumoReparacion isumoReparacionSetIsumoReparacionToAttach : reparacion.getIsumoReparacionSet()) {
                isumoReparacionSetIsumoReparacionToAttach = em.getReference(isumoReparacionSetIsumoReparacionToAttach.getClass(), isumoReparacionSetIsumoReparacionToAttach.getId());
                attachedIsumoReparacionSet.add(isumoReparacionSetIsumoReparacionToAttach);
            }
            reparacion.setIsumoReparacionSet(attachedIsumoReparacionSet);
            Set<Pago> attachedPagoSet = new HashSet<Pago>();
            for (Pago pagoSetPagoToAttach : reparacion.getPagoSet()) {
                pagoSetPagoToAttach = em.getReference(pagoSetPagoToAttach.getClass(), pagoSetPagoToAttach.getId());
                attachedPagoSet.add(pagoSetPagoToAttach);
            }
            reparacion.setPagoSet(attachedPagoSet);
            em.persist(reparacion);
            if (calzado != null) {
                calzado.setReparacion(reparacion);
                calzado = em.merge(calzado);
            }
            if (estadoReparacion != null) {
                estadoReparacion.getReparacionSet().add(reparacion);
                estadoReparacion = em.merge(estadoReparacion);
            }
            if (cliente != null) {
                cliente.getReparacionSet().add(reparacion);
                cliente = em.merge(cliente);
            }
            if (zapateroEncargado != null) {
                zapateroEncargado.getReparacionSet().add(reparacion);
                zapateroEncargado = em.merge(zapateroEncargado);
            }
            for (IsumoReparacion isumoReparacionSetIsumoReparacion : reparacion.getIsumoReparacionSet()) {
                Reparacion oldReparacionOfIsumoReparacionSetIsumoReparacion = isumoReparacionSetIsumoReparacion.getReparacion();
                isumoReparacionSetIsumoReparacion.setReparacion(reparacion);
                isumoReparacionSetIsumoReparacion = em.merge(isumoReparacionSetIsumoReparacion);
                if (oldReparacionOfIsumoReparacionSetIsumoReparacion != null) {
                    oldReparacionOfIsumoReparacionSetIsumoReparacion.getIsumoReparacionSet().remove(isumoReparacionSetIsumoReparacion);
                    oldReparacionOfIsumoReparacionSetIsumoReparacion = em.merge(oldReparacionOfIsumoReparacionSetIsumoReparacion);
                }
            }
            for (Pago pagoSetPago : reparacion.getPagoSet()) {
                Reparacion oldReparacionOfPagoSetPago = pagoSetPago.getReparacion();
                pagoSetPago.setReparacion(reparacion);
                pagoSetPago = em.merge(pagoSetPago);
                if (oldReparacionOfPagoSetPago != null) {
                    oldReparacionOfPagoSetPago.getPagoSet().remove(pagoSetPago);
                    oldReparacionOfPagoSetPago = em.merge(oldReparacionOfPagoSetPago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reparacion reparacion) throws RuntimeException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reparacion persistentReparacion = em.find(Reparacion.class, reparacion.getId());
            Calzado calzadoOld = persistentReparacion.getCalzado();
            Calzado calzadoNew = reparacion.getCalzado();
            EstadoReparacion estadoReparacionOld = persistentReparacion.getEstadoReparacion();
            EstadoReparacion estadoReparacionNew = reparacion.getEstadoReparacion();
            Persona clienteOld = persistentReparacion.getCliente();
            Persona clienteNew = reparacion.getCliente();
            Persona zapateroEncargadoOld = persistentReparacion.getZapateroEncargado();
            Persona zapateroEncargadoNew = reparacion.getZapateroEncargado();
            Set<IsumoReparacion> isumoReparacionSetOld = persistentReparacion.getIsumoReparacionSet();
            Set<IsumoReparacion> isumoReparacionSetNew = reparacion.getIsumoReparacionSet();
            Set<Pago> pagoSetOld = persistentReparacion.getPagoSet();
            Set<Pago> pagoSetNew = reparacion.getPagoSet();
            List<String> illegalOrphanMessages = null;
            if (calzadoNew != null && !calzadoNew.equals(calzadoOld)) {
                Reparacion oldReparacionOfCalzado = calzadoNew.getReparacion();
                if (oldReparacionOfCalzado != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Calzado " + calzadoNew + " already has an item of type Reparacion whose calzado column cannot be null. Please make another selection for the calzado field.");
                }
            }
            for (IsumoReparacion isumoReparacionSetOldIsumoReparacion : isumoReparacionSetOld) {
                if (!isumoReparacionSetNew.contains(isumoReparacionSetOldIsumoReparacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain IsumoReparacion " + isumoReparacionSetOldIsumoReparacion + " since its reparacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (calzadoNew != null) {
                calzadoNew = em.getReference(calzadoNew.getClass(), calzadoNew.getId());
                reparacion.setCalzado(calzadoNew);
            }
            if (estadoReparacionNew != null) {
                estadoReparacionNew = em.getReference(estadoReparacionNew.getClass(), estadoReparacionNew.getId());
                reparacion.setEstadoReparacion(estadoReparacionNew);
            }
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getId());
                reparacion.setCliente(clienteNew);
            }
            if (zapateroEncargadoNew != null) {
                zapateroEncargadoNew = em.getReference(zapateroEncargadoNew.getClass(), zapateroEncargadoNew.getId());
                reparacion.setZapateroEncargado(zapateroEncargadoNew);
            }
            /*Set<IsumoReparacion> attachedIsumoReparacionSetNew = new HashSet<IsumoReparacion>();
            for (IsumoReparacion isumoReparacionSetNewIsumoReparacionToAttach : isumoReparacionSetNew) {
                isumoReparacionSetNewIsumoReparacionToAttach = em.getReference(isumoReparacionSetNewIsumoReparacionToAttach.getClass(), isumoReparacionSetNewIsumoReparacionToAttach.getId());
                attachedIsumoReparacionSetNew.add(isumoReparacionSetNewIsumoReparacionToAttach);
            }*/
            /*isumoReparacionSetNew = attachedIsumoReparacionSetNew;
            reparacion.setIsumoReparacionSet(isumoReparacionSetNew);
            Set<Pago> attachedPagoSetNew = new HashSet<Pago>();
            for (Pago pagoSetNewPagoToAttach : pagoSetNew) {
                pagoSetNewPagoToAttach = em.getReference(pagoSetNewPagoToAttach.getClass(), pagoSetNewPagoToAttach.getId());
                attachedPagoSetNew.add(pagoSetNewPagoToAttach);
            }*/
            /*pagoSetNew = attachedPagoSetNew;
            reparacion.setPagoSet(pagoSetNew);*/
            reparacion = em.merge(reparacion);
            if (calzadoOld != null && !calzadoOld.equals(calzadoNew)) {
                calzadoOld.setReparacion(null);
                calzadoOld = em.merge(calzadoOld);
            }
            if (calzadoNew != null && !calzadoNew.equals(calzadoOld)) {
                calzadoNew.setReparacion(reparacion);
                calzadoNew = em.merge(calzadoNew);
            }
            if (estadoReparacionOld != null && !estadoReparacionOld.equals(estadoReparacionNew)) {
                estadoReparacionOld.getReparacionSet().remove(reparacion);
                estadoReparacionOld = em.merge(estadoReparacionOld);
            }
            if (estadoReparacionNew != null && !estadoReparacionNew.equals(estadoReparacionOld)) {
                estadoReparacionNew.getReparacionSet().add(reparacion);
                estadoReparacionNew = em.merge(estadoReparacionNew);
            }
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getReparacionSet().remove(reparacion);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getReparacionSet().add(reparacion);
                clienteNew = em.merge(clienteNew);
            }
            if (zapateroEncargadoOld != null && !zapateroEncargadoOld.equals(zapateroEncargadoNew)) {
                zapateroEncargadoOld.getReparacionSet().remove(reparacion);
                zapateroEncargadoOld = em.merge(zapateroEncargadoOld);
            }
            if (zapateroEncargadoNew != null && !zapateroEncargadoNew.equals(zapateroEncargadoOld)) {
                zapateroEncargadoNew.getReparacionSet().add(reparacion);
                zapateroEncargadoNew = em.merge(zapateroEncargadoNew);
            }
            /*for (IsumoReparacion isumoReparacionSetNewIsumoReparacion : isumoReparacionSetNew) {
                if (!isumoReparacionSetOld.contains(isumoReparacionSetNewIsumoReparacion)) {
                    Reparacion oldReparacionOfIsumoReparacionSetNewIsumoReparacion = isumoReparacionSetNewIsumoReparacion.getReparacion();
                    isumoReparacionSetNewIsumoReparacion.setReparacion(reparacion);
                    isumoReparacionSetNewIsumoReparacion = em.merge(isumoReparacionSetNewIsumoReparacion);
                    if (oldReparacionOfIsumoReparacionSetNewIsumoReparacion != null && !oldReparacionOfIsumoReparacionSetNewIsumoReparacion.equals(reparacion)) {
                        oldReparacionOfIsumoReparacionSetNewIsumoReparacion.getIsumoReparacionSet().remove(isumoReparacionSetNewIsumoReparacion);
                        oldReparacionOfIsumoReparacionSetNewIsumoReparacion = em.merge(oldReparacionOfIsumoReparacionSetNewIsumoReparacion);
                    }
                }
            }
            for (Pago pagoSetOldPago : pagoSetOld) {
                if (!pagoSetNew.contains(pagoSetOldPago)) {
                    pagoSetOldPago.setReparacion(null);
                    pagoSetOldPago = em.merge(pagoSetOldPago);
                }
            }*/
            /*for (Pago pagoSetNewPago : pagoSetNew) {
                if (!pagoSetOld.contains(pagoSetNewPago)) {
                    Reparacion oldReparacionOfPagoSetNewPago = pagoSetNewPago.getReparacion();
                    pagoSetNewPago.setReparacion(reparacion);
                    pagoSetNewPago = em.merge(pagoSetNewPago);
                    if (oldReparacionOfPagoSetNewPago != null && !oldReparacionOfPagoSetNewPago.equals(reparacion)) {
                        oldReparacionOfPagoSetNewPago.getPagoSet().remove(pagoSetNewPago);
                        oldReparacionOfPagoSetNewPago = em.merge(oldReparacionOfPagoSetNewPago);
                    }
                }
            }*/
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            ex.printStackTrace();
            /*if (msg == null || msg.length() == 0) {
                Integer id = reparacion.getId();
                if (findReparacion(id) == null) {
                    throw new NonexistentEntityException("The reparacion with id " + id + " no longer exists.");
                }
            }
            throw ex;*/
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
            Reparacion reparacion;
            try {
                reparacion = em.getReference(Reparacion.class, id);
                reparacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reparacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<IsumoReparacion> isumoReparacionSetOrphanCheck = reparacion.getIsumoReparacionSet();
            for (IsumoReparacion isumoReparacionSetOrphanCheckIsumoReparacion : isumoReparacionSetOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reparacion (" + reparacion + ") cannot be destroyed since the IsumoReparacion " + isumoReparacionSetOrphanCheckIsumoReparacion + " in its isumoReparacionSet field has a non-nullable reparacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Calzado calzado = reparacion.getCalzado();
            if (calzado != null) {
                calzado.setReparacion(null);
                calzado = em.merge(calzado);
            }
            EstadoReparacion estadoReparacion = reparacion.getEstadoReparacion();
            if (estadoReparacion != null) {
                estadoReparacion.getReparacionSet().remove(reparacion);
                estadoReparacion = em.merge(estadoReparacion);
            }
            Persona cliente = reparacion.getCliente();
            if (cliente != null) {
                cliente.getReparacionSet().remove(reparacion);
                cliente = em.merge(cliente);
            }
            Persona zapateroEncargado = reparacion.getZapateroEncargado();
            if (zapateroEncargado != null) {
                zapateroEncargado.getReparacionSet().remove(reparacion);
                zapateroEncargado = em.merge(zapateroEncargado);
            }
            Set<Pago> pagoSet = reparacion.getPagoSet();
            for (Pago pagoSetPago : pagoSet) {
                pagoSetPago.setReparacion(null);
                pagoSetPago = em.merge(pagoSetPago);
            }
            em.remove(reparacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reparacion> findReparacionEntities() {
        return findReparacionEntities(true, -1, -1);
    }

    public List<Reparacion> findReparacionEntities(int maxResults, int firstResult) {
        return findReparacionEntities(false, maxResults, firstResult);
    }

    private List<Reparacion> findReparacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reparacion.class));
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

    public Reparacion findReparacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reparacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getReparacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reparacion> rt = cq.from(Reparacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
