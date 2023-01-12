<%-- 
    Document   : paginaciones
    Created on : 10/01/2020, 03:44:38 PM
    Author     : Xom
--%>
<%@page import="entidades.Partida"%>
<%@page import="java.util.List"%>
<%@page import="logica.logicaContables"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Procesos.Mayor"%>
<%
    String accion = request.getParameter("accion");
    logicaContables lc = new logicaContables();
    if (accion.equals("paginacionmayor")) {
        //filtros
        int actual = Integer.parseInt(request.getParameter("actual"));
        int nivel = Integer.parseInt(request.getParameter("nivel"));
        String nombrecuenta = request.getParameter("nombrecuenta");
        String codigoinicio = request.getParameter("codigoinicio");
        String codigofinal = request.getParameter("codigofinal");
        List<Mayor> listacuentasperiodos = null;//esta almacenara todas las cuentas con el filtro que se especifique
        if (nivel > 0) {
            listacuentasperiodos = (ArrayList<Mayor>) lc.listadoMayor(nivel);
        } else if (!nombrecuenta.isEmpty()) {
            listacuentasperiodos = (ArrayList<Mayor>) lc.listadoMayor(nombrecuenta);
        } else if (!codigoinicio.isEmpty() && !codigofinal.isEmpty()) {
            listacuentasperiodos = (ArrayList<Mayor>) lc.listadoMayor(codigoinicio, codigofinal);
        }
%>
<div class=" col-md-12 text-right">
    <div class="btn btn-group alert-success" id="paginationmayor">
        <%
            int i = 1, val = 1;
            for (Mayor x : listacuentasperiodos) {
                x.liquidacion();
                if (x.getTama() > 0) {
                    if (i % 10 == 0) {
        %>
        <%
            if (actual == i) {
        %>
        <a class="btn btn-group btn-light text-center active" data-numpagination="<%=i%>"><%= val%></a>
        <%
        } else {
        %>
        <a class="btn btn-group btn-light text-center" data-numpagination="<%=i%>"><%= val%></a>
        <%}
                        val++;
                    }
                    i++;
                }
            }
            if ((i - 1) % 10 != 0) {
        %>
        <%
            if (actual == (i - 1)) {
        %>
        <a class="btn btn-group btn-light text-center active" data-numpagination="<%=i - 1%>"><%= val%></a>
        <%
        } else {
            if (listacuentasperiodos.size() > 0 && listacuentasperiodos.size() < 10) {%>
        <a class="btn btn-group btn-light text-center active" data-numpagination="<%=i - 1%>"><%= val%></a>
        <%} else {%>
        <a class="btn btn-group btn-light text-center" data-numpagination="<%=i - 1%>"><%= val%></a>
        <%}
                }
            }
        %>
    </div>
</div>


<%
} else if (accion.equals("paginaciondiario")) {
    int actual = Integer.parseInt(request.getParameter("actual"));
    String numero = request.getParameter("numero");
    String fecha = request.getParameter("fecha");
    String nivelinicio = request.getParameter("ninicio");
    String fechainicio = request.getParameter("finicio");
    String nivelfin = request.getParameter("nfin");
    String fechafin = request.getParameter("ffin");
    ArrayList<Partida> list = null;
    if (numero.isEmpty() && !fecha.isEmpty()) {
        list = (ArrayList<Partida>) lc.listapartidasperiodo(" ", fecha);
    } else if (!numero.isEmpty() && fecha.isEmpty()) {
        list = (ArrayList<Partida>) lc.listapartidasperiodo(numero, " ");
    } else if (!numero.isEmpty() && !fecha.isEmpty()) {
        list = (ArrayList<Partida>) lc.listapartidasperiodo(numero, fecha);
    } else if (!nivelinicio.isEmpty() && !nivelfin.isEmpty()) {
        list = (ArrayList<Partida>) lc.listapartidasperiodo(Integer.parseInt(nivelinicio), Integer.parseInt(nivelfin), "", "");
    } else if (!fechainicio.isEmpty() && !fechafin.isEmpty()) {
        list = (ArrayList<Partida>) lc.listapartidasperiodo(0, 0, fechainicio, fechafin);
    } else {
        list = (ArrayList<Partida>) lc.listapartidasperiodo("", "");
    }
%>
<div class=" col-md-12 text-right">
    <div class="btn btn-group alert-success" id="paginationpart">
                <%
                    int i = 1, val = 1, val2 = 0;
                    for (Partida x : list) {
                        if (i % 10 == 0) {
                %>
                <%
                    if (actual == i) {
                %>
        <a class="btn btn-group btn-light text-center active" data-numpagination="<%=i%>"><%= val%></a>
        <%
        } else {
        %>
        <a class="btn btn-group btn-light text-center" data-numpagination="<%=i%>"><%= val%></a>
        <%}
                    val++;
                }
                i++;
            }
            if ((i - 1) % 10 != 0) {
        %>
        <%if (actual == (i - 1)) {%>
        <a class="btn btn-group btn-light text-center active" data-numpagination="<%=i - 1%>"><%= val%></a>
        <%} else {
            if (list.size() > 0 && list.size() < 10) {%>
        <a class="btn btn-group btn-light text-center active" data-numpagination="<%=i - 1%>"><%= val%></a>
        <%} else {%>
        <a class="btn btn-group btn-light text-center" data-numpagination="<%=i - 1%>"><%= val%></a>
        <%}
                }
            }%>
    </div>
</div>
<%
    }
%>