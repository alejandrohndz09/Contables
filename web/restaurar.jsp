<%-- 
    Document   : restaurar
    Created on : 10/01/2020, 12:04:16 PM
    Author     : Xom
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Respaldo de la información</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">

        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <!--script de las validaciones-->
        <script src="js/custom 3.js" type="text/javascript"></script>
        <!--script de sweetalert para los mensajes de registros de los datos-->
        <script src="sweetalert-master/docs/assets/sweetalert/sweetalert.min.js" type="text/javascript"></script>
    </head>
    <body>
        <!--diseño del navbar-->
        <%@include file="navbar.jsp" %>

        <!--diseño del contenedor de los elementos del back up-->
        <br><br><br>
        <div class="container">
            <div class="card-box">
                <div class="tab-content" id="myTabContent">
                    <div class="tab-pane fade show active bg-white py-2">
                        <br>
                        <div class="container">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Importar respaldo de la información</h1>
                            </div>
                        </div>
                        <div class="container">
                            <div class="bg-white">
                                <div class="row px-5 py-5">
                                    <div class="col-md-3"></div>
                                    <div class="col-md-6 bg-light">
                                        <h3 class="bg-info text-center text-white px-2 py-3 font-weight-bold mt-3">
                                            Todos los campos son obligatorios
                                        </h3>
                                        <form id="formimport" name="formimport" action="#" method="post" class="py-3">
                                            <div class="row px-5">
                                                <div class="form-group col-md-12">
                                                    <div class="input-group mb-1"> 
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addo1"><i class="fa fa-navicon"></i></span>
                                                        </div>
                                                        <select id="puertoimport" name="puertoimport" class="form-control">
                                                            <option value="">Seleccione un puerto</option>
                                                            <option value="5432">5432</option>
                                                            <option value="5434">5434</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12">
                                                    <div class="input-group mb-1"> 
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addo1"><i class="fa fa-database"></i></span>
                                                        </div>
                                                        <input type="text" id="databaseimport" name="databaseimport" class="form-control" placeholder="Nombre de la base de datos">
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12">
                                                    <div class="input-group mb-1"> 
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addo1"><i class="fa fa-file-archive-o"></i></span>
                                                        </div>
                                                        <input type="file" id="backup" name="backup" class="form-control">
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12">
                                                    <div class="input-group mb-1"> 
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addo1"><i class="fa fa-user"></i></span>
                                                        </div>
                                                        <input type="text" id="usuarioimport" name="usuarioimport" class="form-control" placeholder="Nombre del usuario">
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-12">
                                                    <div class="input-group mb-1"> 
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addo1"><i class="fa fa-expeditedssl"></i></span>
                                                        </div>
                                                        <input type="password" id="passimport" name="passimport" class="form-control" placeholder="Contraseña del usuario">
                                                    </div>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="form-group col-md-12 text-center">
                                                <button type="submit" class="btn btn-success"><span class="fa fa-database"></span> Importar</button>
                                                <button type="reset" class="btn btn-danger"><span class="fa fa-trash-o"></span> Cancelar</button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-md-3"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--footer exportado-->
        <%@include file="footer.jsp" %>
    </body>
</html>
