/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zapateria.utilidades;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JOSE
 */
public class Constantes {
    
    public static String CONTEXTO = "zapateria";
    public static String ZAPATERO_ROL = "Zapatero";
    public static String CLIENTE_ROL = "Cliente";
    public static String ADMIN_ROL = "Administrador";
    
    public static String USUARIO = "usuario";
    
    public static String ADMIN_EMPTY = "";
    public static String ADMIN_PERSONA = "Administrar personas";
    public static String ADMIN_INSUMO = "Administrar insumos";
    public static String ADMIN_ADMINISTRAR = "Administrar";
    
    // variable miembro de constante para el manejo de informacion en session
    public static Map<String, Object> session = new HashMap<>();
    
}
