<%-- 
    Document   : libromayoraux
    Created on : 29/09/2019, 06:03:03 PM
    Author     : Xom
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="entidades.Periodo"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.logging.SimpleFormatter"%>
<%@page import="java.util.List"%>
<%@page import="entidades.Debehaber"%>
<%@page import="Procesos.Mayor"%>
<%@page import="java.util.ArrayList"%>
<%@page import="logica.logicaContables"%>
<%
    //paginación
    String anterior = request.getParameter("anterior");
    String actual = request.getParameter("actual");
    //filtros
    int nivel = Integer.parseInt(request.getParameter("nivel"));
    String nombrecuenta = request.getParameter("nombrecuenta");
    String codigoinicio = request.getParameter("codigoinicio");
    String codigofinal = request.getParameter("codigofinal");
    //formatos de miles
    DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
    simbolo.setDecimalSeparator('.');
    simbolo.setGroupingSeparator(',');
    DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
    //procesos
    logicaContables lc = new logicaContables();
    Periodo periodo = lc.PeriodoenCurso();
    List<Mayor> listmayor = null;
    List<Mayor> listacuentasperiodos = null;//esta almacenara todas las cuentas con el filtro que se especifique
    List<Mayor> auxiliarmayor = null;
    //proceso de inicio de filtros
    if (nivel > 0) {
        listacuentasperiodos = (ArrayList<Mayor>) lc.listadoMayor(nivel);
    } else if (!nombrecuenta.isEmpty()) {
        listacuentasperiodos = (ArrayList<Mayor>) lc.listadoMayor(nombrecuenta);
    } else if (!codigoinicio.isEmpty() && !codigofinal.isEmpty()) {
        listacuentasperiodos = (ArrayList<Mayor>) lc.listadoMayor(codigoinicio, codigofinal);
    }
    listmayor = lc.listaperiodocuentas(listacuentasperiodos);
    /*out.print("nivel:" + nivel + " / nombre:" + nombrecuenta + " / codigoinicio:" + codigoinicio + " / codigofinal:" + codigofinal + " / tamaño lista:" + listmayor.size());
    out.print(" ***************** anterior:" + anterior + "/ actual:" + actual);
    out.print("**** lista original" + listacuentasperiodos.size());*/
    if (listmayor.size() > 10) {//si hay mas de 10 filtros entonces poner en funcion la paginación
        auxiliarmayor = listmayor.subList(Integer.parseInt(anterior), Integer.parseInt(actual));
    } else {//si no solo mostrarla nada más los registros existentes
        auxiliarmayor = listmayor;
    }
    if (auxiliarmayor.size() > 0 && listmayor != null) {
        for (Mayor my : auxiliarmayor) {
            int i = 0;
%>
<table class="table table-sm text-center">
    <thead>
        <tr class="alert-warning font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"> 
            <th><%= my.getCuentamayor().getCodigocuenta()%></th>
            <th colspan="7"><%= my.getCuentamayor().getNombrecuenta()%></th>
        </tr>
    </thead>
    <thead>
        <tr class="alert-info font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
            <th>Fecha</th>
            <th colspan="3">Concepto</th>
            <th>R</th>
            <th rowspan="2">Deudor</th>
            <th rowspan="2">Acreedor</th>
            <th rowspan="2">Saldo</th>
        </tr>
    </thead>
    <tbody>
        <%
            //out.print(" lista original" + my.getTama());
            float acumulador1 = 0, acumulador2 = 0, result = 0;
            for (Debehaber dh : lc.ordenarDH(my.getDh())) {
        %>
        <tr>
            <%
                if (periodo != null) {
                    if (dh.getPartida().getPeriodo().getIdperiodo() == periodo.getIdperiodo() && dh.getPartida() != null && !dh.getPartida().getDescripcion().equals("por cierre del ejercicio")) {
            %>

            <td style="border-right: 0px ;border-left:  1px solid #dee2e6;border-top: none !important"><%= dh.getPartida().getFecha()%></td>
            <td colspan="3" class="text-justify" style="border-right: 1px solid #dee2e6;border-left:  0px;border-top: none !important"><%= dh.getPartida().getDescripcion()%></td>
            <td style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= dh.getPartida().getNumpartida()%></td>
            <%if (dh.getTipotransaccion().equals('c')) {%>
            <td class="alert-link" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">$ <%= f.format(dh.getMontopartida().floatValue()) %></td>
            <td style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"></td>
            <%} else if (dh.getTipotransaccion().equals('a')) {%>
            <td style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"></td>
            <td class="alert-link" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">$ <%= f.format(dh.getMontopartida().floatValue()) %></td>
            <%}

                if (i == my.getTama() - 1) {%>
            <%
                if (my.getSaldoDeudor().floatValue() > 0) {
            %>
            <td class="alert-success text-success font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">$ <%= f.format(my.getSaldoDeudor().floatValue()) %></td>
            <%
            } else if (my.getSaldoAcreedor().floatValue() > 0) {

            %>
            <td class="alert-info text-info font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">$ <%= f.format(my.getSaldoAcreedor().floatValue()) %></td>
            <%

            } else {

            %>
            <td class="alert-danger text-danger font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">$ 0.00</td>
            <%                }
            } else {
                if (dh.getTipotransaccion().equals('c')) {
                    acumulador1 += Float.parseFloat(String.valueOf(dh.getMontopartida()));
                } else if (dh.getTipotransaccion().equals('a')) {
                    acumulador2 += Float.parseFloat(String.valueOf(dh.getMontopartida()));
                }
                if (acumulador2 > acumulador1) {
                    result = acumulador2 - acumulador1;
                } else {
                    result = acumulador1 - acumulador2;
                }

            %>
            <td style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">$ <%= f.format(result)%></td>
            <%
                        }
                        i++;
                    }
                }
            %>
        </tr>
        <%
                //i++;
            }
        %>
    </tbody>
    <tr>
        <td style="border-bottom: 10px !important"></td>
        <td colspan="3" style="border-bottom: 10px !important"></td>
        <td style="border-bottom: 10px !important"></td>
        <td style="border-bottom: 10px !important"></td>
        <td style="border-bottom: 10px !important"></td>

    </tr>
</table>
<%
    }
} else {%>
<div class="col-md-12 py-4">
    <div class="sticky-scroll-box alert-warning py-3">
        <div class="col-md-12 text-center">
            <span class="fa fa-warning text-warning" style="font-size: 28px"></span> 
            <h5 class="font-weight-bold text-warning">No se han encontrado resultados para la búsqueda especificada.</h5>
        </div>
    </div>
</div>
<%
    }
%>
