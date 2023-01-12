<%-- 
    Document   : menuvertical
    Created on : 22/09/2019, 11:19:40 AM
    Author     : Xom
--%>
<%@page import="Procesos.ValidaPeriodo"%>
<ul class="nav nav-tabs tabs-bordered" id="myTab" role="tablist">
    <li class="nav-item">
        <a class="nav-link" id="inicioperiodo-tab" href="inicioperiodo.jsp" role="tab" aria-controls="inicioperiodo" aria-selected="true"><span class="font-weight-bold text-info fa fa-cogs mr-1 py-1"></span> Periodo</a>
    </li>
    <%if (ValidaPeriodo.encurso()) {%>
    <li class="nav-item">
        <a class="nav-link" id="registrarpartida-tab" href="registrarpartidas.jsp" role="tab" aria-controls="registrarpartida" aria-selected="true"><span class="font-weight-bold text-info fa fa-edit mr-1 py-1"></span> Partidas</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="librodiario-tab" href="librodiario.jsp" role="tab" aria-controls="librodiario" aria-selected="false"><span class="font-weight-bold text-info fa fa-list mr-1 py-1"></span> Libro Diario</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="libromayor-tab" href="libromayor.jsp" role="tab" aria-controls="libromayor" aria-selected="false"><span class="font-weight-bold text-info fa fa-list-alt mr-1 py-1"></span> Libro Mayor</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="balanza-tab" href="balanzacomprobacion.jsp" role="tab" aria-controls="balanza" aria-selected="false"><span class="font-weight-bold text-info fa fa-money mr-1 py-1"></span> Balanza</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="estadoresultado-tab" href="estadoresultado.jsp" role="tab" aria-controls="estadoresultado" aria-selected="false"><span class="font-weight-bold text-info fa fa-retweet mr-1 py-1"></span> Estado de Resultado</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="balancegeneral-tab"  href="balancegeneral.jsp" role="tab" aria-controls="balancegeneral" aria-selected="false"><span class="font-weight-bold text-info fa fa-balance-scale mr-1 py-1"></span> Balance General</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="cierre-tab" href="#" data-toggle="modal" data-target="#generarcierre" role="tab" aria-controls="cierre" aria-selected="false"><span class="font-weight-bold text-info fa fa-remove mr-1 py-1"></span> Cierre Contable</a>
    </li>
    <%}%>
</ul>

<div class="modal fade" id="generarcierre" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLongTitle"><strong>Realizar Cierre</strong></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body text-justify">
                <span class="fa fa-check-square"></span> Asegurese de haber ingresado el inventario final de mercadería.<br><br>
                <span class="fa fa-check-square"></span> Asegurese de haber creado la carpeta <strong>Respaldo Contabilidad</strong> en el disco local <strong>C</strong>.<br><br>
                <span class="fa fa-check-square"></span> Al realizar el cierre ya no se podran revertir los cambios, por lo cual se recomienda hacer un back up de la información actual que se almacenara en la carpeta <strong>Respaldo Contabilidad</strong> en el disco local <strong>C</strong>.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Cancelar</button>
                <a type="button" href="cierre.jsp" class="btn btn-info">Finalizar Periodo</a>
            </div>
        </div>
    </div>
</div>