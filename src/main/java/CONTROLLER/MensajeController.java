/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.Conexion;
import DAO.MensajeJpaController;
import DAO.exceptions.NonexistentEntityException;
import DTO.Mensaje;
import DTO.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author stive MensajeController
 */
@WebServlet(name = "MensajeController", urlPatterns = {"/MensajeController"})
public class MensajeController extends HttpServlet {

    Conexion con = Conexion.getConexion();
    MensajeJpaController mensajeDAO = new MensajeJpaController(con.getBd());
    List<Mensaje> mensajes = mensajeDAO.findMensajeEntities();
    Mensaje m = new Mensaje();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (accion.equalsIgnoreCase("registrar")) {
            request.getRequestDispatcher("./JSP/Registrar.jsp").forward(request, response);
        } else if (accion.equalsIgnoreCase("listar")) {
            request.getRequestDispatcher("./JSP/Listar.jsp").forward(request, response);
        } else if (accion.equalsIgnoreCase("crear")) {
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String website = request.getParameter("website");
            String message = request.getParameter("message");
            String usuario = request.getParameter("usuario");
            Usuario u = new Usuario();
            u.setUsuario(usuario);
            m = new Mensaje();
            m.setNombre(nombre);
            m.setEmail(email);
            m.setWebsite(website);
            m.setMessage(message);
            m.setUsuario(u);
            if (m != null) {
                mensajeDAO.create(m);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        } else if (accion.equalsIgnoreCase("editar")) {
            int id = Integer.parseInt(request.getParameter("id"));
            m = mensajeDAO.findMensaje(id);
            request.getSession().setAttribute("mensaje", m);
            request.getRequestDispatcher("./JSP/Editar.jsp").forward(request, response);
        } else if (accion.equalsIgnoreCase("eliminar")) {
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                mensajeDAO.destroy(id);
                request.getRequestDispatcher("./JSP/Listar.jsp").forward(request, response);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(MensajeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.getSession().setAttribute("mensaje", m);
        } else if (accion.equalsIgnoreCase("editarMensaje")) {
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String website = request.getParameter("website");
            String message = request.getParameter("message");
            String usuario = request.getParameter("usuario");
            Usuario u = new Usuario();
            u.setUsuario(usuario);

            m.setNombre(nombre);
            m.setEmail(email);
            m.setWebsite(website);
            m.setMessage(message);
            m.setUsuario(u);
            try {
                mensajeDAO.edit(m);
                request.getRequestDispatcher("./JSP/Listar.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(MensajeController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
