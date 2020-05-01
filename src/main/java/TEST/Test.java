/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST;

import DAO.Conexion;
import DAO.UsuarioJpaController;
import DTO.Usuario;
import java.util.List;

/**
 *
 * @author stive
 */
public class Test {
    
    public static void main(String[] args) {
    Conexion con = Conexion.getConexion();
    UsuarioJpaController UsuarioDAO = new UsuarioJpaController(con.getBd());
    List<Usuario> usuarios = UsuarioDAO.findUsuarioEntities();
    for(Usuario u: usuarios){
        System.out.println("El usuario es "+ u.getUsuario());
    }
    }
}
