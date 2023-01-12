<%-- 
    Document   : cierre
    Created on : 4/11/2019, 09:26:36 AM
    Author     : Xom
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="entidades.Debehaber"%>
<%@page import="entidades.Partida"%>
<%@page import="Procesos.Cierre"%>
<%-- 
    Document   : registrosperiodos
    Created on : 2/11/2019, 05:34:55 PM
    Author     : Xom
--%>
<%@page import="entidades.Periodo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entidades.Inventario"%>
<%@page import="logica.logicaContables"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Cierre del Ejercicio</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">

        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <!--script de sweetalert para los mensajes de registros de los datos-->
        <script src="sweetalert-master/docs/assets/sweetalert/sweetalert.min.js" type="text/javascript"></script>
    </head>
    <body>
        <!--diseño del navbar-->
        <%@include file="navbar.jsp" %>
        <%if (!ValidaPeriodo.encurso() || !ValidaPeriodo.terminado()) {%>
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
                        <%
                            logicaContables lc = new logicaContables();
                            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                            simbolo.setDecimalSeparator('.');
                            simbolo.setGroupingSeparator(',');
                            DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
                            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
                            Periodo actual = lc.PeriodoenCurso();
                            Inventario i = lc.recuperaInventario("Inventario Final", actual.getIdperiodo());
                            String inf = "0.00";
                            if (i != null) {
                                inf = lc.recuperaInventario("Inventario Final", actual.getIdperiodo()).getValor();
                            } else {
                                inf = "0.00";
                            }
                            if (i != null) {
                        %>
                        <br>
                        <div class="container">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Cierre del Ejercicio</h1>
                            </div>
                        </div>
                        <br>
                        <div class="container px-3">
                            <div class="row px-5">
                                <div class="text-center col-md-12 py-2 alert-info">
                                    <h4 class="font-weight-bold">Partidas de ajuste y de cierre del ejercicio</h4>
                                </div>
                            </div>
                        </div>
                        <div class="container">
                            <div class="row px-5 py-3" id="partidasdecierre">

                                <%
                                    BigDecimal tdebe = new BigDecimal("0.00");
                                    BigDecimal thaber = new BigDecimal("0.00");
                                    Cierre c = new Cierre();
                                    c.generarPartidasCierreAjuste();
                                    ArrayList<Partida> listpartperiodo = c.getLista();
                                    if (listpartperiodo.size() > 0) {
                                        for (Partida p : listpartperiodo) {


                                %>
                                <div class="col-md-12 py-3">
                                    <div class="sticky-scroll-box bg-light">
                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="col-md-12 alert-success text-center py-3">
                                                    <h4 class="font-weight-bold text-success">Partida #<%= p.getNumpartida()%></h4>
                                                </div>
                                                <div class="col-md-5 alert-success text-right py-3" style="display: none">
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
                                                <h6><%= sdf.format(p.getFecha()) %></h6>
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
                                                            tdebe = tdebe.add(new BigDecimal(Float.parseFloat(String.valueOf(dh.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
                                                    %>
                                                <div class="col-md-3"><h6>$ <%= f.format(dh.getMontopartida().floatValue()) %></h6></div>
                                                <div class="col-md-3"><h6><!--$ 00.00--></h6></div>
                                                <% } else if (dh.getTipotransaccion().equals('a')) {
                                                    th = th.add(new BigDecimal(Float.parseFloat(String.valueOf(dh.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
                                                    thaber = thaber.add(new BigDecimal(Float.parseFloat(String.valueOf(dh.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
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
                                <%
                                        }
                                    }
                                %>
                                <div class="col-md-12">
                                    <div class="sticky-scroll-box bg-light py-2">
                                        <div class="row">
                                            <div class="col-md-6 font-weight-bold px-4">
                                                Totales
                                            </div>
                                            <%
                                                if (tdebe.equals(thaber)) {

                                            %>
                                            <div class="col-md-3 text-center text-primary font-weight-bold">
                                                <span class="fa fa-check"></span> $ <%= f.format(tdebe.floatValue()) %>
                                            </div>
                                            <div class="col-md-3 text-center text-primary font-weight-bold">
                                                <span class="fa fa-check"></span> $ <%= f.format(thaber.floatValue()) %>
                                            </div>
                                            <%
                                            } else {

                                            %>
                                            <div class="col-md-3 text-center text-danger font-weight-bold">
                                                <span class="fa fa-remove"></span> $ <%= f.format(tdebe.floatValue()) %>
                                            </div>
                                            <div class="col-md-3 text-center text-danger font-weight-bold">
                                                <span class="fa fa-remove"></span> $ <%= f.format(thaber.floatValue()) %>
                                            </div>
                                            <%
                                                }
                                            %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br><br>
                        <%
                        } else {

                        %>    
                        <br>
                        <div class ="container-fluid">
                            <div class="text-center alert-warning py-2 font-weight-bold">
                                <h5 class="font-weight-bold text-warning"><span class="fa fa-info-circle"></span> Para poder realizar el <strong>Cierre</strong> debe asignar el inventario final de mercadería</h5>
                            </div>
                        </div>
                        <%                    }
                        %><br>
                    </div>
                </div>
            </div>
        </div>
        <%}
        %>
        <!--footer exportado-->
        <%@include file="footer.jsp" %>
    </body>
</html>


