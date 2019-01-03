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
import org.zapateria.entidad.Calzado;
import org.zapateria.entidad.EstadoReparacion;
import org.zapateria.entidad.Persona;
import org.zapateria.entidad.IsumoReparacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zapateria.controller.exceptions.NonexistentEntityException;
import org.zapateria.controller.exceptions.PreexistingEntityException;
import org.zapateria.entidad.Pago;
import org.zapateria.entidad.Reparacion;

/**
 *
 * @author jose_
 */
public class ReparacionJpaController implements Serializable {

    public ReparacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reparacion reparacion) throws PreexistingEntityException, Exception {
        if (reparacion.getIsumoReparacions() == null) {
            reparacion.setIsumoReparacions(new ArrayList<IsumoReparacion>());
        }
        if (reparacion.getPagos() == null) {
            reparacion.setPagos(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Calzado calzadoBean = reparacion.getCalzadoBean();
            if (calzadoBean != null) {
                calzadoBean = em.getReference(calzadoBean.getClass(), calzadoBean.getId());
                reparacion.setCalzadoBean(calzadoBean);
            }
            EstadoReparacion estadoReparacionBean = reparacion.getEstadoReparacionBean();
            if (estadoReparacionBean != null) {
                estadoReparacionBean = em.getReference(estadoReparacionBean.getClass(), estadoReparacionBean.getId());
                reparacion.setEstadoReparacionBean(estadoReparacionBean);
            }
            Persona persona1 = reparacion.getPersona1();
            if (persona1 != null) {
                persona1 = em.getReference(persona1.getClass(), persona1.getId());
                reparacion.setPersona1(persona1);
            }
            Persona persona2 = reparacion.getPersona2();
            if (persona2 != null) {
                persona2 = em.getReference(persona2.getClass(), persona2.getId());
                reparacion.setPersona2(persona2);
            }
            List<IsumoReparacion> attachedIsumoReparacions = new ArrayList<IsumoReparacion>();
            for (IsumoReparacion isumoReparacionsIsumoReparacionToAttach : reparacion.getIsumoReparacions()) {
                isumoReparacionsIsumoReparacionToAttach = em.getReference(isumoReparacionsIsumoReparacionToAttach.getClass(), isumoReparacionsIsumoReparacionToAttach.getId());
                attachedIsumoReparacions.add(isumoReparacionsIsumoReparacionToAttach);
            }
            reparacion.setIsumoReparacions(attachedIsumoReparacions);
            List<Pago> attachedPagos = new ArrayList<Pago>();
            for (Pago pagosPagoToAttach : reparacion.getPagos()) {
                pagosPagoToAttach = em.getReference(pagosPagoToAttach.getClass(), pagosPagoToAttach.getId());
                attachedPagos.add(pagosPagoToAttach);
            }
            reparacion.setPagos(attachedPagos);
            em.persist(reparacion);
            if (calzadoBean != null) {
                calzadoBean.getReparacions().add(reparacion);
                calzadoBean = em.merge(calzadoBean);
            }
            if (estadoReparacionBean != null) {
                estadoReparacionBean.getReparacions().add(reparacion);
                estadoReparacionBean = em.merge(estadoReparacionBean);
            }
            if (persona1 != null) {
                persona1.getReparacions1().add(reparacion);
                persona1 = em.merge(persona1);
            }
            if (persona2 != null) {
                persona2.getReparacions1().add(reparacion);
                persona2 = em.merge(persona2);
            }
            for (IsumoReparacion isumoReparacionsIsumoReparacion : reparacion.getIsumoReparacions()) {
                Reparacion oldReparacionBeanOfIsumoReparacionsIsumoReparacion = isumoReparacionsIsumoReparacion.getReparacionBean();
                isumoReparacionsIsumoReparacion.setReparacionBean(reparacion);
                isumoReparacionsIsumoReparacion = em.merge(isumoReparacionsIsumoReparacion);
                if (oldReparacionBeanOfIsumoReparacionsIsumoReparacion != null) {
                    oldReparacionBeanOfIsumoReparacionsIsumoReparacion.getIsumoReparacions().remove(isumoReparacionsIsumoReparacion);
                    oldReparacionBeanOfIsumoReparacionsIsumoReparacion = em.merge(oldReparacionBeanOfIsumoReparacionsIsumoReparacion);
                }
            }
            for (Pago pagosPago : reparacion.getPagos()) {
                Reparacion oldReparacionBeanOfPagosPago = pagosPago.getReparacionBean();
                pagosPago.setReparacionBean(reparacion);
                pagosPago = em.merge(pagosPago);
                if (oldReparacionBeanOfPagosPago != null) {
                    oldReparacionBeanOfPagosPago.getPagos().remove(pagosPago);
                    oldReparacionBeanOfPagosPago = em.merge(oldReparacionBeanOfPagosPago);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReparacion(reparacion.getId()) != null) {
                throw new PreexistingEntityException("Reparacion " + reparacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reparacion reparacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reparacion persistentReparacion = em.find(Reparacion.class, reparacion.getId());
            Calzado calzadoBeanOld = persistentReparacion.getCalzadoBean();
            Calzado calzadoBeanNew = reparacion.getCalzadoBean();
            EstadoReparacion estadoReparacionBeanOld = persistentReparacion.getEstadoReparacionBean();
            EstadoReparacion estadoReparacionBeanNew = reparacion.getEstadoReparacionBean();
            Persona persona1Old = persistentReparacion.getPersona1();
            Persona persona1New = reparacion.getPersona1();
            Persona persona2Old = persistentReparacion.getPersona2();
            Persona persona2New = reparacion.getPersona2();
            List<IsumoReparacion> isumoReparacionsOld = persistentReparacion.getIsumoReparacions();
            List<IsumoReparacion> isumoReparacionsNew = reparacion.getIsumoReparacions();
            List<Pago> pagosOld = persistentReparacion.getPagos();
            List<Pago> pagosNew = reparacion.getPagos();
            if (calzadoBeanNew != null) {
                calzadoBeanNew = em.getReference(calzadoBeanNew.getClass(), calzadoBeanNew.getId());
                reparacion.setCalzadoBean(calzadoBeanNew);
            }
            if (estadoReparacionBeanNew != null) {
                estadoReparacionBeanNew = em.getReference(estadoReparacionBeanNew.getClass(), estadoReparacionBeanNew.getId());
                reparacion.setEstadoReparacionBean(estadoReparacionBeanNew);
            }
            if (persona1New != null) {
                persona1New = em.getReference(persona1New.getClass(), persona1New.getId());
                reparacion.setPersona1(persona1New);
            }
            if (persona2New != null) {
                persona2New = em.getReference(persona2New.getClass(), persona2New.getId());
                reparacion.setPersona2(persona2New);
            }
            List<IsumoReparacion> attachedIsumoReparacionsNew = new ArrayList<IsumoReparacion>();
            for (IsumoReparacion isumoReparacionsNewIsumoReparacionToAttach : isumoReparacionsNew) {
                isumoReparacionsNewIsumoReparacionToAttach = em.getReference(isumoReparacionsNewIsumoReparacionToAttach.getClass(), isumoReparacionsNewIsumoReparacionToAttach.getId());
                attachedIsumoReparacionsNew.add(isumoReparacionsNewIsumoReparacionToAttach);
            }
            isumoReparacionsNew = attachedIsumoReparacionsNew;
            reparacion.setIsumoReparacions(isumoReparacionsNew);
            List<Pago> attachedPagosNew = new ArrayList<Pago>();
            for (Pago pagosNewPagoToAttach : pagosNew) {
                pagosNewPagoToAttach = em.getReference(pagosNewPagoToAttach.getClass(), pagosNewPagoToAttach.getId());
                attachedPagosNew.add(pagosNewPagoToAttach);
            }
            pagosNew = attachedPagosNew;
            reparacion.setPagos(pagosNew);
            reparacion = em.merge(reparacion);
            if (calzadoBeanOld != null && !calzadoBeanOld.equals(calzadoBeanNew)) {
                calzadoBeanOld.getReparacions().remove(reparacion);
                calzadoBeanOld = em.merge(calzadoBeanOld);
            }
            if (calzadoBeanNew != null && !calzadoBeanNew.equals(calzadoBeanOld)) {
                calzadoBeanNew.getReparacions().add(reparacion);
                calzadoBeanNew = em.merge(calzadoBeanNew);
            }
            if (estadoReparacionBeanOld != null && !estadoReparacionBeanOld.equals(estadoReparacionBeanNew)) {
                estadoReparacionBeanOld.getReparacions().remove(reparacion);
                estadoReparacionBeanOld = em.merge(estadoReparacionBeanOld);
            }
            if (estadoReparacionBeanNew != null && !estadoReparacionBeanNew.equals(estadoReparacionBeanOld)) {
                estadoReparacionBeanNew.getReparacions().add(reparacion);
                estadoReparacionBeanNew = em.merge(estadoReparacionBeanNew);
            }
            if (persona1Old != null && !persona1Old.equals(persona1New)) {
                persona1Old.getReparacions1().remove(reparacion);
                persona1Old = em.merge(persona1Old);
            }
            if (persona1New != null && !persona1New.equals(persona1Old)) {
                persona1New.getReparacions1().add(reparacion);
                persona1New = em.merge(persona1New);
            }
            if (persona2Old != null && !persona2Old.equals(persona2New)) {
                persona2Old.getReparacions1().remove(reparacion);
                persona2Old = em.merge(persona2Old);
            }
            if (persona2New != null && !persona2New.equals(persona2Old)) {
                persona2New.getReparacions1().add(reparacion);
                persona2New = em.merge(persona2New);
            }
            for (IsumoReparacion isumoReparacionsOldIsumoReparacion : isumoReparacionsOld) {
                if (!isumoReparacionsNew.contains(isumoReparacionsOldIsumoReparacion)) {
                    isumoReparacionsOldIsumoReparacion.setReparacionBean(null);
                    isumoReparacionsOldIsumoReparacion = em.merge(isumoReparacionsOldIsumoReparacion);
                }
            }
            for (IsumoReparacion isumoReparacionsNewIsumoReparacion : isumoReparacionsNew) {
                if (!isumoReparacionsOld.contains(isumoReparacionsNewIsumoReparacion)) {
                    Reparacion oldReparacionBeanOfIsumoReparacionsNewIsumoReparacion = isumoReparacionsNewIsumoReparacion.getReparacionBean();
                    isumoReparacionsNewIsumoReparacion.setReparacionBean(reparacion);
                    isumoReparacionsNewIsumoReparacion = em.merge(isumoReparacionsNewIsumoReparacion);
                    if (oldReparacionBeanOfIsumoReparacionsNewIsumoReparacion != null && !oldReparacionBeanOfIsumoReparacionsNewIsumoReparacion.equals(reparacion)) {
                        oldReparacionBeanOfIsumoReparacionsNewIsumoReparacion.getIsumoReparacions().remove(isumoReparacionsNewIsumoReparacion);
                        oldReparacionBeanOfIsumoReparacionsNewIsumoReparacion = em.merge(oldReparacionBeanOfIsumoReparacionsNewIsumoReparacion);
                    }
                }
            }
            for (Pago pagosOldPago : pagosOld) {
                if (!pagosNew.contains(pagosOldPago)) {
                    pagosOldPago.setReparacionBean(null);
                    pagosOldPago = em.merge(pagosOldPago);
                }
            }
            for (Pago pagosNewPago : pagosNew) {
                if (!pagosOld.contains(pagosNewPago)) {
                    Reparacion oldReparacionBeanOfPagosNewPago = pagosNewPago.getReparacionBean();
                    pagosNewPago.setReparacionBean(reparacion);
                    pagosNewPago = em.merge(pagosNewPago);
                    if (oldReparacionBeanOfPagosNewPago != null && !oldReparacionBeanOfPagosNewPago.equals(reparacion)) {
                        oldReparacionBeanOfPagosNewPago.getPagos().remove(pagosNewPago);
                        oldReparacionBeanOfPagosNewPago = em.merge(oldReparacionBeanOfPagosNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reparacion.getId();
                if (findReparacion(id) == null) {
                    throw new NonexistentEntityException("The reparacion with id " + id + " no longer exists.");
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
            Reparacion reparacion;
            try {
                reparacion = em.getReference(Reparacion.class, id);
                reparacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reparacion with id " + id + " no longer exists.", enfe);
            }
            Calzado calzadoBean = reparacion.getCalzadoBean();
            if (calzadoBean != null) {
                calzadoBean.getReparacions().remove(reparacion);
                calzadoBean = em.merge(calzadoBean);
            }
            EstadoReparacion estadoReparacionBean = reparacion.getEstadoReparacionBean();
            if (estadoReparacionBean != null) {
                estadoReparacionBean.getReparacions().remove(reparacion);
                estadoReparacionBean = em.merge(estadoReparacionBean);
            }
            Persona persona1 = reparacion.getPersona1();
            if (persona1 != null) {
                persona1.getReparacions1().remove(reparacion);
                persona1 = em.merge(persona1);
            }
            Persona persona2 = reparacion.getPersona2();
            if (persona2 != null) {
                persona2.getReparacions1().remove(reparacion);
                persona2 = em.merge(persona2);
            }
            List<IsumoReparacion> isumoReparacions = reparacion.getIsumoReparacions();
            for (IsumoReparacion isumoReparacionsIsumoReparacion : isumoReparacions) {
                isumoReparacionsIsumoReparacion.setReparacionBean(null);
                isumoReparacionsIsumoReparacion = em.merge(isumoReparacionsIsumoReparacion);
            }
            List<Pago> pagos = reparacion.getPagos();
            for (Pago pagosPago : pagos) {
                pagosPago.setReparacionBean(null);
                pagosPago = em.merge(pagosPago);
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
