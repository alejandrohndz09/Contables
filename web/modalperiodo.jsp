<%-- 
    Document   : modalperiodo
    Created on : 3/11/2019, 08:30:10 AM
    Author     : Xom
--%>

<%@page import="java.math.RoundingMode"%>
<%@page import="Procesos.Mayor"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Procesos.EstadodeResultado"%>
<%@page import="entidades.Inventario"%>
<%@page import="entidades.Periodo"%>
<%@page import="logica.logicaContables"%>
<%
    String idperiodo = request.getParameter("idperiodo");
    logicaContables lc = new logicaContables();
    Periodo p = lc.buscaPeriodo(Integer.parseInt(idperiodo));
    Mayor m,mayor,aux;
    Inventario i = lc.recuperaInventario("Inventario Final", p.getIdperiodo());
    SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-YYYY");

%>

<div class="row px-5 py-5">
    <div class="col-md-6"><strong><span class="text-muted">Fecha de Inicio</span></strong></div>
    <div class="col-md-6 text-right"><strong><span class="text-primary"><%= sdf.format(p.getFechainicial()) %></span></strong></div>
    <div class="col-md-6"><strong><span class="text-muted">Fecha de Finalización</span></strong></div>
    <div class="col-md-6 text-right"><strong><span class="text-primary"><%= sdf.format(p.getFechafinal()) %></span></strong></div>

    <%
        if (i != null) {
            EstadodeResultado er = new EstadodeResultado(i.getValor());
            if (er.getInventariofinal().floatValue() > 0) {
                m=lc.mayorizarCuenta("210402");
                m.liquidacion();
                mayor=lc.mayorizarCuenta("310201");
                mayor.liquidacion();
                aux=lc.mayorizarCuenta("310302");
                aux.liquidacion();
                BigDecimal isr=m.getSaldoAcreedor();
                BigDecimal rl=mayor.getSaldoAcreedor();
                BigDecimal ue=aux.getSaldoAcreedor();
    %>

    <div class="col-md-6"><strong><span class="text-muted">Inventario Final</span></strong></div>
    <div class="col-md-6 text-right"><strong><span class="text-primary"><div class="row"><div class="col-md-4 text-center">$</div><div class="col-md-8"><%= er.getInventariofinal()%></div></div></span></strong></div>
    <hr>
    <div class="col-md-6"><strong><span class="text-muted">Impuesto Sobre la Renta</span></strong></div>
    <div class="col-md-6 text-right"><strong><span class="text-success"><div class="row"><div class="col-md-4 text-center">$</div><div class="col-md-8"><%= isr.setScale(2, RoundingMode.HALF_UP) %></div></div></span></strong></div>
    <div class="col-md-6"><strong><span class="text-muted">Reserva Legal</span></strong></div>
    <div class="col-md-6 text-right"><strong><span class="text-success"><div class="row"><div class="col-md-4 text-center">$</div><div class="col-md-8"><%= rl.setScale(2, RoundingMode.HALF_UP) %></div></div></span></strong></div>
    <div class="col-md-6"><strong><span class="text-muted">Utilidad del Ejercicio</span></strong></div>
    <div class="col-md-6 text-right"><strong><span class="text-success"><div class="row"><div class="col-md-4 text-center">$</div><div class="col-md-8"><%= ue.setScale(2, RoundingMode.HALF_UP) %></div></div></span></strong></div>

    <%}
    } else {%>

    <div class="font-weight-bold py-3 text-justify">
        <div class="alert-info px-5 py-5">No hay inventario de mercadería final registrado. Si desea ver el valor del ISR, Reserva Legal y la Utilidad del ejercicio, valla al estado de resultado y asigne un valor al inventario final.</div>  
    </div>

    <%}%>
</div>
