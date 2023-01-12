<%-- 
    Document   : librodiario
    Created on : 6/09/2019, 12:36:28 PM
    Author     : Xom
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="entidades.Partida"%>
<%@page import="logica.logicaContables"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Libro diario</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">

        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <!--script de las validaciones-->
        <script src="js/filtros.js" type="text/javascript"></script>
        <!--script de sweetalert para los mensajes de registros de los datos-->
        <script src="sweetalert-master/docs/assets/sweetalert/sweetalert.min.js" type="text/javascript"></script>
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
                        <div class="container">
                            <div class="text-center col-md-12 text-white bg-info py-2 px-6">
                                <h1 class="font-weight-bold"> Listado de partidas registradas</h1>
                            </div>
                        </div>
                        <br>
                        <div class="container">
                            <div class="row px-5 py-4">
                                <div class="col-md-12 text-center alert-info pt-2 pb-2">
                                    <h3 class="font-weight-bold">Filtros</h3>
                                    <h6 class="font-weight-bold">Filtrar por # de partida ó fecha en especifico, rango de # de partidas ó rango de fechas de registros.</h6>
                                </div>
                                <div class="bg-light col-md-12 py-2">
                                    <form action="#" id="formfiltrobusqueda" name="formfiltrobusqueda">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-5">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-filter"></i></span>
                                                                </div>
                                                                <input type="number" id="numero" name="numero" data-accion="search" class="form-control" placeholder="N° de partida">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <p class="font-weight-bold col-md-2 text-center">ó</p>
                                                    <div class="col-md-5">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-calendar-o"></i></span>
                                                                </div>
                                                                <input type="date" id="fecha" name="fecha" data-accion="search" class="form-control">
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-5">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-filter"></i></span>
                                                                </div>
                                                                <input type="number" id="ninicio" name="ninicio" class="form-control" data-accion="niveles" placeholder="N° de partida">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <p class="font-weight-bold col-md-2 text-center">Hasta</p>
                                                    <div class="col-md-5">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-filter"></i></span>
                                                                </div>
                                                                <input type="number" id="nfin" name="nfin" class="form-control" data-accion="niveles" placeholder="N° de partida">
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-5">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-calendar-check-o"></i></span>
                                                                </div>
                                                                <input type="date" id="finicio" name="finicio" data-accion="fechas" class="form-control">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <p class="font-weight-bold col-md-2 text-center">Hasta</p>
                                                    <div class="col-md-5">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-calendar-check-o"></i></span>
                                                                </div>
                                                                <input type="date" id="ffin" name="ffin" data-accion="fechas" class="form-control">
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <div class="row px-5 py-3" id="partidasregistradas">
                            </div>
                            <div class="row px-5" id="paginaciondiario" style="padding-top: 0px;margin-top: -55px;">

                            </div>
                            <br><br>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
        <!--footer exportado-->
        <%@include file="footer.jsp" %>
    </body>
</html>
