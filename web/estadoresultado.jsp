<%-- 
    Document   : estadoresultado
    Created on : 10/08/2019, 10:52:53 AM
    Author     : Xom
--%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entidades.Partida"%>
<%@page import="entidades.Inventario"%>
<%@page import="Procesos.EstadodeResultado"%>
<%@page import="entidades.Periodo"%>
<%@page import="logica.logicaContables"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Estado de resultados</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">

        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <!--script de sweetalert para los mensajes de registros de los datos-->
        <script src="sweetalert-master/docs/assets/sweetalert/sweetalert.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            function fullvalidacion() {
                var inventario = document.getElementById("inf").value;
                if (inventario !== '') {
                    if (parseFloat(inventario) > 0) {
                        document.inventario.submit();
                    } else {
                        swal({
                            title: "Monto incorrecto",
                            text: "El monto debe ser mayora cero",
                            icon: 'warning'
                        });
                    }
                } else {
                    swal({
                        title: "Advertencia",
                        text: "Asigne un valor al inventario",
                        icon: 'warning'
                    });
                }
            }
        </script>
    </head>
    <body>
        <!--diseño del navbar-->
        <%@include file="navbar.jsp" %>
        <%if (!ValidaPeriodo.encurso()) {%>
        <script type="text/javascript">
            window.location = "inicioperiodo.jsp";
        </script>
        <%} else {%>
        <!--diseño del contenedor de los elementos-->
        <br><br><br>
        <div class="container">
            <div class="card-box">
                <%@include file="menuvertical.jsp" %>
                <!--configuraciones del menu-->
                <div class="tab-content" id="myTabContent">
                    <%
                        logicaContables lc = new logicaContables();
                        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                        simbolo.setDecimalSeparator('.');
                        simbolo.setGroupingSeparator(',');
                        DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
                        Periodo actual = lc.PeriodoenCurso();
                        ArrayList<Partida> lveri = (ArrayList<Partida>) lc.listapartidasperiodo("", "");
                        if (lveri.size() > 0) {

                            if (actual != null) {
                                Inventario i = lc.recuperaInventario("Inventario Final", actual.getIdperiodo());
                                String inf = "0.00";
                                if (i != null) {
                                    inf = lc.recuperaInventario("Inventario Final", actual.getIdperiodo()).getValor();
                                } else {
                                    inf = "0.00";
                                }
                    %>
                    <div class="tab-pane fade show active bg-white py-2">
                        <br>
                        <div class="row px-5" style="margin-left: -32px !important;margin-right:  -32px !important">
                            <div class="col-md-12 bg-light">
                                <div class="text-center text-white bg-secondary py-2 font-weight-bold" style="margin-left: -15px !important;margin-right:  -15px !important">
                                    <h1 class="font-weight-bold">Inventario Final de Mercadería</h1>
                                </div>
                                <br>
                                <div class="container text-center">
                                    <form action="CtrInventario" method="post" name="inventario" id="inventario">
                                        <input type="hidden" name="idperiodo" id="idperiodo" value="<%= (i != null) ? i.getPeriodo().getIdperiodo() : actual.getIdperiodo()%>">
                                        <div class="row px-5">
                                            <div class="col-md-1"></div>
                                            <div class="col-md-8">
                                                <div class="form-group">
                                                    <div class="input-group mb-3"> 
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addo1"><i class="fa fa-info-circle"></i></span>
                                                        </div>
                                                        <%if (actual.getTerminado() == false) {%>
                                                        <input type="text" id="inf" name="inf" class="form-control" value="<%= (i != null) ? i.getValor() : ""%>" placeholder="Ingrese el valor del inventario final">
                                                        <%} else {%>
                                                        <input readonly type="text" id="inf" name="inf" class="form-control" value="<%= (i != null) ? i.getValor() : ""%>" placeholder="Ingrese el valor del inventario final">
                                                        <%}%>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 text-left">
                                                <%if (actual.getTerminado() == false) {
                                                        if (i == null) {%>
                                                <button type="button" onclick="fullvalidacion()" class="btn btn-outline-success" id="save"><span class="font-weight-bold"><li class="fa fa-save"></li></span> Registrar</button>
                                                            <%} else {%>
                                                <button type="button" onclick="fullvalidacion()" class="btn btn-outline-info" id="editar"><span class="font-weight-bold"><li class="fa fa-pencil"></li></span> Modificar</button>
                                                            <%}%>
                                                            <%} else {%>
                                                <div class=" py-2 bg-info text-white text-center font-weight-bold">Inventario no Modificable</div>
                                                <%}%>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <br>
                        <%
                            EstadodeResultado er = new EstadodeResultado(inf);
                            if (er.getInventariofinal().doubleValue() <= 0) {
                        %>
                        <div class="col-md-12 py-4 px-5">
                            <div class="sticky-scroll-box alert-warning py-3">
                                <div class="col-md-12 text-center">
                                    <span class="fa fa-info-circle text-warning" style="font-size: 28px"></span> 
                                    <h5 class="font-weight-bold text-warning">Ingrese el inventario de mercadería final, para visualizar el estado de resultados</h5>
                                </div>
                            </div>
                        </div>
                        <%} else {%>
                        <div class="container">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Estado de Resultados</h1>
                            </div>
                        </div>
                        <br>
                        <div class="container">
                            <div class="row px-5">
                                <!--parte donde se gerara el estado de resultado-->
                                <div class="col-lg-12 pb-4">
                                    <div class="bg-white  pt-2 pb-2 px-3 py-3">
                                        <table class="table table-sm table-hover">
                                            <tbody>
                                                <tr>
                                                    <td class="alert-info" style="border: none"></td>
                                                    <td class="alert-info" style="border: none">VENTAS</td>
                                                    <td class="alert-info" style="border: none"></td>
                                                    <td class="alert-info" style="border: none">$ <%= f.format(er.getVentas().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>REBAJAS SOBRE VENTAS</td>
                                                     <td></td>
                                                    <td class="">$ <%= f.format(er.getRebajassobreventas().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>DEVOLUCIONES SOBRE VENTAS</td>
                                                    <td></td>
                                                    <td class="">$ <%= f.format(er.getDevolucionessobreventas().floatValue()) %></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>VENTAS NETAS</td>
                                                    <td></td>
                                                    <td class="">$ <%= f.format(er.ventasNetas().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>COSTO DE VENTAS</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.costoVentas().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-info"></td>
                                                    <td class="alert-info">COMPRAS</td>
                                                    <td class="alert-info">$ <%= f.format(er.getCompras().floatValue()) %></td>
                                                    <td class="alert-info"></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-danger">(+)</td>
                                                    <td>GASTOS SOBRE COMPRAS</td>
                                                    <td>$ <%= f.format(er.getGastosobrecompras().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>COMPRAS TOTALES</td>
                                                    <td>$ <%= f.format(er.comprasTotales().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>REBAJAS SOBRE COMPRAS</td>
                                                    <td>$ <%= f.format(er.getRebajassobrecompras().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>DEVOLUCIONES SOBRE COMPRAS</td>
                                                    <td>$ <%= f.format(er.getDevolucionessobrecompras().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>COMPRAS NETAS</td>
                                                    <td>$ <%= f.format(er.comprasNetas().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-danger">(+)</td>
                                                    <td>INVENTARIO INICIAL</td>
                                                    <td>$ <%= f.format(er.getInventarioinicial().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>MERCADERIA DISPONIBLE</td>
                                                    <td>$ <%= f.format(er.mercaderiaDisponible().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>INVENTARIO FINAL</td>
                                                    <td>$ <%= f.format(er.getInventariofinal().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>COSTO DE VENTA</td>
                                                    <td>$ <%= f.format(er.costoVentas().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>UTILIDAD BRUTA</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.utilidadBruta().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>GASTOS DE OPERACION</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.gastosOperacion().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td></td>
                                                    <td class="alert-info">GASTOS DE ADMINISTRACION</td>
                                                    <td class="alert-info">$ <%= f.format(er.getGastosadmon().floatValue()) %></td>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <td style="border: none"></td>
                                                    <td class="alert-info">GASTOS DE VENTA</td>
                                                    <td class="alert-info">$ <%= f.format(er.getGastosventa().floatValue()) %></td>
                                                    <td style="border: none"></td>
                                                </tr>
                                                <tr>
                                                    <td style="border: none"></td>
                                                    <td class="alert-info">GASTOS FINANCIEROS</td>
                                                    <td class="alert-info">$ <%= f.format(er.getGastosfinancieros().floatValue()) %></td>
                                                    <td style="border: none"></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>UTILIDAD DE OPERACION</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.utilidadOperacion().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>OTROS GASTOS</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.getOtrosgastos().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-danger">(+)</td>
                                                    <td>OTROS INGRESOS</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.getOtrosingresos().floatValue()) %></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>UTILIDAD ANTES DE IMPUESTO Y RESERVAS</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.utilidadAntesImpuestoReservas().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>RESERVA LEGAR 7%</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.reservaLegal().floatValue()) %></td>
                                                </tr>
                                                <tr class="alert-link">
                                                    <td>(=)</td>
                                                    <td>UTILIDAD ANTES DEL IMPUESTO</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.utilidadAntesdelImpuesto().floatValue()) %></td>
                                                </tr>
                                                <tr>
                                                    <td class="alert-warning">(-)</td>
                                                    <td>IMPUESTO SOBRE LA RENTA</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.impuestoSobreLaRenta().floatValue()) %></td>
                                                </tr>
                                                <tr class="alert-success">
                                                    <td>(=)</td>
                                                    <td>UTILIDAD DEL EJERCICIO</td>
                                                    <td></td>
                                                    <td>$ <%= f.format(er.utilidadDelEjercicio().floatValue()) %></td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="col-md-12 text-right">
                                        <a href="EstadoResultado" class="btn btn-info" target="blank"><span class="fa fa-print"></span> Imprimir</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%}
                            }
                        } else {%>
                        <div class="col-md-12 py-4 px-5">
                            <div class="sticky-scroll-box alert-danger py-3">
                                <div class="col-md-12 text-center">
                                    <span class="fa fa-info-circle text-danger" style="font-size: 28px"></span> 
                                    <h5 class="font-weight-bold text-danger">Aún no hay partidas registradas correspondientes al periodo actual.</h5>
                                </div>
                            </div>
                        </div>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
        <!--footer exportado-->
        <%@include file="footer.jsp" %>
    </body>
</html>
