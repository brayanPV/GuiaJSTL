/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLLER;

import DAO.Conexion;
import DAO.MensajeJpaController;
import DTO.Mensaje;
import DTO.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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

        } else if (accion.equalsIgnoreCase("crear")) {
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String website = request.getParameter("website");
            String message = request.getParameter("message");
            String usuario = request.getParameter("usuario");
            Usuario u = new Usuario();
            u.setUsuario(usuario);
            Mensaje m = new Mensaje();
            m.setNombre(nombre);
            m.setEmail(email);
            m.setWebsite(website);
            m.setMessage(message);
            m.setUsuario(u);
            if (m != null) {
                mensajeDAO.create(m);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        }else {
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
