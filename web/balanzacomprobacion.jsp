<%-- 
    Document   : balanzacomprobacion
    Created on : 10/08/2019, 10:52:53 AM
    Author     : Xom
--%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="entidades.Periodo"%>
<%@page import="entidades.Partida"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Procesos.Mayor"%>
<%@page import="logica.logicaContables"%>
<%@page import="java.math.BigDecimal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Balanza de sumas y saldos</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">

        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
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
                    <div class="tab-pane fade show active bg-white py-2">
                        <br>
                        <div class ="container">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Balanza de comprobación</h1>
                            </div>
                        </div>
                        <br>
                        <!--BALANZA ANTES DEL CIERRE DEL EJERCICIO-->
                        <%
                            logicaContables lc = new logicaContables();
                            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                            simbolo.setDecimalSeparator('.');
                            simbolo.setGroupingSeparator(',');
                            DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
                            ArrayList<Partida> lveri = (ArrayList<Partida>) lc.listapartidasperiodo("", "");
                            if (lveri.size() > 0) {
                        %>
                        <div class="container">
                            <div class ="row bg-white pt-2 pb-4 py-3 px-5">
                                <div class="col-lg-12">
                                    <%
                                        /*acumuladores respectivos de cada cuenta, se utilizaran
                                        para cuadrar los movimientos y saldos de todas las cuentas*/
                                        BigDecimal tdebe = new BigDecimal("0.00");
                                        BigDecimal thaber = new BigDecimal("0.00");
                                        BigDecimal tdeudor = new BigDecimal("0.00");
                                        BigDecimal tacreeedor = new BigDecimal("0.00");
                                        int i = 1;//contador
                                        ArrayList<Mayor> balanza = (ArrayList<Mayor>) lc.listadoMayor(4);/*obtenemos las cuentas que se han utilizado
                                        para las transacciones, especificamente las de nivel 4 ya que son las que poseen saldos*/
                                    %>
                                    <table class="table table-sm text-center">
                                        <thead>
                                            <tr class="alert-info font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
                                                <th rowspan="2">N&deg;</th>
                                                <th rowspan="2">CÓDIGO</th>
                                                <th colspan="3" rowspan="2">CUENTAS</th>
                                                <th colspan="2" class="alert-warning font-weight-bold">MOVIMIENTOS</th>
                                                <th colspan="2" class="alert-warning font-weight-bold">SALDOS</th>

                                            </tr>
                                            <tr class="alert-info font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
                                                <th>Debe</th>
                                                <th>Haber</th>
                                                <th>Deudor</th>
                                                <th>Acreedor</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                for (Mayor b : balanza) {
                                                    b.issdsa();/*generación de los saldos acumulados*/
                                                    if (b.getSaldoAcreedor().floatValue() > 0 || b.getSaldoDeudor().floatValue() > 0) {
                                                        if (b.getDh().size() > 0) {
                                            %>
                                            <tr>
                                                <td style="border-right: 0px ;border-left:  1px solid #dee2e6;border-top: none !important"><%= i%></td>
                                                <td style="border-right: 0px ;border-left:  1px solid #dee2e6;border-top: none !important"><%= b.getCuentamayor().getCodigocuenta()%></td>
                                                <td colspan="3" style="text-align: justify;border-right: 1px solid #dee2e6;border-left:  0px;border-top: none !important"><%= b.getCuentamayor().getNombrecuenta()%></td>
                                                <td style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= (b.getDebe().floatValue() > 0) ? "$ " + f.format(b.getDebe().floatValue()) : ""%></td>
                                                <td class="bg-light alert-link" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= (b.getHaber().floatValue() > 0) ? "$ " + f.format(b.getHaber().floatValue()) : ""%></td>
                                                <td style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= (b.getSaldoDeudor().floatValue() > 0) ? "$ " + f.format(b.getSaldoDeudor().floatValue()) : ""%></td>
                                                <td class="bg-light alert-link" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= (b.getSaldoAcreedor().floatValue() > 0) ? "$ " + f.format(b.getSaldoAcreedor().floatValue()) : ""%></td>
                                            </tr>
                                            <%
                                                            tdebe = tdebe.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getDebe()))).setScale(2, RoundingMode.HALF_UP));
                                                            tdeudor = tdeudor.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP));
                                                            thaber = thaber.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getHaber()))).setScale(2, RoundingMode.HALF_UP));
                                                            tacreeedor = tacreeedor.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP));
                                                            i++;
                                                        }
                                                    }
                                                }
                                            %>
                                        </tbody>
                                        <%
                                            if ((tdebe.equals(thaber) && tdeudor.equals(tacreeedor)) && (tdebe.floatValue() > 0 && thaber.floatValue() > 0 && tdeudor.floatValue() > 0 && tacreeedor.floatValue() > 0)) {
                                        %>
                                        <tfoot class="alert-success font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
                                        <td colspan="5" style="text-align: left; padding-left: 55px!important;">TOTALES</td>
                                        <td>$ <%= f.format(tdebe.floatValue())%></td>
                                        <td>$ <%= f.format(thaber.floatValue())%></td>
                                        <td>$ <%= f.format(tdeudor.floatValue())%></td>
                                        <td>$ <%= f.format(tacreeedor.floatValue())%></td>
                                        </tfoot>
                                        <%
                                        } else {
                                        %>
                                        <tfoot class="alert-danger font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
                                        <td colspan="5" style="text-align: left; padding-left: 55px!important;">TOTALES</td>
                                        <td>$ <%= f.format(tdebe.floatValue())%></td>
                                        <td>$ <%= f.format(thaber.floatValue())%></td>
                                        <td>$ <%= f.format(tdeudor.floatValue())%></td>
                                        <td>$ <%= f.format(tacreeedor.floatValue())%></td>
                                        </tfoot>
                                        <%}%>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <!--BALANZA DESPUES DEL CIERRE DEL EJERCICIO-->
                        <%
                            Periodo p = lc.PeriodoenCurso();
                            if (p.getTerminado() == true) {
                        %>
                        <br>
                        <div class ="container">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Balanza de comprobación despues del cierre</h1>
                            </div>
                        </div>
                        <br>
                        <div class="container">
                            <div class ="row bg-white pt-2 pb-4 py-3 px-5">
                                <div class="col-lg-12">
                                    <%
                                        /*acumuladores respectivos de cada cuenta, se utilizaran
                                        para cuadrar los movimientos y saldos de todas las cuentas*/
                                        BigDecimal tdebef = new BigDecimal("0.00");
                                        BigDecimal thaberf = new BigDecimal("0.00");
                                        BigDecimal tdeudorf = new BigDecimal("0.00");
                                        BigDecimal tacreeedorf = new BigDecimal("0.00");
                                        int iff = 1;//contador
                                        ArrayList<Mayor> balanzaf = (ArrayList<Mayor>) lc.listadoMayor(4);/*obtenemos las cuentas que se han utilizado
                                        para las transacciones, especificamente las de nivel 4 ya que son las que poseen saldos*/
                                    %>
                                    <table class="table table-sm text-center">
                                        <thead>
                                            <tr class="alert-info font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
                                                <th rowspan="2">N&deg;</th>
                                                <th rowspan="2">CÓDIGO</th>
                                                <th colspan="3" rowspan="2">CUENTAS</th>
                                                <th colspan="2" class="alert-warning font-weight-bold">MOVIMIENTOS</th>
                                                <th colspan="2" class="alert-warning font-weight-bold">SALDOS</th>

                                            </tr>
                                            <tr class="alert-info font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
                                                <th>Debe</th>
                                                <th>Haber</th>
                                                <th>Deudor</th>
                                                <th>Acreedor</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                for (Mayor b : balanzaf) {
                                                    b.liquidacion();/*generación de los saldos liquidados*/
                                                    if (b.getSaldoAcreedor().floatValue() >= 0 || b.getSaldoDeudor().floatValue() >= 0) {
                                                        if (b.getDh().size() > 0) {
                                            %>
                                            <tr>
                                                <td style="border-right: 0px ;border-left:  1px solid #dee2e6;border-top: none !important"><%= iff%></td>
                                                <td style="border-right: 0px ;border-left:  1px solid #dee2e6;border-top: none !important"><%= b.getCuentamayor().getCodigocuenta()%></td>
                                                <td colspan="3" style="text-align: justify;border-right: 1px solid #dee2e6;border-left:  0px;border-top: none !important"><%= b.getCuentamayor().getNombrecuenta()%></td>
                                                <td style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= (b.getDebe().floatValue() > 0) ? "$ " + f.format(b.getDebe().floatValue()) : ""%></td>
                                                <td class="bg-light alert-link" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= (b.getHaber().floatValue() > 0) ? "$ " + f.format(b.getHaber().floatValue()) : ""%></td>
                                                <td style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= (b.getSaldoDeudor().floatValue() > 0) ? "$ " + f.format(b.getSaldoDeudor().floatValue()) : ""%></td>
                                                <td class="bg-light alert-link" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important"><%= (b.getSaldoAcreedor().floatValue() > 0) ? "$ " + f.format(b.getSaldoAcreedor().floatValue()) : ""%></td>
                                            </tr>
                                            <%
                                                            tdebef = tdebef.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getDebe()))).setScale(2, RoundingMode.HALF_UP));
                                                            tdeudorf = tdeudorf.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP));
                                                            thaberf = thaberf.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getHaber()))).setScale(2, RoundingMode.HALF_UP));
                                                            tacreeedorf = tacreeedorf.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP));
                                                            iff++;
                                                        }
                                                    }
                                                }
                                            %>
                                        </tbody>
                                        <%
                                            if ((tdebef.equals(thaberf) && tdeudorf.equals(tacreeedorf)) && (tdebef.floatValue() > 0 && thaberf.floatValue() > 0 && tdeudorf.floatValue() > 0 && tacreeedorf.floatValue() > 0)) {
                                        %>
                                        <tfoot class="alert-success font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
                                        <td colspan="5" style="text-align: left; padding-left: 55px!important;">TOTALES</td>
                                        <td>$ <%= f.format(tdebef.floatValue())%></td>
                                        <td>$ <%= f.format(thaberf.floatValue())%></td>
                                        <td>$ <%= f.format(tdeudorf.floatValue())%></td>
                                        <td>$ <%= f.format(tacreeedorf.floatValue())%></td>
                                        </tfoot>
                                        <%
                                        } else {
                                        %>
                                        <tfoot class="alert-danger font-weight-bold" style="border-right: 1px solid #dee2e6;border-left:  1px solid #dee2e6;border-top: none !important">
                                        <td colspan="5" style="text-align: left; padding-left: 55px!important;">TOTALES</td>
                                        <td>$ <%= f.format(tdebef.floatValue())%></td>
                                        <td>$ <%= f.format(thaberf.floatValue())%></td>
                                        <td>$ <%= f.format(tdeudorf.floatValue())%></td>
                                        <td>$ <%= f.format(tacreeedorf.floatValue())%></td>
                                        </tfoot>
                                        <%}%>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <%}%>
                        <div class="col-md-12 text-right px-5 mb-3">
                            <a href="BalanzaComprobacion?tipobalanza=antescierre" class="btn btn-info" target="blank"><span class="fa fa-print"></span> Imprimir</a>
                            <%if (p.getTerminado() == true) {%>
                            <a href="BalanzaComprobacion?tipobalanza=despuescierre" class="btn btn-success" target="blank"><span class="fa fa-print"></span> Imprimir</a>
                            <%}%>
                        </div>
                        <%} else {%>
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