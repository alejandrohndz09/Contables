/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import backup.PostgresBackup;
import backup.PostgresRestore;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Xom
 */
public class CtrRespaldo extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            RequestDispatcher despachador = null;
            String accion = request.getParameter("accion");
            if (accion.equals("respaldo")) {
                PostgresBackup base = new PostgresBackup();
                String a = request.getParameter("gestor");
                String b = request.getParameter("usuario");
                String c = request.getParameter("database");
                String d = request.getParameter("contraseña");
                String e = request.getParameter("puerto");
                try {
                    base.realizaBackup(a, e, b, c, d);

                } catch (InterruptedException ex) {
                    Logger.getLogger(CtrRespaldo.class.getName()).log(Level.SEVERE, null, ex);
                }
                despachador = request.getRequestDispatcher("respaldo.jsp");
                despachador.forward(request, response);
            } else if (accion.equals("importar")) {
                PostgresRestore restaurar = new PostgresRestore();
                String puerto = request.getParameter("puerto");
                String database = request.getParameter("database");
                String nombrearchivo = request.getParameter("archivoname");
                String user = request.getParameter("usuario");
                String pass = request.getParameter("password");
                int valor = restaurar.restore(puerto, user, pass, database, nombrearchivo);
                if (valor == 1) {
                    String json = "{\"estado\":\"" + valor + "\"}";
                    out.print(json);
                } else {
                    String json = "{\"estado\":\"" + valor + "\"}";
                    out.print(json);
                }
            }
        }
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
        processRequest(request, response);
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
