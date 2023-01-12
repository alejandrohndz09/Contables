/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Cuenta;
import entidades.Debehaber;
import entidades.Partida;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import javax.json.Json;
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
public class CtrPartida extends HttpServlet {

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
            /*variables de recibimiento de los parametros enviados desde custom2.js*/

 /*variables para la creacion de los cargos y abonos*/
            String codcuentas[];
            String cargoabono[];
            String opcionca[];
            /*variables para la creacion de la partida*/
            String numpartida;
            String fecha;
            String transaccion;

            /*obteniendo los datos*/
            codcuentas = request.getParameterValues("codcuentas[]");
            cargoabono = request.getParameterValues("cargoabono[]");
            opcionca = request.getParameterValues("opcionca[]");
            numpartida = request.getParameter("numpartida");
            fecha = request.getParameter("fecha");
            transaccion = request.getParameter("transaccion");

            boolean respuesta = true;
            String json;
            /*validacion del tamaño de los codigos y los cargos/abonos y las opciones, de si son iguales*/
            if (codcuentas.length != cargoabono.length || codcuentas.length != opcionca.length || cargoabono.length != opcionca.length) {
                respuesta = false;
                json = muestramensaje("Alteraciones en el Servidor", "La transacción ha fallado, al parecer han habido alteraciones de datos en el servidor", "error");
                out.print(json);
                return;
            }

            if (codcuentas.length == 0 || cargoabono.length == 0 || opcionca.length == 0) {
                respuesta = false;
                json = muestramensaje("Alteraciones en el Servidor", "La transaccion ha fallado, al parecer no hay montos/cuentas asignados", "error");
                out.print(json);
                return;
            }

            if (numpartida.isEmpty() || fecha.isEmpty() || transaccion.isEmpty()) {
                respuesta = false;
                json = muestramensaje("Alteraciones en el Servidor", "La transacción ha fallado, al parecer han habido alteraciones de datos en el servidor", "error");
                out.print(json);
                return;
            }

            Partida partida;
            Partida validacion;
            Cuenta validacion2;
            Debehaber dh;
            logicaContables lc = new logicaContables();

            /*arreglo de cargos y abonos*/
            ArrayList<Debehaber> debeshabers = new ArrayList<>();
            int cantidadca = cargoabono.length;

            /*asignamos los datos respectivos a las partidas*/
            partida = new Partida();
            validacion = new Partida();
            partida.setFecha(Date.valueOf(fecha));
            partida.setDescripcion(transaccion);
            validacion = lc.buscapartidanumeroperiodoexistente(Integer.parseInt(numpartida));
            if (validacion == null) {
                partida.setNumpartida(Integer.parseInt(numpartida.trim()));
            } else {
                respuesta = false;
                json = muestramensaje("Número de Partida ya existe", "El número de la partida asignado ya existe en el registro de partidas del periodo correspondiente", "error");
                out.print(json);
                return;
            }

            /*asignacion de cargos y abonos a la partida a registrar*/
            if (cantidadca > 0) {
                for (int i = 0; i < cantidadca; i++) {
                    dh = new Debehaber();/*creamos una nueva instancia para cargo/abono*/
                    validacion2 = new Cuenta();/*agregar una nueva instancia de cuenta asignada para cada c/a*/
                    dh.setMontopartida(BigDecimal.valueOf(Float.parseFloat(cargoabono[i])));
                    dh.setTipotransaccion(opcionca[i].charAt(0));
                    dh.setPartida(partida);
                    validacion2 = lc.buscarcodigo("", codcuentas[i], 0);
                    if (validacion2 == null) {
                        respuesta = false;
                        json = muestramensaje("La cuenta no existe", "La cuenta que se intenta registrar a la partida #" + partida.getNumpartida() + " no existe, agregue una cuenta que exista en su catalogo", "error");
                        out.print(json);
                        return;
                    } else {
                        dh.setCuenta(validacion2);
                    }
                    debeshabers.add(dh);
                }
            } else {
                respuesta = false;
                json = muestramensaje("Total de cargos y abonos", "El registro de la Partida #" + partida.getNumpartida() + " no se ha almacenado, debido a que no tiene asignado cargos y abonos.", "error");
                out.print(json);
                return;
            }
            /*validacion de que las partidas deben estar cuadradas*/
            float totcargo = 0, totabono = 0;
            for (Debehaber d : debeshabers) {
                if (d.getTipotransaccion().equals('c')) {
                    totcargo += Float.parseFloat(String.valueOf(d.getMontopartida()));
                } else if (d.getTipotransaccion().equals('a')) {
                    totabono += Float.parseFloat(String.valueOf(d.getMontopartida()));
                }
            }

            if (totcargo != totabono) {
                respuesta = false;
                json = muestramensaje("La transacción no esta cuadrada", "La Partida #" + partida.getNumpartida() + " el monto del cargo= " + totcargo + " y el monto del abono= " + totabono + " no son iguales.", "error");
                out.print(json);
                return;
            }

            if (respuesta) {
                json = muestramensaje("Registro guardado", "El registro de la Partida #" + partida.getNumpartida() + " ha sido satisfactorio.", "success");
                lc.guardarpartidacargoabono(partida, debeshabers);
                out.print(json);
            } else {
                json = muestramensaje("Error de guardado", "El registro de la Partida #" + partida.getNumpartida() + " no se ha almacenado, revise y vuelava a intentarlo.", "error");
                out.print(json);
            }
            //despachador = request.getRequestDispatcher("registrarpartidas.jsp");
            //despachador.forward(request, response);
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

    public String muestramensaje(String title, String text, String icon) {
        String json = "{\"title\":\"" + title + "\",\"text\":\"" + text + "\",\"icon\":\"" + icon + "\"}";
        return json;
    }
}
