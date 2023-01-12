<%-- 
    Document   : buscarcuenta
    Created on : 8/09/2019, 11:35:14 AM
    Author     : Xom
--%>

<%@page import="java.util.List"%>
<%@page import="entidades.Cuenta"%>
<%@page import="logica.logicaContables"%>
<%
    String opcionbuscar = request.getParameter("codcuenta");
    logicaContables lc = new logicaContables();
    List<Cuenta> lista = lc.nombrecodigo(opcionbuscar);
    if (!opcionbuscar.trim().isEmpty()) {
        if (lista.size() > 0) {
            for (Cuenta c : lista) {
%>
<div data-type="result" class="px-3">
    <div class="form-group">
        <div class="input-group mb-3"> 
            <div class="input-group-prepend" style="margin-right: -156px!important">
                <input type="text" readonly="readonly" id="codcuenta" value="<%= c.getCodigocuenta()%>" name="codcuenta" class="form-control text-center font-weight-bold" placeholder="Código Cuenta" style="width: 40%!important; background-color: #E9EFF8!important;border-color: #17a2b8!important;color: #17a2b8!important">
            </div>
            <input type="text" readonly="readonly" id="nombrecuenta" value="<%= c.getNombrecuenta()%>" name="nombrecuenta" class="form-control font-weight-bold" placeholder="Nombre Cuenta" style="background-color: white !important; border-color:#28a745 !important;color: #28a745!important; cursor: pointer; ">
        </div>
    </div>
</div>
<%}
} else {%>
<div class="px-4">
    <span class="text-danger fa fa-times-circle font-weight-bold" style="font-size: 50px"></span><br>
    <span class="form-group font-weight-bold text-danger"> Ningun registro encontrado</span>
</div><br>
<%}
} else {%>
<div class="px-3">
    <span class="text-info fa fa-book font-weight-bold" style="font-size: 30px;"></span><br>
    <span class="form-group font-weight-bold text-info"> Ingrese el nombre/código de las cuentas para realizar la transacción.</span>
</div><br>
<%}%>
