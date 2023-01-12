<%-- 
    Document   : balanzacomprobacion
    Created on : 10/08/2019, 10:52:53 AM
    Author     : Xom
--%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Procesos.Mayor"%>
<%@page import="Procesos.EstadodeResultado"%>
<%@page import="entidades.Inventario"%>
<%@page import="entidades.Periodo"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="logica.logicaContables"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Balance general</title>
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
        <div class="container-fluid">
            <div class="card-box">
                <div style="margin-left:16%;margin-right: 16% ">
                    <%@include file="menuvertical.jsp" %>
                </div>
                <!--configuraciones del menu-->
                <div class="tab-content" id="myTabContent" style="padding-left: 2%;padding-right: 2% ">
                    <div class="tab-pane fade show active bg-white py-2">
                        <%                            
                            logicaContables lc = new logicaContables();
                            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                            simbolo.setDecimalSeparator('.');
                            simbolo.setGroupingSeparator(',');
                            DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
                            Periodo actual = lc.PeriodoenCurso();
                            Inventario i = lc.recuperaInventario("Inventario Final", actual.getIdperiodo());
                            String inf = "0.00";
                            if (i != null) {
                                inf = lc.recuperaInventario("Inventario Final", actual.getIdperiodo()).getValor();
                            } else {
                                inf = "0.00";
                            }
                            /*acumuladores*/
                            BigDecimal totActivo = new BigDecimal("0.00");
                            BigDecimal totActivoC = new BigDecimal("0.00");
                            BigDecimal totActivoNC = new BigDecimal("0.00");
                            BigDecimal totPasivoC = new BigDecimal("0.00");
                            BigDecimal totPasivoNC = new BigDecimal("0.00");
                            BigDecimal totPatrimonio = new BigDecimal("0.00");
                            BigDecimal totPT = new BigDecimal("0.00");
                            EstadodeResultado er = new EstadodeResultado(inf);
                            /*cuentas de mayorizacion*/
                            ArrayList<Mayor> activoC = (ArrayList<Mayor>) lc.listadoMayorBG(4, "11");
                            ArrayList<Mayor> activoNC = (ArrayList<Mayor>) lc.listadoMayorBG(4, "12");
                            ArrayList<Mayor> pasivoC = (ArrayList<Mayor>) lc.listadoMayorBG(4, "21");
                            ArrayList<Mayor> pasivoNC = (ArrayList<Mayor>) lc.listadoMayorBG(4, "22");
                            ArrayList<Mayor> patrimonio = (ArrayList<Mayor>) lc.listadoMayorBG(4, "31");
                        %>
                        <br>
                        <div class ="container-fluid">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Balance General</h1>
                            </div>
                        </div>
                        <%if (er.getInventariofinal().floatValue() > 0) {%>
                        <br> 
                        <div class="container-fluid">
                            <div class ="row pt-2 pb-4 px-5 py-3">
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>A c t i v o</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>D e b e</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>H a b e r</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>P a s i v o</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>D e b e</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>H a b e r</strong>
                                </div>
                                <div class="col-md-12">
                                    <div class="row text-center">
                                        <div class="col-md-6" style="border: 1px solid #f3f3f3 !important;">
                                            <div class="row text-center">
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning">
                                                    Corriente
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning"></div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor mac : activoC) {
                                                            mac.issdsa();
                                                            if (mac.getSaldoDeudor().floatValue() != 0 && mac.getSaldoAcreedor().floatValue() == 0 && !mac.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {
                                                                totActivoC = totActivoC.add(new BigDecimal(String.valueOf(mac.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);
                                                            } else if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0 && !mac.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {
                                                                totActivoC = totActivoC.subtract(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            } else if (mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") || mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {/*estas se restaran del total debido a que su
                                                                comportamiento es como la cuenta de un pasivo se procede a restar el saldo (naturaleza del saldo= ACREEDOR)*/
                                                                totActivoC = totActivoC.subtract(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                        totActivoC = totActivoC.add(er.getInventariofinal()).setScale(2, RoundingMode.HALF_UP);
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-right">$</div><div class="col-md-9 text-right"><%= f.format(totActivoC.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%for (Mayor mac : activoC) {
                                                                if (mac.getSaldoDeudor().floatValue() != 0 && mac.getSaldoAcreedor().floatValue() == 0 && !mac.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA")) {
                                                        %>
                                                        <div class="col-md-4"><%= mac.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(mac.getSaldoDeudor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%} else if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0 && !mac.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA")) {
                                                        %>
                                                        <div class="col-md-4"><%= mac.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4 "><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(mac.getSaldoAcreedor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div><%
                                                                }
                                                            }%>
                                                        <div class="col-md-4">INVENTARIO FINAL</div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(er.getInventariofinal().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                    </div>
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning">
                                                    No Corriente
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning"></div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor manc : activoNC) {
                                                            manc.issdsa();
                                                            if (manc.getSaldoDeudor().floatValue() != 0 && manc.getSaldoAcreedor().floatValue() == 0 && !manc.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA") && !manc.getCuentamayor().getNombrecuenta().equals("DEPRECIACIÓN ACUMULADA")) {
                                                                totActivoNC = totActivoNC.add(new BigDecimal(String.valueOf(manc.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);
                                                            } else if (manc.getCuentamayor().getNombrecuenta().equals("DEPRECIACIÓN ACUMULADA")) {/*se restara de la cuenta propiedad planta y equipo debido a que su  naturaleza del saldo es Acreedor*/
                                                                totActivoNC = totActivoNC.subtract(new BigDecimal(String.valueOf(manc.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-right">$</div><div class="col-md-9 text-right"><%= f.format(totActivoNC.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%
                                                            for (Mayor mancc : activoNC) {
                                                                if (mancc.getSaldoDeudor().floatValue() != 0 && mancc.getSaldoAcreedor().floatValue() == 0 && !mancc.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA")) {
                                                        %>
                                                        <div class="col-md-4"><%= mancc.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(mancc.getSaldoDeudor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-6" style="border-right: 1px solid #f3f3f3 !important;">
                                            <div class="row text-center">
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning">
                                                    Corriente
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning"></div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor mac : pasivoC) {
                                                            mac.issdsa();
                                                            if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                                                                totPasivoC = totPasivoC.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                        totPasivoC = totPasivoC.add(er.impuestoSobreLaRenta()).setScale(2, RoundingMode.HALF_UP);
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPasivoC.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%for (Mayor pac : pasivoC) {
                                                                if (pac.getSaldoDeudor().floatValue() == 0 && pac.getSaldoAcreedor().floatValue() != 0) {

                                                        %>
                                                        <div class="col-md-4"><%= pac.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(pac.getSaldoAcreedor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%}
                                                            }
                                                        %>
                                                        <div class="col-md-4">IMPUESTO SOBRE LA RENTA</div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(er.impuestoSobreLaRenta().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                    </div>
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning">
                                                    No Corriente
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning"></div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor mac : pasivoNC) {
                                                            mac.issdsa();
                                                            if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                                                                totPasivoNC = totPasivoNC.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPasivoNC.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%for (Mayor panc : pasivoNC) {
                                                                if (panc.getSaldoDeudor().floatValue() == 0 && panc.getSaldoAcreedor().floatValue() != 0) {

                                                        %>
                                                        <div class="col-md-4"><%= panc.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(panc.getSaldoAcreedor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%}
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                                <div class="col-md-12 text-center alert-success py-3 font-weight-bold">
                                                    <div class="row">
                                                        <div class="col-md-8">
                                                            <strong class="text-success">Total Pasivo</strong>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <strong class="text-success"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= totPasivoC.add(totPasivoNC).setScale(2, RoundingMode.HALF_UP).toString()%></div></div></strong>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-8 font-weight-bold py-2 alert-info" style="font-size: 20px">
                                                    <strong>P a t r i m o n i o</strong>
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor mac : patrimonio) {
                                                            mac.issdsa();
                                                            if (mac.getSaldoDeudor().floatValue() != 0 || mac.getSaldoAcreedor().floatValue() != 0) {
                                                                totPatrimonio = totPatrimonio.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                        totPatrimonio = totPatrimonio.add(er.reservaLegal().add(er.utilidadDelEjercicio())).setScale(2, RoundingMode.HALF_UP);
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPatrimonio.floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%for (Mayor patri : patrimonio) {
                                                                if (patri.getSaldoDeudor().floatValue() != 0 || patri.getSaldoAcreedor().floatValue() != 0) {
                                                                    if (patri.getSaldoDeudor().floatValue() != 0 && patri.getSaldoAcreedor().floatValue() == 0) {

                                                        %>

                                                        <div class="col-md-4"><%= patri.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(patri.getSaldoDeudor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%} else if (patri.getSaldoDeudor().floatValue() == 0 && patri.getSaldoAcreedor().floatValue() != 0) {%>
                                                        <div class="col-md-4"><%= patri.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(patri.getSaldoAcreedor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%
                                                                    }
                                                                }
                                                            }
                                                        %>
                                                        <div class="col-md-4">RESERVA LEGAL</div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(er.reservaLegal().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <div class="col-md-4">UTILIDAD DEL EJERCICIO</div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%=  f.format(er.utilidadDelEjercicio().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%
                                    totActivo = totActivo.add(new BigDecimal(String.valueOf(totActivoC.add(new BigDecimal(String.valueOf(totActivoNC)).setScale(2, RoundingMode.HALF_UP)))).setScale(2, RoundingMode.HALF_UP));
                                    totPT = totPT.add(new BigDecimal(String.valueOf(totPatrimonio.add(new BigDecimal(String.valueOf(totPasivoC.add(new BigDecimal(String.valueOf(totPasivoNC))))))))).setScale(2, RoundingMode.HALF_UP);
                                %>
                                <%if (totActivo.equals(totPT)) {%>
                                <div class="col-md-6 text-center alert-success py-3 font-weight-bold">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <strong class="text-success">Total Activo</strong>
                                        </div>
                                        <div class="col-md-4">
                                            <strong class="text-success"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totActivo.floatValue())%></div></div></strong>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6 text-center alert-success py-3 font-weight-bold">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <strong class="text-success">Total Pasivo + Patrimonio</strong>
                                        </div>
                                        <div class="col-md-4">
                                            <strong class="text-success"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPT.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div></strong>
                                        </div>
                                    </div>
                                </div>
                                <%} else {%>
                                <div class="col-md-6 text-center alert-danger py-3 font-weight-bold">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <strong class="text-danger">Total Activo</strong>
                                        </div>
                                        <div class="col-md-4">
                                            <strong class="text-danger"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totActivo.floatValue())%></div></div></strong>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6 text-center alert-danger py-3 font-weight-bold">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <strong class="text-danger">Total Pasivo + Patrimonio</strong>
                                        </div>
                                        <div class="col-md-4">
                                            <strong class="text-danger"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPT.floatValue())%></div></div></strong>
                                        </div>
                                    </div>
                                </div>
                                <%}%>
                            </div>
                        </div>
                        <%
                            //INICIO DEL BALANCE GENERAL DESPUES DEL CIERRE
                            Periodo per = lc.PeriodoenCurso();
                            if (per.getTerminado() == true) {

                                /*acumuladores del balance general despues del cierre*/
                                BigDecimal totActivof = new BigDecimal("0.00");
                                BigDecimal totActivoCf = new BigDecimal("0.00");
                                BigDecimal totActivoNCf = new BigDecimal("0.00");
                                BigDecimal totPasivoCf = new BigDecimal("0.00");
                                BigDecimal totPasivoNCf = new BigDecimal("0.00");
                                BigDecimal totPatrimoniof = new BigDecimal("0.00");
                                BigDecimal totPTf = new BigDecimal("0.00");
                                /*cuentas de mayorizacion despues del cierre*/
                                ArrayList<Mayor> activoCf = (ArrayList<Mayor>) lc.listadoMayorBG(4, "11");
                                ArrayList<Mayor> activoNCf = (ArrayList<Mayor>) lc.listadoMayorBG(4, "12");
                                ArrayList<Mayor> pasivoCf = (ArrayList<Mayor>) lc.listadoMayorBG(4, "21");
                                ArrayList<Mayor> pasivoNCf = (ArrayList<Mayor>) lc.listadoMayorBG(4, "22");
                                ArrayList<Mayor> patrimoniof = (ArrayList<Mayor>) lc.listadoMayorBG(4, "31");
                        %>
                        <br>
                        <div class ="container-fluid">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Balance General despues del cierre</h1>
                            </div>
                        </div>
                        <br> 
                        <div class="container-fluid">
                            <div class ="row pt-2 pb-4 px-5 py-3">
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>A c t i v o</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>D e b e</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>H a b e r</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>P a s i v o</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>D e b e</strong>
                                </div>
                                <div class="col-md-2 text-center alert-info py-3 font-weight-bold" style="font-size: 20px">
                                    <strong>H a b e r</strong>
                                </div>
                                <div class="col-md-12">
                                    <div class="row text-center">
                                        <div class="col-md-6" style="border: 1px solid #f3f3f3 !important;">
                                            <div class="row text-center">
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning">
                                                    Corriente
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning"></div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor mac : activoCf) {
                                                            mac.liquidacion();
                                                            if (mac.getSaldoDeudor().floatValue() != 0 && mac.getSaldoAcreedor().floatValue() == 0 && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {
                                                                totActivoCf = totActivoCf.add(new BigDecimal(String.valueOf(mac.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);
                                                            } else if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0 && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {
                                                                totActivoCf = totActivoCf.subtract(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            } else if (mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") || mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {/*estas se restaran del total debido a que su
                                                                comportamiento es como la cuenta de un pasivo se procede a restar el saldo (naturaleza del saldo= ACREEDOR)*/
                                                                totActivoCf = totActivoCf.subtract(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-right">$</div><div class="col-md-9 text-right"><%= f.format(totActivoCf.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%for (Mayor mac : activoCf) {
                                                                if (mac.getSaldoDeudor().floatValue() != 0 && mac.getSaldoAcreedor().floatValue() == 0) {
                                                        %>
                                                        <div class="col-md-4"><%= mac.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(mac.getSaldoDeudor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%} else if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                                                        %>
                                                        <div class="col-md-4"><%= mac.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4 "><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(mac.getSaldoAcreedor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div><%
                                                                }
                                                            }%>
                                                    </div>
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning">
                                                    No Corriente
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning"></div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor manc : activoNCf) {
                                                            manc.liquidacion();
                                                            if (manc.getSaldoDeudor().floatValue() != 0 && manc.getSaldoAcreedor().floatValue() == 0 && !manc.getCuentamayor().getNombrecuenta().equals("DEPRECIACIÓN ACUMULADA")) {
                                                                totActivoNCf = totActivoNCf.add(new BigDecimal(String.valueOf(manc.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);
                                                            } else if (manc.getCuentamayor().getNombrecuenta().equals("DEPRECIACIÓN ACUMULADA")) {/*se restara de la cuenta propiedad planta y equipo debido a que su  naturaleza del saldo es Acreedor*/
                                                                totActivoNCf = totActivoNCf.subtract(new BigDecimal(String.valueOf(manc.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-right">$</div><div class="col-md-9 text-right"><%= f.format(totActivoNC.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%
                                                            for (Mayor mancc : activoNCf) {
                                                                if (mancc.getSaldoDeudor().floatValue() != 0 && mancc.getSaldoAcreedor().floatValue() == 0) {
                                                        %>
                                                        <div class="col-md-4"><%= mancc.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(mancc.getSaldoDeudor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-6" style="border-right: 1px solid #f3f3f3 !important;">
                                            <div class="row text-center">
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning">
                                                    Corriente
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning"></div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor mac : pasivoCf) {
                                                            mac.liquidacion();
                                                            if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                                                                totPasivoCf = totPasivoCf.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPasivoCf.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%for (Mayor pac : pasivoCf) {
                                                                if (pac.getSaldoDeudor().floatValue() == 0 && pac.getSaldoAcreedor().floatValue() != 0) {

                                                        %>
                                                        <div class="col-md-4"><%= pac.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(pac.getSaldoAcreedor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%}
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning">
                                                    No Corriente
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 alert-warning"></div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor mac : pasivoNC) {
                                                            mac.liquidacion();
                                                            if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                                                                totPasivoNCf = totPasivoNCf.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPasivoNCf.setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%for (Mayor panc : pasivoNCf) {
                                                                if (panc.getSaldoDeudor().floatValue() == 0 && panc.getSaldoAcreedor().floatValue() != 0) {

                                                        %>
                                                        <div class="col-md-4"><%= panc.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(panc.getSaldoAcreedor().floatValue())%></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%}
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                                <div class="col-md-12 text-center alert-success py-3 font-weight-bold">
                                                    <div class="row">
                                                        <div class="col-md-8">
                                                            <strong class="text-success">Total Pasivo</strong>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <strong class="text-success"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPasivoCf.add(totPasivoNCf).setScale(2, RoundingMode.HALF_UP).floatValue())%></div></div></strong>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-8 font-weight-bold py-2 alert-info" style="font-size: 20px">
                                                    <strong>P a t r i m o n i o</strong>
                                                </div>
                                                <div class="col-md-4 font-weight-bold py-2 bg-light">
                                                    <%for (Mayor mac : patrimoniof) {
                                                            mac.liquidacion();
                                                            if (mac.getSaldoDeudor().floatValue() != 0 || mac.getSaldoAcreedor().floatValue() != 0) {
                                                                totPatrimoniof = totPatrimoniof.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                                                            }
                                                        }
                                                    %>
                                                    <div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPatrimoniof.floatValue()) %></div></div>
                                                </div>
                                                <div class="col-md-12 py-2 text-justify">
                                                    <div class="row">
                                                        <%for (Mayor patri : patrimoniof) {
                                                                if (patri.getSaldoDeudor().floatValue() != 0 || patri.getSaldoAcreedor().floatValue() != 0) {
                                                                    if (patri.getSaldoDeudor().floatValue() != 0 && patri.getSaldoAcreedor().floatValue() == 0) {

                                                        %>

                                                        <div class="col-md-4"><%= patri.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(patri.getSaldoDeudor().floatValue()) %></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%} else if (patri.getSaldoDeudor().floatValue() == 0 && patri.getSaldoAcreedor().floatValue() != 0) {%>
                                                        <div class="col-md-4"><%= patri.getCuentamayor().getNombrecuenta()%></div>
                                                        <div class="col-md-4"><div class="row"><div class="col-md-6 text-center">$</div><div class="col-md-6 text-right"><%= f.format(patri.getSaldoAcreedor().floatValue()) %></div></div></div>
                                                        <div class="col-md-4"></div>
                                                        <%
                                                                    }
                                                                }
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%
                                    totActivof = totActivof.add(new BigDecimal(String.valueOf(totActivoCf.add(new BigDecimal(String.valueOf(totActivoNCf)).setScale(2, RoundingMode.HALF_UP)))).setScale(2, RoundingMode.HALF_UP));
                                    totPTf = totPTf.add(new BigDecimal(String.valueOf(totPatrimoniof.add(new BigDecimal(String.valueOf(totPasivoCf.add(new BigDecimal(String.valueOf(totPasivoNCf))))))))).setScale(2, RoundingMode.HALF_UP);
                                %>
                                <%if (totActivof.equals(totPTf)) {%>
                                <div class="col-md-6 text-center alert-success py-3 font-weight-bold">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <strong class="text-success">Total Activo</strong>
                                        </div>
                                        <div class="col-md-4">
                                            <strong class="text-success"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totActivof.floatValue()) %></div></div></strong>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6 text-center alert-success py-3 font-weight-bold">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <strong class="text-success">Total Pasivo + Patrimonio</strong>
                                        </div>
                                        <div class="col-md-4">
                                            <strong class="text-success"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPTf.setScale(2, RoundingMode.HALF_UP).floatValue()) %></div></div></strong>
                                        </div>
                                    </div>
                                </div>
                                <%} else {%>
                                <div class="col-md-6 text-center alert-danger py-3 font-weight-bold">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <strong class="text-danger">Total Activo</strong>
                                        </div>
                                        <div class="col-md-4">
                                            <strong class="text-danger"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totActivof.floatValue()) %></div></div></strong>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6 text-center alert-danger py-3 font-weight-bold">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <strong class="text-danger">Total Pasivo + Patrimonio</strong>
                                        </div>
                                        <div class="col-md-4">
                                            <strong class="text-danger"><div class="row"><div class="col-md-3 text-center">$</div><div class="col-md-9 text-right"><%= f.format(totPTf.floatValue()) %></div></div></strong>
                                        </div>
                                    </div>
                                </div>
                                <%}%>
                            </div>
                        </div>
                        <%
                            }%>
                        <div class="col-md-12 text-right px-5">
                            <a href="BalanceGeneral?tipobalance=antescierre" class="btn btn-info" target="blank"><span class="fa fa-print"></span> Imprimir</a>
                            <%if (per.getTerminado() == true) {%>
                            <a href="BalanceGeneral?tipobalance=despuescierre" class="btn btn-success" target="blank"><span class="fa fa-print"></span> Imprimir</a>
                            <%}%>
                        </div>
                        <%} else {
                        %>
                        <br>
                        <div class ="container-fluid">
                            <div class="text-center alert-warning py-2 font-weight-bold">
                                <h5 class="font-weight-bold text-warning"><span class="fa fa-info-circle"></span> Para poder visualizar el <strong>Balance General</strong> debe asignar el inventario final de mercadería</h5>
                            </div>
                        </div>
                        <%}%><br>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
        <!--footer exportado-->
        <%@include file="footer.jsp" %>
    </body>
</html>