package org.zapateria.inicio;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author geiner
 */
public class Zapateria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO llamar a login.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zapateria");
        EntityManager em = emf.createEntityManager();
                
        em.getTransaction().begin();
        List resultado = em.createNativeQuery("Select * from calzado").getResultList();
        
        System.out.println(" Resultado : " + resultado);
        
        em.getTransaction().commit();
        em.close();
    }
    
}
