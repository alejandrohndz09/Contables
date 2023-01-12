
<%@page import="entidades.Periodo"%>
<%@page import="logica.logicaContables"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Sistemas Contables</title>
        <link rel="icon" href="img/Iniciales.png">
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/index.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">

        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@latest/dist/style.css" rel="stylesheet" />
        <script src="https://use.fontawesome.com/releases/v6.1.0/js/all.js" crossorigin="anonymous"></script>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/styles.css" type="text/css">

        <link rel="stylesheet" href="css/bootstrap-treeview.css" type="text/css">
    </head>
    <body class="sb-nav-fixed">
        <%@include file="navbar.jsp" %>
        <div id="layoutSidenav">
            <%@include file="slide.jsp" %>
            <div id="layoutSidenav_content">
                <main>   
                    <div class="container">
                        <div class="row">
                            <%
                                logicaContables lc = new logicaContables();
                                Periodo p = lc.PeriodoenCurso();
                                if (p == null) {
                            %>
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-6 py-3">
                                        <div class="bg-info text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-info">
                                                    Catalogo de Cuentas
                                                </div>
                                                <div class="card-body text-info">
                                                    <span class="font-weight-bold"><i class="fa fa-folder-open" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer bg-info">
                                                    <a href="catalogocuentas.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="bg-primary text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-primary">
                                                    Iniciar Periodo
                                                </div>
                                                <div class="card-body text-primary">
                                                    <span class="font-weight-bold"><i class="fa fa-cogs" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer bg-primary">
                                                    <a href="inicioperiodo.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%} else if (p.getEnproceso() == true && p.getTerminado() == false) {%>
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-6 py-3">
                                        <div class="bg-info text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-info">
                                                    Catalogo de Cuentas
                                                </div>
                                                <div class="card-body text-info">
                                                    <span class="font-weight-bold"><i class="fa fa-folder-open" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer bg-info">
                                                    <a href="catalogocuentas.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="bg-primary text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-primary">
                                                    Iniciar Periodo
                                                </div>
                                                <div class="card-body text-primary">
                                                    <span class="font-weight-bold"><i class="fa fa-cogs" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer bg-primary">
                                                    <a href="inicioperiodo.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="alert-info font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title">
                                                    Libro Mayor
                                                </div>
                                                <div class="card-body">
                                                    <span class="font-weight-bold"><i class="fa fa-list" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer alert-info">
                                                    <a href="libromayor.jsp" class="alert-info" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="alert-primary font-weight-bold text-center" style="border-radius: 5px !important">
                                            <div class="card">
                                                <div class="card-title">
                                                    Balanza de Comprobación
                                                </div>
                                                <div class="card-body">
                                                    <span class="font-weight-bold"><i class="fa fa-money" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer alert-primary">
                                                    <a href="balanzacomprobacion.jsp" class="alert-primary" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-6 py-3">
                                        <div class="bg-success text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-success">
                                                    Registrar Partidas
                                                </div>
                                                <div class="card-body text-success">
                                                    <span class="font-weight-bold"><i class="fa fa-edit" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer bg-success">
                                                    <a href="registrarpartidas.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="bg-warning text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-warning">
                                                    Libro Diario
                                                </div>
                                                <div class="card-body text-warning">
                                                    <span class="font-weight-bold"><i class="fa fa-book" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer bg-warning">
                                                    <a href="librodiario.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="alert-success font-weight-bold text-center" style="border-radius: 5px !important">
                                            <div class="card">
                                                <div class="card-title">
                                                    Estado de Resultados
                                                </div>
                                                <div class="card-body">
                                                    <span class="font-weight-bold"><i class="fa fa-refresh" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer alert-success">
                                                    <a href="estadoresultado.jsp" class="alert-success" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="alert-warning font-weight-bold text-center" style="border-radius: 5px !important">
                                            <div class="card">
                                                <div class="card-title">
                                                    Balance General
                                                </div>
                                                <div class="card-body">
                                                    <span class="font-weight-bold"><i class="fa fa-balance-scale" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer alert-warning">
                                                    <a href="balancegeneral.jsp" class="alert-warning" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2"></div>
                            <div class="col-md-8 py-3">
                                <div class="alert-dark font-weight-bold text-center" style="border-radius: 5px !important">
                                    <div class="card">
                                        <div class="card-title">
                                            Cierre del Periodo
                                        </div>
                                        <div class="card-body">
                                            <span class="font-weight-bold"><i class="fa fa-remove" style="font-size: 55px"></i></span>
                                        </div>
                                        <div class="card-footer alert-dark">
                                            <a href="#" class="alert-dark" style="text-decoration: none" data-toggle="modal" data-target="#generarcierre"><span class="fa fa-save"></span> Generar Cierre</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2"></div>
                            <%} else if (p.getEnproceso() == true && p.getTerminado() == true) {%>
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-6 py-3">
                                        <div class="bg-info text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-info">
                                                    Catalogo de Cuentas
                                                </div>
                                                <div class="card-body text-info">
                                                    <span class="font-weight-bold"><i class="fa fa-folder-open" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer bg-info">
                                                    <a href="catalogocuentas.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="bg-primary text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-primary">
                                                    Iniciar Periodo
                                                </div>
                                                <div class="card-body text-primary">
                                                    <span class="font-weight-bold"><i class="fa fa-cogs" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer bg-primary">
                                                    <a href="inicioperiodo.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="alert-info font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title">
                                                    Libro Mayor
                                                </div>
                                                <div class="card-body">
                                                    <span class="font-weight-bold"><i class="fa fa-list" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer alert-info">
                                                    <a href="libromayor.jsp" class="alert-info" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="alert-primary font-weight-bold text-center" style="border-radius: 5px !important">
                                            <div class="card">
                                                <div class="card-title">
                                                    Balanza de Comprobación
                                                </div>
                                                <div class="card-body">
                                                    <span class="font-weight-bold"><i class="fa fa-money" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer alert-primary">
                                                    <a href="balanzacomprobacion.jsp" class="alert-primary" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-6 py-3">
                                        <div class="bg-warning text-white font-weight-bold text-center">
                                            <div class="card">
                                                <div class="card-title text-warning">
                                                    Libro Diario
                                                </div>
                                                <div class="card-body text-warning">
                                                    <span class="font-weight-bold"><i class="fa fa-book" style="font-size: 55px"></i></span>
                                                </div>      
                                                <div class="card-footer bg-warning">
                                                    <a href="librodiario.jsp" class="text-white" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="alert-success font-weight-bold text-center" style="border-radius: 5px !important">
                                            <div class="card">
                                                <div class="card-title">
                                                    Estado de Resultados
                                                </div>
                                                <div class="card-body">
                                                    <span class="font-weight-bold"><i class="fa fa-refresh" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer alert-success">
                                                    <a href="estadoresultado.jsp" class="alert-success" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="alert-warning font-weight-bold text-center" style="border-radius: 5px !important">
                                            <div class="card">
                                                <div class="card-title">
                                                    Balance General
                                                </div>
                                                <div class="card-body">
                                                    <span class="font-weight-bold"><i class="fa fa-balance-scale" style="font-size: 55px"></i></span>
                                                </div>
                                                <div class="card-footer alert-warning">
                                                    <a href="balancegeneral.jsp" class="alert-warning" style="text-decoration: none"><span class="fa fa-eye"></span> Ir</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%}%>
                        </div>
                    </div>
                </main>
            </div>
        </div>
        <div class="modal fade" id="generarcierre" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLongTitle"><strong>Realizar Cierre</strong></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body text-justify">
                        <span class="fa fa-check-square"></span> Asegurese de haber ingresado el inventario final de mercadería.<br><br>
                        <span class="fa fa-check-square"></span> Asegurese de haber creado la carpeta <strong>Respaldo Contabilidad</strong> en el disco local <strong>C</strong>.<br><br>
                        <span class="fa fa-check-square"></span> Al realizar el cierre ya no se podran revertir los cambios, por lo cual se recomienda hacer un back up de la información actual que se almacenara en la carpeta <strong>Respaldo Contabilidad</strong> en el disco local <strong>C</strong>.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Cancelar</button>
                        <a type="button" href="cierre.jsp" class="btn btn-info">Finalizar Periodo</a>
                    </div>
                </div>
            </div>
        </div>

        <script src='js/bootstrap-treeview.js'></script>
        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js" crossorigin="anonymous"></script>
        <script src="js/chart-area-demo.js"></script>
        <script src="js/chart-bar-demo.js"></script>

        <script src="https://cdn.jsdelivr.net/npm/simple-datatables@latest"  crossorigin="anonymous"></script>
        <script src="js/datatables-simple-demo.js"></script>

        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.js" type="text/javascript"></script>
        <script src="js/scripts.js"></script>
    </body>
</html>