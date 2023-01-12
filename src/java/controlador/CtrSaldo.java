/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import Procesos.Mayor;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.logicaContables;

/**
 *
 * @author Xom
 */
public class CtrSaldo extends HttpServlet {

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
            //parametros recibidos por ajax
            String codigo = request.getParameter("codigo");
            String tiposaldo = request.getParameter("tiposaldo");
            BigDecimal monto = new BigDecimal(Float.parseFloat(request.getParameter("monto")));
            //variables para la comprobacion de saldos
            logicaContables lc = new logicaContables();
            Mayor cuenta, aux;
            String si = "si", no = "no",notransaccion="notransaccion";
            char param = codigo.charAt(0);
            String operacion = codigo.substring(0, 2);//para los costos de operacion que se lleven ya que la 41 y 51 solo pueden ser abonada o cargadas
            if (param == '1' || param=='7') {
                cuenta = lc.mayorizarCuenta(codigo);
                cuenta.liquidacion();
                if (tiposaldo.equals("cargo")) {
                    String json = "{\"estado\":\"" + si + "\",\"tiposaldo\":\"" + cuenta.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + cuenta.getSaldoDeudor() + "\"}";
                    out.print(json);
                } else if (tiposaldo.equals("abono")) {
                    if (cuenta.getSaldoDeudor().floatValue() > monto.floatValue()) {
                        String json = "{\"estado\":\"" + si + "\",\"tiposaldo\":\"" + cuenta.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + cuenta.getSaldoDeudor() + "\"}";
                        out.print(json);
                    } else {
                        String json = "{\"estado\":\"" + no + "\",\"tiposaldo\":\"" + cuenta.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + cuenta.getSaldoDeudor() + "\"}";
                        out.print(json);
                    }
                }
            }
            if (param == '2' || param=='3' || param=='8') {
                aux = lc.mayorizarCuenta(codigo);
                aux.liquidacion();
                if (tiposaldo.equals("abono")) {
                    String json = "{\"estado\":\"" + si + "\",\"tiposaldo\":\"" + aux.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + aux.getSaldoAcreedor() + "\"}";
                    out.print(json);
                } else if (tiposaldo.equals("cargo")) {
                    if (aux.getSaldoAcreedor().floatValue() > monto.floatValue()) {
                        String json = "{\"estado\":\"" + si + "\",\"tiposaldo\":\"" + aux.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + aux.getSaldoAcreedor() + "\"}";
                        out.print(json);
                    } else {
                        String json = "{\"estado\":\"" + no + "\",\"tiposaldo\":\"" + aux.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + aux.getSaldoAcreedor() + "\"}";
                        out.print(json);
                    }
                }
            }
            /*if (param == '3') {
                aux = lc.mayorizarCuenta(codigo);
                aux.liquidacion();
                if (tiposaldo.equals("abono")) {
                    String json = "{\"estado\":\"" + si + "\",\"tiposaldo\":\"" + aux.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + aux.getSaldoAcreedor() + "\"}";
                    out.print(json);
                } else if (tiposaldo.equals("cargo")) {
                    if (aux.getSaldoAcreedor().floatValue() > monto.floatValue()) {
                        String json = "{\"estado\":\"" + si + "\",\"tiposaldo\":\"" + aux.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + aux.getSaldoAcreedor() + "\"}";
                        out.print(json);
                    } else {
                        String json = "{\"estado\":\"" + no + "\",\"tiposaldo\":\"" + aux.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + aux.getSaldoAcreedor() + "\"}";
                        out.print(json);
                    }
                }
            }*/
            if (param == '4') {
                cuenta = lc.mayorizarCuenta(codigo);
                cuenta.liquidacion();
                if (tiposaldo.equals("cargo")) {
                    String json = "{\"estado\":\"" + si + "\",\"tiposaldo\":\"" + cuenta.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + cuenta.getSaldoDeudor() + "\"}";
                    out.print(json);
                } else if (tiposaldo.equals("abono")) {
                    String json = "{\"estado\":\"" + no + "\",\"tiposaldo\":\"" + cuenta.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + cuenta.getSaldoDeudor() + "\"}";
                    out.print(json);
                }
            }

            if (param == '5') {
                aux = lc.mayorizarCuenta(codigo);
                aux.liquidacion();
                if (tiposaldo.equals("cargo")) {
                    String json = "{\"estado\":\"" + no + "\",\"tiposaldo\":\"" + aux.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + aux.getSaldoAcreedor() + "\"}";
                    out.print(json);
                } else if (tiposaldo.equals("abono")) {
                    String json = "{\"estado\":\"" + si + "\",\"tiposaldo\":\"" + aux.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + aux.getSaldoAcreedor() + "\"}";
                    out.print(json);
                }
            }
            
            if (param=='6') {
                cuenta=lc.mayorizarCuenta(codigo);
                cuenta.liquidacion();
                String json = "{\"estado\":\"" + notransaccion + "\",\"tiposaldo\":\"" + cuenta.getCuentamayor().getTipocuenta() + "\",\"monto\":\"" + monto + "\"}";
                out.print(json);
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
