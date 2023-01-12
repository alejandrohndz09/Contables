<%-- 
    Document   : librodiarioaux
    Created on : 22/09/2019, 04:04:56 PM
    Author     : Xom
--%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entidades.Debehaber"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="entidades.Partida"%>
<%@page import="logica.logicaContables"%>

<%
    //filtros por datos
    String numero = request.getParameter("numero");
    String fecha = request.getParameter("fecha");
    //filtros por rangos
    String nivelinicio = request.getParameter("ninicio");
    String fechainicio = request.getParameter("finicio");
    String nivelfin = request.getParameter("nfin");
    String fechafin = request.getParameter("ffin");
    //numero de paginaciones
    String anterior = request.getParameter("anterior");
    String actual = request.getParameter("actual");
    //acumuladores y formato de numeros
    DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
    simbolo.setDecimalSeparator('.');
    simbolo.setGroupingSeparator(',');
    DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
    BigDecimal tdebe = new BigDecimal("0.00");
    BigDecimal thaber = new BigDecimal("0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    //controlador principal
    logicaContables lc = new logicaContables();
    //arreglos de datos
    ArrayList<Partida> aux = (ArrayList<Partida>) lc.listapartidasperiodo("", "");
    ArrayList<Partida> list = null;
    List<Partida> listpartperiodo = null;
    if (aux.size() > 0) {
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
        if (list.size() > 0) {
            if (list.size() > 10) {
                listpartperiodo = list.subList(Integer.parseInt(anterior), Integer.parseInt(actual));
            } else {
                listpartperiodo = list;
            }
            if (listpartperiodo.size() > 0 && list != null) {
                for (Partida p : listpartperiodo) {
%>
<div class="col-md-12 py-3">
    <div class="sticky-scroll-box bg-light">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-12 alert-success text-center py-3">
                    <h4 class="font-weight-bold text-success">Partida #<%= p.getNumpartida()%></h4>
                </div>
                <div class="col-md-5 alert-success text-right py-3" style="display: none;">
                    <a data-eliminar="eliminarpartida" href="#" class="text-danger h5 text-right"><span class="fa fa-trash"></span></a>
                </div>
            </div>
        </div>
        <br>
        <div class="row px-5 text-center">
            <div class="col-md-1">
                <h6 class="font-weight-bold">Fecha:</h6>
            </div>
            <div class="col-md-2 text-justify" data-type="fechpartida">
                <h6><%= sdf.format(p.getFecha())%></h6>
            </div>
            <div class="col-md-2">
                <h56 class="font-weight-bold">Descripción de la Transacción:</h6>
            </div>
            <div class="col-md-7 text-justify" data-type="despartida">
                <h6><%= p.getDescripcion()%></h6>
            </div>
        </div>
        <br>
        <div class="col-md-12 text-center">
            <div class="row px-2 py-2 text-center alert-info">
                <div class="col-md-3"><h6 class="font-weight-bold">Código</h6></div>
                <div class="col-md-3"><h6 class="font-weight-bold">Cuenta</h6></div>
                <div class="col-md-3"><h6 class="font-weight-bold">Debe</h6></div>
                <div class="col-md-3"><h6 class="font-weight-bold">Haber</h6></div>
            </div>
            <%
                BigDecimal td = new BigDecimal("0.00");
                BigDecimal th = new BigDecimal("0.00");
                for (Debehaber dh : lc.ordenarDH(p.getDebehabers())) {
            %>
            <div class="row px-2 py-2 text-center alert-light">
                <div class="col-md-3"><h6 class="font-weight-bold"><%= dh.getCuenta().getCodigocuenta().trim()%></h6></div>
                <div class="col-md-3" style="text-align: justify"><h6 class="font-weight-bold"><%= dh.getCuenta().getNombrecuenta()%></h6></div>
                    <%
                        if (dh.getTipotransaccion().equals('c')) {
                            td = td.add(new BigDecimal(Float.parseFloat(String.valueOf(dh.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
                    %>
                <div class="col-md-3"><h6>$ <%= f.format(dh.getMontopartida().floatValue()) %></h6></div>
                <div class="col-md-3"><h6><!--$ 00.00--></h6></div>
                <% } else if (dh.getTipotransaccion().equals('a')) {
                    th = th.add(new BigDecimal(Float.parseFloat(String.valueOf(dh.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
                %>
                <div class="col-md-3"><h6><!--$ 00.00--></h6></div>
                <div class="col-md-3"><h6>$ <%= f.format(dh.getMontopartida().floatValue()) %></h6></div>
                <% }%>
            </div>
            <% }%>
            <%
                if (td.equals(th)) {
            %>
            <div class="row px-2 py-2 text-center alert-success">
                <div class="col-md-6"><h6 class="font-weight-bold text-justify">Total debe/haber</h6></div>
                <div class="col-md-3"><h6 class="font-weight-bold">$ <%= f.format(td.floatValue()) %></h6></div>
                <div class="col-md-3"><h6 class="font-weight-bold">$ <%= f.format(th.floatValue()) %></h6></div>
            </div>
            <%} else {%>
            <div class="row px-2 py-2 text-center alert-danger">
                <div class="col-md-6"><h6 class="font-weight-bold text-justify">Total debe/haber</h6></div>
                <div class="col-md-3"><h6 class="font-weight-bold">$ <%= f.format(td.floatValue()) %></h6></div>
                <div class="col-md-3"><h6 class="font-weight-bold">$ <%= f.format(th.floatValue()) %></h6></div>
            </div>
            <%}%>
        </div>
    </div>
</div>
<%}

    for (Partida p : list) {
        for (Debehaber dh : lc.ordenarDH(p.getDebehabers())) {
            if (dh.getTipotransaccion().equals('c')) {
                tdebe = tdebe.add(new BigDecimal(Float.parseFloat(String.valueOf(dh.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
            } else if (dh.getTipotransaccion().equals('a')) {
                thaber = thaber.add(new BigDecimal(Float.parseFloat(String.valueOf(dh.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
            }
        }
    }

%>
<div class="col-md-12">
    <div class="sticky-scroll-box bg-light py-2">
        <div class="row">
            <div class="col-md-6 font-weight-bold px-4">
                Totales
            </div>
            <%if (tdebe.equals(thaber)) {%>
            <div class="col-md-3 text-center text-primary font-weight-bold">
                <span class="fa fa-check"></span> $ <%= f.format(tdebe.floatValue()) %>
            </div>
            <div class="col-md-3 text-center text-primary font-weight-bold">
                <span class="fa fa-check"></span> $ <%= f.format(thaber.floatValue()) %>
            </div>
            <%} else {%>
            <div class="col-md-3 text-center text-danger font-weight-bold">
                <span class="fa fa-remove"></span> $ <%= f.format(tdebe.floatValue()) %>
            </div>
            <div class="col-md-3 text-center text-danger font-weight-bold">
                <span class="fa fa-remove"></span> $ <%= f.format(thaber.floatValue()) %>
            </div>
            <%}%>
        </div>
    </div>
</div>
<%} else {%>
<div class="col-md-12 py-4">
    <div class="sticky-scroll-box alert-warning py-3">
        <div class="col-md-12 text-center">
            <span class="fa fa-warning text-warning" style="font-size: 28px"></span> 
            <h5 class="font-weight-bold text-warning">La(s) busqueda(s)  especificada(s) no corresponde a ninguna de las partidas registradas.</h5>
        </div>
    </div>
</div>
<%}%>
<div class="col-md-12 py-3"></div>
<div class="col-md-12">
    <%
        if (list.size() > 1) {

            if (Integer.parseInt(actual) > list.size()) {
    %>
    <strong class="text-muted">Mostrando desde la Partida #<%=(Integer.parseInt(anterior) + 1)%> hasta la #<%=list.size()%>, de un total de <%=list.size()%></strong>
    <%} else {%>
    <strong class="text-muted">Mostrando desde la Partida #<%=(Integer.parseInt(anterior) + 1)%> hasta la #<%=actual%>, de un total de <%=list.size()%></strong>
    <%}
    } else {%>
    <strong class="text-muted">Mostrando desde la Partida #1 hasta la #1, de un total de <%=list.size()%></strong>
    <%}%>
</div>
<%} else {%>
<div class="col-md-12 py-4">
    <div class="sticky-scroll-box alert-warning py-3">
        <div class="col-md-12 text-center">
            <span class="fa fa-warning text-warning" style="font-size: 28px"></span> 
            <h5 class="font-weight-bold text-warning">La(s) busqueda(s)  especificada(s) no corresponde a ninguna de las partidas registradas.</h5>
        </div>
    </div>
    <%if (list.size() == 0)%><br>
    <strong class="text-muted">Mostrando desde la Partida #0 hasta la #0, de un total de <%=list.size()%></strong>
    <%%>
</div>
<%}%>
<%} else {%>
<div class="col-md-12 py-4">
    <div class="sticky-scroll-box alert-danger py-3">
        <div class="col-md-12 text-center">
            <span class="fa fa-info-circle text-danger" style="font-size: 28px"></span> 
            <h5 class="font-weight-bold text-danger">Aún no hay partidas registradas correspondientes al periodo actual.</h5>
        </div>
    </div>
</div>
<%}%>
