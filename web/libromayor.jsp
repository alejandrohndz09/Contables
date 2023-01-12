<%-- 
    Document   : libromayor
    Created on : 6/09/2019, 12:36:58 PM
    Author     : Xom
--%>
<%@page import="entidades.Partida"%>
<%@page import="entidades.Debehaber"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Procesos.Mayor"%>
<%@page import="logica.logicaContables"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Libro mayor</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/filtromayor.js" type="text/javascript"></script>
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
                        <div class ="container">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Libro Mayor</h1>
                            </div>
                        </div>
                        <br>
                        <%
                            logicaContables lc = new logicaContables();
                            ArrayList<Partida> lveri = (ArrayList<Partida>) lc.listapartidasperiodo("", "");
                            if (lveri.size() > 0) {
                        %>
                        <div class="container">
                            <div class ="row bg-white  pt-2 pb-4 px-5 py-3">
                                <div class="col-md-12 text-center alert-info pt-2 pb-2">
                                    <h3 class="font-weight-bold">Filtros</h3>
                                    <h6 class="font-weight-bold">Filtrar por nivel, una sola cuenta ó especificar un rango de cuentas.</h6>
                                </div>
                                <div class="bg-light col-md-12" style="margin-bottom: 25px;padding-top: 15px">
                                    <form action="" method="post" id="filtromayor" name="filtromayor">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-4">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-filter"></i></span>
                                                                </div>
                                                                <input type="number" id="nivel" name="nivel" class="form-control" placeholder="Filtrar por nivel">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <p class="font-weight-bold col-md-1 text-center">ó</p>
                                                    <div class="col-md-4">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-folder"></i></span>
                                                                </div>
                                                                <input type="text" id="cuenta" name="cuenta" class="form-control" placeholder="Buscar por nombre ó código">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-3 text-center">
                                                        <a href="#" class="btn btn-outline-success" id="buscar"><span class="font-weight-bold"><li class="fa fa-search"></li></span> Buscar</a>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-4">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-file-code-o"></i></span>
                                                                </div>
                                                                <input type="text" id="cinicio" name="cinicio" class="form-control" placeholder="Código de la cuenta de inicio">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <p class="font-weight-bold col-md-1 text-center">Hasta</p>
                                                    <div class="col-md-4">
                                                        <div class="form-group">
                                                            <div class="input-group mb-3"> 
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addo1"><i class="fa fa-file-code-o"></i></span>
                                                                </div>
                                                                <input type="text" id="cfin" name="cfin" class="form-control" placeholder="Código de la cuenta final">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-3 text-center">
                                                        <a href="#" class="btn btn-outline-success" id="rango"><span class="font-weight-bold"><li class="fa fa-filter"></li></span> Buscar</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div><br>
                                <div class="col-lg-12">
                                    <!--
                                    
                                        alert-success representa el saldo deudor
                                        alert-primary representa el saldo acreedor
                                    
                                    -->
                                    <div class="alert alert-info text-center">El saldo en color verde representa el <strong class="text-success"> SALDO DEUDOR</strong> y el saldo de color azul representa el <strong class="text-info">SALDO ACREEDOR</strong></div>
                                    <div id="libro-mayor">
                                        <!--aqui se anexara el libro mayor mediante proceso ajax-->
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row px-5" id="paginacionmayor">

                        </div>
                        <br><br>
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
