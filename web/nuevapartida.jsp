<%-- 
    Document   : nuevapartida
    Created on : 10/08/2019, 10:52:53 AM
    Author     : Xom
--%>
<%@page import="logica.logicaContables"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
    logicaContables lc = new logicaContables();
    //String c = request.getParameter("num-partida");
    int i = lc.listapartidasperiodo("", "").size();
    if (lc.listapartidasperiodo("", "") != null) {
        try {
            if (i == 0) {
                i = 1;
            } else {
                i++;
            }
        } catch (Exception e) {
            return;
        }
    } else {
        i = 0;
        return;
    }
%>
<%
    String opcion = request.getParameter("opcion");
    if (opcion == null) {
        return;
    }
    if (opcion.equals("nuevapartida")) {
%>

<div class="bg-white" data-num-partida="<%=i%>" data-type="partida">
    <div class="row text-center bg-light py-2">
        <div class="col-md-12">
            <h4 class="font-weight-bold" style="color: red">Partida # <span data-type="numeropartida"><%=i%></span></h4>
        </div>
        <div class="col-md-4 font-weight-bold">
            Concepto
        </div>
        <div class="col-md-4 font-weight-bold">
            Debe
        </div>
        <div class="col-md-4 font-weight-bold">
            Haber
        </div>
    </div>
    <div class="row px-1">
        <!--cargos y abonos con las cuentas-->
        <div class="col-md-12 text-center alert alert-danger" id="mensaje-error" hidden role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">×</span>
            </button>
            <strong>Error!</strong> Las cuentas de cierre solo realizan operaciones al finalizar el ejercico
        </div>
        <div class="col-md-12 text-center font-weight-bold" data-type="lista-ca">
            <div class="row" data-type="ca">
                <div class="col-md-4">
                    <br>
                    <span class="badge badge-success" id="icono-mensaje" hidden><li class="fa fa-check"></li></span>
                    <a href="#" data-type="quitar" class="btn btn-link text-danger font-weight-bold"><span class="fa fa-trash"></span></a>
                    <a href="#" data-codigo="" class="text-muted font-weight-bold" style="text-decoration: none !important">Seleccione cuenta</a>
                </div>
                <div class="col-md-4">
                    <br>
                    <input type="text" class="form-control" name="cargo" id="cargo" onkeypress="return saldo(event, this);" placeholder="00.00">
                </div>
                <div class="col-md-4">
                    <br>
                    <input type="text" class="form-control" name="abono" id="abono" onkeypress="return saldo(event, this);" placeholder="00.00">
                </div>
            </div>
            <div class="row" data-type="ca">
                <div class="col-md-4">
                    <br>
                    <span class="badge badge-danger" id="icono-mensaje" hidden><li class="fa fa-remove"></li></span>
                    <a href="#" data-type="quitar" class="btn btn-link text-danger font-weight-bold"><span class="fa fa-trash"></span></a>
                    <a href="#" data-codigo="" class="text-muted font-weight-bold" style="text-decoration: none !important">Seleccione cuenta</a>
                </div>
                <div class="col-md-4">
                    <br>
                    <input type="text" class="form-control" name="cargo" id="cargo" onkeypress="return saldo(event, this);" placeholder="00.00">
                </div>
                <div class="col-md-4">
                    <br>
                    <input type="text" class="form-control" name="abono" id="abono" onkeypress="return saldo(event, this);" placeholder="00.00">
                </div>
            </div>
        </div>
    </div>
    <br>
    <div class="row mb-1 px-1 bg-light" data-totales="totales">
        <div class="col-md-2 text-center font-weight-bold">
            <a data-type="btn-agregar" href="#" class="btn btn-outline-success"><span class="fa fa-plus"></span> Cuentas</a>
        </div>
        <div class="col-md-3 text-center font-weight-bold">
            <label class="form-group">Total debe/haber</label>
        </div>
        <div class="col-md-3 text-center font-weight-bold" data-debe="topdebe">
            $ 00.00
        </div>
        <div class="col-md-3 text-right font-weight-bold" data-haber="tophaber">
            $ 00.00
        </div>
    </div>
    <br>
    <div class="row text-center px-2">
        <div class="col-md-1 text-center font-weight-bold">Fecha</div>
        <div class="col-md-3"><input type="date" class="form-control" name="fecharegistro" id="fecharegistro" value="<%= LocalDate.now()%>"></div>
        <div class="col-md-8">
            <div class="row font-weight-bold text-center">
                <div class="col-md-3">
                    <label class="form-group">Descripción de la Transacción</label>
                </div>
                <div class="col-9">
                    <textarea class="form-control" type="text" name="transaccion" id="transaccion"></textarea>
                </div>
            </div>
        </div>
    </div>
    <br>
</div>
<%} else if (opcion.equals("nuevodebehaber")) {%>
<div class="row" data-type="ca">
    <div class="col-md-4">
        <br>
        <span class="badge badge-success" id="icono-mensaje" hidden><li class="fa fa-check"></li></span>
        <a href="#" data-type="quitar" class="btn btn-link text-danger font-weight-bold"><span class="fa fa-trash"></span></a>
        <a href="#" data-codigo="" class="text-muted font-weight-bold" style="text-decoration: none !important">Seleccione cuenta</a>
    </div>
    <div class="col-md-4">
        <br>
        <input type="text" class="form-control" name="cargo" id="cargo" onkeypress="return saldo(event, this);" placeholder="00.00">
    </div>
    <div class="col-md-4">
        <br>
        <input type="text" class="form-control" name="abono" id="abono" onkeypress="return saldo(event, this);" placeholder="00.00">
    </div>
</div>
<%}%>