package org.zapateria.mapper;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Persistence;
import org.zapateria.logica.Usuario;
import org.zapateria.utilidades.Constantes;

/**
 * @author geiner
 * @version 1.0
 * @created 19-dic.-2018 16:15:34
 */
public class UsuarioMapper {

    private org.zapateria.controller.UsuarioMapper usuarioController;

    public void finalize() throws Throwable {

    }

    public UsuarioMapper() {

        this.usuarioController = new org.zapateria.controller.UsuarioMapper(Persistence.createEntityManagerFactory(Constantes.CONTEXTO));
        System.out.println(" usuarioController : " + this.usuarioController);
        // TODO llamar a login.
        // EntityManagerFactory emf = Persistence.createEntityManagerFactory(Constantes.CONTEXTO);
        // EntityManager em = emf.createEntityManager();

        // em.getTransaction().begin();
        // List resultado = em.createNativeQuery("Select * from calzado").getResultList();

        // System.out.println(" Resultado : " + resultado);

        // em.getTransaction().commit();
        // em.close();
    }

    /**
     *
     * @param usuario
     */
    public void actualizar(Usuario usuario) {

    }

    /**
     *
     * @param id
     */
    public Set<Usuario> buscarPorId(Integer id) {
        return null;
    }

    /**
     *
     * @param usuario
     */
    public Usuario buscarPorNombreUsuario(String usuario) {
        return null;
    }

    public Set<Usuario> buscarTodos() {
        return null;
    }

    /**
     *
     * @param usuario
     */
    public void eliminar(Usuario usuario) {

    }

    /**
     *
     * @param id
     */
    public void eliminarPorId(Integer id) {

    }

    /**
     *
     * @param usuario
     */
    public void insertar(Usuario usuario) {
       //  usuarioController.create(null);
    }
    
     public boolean consultarUsuario(Usuario usuario) {
         boolean login = false;
         try {
             usuario = usuarioController.consultarUsuario(usuario);
             if ( Objects.nonNull(login) && Objects.nonNull(usuario.getPersona())  )
                 login = true;
         } catch(Exception e) {
             login = false;
         }
         return login;
     }
    
    
}//end UsuarioMapper
