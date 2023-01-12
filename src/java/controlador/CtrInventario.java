/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Inventario;
import entidades.Periodo;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.logicaContables;

/**
 *
 * @author Xom
 */
public class CtrInventario extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            String idperio = request.getParameter("idperiodo");
            String inf = request.getParameter("inf");
            if (Float.parseFloat(inf) <= 0) {
                out.print("El inventario no puede ser menor a cero");
                return;
            }

            if ("".equals(inf)) {
                out.print("Asigne un valor al inventario final de mercaderia");
                return;
            }
            logicaContables lc = new logicaContables();
            Inventario aux;
            Periodo periodo;
            Inventario i = new Inventario("Inventario Final", inf);
            if (idperio.isEmpty()) {
                lc.guardarInventario(i);
                despachador = request.getRequestDispatcher("estadoresultado.jsp");
            } else {
                periodo = lc.buscaPeriodo(Integer.parseInt(idperio));
                aux = lc.recuperaInventario("Inventario Final", Integer.parseInt(idperio));
                if (aux == null) {
                    i.setPeriodo(periodo);
                    lc.guardarInventario(i);
                } else {
                    aux.setValor(inf);
                    aux.setPeriodo(periodo);
                    lc.modificaInventario(aux);
                }
                despachador = request.getRequestDispatcher("estadoresultado.jsp");
            }
            despachador.forward(request, response);
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
