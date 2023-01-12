<%-- 
    Document   : registrarpartidas
    Created on : 28/08/2019, 04:28:16 PM
    Author     : Xom
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="entidades.Cuenta"%>
<%@page import="logica.logicaContables"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Registro de Partidas</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">

        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <!--script de las validaciones-->
        <script src="js/custom 2.js" type="text/javascript"></script>
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
                        <br>
                        <div class="container">
                            <div class="text-center text-white bg-info py-2">
                                <h1 class="font-weight-bold"> Nuevo registro de partidas</h1>
                            </div>
                        </div>
                        <br>
                        <%
                            logicaContables lc = new logicaContables();
                            ArrayList<Cuenta> c = (ArrayList<Cuenta>) lc.listadocuentas();
                            if (c.size() > 0) {
                        %> 
                        <div class="container">
                            <div class="row px-3 pt-2 pb-2 py-3">
                                <div class="sticky-scroll-box col-md-3 text-center">
                                    <div class="row bg-light" style="margin-right: 0px;border-radius: 5px;padding-bottom: 25px;padding-top: 25px">
                                        <label class="form-group col-md-12 font-weight-bold text-success">Buscar cuenta:</label>
                                        <form name="buscarcuenta" id="buscarcuenta" action="#" method="post">
                                            <div class="col-md-12">
                                                <div class="form-group">
                                                    <div class="input-group mb-3"> 
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addo1"><i class="fa fa-bank"></i></span>
                                                        </div>
                                                        <input type="text" id="codigocuenta" name="codigocuenta" class="form-control" placeholder="Código/Nombre Cuenta">
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                        <div class="row">
                                            <div class="col-md-12" id="cuentasencontradas"><!--aqui agregar el scroll-->
                                            </div>
                                        </div>
                                        <div class="col-md-12" id="limpiarbusqueda">
                                            <span class="input-group-btn">
                                                <a data-type="btn-limpiar" href="#" class="btn btn-outline-danger col-md-12 px-2 py-2 font-weight-bold"><span class="fa fa-trash-o"></span> Limpiar Búsqueda</a>
                                            </span>
                                        </div>
                                        <!--descomentar esta parte si se desea registrar 1,2,3,4,5... partidas al mismo tiempo-->
                                        <!--<div class="col-md-12">
                                            <br>
                                            <span class="input-group-btn">
                                                <button type="button" class="btn btn-outline-info col-md-12 px-2 py-2 font-weight-bold" onclick="agregarpartidas();"><i class="fa fa-book"></i> Nueva Partida</button>
                                            </span>
                                        </div>-->
                                    </div>
                                </div>
                                <div class="col-md-9">
                                    <form action="#" method="post" name="registropartidas" id="registropartidas">
                                        <div id="partidas">

                                        </div>
                                        <div class="text-center font-weight-bold pb-4">
                                            <br>
                                            <hr>
                                            <button type="submit" class="btn btn-primary"><span class="fa fa-book"></span> Registrar Partidas</button>
                                            <button type="button" onclick="limpiarform();" class="btn btn-danger"><span class="fa fa-refresh"></span> Cancelar Registro</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <%} else {%>
                        <div class="container px-5">
                            <div class="alert-warning py-3">
                                <div class="col-md-12 text-center">
                                    <span class="fa fa-warning text-warning" style="font-size: 28px"></span> 
                                    <h5 class="font-weight-bold text-warning">Antes de registrar una transacción asegurese de haber registrado ya el catalogo de cuentas.</h5>
                                </div>
                            </div>
                        </div><br>
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