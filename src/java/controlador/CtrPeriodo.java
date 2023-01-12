/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Partida;
import entidades.Periodo;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
public class CtrPeriodo extends HttpServlet {

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
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            Date fi = Date.valueOf(request.getParameter("fechainicio"));
            Date ff = Date.valueOf(request.getParameter("fechafinal"));
            String idperiodo = request.getParameter("idperiodo");
            Periodo periodo, bPeriodo, aux;
            logicaContables lc = new logicaContables();

            if (idperiodo.isEmpty() && lc.PeriodoenCurso() == null) {
                aux = lc.bp(fi, ff);
                if (aux == null) {
                    periodo = new Periodo(fi, ff, true, false);
                    lc.guardarPeriodo(periodo);
                    if (lc.obtenerUltima() != null) {//si esta condicion se cumple quiere decir que ya ha habido periodos anteriores
                        //por lo cual hay que recuperar la ultima partida registrada, faltan procesos aun
                        Partida primera;
                        primera = lc.obtenerUltima();
                        primera.setNumpartida(1);
                        primera.setFecha(periodo.getFechainicial());
                        primera.setPeriodo(lc.PeriodoenCurso());
                        lc.actualizarPartida(primera);
                    } else {
                        out.print("No hay periodos aun registrados");
                    }
                    despachador = request.getRequestDispatcher("inicioperiodo.jsp");
                } else {
                    despachador = request.getRequestDispatcher("inicioperiodo.jsp");
                }
            } else {
                if (!idperiodo.isEmpty() && lc.PeriodoenCurso() == null) {
                    periodo = lc.buscaPeriodo(Integer.parseInt(idperiodo));
                    periodo.setEnproceso(true);
                    lc.actualizarPeriodo(periodo);
                    despachador = request.getRequestDispatcher("inicioperiodo.jsp");
                } else {
                    if (!idperiodo.isEmpty() && lc.PeriodoenCurso() != null) {
                        periodo = lc.PeriodoenCurso();
                        periodo.setEnproceso(false);
                        lc.actualizarPeriodo(periodo);
                        bPeriodo = lc.buscaPeriodo(Integer.parseInt(idperiodo));
                        bPeriodo.setEnproceso(true);
                        lc.actualizarPeriodo(bPeriodo);
                        despachador = request.getRequestDispatcher("inicioperiodo.jsp");
                    } else {
                        if (idperiodo.isEmpty() && lc.PeriodoenCurso() != null) {
                            //prueba 
                            periodo = lc.PeriodoenCurso();
                            periodo.setEnproceso(true);
                            lc.actualizarPeriodo(periodo);
                            despachador = request.getRequestDispatcher("inicioperiodo.jsp");
                        }
                    }
                }
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

/*
                            
                            En esta condición aun hay un periodo sin terminar, y se quiere iniciar uno nuevo, eso no es posible,
                            se debe haber finalizado el periodo actual para dar pasao a otro, por lo caul solo se actualizara el estado
                            del periodo a true, para que se siga maneteniendo
                            
 */
 /*if (lc.PeriodoenCurso().getTerminado() == true) {
                                bPeriodo=lc.PeriodoenCurso();
                                bPeriodo.setEnproceso(false);
                                lc.actualizarPeriodo(bPeriodo);
                                aux = lc.bp(fi, ff);
                                if (aux == null) {
                                    periodo = new Periodo(fi, ff, true, false);
                                    lc.guardarPeriodo(periodo);
                                    if (lc.obtenerUltima() != null) {//si esta condicion se cumple quiere decir que ya ha habido periodos anteriores
                                        //por lo cual hay que recuperar la ultima partida registrada, faltan procesos aun
                                        Partida primera;
                                        primera = lc.obtenerUltima();
                                        primera.setNumpartida(1);
                                        primera.setFecha(periodo.getFechainicial());
                                        primera.setPeriodo(lc.PeriodoenCurso());
                                        lc.actualizarPartida(primera);
                                    } else {
                                        out.print("No hay periodos aun registrados");
                                    }
                                    despachador = request.getRequestDispatcher("inicioperiodo.jsp");
                                } else {
                                    despachador = request.getRequestDispatcher("inicioperiodo.jsp");
                                }
                            } else {
                                periodo = lc.PeriodoenCurso();
                                periodo.setEnproceso(true);
                                lc.actualizarPeriodo(periodo);
                                despachador = request.getRequestDispatcher("inicioperiodo.jsp");
                            }*/
