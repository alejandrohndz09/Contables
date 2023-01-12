/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Cuenta;
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
public class CtrCuenta extends HttpServlet {

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
            String accion = request.getParameter("guardar");/*parametro recibido segun opcion sera save o update*/

 /*recibiendo los parametros*/
            logicaContables lc = new logicaContables();
            Cuenta c = new Cuenta();
            Cuenta cod = new Cuenta();
            Cuenta padre, aux;

            String idcuenta = request.getParameter("id");
            String nombrecuenta = request.getParameter("nombre");
            String codigocuenta = request.getParameter("codigo");
            String descripcioncuenta = request.getParameter("descripcion");
            String tiposaldocuenta = request.getParameter("tiposaldo");

            /*verificamos el tipo de texto que trae el tiposaldocuenta*/
            String auxtipocuenta = "";

            if (tiposaldocuenta.equals("Deudor")) {

                auxtipocuenta = "+";

            } else if (tiposaldocuenta.equals("Acreedor")) {

                auxtipocuenta = "-";

            } else if (tiposaldocuenta.equals("De Cierre")) {

                auxtipocuenta = "=";

            }

            /*
            simplemente se utilizo para agregar los apdres a las cuentas
            for (Cuenta setearpadre : lc.setearpadres()) {
                if (setearpadre.getCodigocuenta().length() == 7) {
                    padre = Cuentapadre(setearpadre.getCodigocuenta(), setearpadre.getCodigocuenta().length(), lc);
                    setearpadre.setCuenta(padre);
                    lc.actualizarcuenta(setearpadre);
                }
            }*/

            /*si el idcuenta es vacio, quiere decir que es un nuevo dato a guardar,
            caso contrario se ejecutara la modificación del dato respectivo*/
            if (idcuenta.isEmpty()) {

                c = lc.buscarcodigo(nombrecuenta, "", 0);
                cod = lc.buscarcodigo("", codigocuenta, 0);
                padre = Cuentapadre(codigocuenta, codigocuenta.length(), lc);

                if (c == null && cod == null) {//aquí puse la longitud de codigo i c!=null, si algo falla quiatr esa parte

                    aux = new Cuenta(autoincrementable(lc), nombrecuenta, descripcioncuenta, auxtipocuenta, codigocuenta);
                    aux.setCuenta(padre);
                    lc.guardarcuenta(aux);

                } else {
                    if (cod == null) {
                        if (c.getCodigocuenta().trim().length() == 6 && c.getCodigocuenta().trim().length() == codigocuenta.trim().length()) {
                            aux = new Cuenta(autoincrementable(lc), c.getNombrecuenta(), descripcioncuenta, auxtipocuenta, codigocuenta);
                            aux.setCuenta(padre);
                            lc.guardarcuenta(aux);
                        } else if (c.getNombrecuenta().compareTo(nombrecuenta) == 0 && codigocuenta.trim().length() == 6) {
                            aux = new Cuenta(autoincrementable(lc), nombrecuenta, descripcioncuenta, auxtipocuenta, codigocuenta);
                            aux.setCuenta(padre);
                            lc.guardarcuenta(aux);
                        }
                    } else {
                        out.print("Error, el nombre de la cuenta puede repetirse pero el codigo no");
                    }
                }
            } else if (!idcuenta.isEmpty()) {/*edicion de la cuenta*/

 /*en esta parte de edicion debo manejar la opcion de no modificar el codigo de las cuentas padres a 
                un tamaño del string superior a 1*/
                c = lc.buscarcodigo("", "", Integer.parseInt(idcuenta));
                String codaux = null;

                if (c.getCodigocuenta().length() == 1 && codigocuenta.length() == 1) {/*es cuenta padre por lo tanto su
                    codigo no se modificara, ya que es necesaria para generar sus cuentas hijas*/
                    codaux = c.getCodigocuenta();

                } else if (codigocuenta.length() == 1 && c.getCodigocuenta().length() == 2) {/*es una cuenta que solo 
                    posee dos digitos como codigo*/
                    codaux = c.getCodigocuenta().substring(0, 1) + codigocuenta;

                } else if (codigocuenta.length() == 2 && c.getCodigocuenta().length() % 2 == 0) {/*cuenta que posee mas de dos digitos en su cuenta*/

                    codaux = c.getCodigocuenta().substring(0, c.getCodigocuenta().length() - 2) + codigocuenta;

                } else if (codigocuenta.length() == 2 && c.getCodigocuenta().length() > 2 && c.getCodigocuenta().length() % 2 != 0) {
                    codaux = c.getCodigocuenta().substring(0, c.getCodigocuenta().length() - 3) + codigocuenta + "R";
                } else {

                    out.print("El código ingresado es incorrecto");

                }

                if (codaux == null) {/*si el codigo biene null es decir no trae nada, se procede a guardar el 
                    codigo que se recupero que esta almacenado en el objeto c*/

                    Cuenta ct = new Cuenta(c.getIdcuenta(), nombrecuenta, descripcioncuenta, auxtipocuenta, c.getCodigocuenta());
                    lc.actualizarcuenta(ct);

                } else {/*si el codigo trae algo comprobamos si ya existe o es uno nuevo.
                    En caso de ser un codigo nuevo se lo asignamos a la cuenta, si el codigo ya existe
                    simplemente solo lo volvemos a guardar*/

                    Cuenta p = lc.buscarcodigo("", codaux, 0);
                    Cuenta n = lc.buscarcodigo(nombrecuenta, "", 0);
                    validaEdicion(p, n, c, nombrecuenta, descripcioncuenta, auxtipocuenta, codaux, lc);

                }
            }

            despachador = request.getRequestDispatcher("catalogocuentas.jsp");
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

    /*generacion del id para insertarlo como primary key*/
    public int autoincrementable(logicaContables lista) {
        int auto_increment = 0;

        if (lista.buscacuentas().size() > 0) {
            auto_increment = lista.buscacuentas().get(lista.buscacuentas().size() - 1).getIdcuenta() + 1;
        } else {
            auto_increment = 1;
        }
        return auto_increment;
    }

    public void validaEdicion(Cuenta p, Cuenta n, Cuenta c, String nombrecuenta, String descripcioncuenta, String auxtipocuenta, String codaux, logicaContables lc) {
        Cuenta padre;
        padre = Cuentapadre(codaux, codaux.length(), lc);
        if (p == null && n == null) {

            Cuenta ct = new Cuenta(c.getIdcuenta(), nombrecuenta, descripcioncuenta, auxtipocuenta, codaux);
            ct.setCuenta(padre);
            lc.actualizarcuenta(ct);

        } else if (p == null && n != null) {

            if (n.getCodigocuenta().trim().length() == 6 && codaux.trim().length() == c.getCodigocuenta().trim().length()) {
                Cuenta ct = new Cuenta(c.getIdcuenta(), nombrecuenta, descripcioncuenta, auxtipocuenta, codaux);
                ct.setCuenta(padre);
                lc.actualizarcuenta(ct);
            } else {
                Cuenta ct = new Cuenta(c.getIdcuenta(), n.getNombrecuenta(), descripcioncuenta, auxtipocuenta, codaux);
                ct.setCuenta(padre);
                lc.actualizarcuenta(ct);
            }

        } else {
            padre = Cuentapadre(p.getCodigocuenta(), p.getCodigocuenta().length(), lc);
            Cuenta ct = new Cuenta(p.getIdcuenta(), nombrecuenta, descripcioncuenta, auxtipocuenta, p.getCodigocuenta());
            ct.setCuenta(padre);
            lc.actualizarcuenta(ct);

        }
    }

    public Cuenta Cuentapadre(String codigo, int n, logicaContables lc) {
        Cuenta padre = null, aux = null;
        if (n == 1) {
            padre = null;
        } else if (n == 2) {
            padre = lc.padre(codigo.substring(0, 1));

        } else if (n > 2) {
            if (n % 2 == 0) {
                padre = lc.padre(codigo.substring(0, codigo.length() - 2));
            } else {
                padre = lc.padre(codigo.substring(0, codigo.length() - 3));
                aux = lc.buscarcodigo("", codigo.substring(0, codigo.length() - 3) + "R", 0);
                if (aux != null && padre == null) {
                    padre = aux;
                } else if (padre != null && aux == null) {
                    aux = padre;
                    padre = aux;
                } else {
                    padre = null;
                }
            }
        }
        return padre;
    }
}
