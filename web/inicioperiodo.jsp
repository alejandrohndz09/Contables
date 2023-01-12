<%-- 
    Document   : inicioperiodo
    Created on : 10/08/2019, 10:52:53 AM
    Author     : Xom
--%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entidades.Periodo"%>
<%@page import="logica.logicaContables"%>
<%@page import="java.time.LocalDate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Iniciar periodo</title>
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

        <!--diseño del contenedor de los elementos-->
        <br><br><br>
        <div class="container">
            <div class="card-box">
                <%@include file="menuvertical.jsp" %>
                <!--configuraciones del menu-->
                <div class="tab-content" id="myTabContent">
                    <div class="tab-pane fade show active bg-white py-2">
                        <br>
                        <div class="container">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Iniciar Periodo</h1>
                            </div>
                        </div>
                        <br><br>
                        <div class="container">
                            <div class="row">
                                <div class="col-lg-3"></div>
                                <div class="col-md-6 bg-light"><br>
                                    <h3 class="py-3 bg-info text-center text-white font-weight-bold">Todos los campos son abligatorios</h3>
                                    <div class="pt-2 py-5 px-5">
                                        <%
                                            logicaContables lc = new logicaContables();
                                            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                                            ArrayList<Periodo> list = (ArrayList<Periodo>) lc.listaPeriodos();
                                            Periodo periodo = lc.PeriodoenCurso();

                                        %>
                                        <form action="CtrPeriodo" name="periodo" method="post" id="periodo">
                                            <div class="row">
                                                <div class="col-md-8"><label class="form-group font-weight-bold">Fecha de inicio: </label></div>
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <div class="input-group mb-3"> 
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addo1"><i class="fa fa-calendar-check-o"></i></span>
                                                            </div>
                                                            <input type="date" id="fechainicio" name="fechainicio" value="<%= (periodo == null) ? LocalDate.now().getYear() + "-01-01" : periodo.getFechainicial()%>" class="form-control" placeholder="Fecha Inicio">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-8"><label class="form-group font-weight-bold">Fecha final: </label></div>
                                                <div class="col-md-12"> 
                                                    <div class="form-group">
                                                        <div class="input-group mb-3"> 
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addo2"><i class="fa fa-calendar-times-o"></i></span>
                                                            </div>
                                                            <input type="date" id="fechafinal" name="fechafinal" value="<%= (periodo == null) ? LocalDate.now().getYear() + "-12-31" : periodo.getFechafinal()%>" class="form-control" placeholder="Fecha Final">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="col-md-8"><label class="form-group font-weight-bold">Periodos</label></div>
                                            <div class="col-md-12 alert-info py-3"> 
                                                <div class="form-group">
                                                    <div class="input-group mb-3"> 
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addo2"><i class="fa fa-edit"></i></span>
                                                        </div>
                                                        <select class="form-control" id="idperiodo" name="idperiodo">
                                                            <option value="">Ver periodos...</option>
                                                            <%
                                                                for (Periodo p : list) {

                                                                    if (p.getEnproceso() == true && p.getTerminado() == false) {
                                                            %>
                                                            <option value="<%= p.getIdperiodo()%>"> Periodo en proceso y en curso, desde <%= f.format(p.getFechainicial())%> hasta <%= f.format(p.getFechafinal())%></option>
                                                            <%} else if (p.getEnproceso() == false && p.getTerminado() == false) {%>
                                                            <option value="<%= p.getIdperiodo()%>"> Periodo en curso, desde <%= f.format(p.getFechainicial())%> hasta <%= f.format(p.getFechafinal())%></option>
                                                            <%} else if (p.getEnproceso() == true && p.getTerminado() == true) {%>
                                                            <option value="<%= p.getIdperiodo()%>"> Periodo en proceso, desde <%= f.format(p.getFechainicial())%> hasta <%= f.format(p.getFechafinal())%></option>
                                                            <%
                                                            } else if (p.getEnproceso() == false && p.getTerminado() == true) {%>
                                                            <option value="<%= p.getIdperiodo()%>"> Periodo finalizado, desde <%= f.format(p.getFechainicial())%> hasta <%= f.format(p.getFechafinal())%></option>
                                                            <%}
                                                                }%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="text-center">
                                                <div class="btn-group my-3">
                                                    <button type="button" onclick="validacion()" class="btn btn-success"><span class="fa fa-arrow-circle-down"></span> Iniciar</button>
                                                    <button type="reset" class="btn btn-warning pull-left"><span class="fa fa-dedent"></span> Cancelar</button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>
                        </div>
                        <br><br><br><br>
                    </div>
                </div>
            </div>
        </div>

        <!--footer exportado-->
        <%@include file="footer.jsp" %>
        <script type="text/javascript">
            function validacion() {
                if (document.getElementById('fechainicio').value === '' || document.getElementById('fechafinal').value === '') {
                    swal({
                        title: "Error",
                        text: "Por favor llene los campos correspondientes al periodo",
                        icon: 'error'
                    });
                } else {
                    var fechainicio = document.getElementById('fechainicio').value;
                    var fechafinal = document.getElementById('fechafinal').value;
                    fechainicio = new Date(fechainicio);
                    fechafinal = new Date(fechafinal);
                    var fechai = document.getElementById('fechainicio').val = (fechainicio.getFullYear() + 1) + "-" + (fechainicio.getMonth() + 1) + "-" + fechainicio.getDay();
                    fechainicio = new Date(fechai);
                    if (fechainicio.getFullYear() === fechafinal.getFullYear()) {
                        document.periodo.submit();
                    } else {
                        swal({
                            title: "Error",
                            text: "El año de inicio y final del periodo debe ser igual",
                            icon: 'error'
                        });
                    }
                }
            }
        </script>
    </body>
</html>
