<%-- 
    Document   : catalogocuentas
    Created on : 10/08/2019, 10:52:53 AM
    Author     : Xom
--%>

<%@page import="entidades.Cuenta"%>
<%@page import="java.util.ArrayList"%>
<%@page import="logica.logicaContables"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Catalogo de Cuentas</title>
        <link rel="icon" href="img/Iniciales.png">
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">
        <script src="https://use.fontawesome.com/releases/v6.1.0/js/all.js" crossorigin="anonymous"></script>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/styles.css" type="text/css">

        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@latest/dist/style.css" rel="stylesheet" />

        <link rel="stylesheet" href="css/bootstrap-treeview.css" type="text/css">


    </head>
    <body class="sb-nav-fixed">
        <%@include file="navbar.jsp" %>
        <div id="layoutSidenav">
            <%@include file="slide.jsp" %>
            <div id="layoutSidenav_content">
                <main>
                    <br><br><br>
                    <div class="container">
                        <h1 class="mt-4">Cátalogo de Cuentas</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item"><a href="index.html">Dashboard</a></li>
                            <li class="breadcrumb-item active">Cátalogo</li>
                        </ol>
                    </div>
                    <br>
                    <div class="container">
                        <div class="row">
                            <div class="col-md-5">
                                <div class="row">
                                    <div class="col-md-12">
                                        <!--<div class="alert alert-info">El código debera ingresarse de la forma: #, ##, ####, ######, dependera del nivel al que la cuenta pertenece.</div> -->            
                                        <div class="sticky-scroll-box bg-white  pt-2 pb-2 px-2">
                                            <!--formulario de registro de los datos de la cuenta -->
                                            <form action="CtrCuenta" name="form-cuenta" method="post" id="form-cuenta">
                                                <div>
                                                    <h3 class="h3 text-center text-white bg-info py-2 font-weight-bold titulo-registro">Cuenta</h3>
                                                    <div class="form-group px-5">
                                                        <div class="input-group mb-3"> 
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addo1"><i class="fa fa-folder-o"></i></span>
                                                            </div>
                                                            <input type="text" id="nombre" name="nombre" class="form-control" placeholder="Nombre de la cuenta">
                                                            <input type="text" id="idcuenta" name="idcuenta" class="form-control" placeholder="Id de la cuenta" style="display: none">
                                                        </div>
                                                    </div>

                                                    <div class="form-group px-5">
                                                        <div class="input-group mb-3"> 
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addo2"><i class="fa fa-folder"></i></span>
                                                            </div>
                                                            <input type="text" id="codigo" name="codigo" class="form-control" placeholder="Codigo de cuenta">
                                                        </div>
                                                    </div>

                                                    <div class="form-group px-5">
                                                        <div class="input-group mb-3"> 
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addo3"><i class="fa fa-comment"></i></span>
                                                            </div>
                                                            <textarea id="descripcion" name="descripcion" class="form-control" placeholder="Descripcion de la cuenta"></textarea>
                                                        </div>

                                                    </div>

                                                    <div class="form-group px-5">
                                                        <div class="input-group mb-3"> 
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addo4"><i class="fa fa-balance-scale"></i></span>
                                                            </div>
                                                            <select class="form-control" name="tipocuenta" id="tiposaldo">
                                                                <option selected="selected" value="">Saldo de la Cuenta</option>
                                                                <option value="Deudor">Deudor</option>
                                                                <option value="Acreedor">Acreedor</option>
                                                                <option value="De Cierre">De Cierre</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>

                                                <hr>
                                                <div class="text-center">
                                                    <div class="btn-group my-3">
                                                        <button type="submit" class="btn btn-success" name="guardar"><span class="fa fa-save"></span> Guardar</button>
                                                        <button type="reset" class="btn btn-warning pull-left"><span class="fa fa-dedent"></span> Limpiar</button>
                                                    </div>
                                                </div>
                                            </form>

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--parte donde se gerara el catalogo de cuentas con su respectiva gerarquía-->
                            <div class="col-md-7">
                                <!--<div class="alert alert-info">Las cuentas seran mostradas de manera jerarquizada, de manera que cada cuenta padre contendra sus cuentas hijas, de igual manera las cuentas hijas padres, si las posee.</div>-->

                                <div class="bg-white  pt-2 pb-2 px-2">
                                    <h3 class="h3 text-center text-white bg-dark     py-2 font-weight-bold">Catalogo de Cuentas</h3>
                                    <div  id="arbol-cuentas">
                                        <!--incluiremos el catalogo de cuentas via ajax, por lo cual aqui no sera visible nada-->
                                        <ul class="file-tree" id="cc">
                                        </ul>
                                    </div>
                                    <hr>
                                    <form id="form-eliminar">
                                        <div class="text-center">
                                            <div class="btn-group">
                                                <button type="submit" id="eliminar" class="text-sm-center  btn btn-danger"><span class="fa fa-trash-o"></span> Eliminar</button> 
                                                <button type="button" class="text-sm-center  btn btn-primary" onclick="deseleccionarCuenta()"><span class="fa fa-pencil"></span> Deseleccionar</button>
                                            </div>
                                        </div>
                                    </form>
                                    <hr>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>


        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.js" type="text/javascript"></script>
        <script src="js/scripts.js"></script>
        <!--script de las validaciones-->
        <script src="js/custom.js" type="text/javascript"></script>
        <!--script de sweetalert para los mensajes de registros de los datos-->
        <script src="sweetalert-master/docs/assets/sweetalert/sweetalert.min.js" type="text/javascript"></script>
    </body>
</html>
