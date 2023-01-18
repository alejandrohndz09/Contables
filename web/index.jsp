
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
            <div id="layoutSidenav_content" class="d-flex align-items-center justify-content-center">
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
                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-credit-card" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5 >Cátalogo de Cuentas</h5>

                                                <a href="catalogocuentas.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold"><i class="fa fa-square-plus" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5 >Iniciar Periodo</h5>

                                                <a href="inicioperiodo.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%} else if (p.getEnproceso() == true && p.getTerminado() == false) {%>
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-6 py-3">

                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-credit-card" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5 >Cátalogo de Cuentas</h5>

                                                <a href="catalogocuentas.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-money-bill-transfer" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5>Ingreso de partidas</h5>

                                                <a href="registrarpartidas.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="col-md-6 py-3">

                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-coins" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5 >Balanza de Comprobación</h5>

                                                <a href="balanzacomprobacion.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold"><i class="fa fa-square-plus" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5 >Iniciar Periodo</h5>

                                                <a href="inicioperiodo.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="row">
                                    
                                    <div class="col-md-6 py-3">
                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-book" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5>Libro Diario</h5>

                                                <a href="librodiario.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-list" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5 >Libro Mayor</h5>

                                                <a href="libromayor.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-chart-column" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5>Estado de Resultados</h5>

                                                <a href="estadoresultado.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="col-md-6 py-3">
                                        <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-balance-scale" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5>Balance General</h5>

                                                <a href="balancegeneral.jsp" class="stretched-link"></a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4"></div>
                            <div class="col-md-3 py-3">
                                <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                    <div class="d-flex align-items-center"><span class="font-weight-bold">
                                            <i class="fa fa-square-minus" style="color: #f5a623; font-size: 55px"></i></span></div>
                                    <div class="mx-2 d-flex align-items-center justify-content-center">
                                        <h5>Cierre de Ejercicio</h5>

                                        <a href="#" class="stretched-link" data-toggle="modal" data-target="#generarcierre"></a>
                                    </div>
                                </div>
                                
                            </div>
                            <div class="col-md-4"></div>
                            <%} else if (p.getEnproceso() == true && p.getTerminado() == true) {%>
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-6 py-3">
                                       <div class="d-flex justify-content-center position-relative shadow p-3 mb-5 bg-body rounded" style="min-height: 150px;" >
                                            <div class="d-flex align-items-center"><span class="font-weight-bold">
                                                    <i class="fa fa-credit-card" style="color: #f5a623; font-size: 55px"></i></span></div>
                                            <div class="mx-2 d-flex align-items-center justify-content-center">
                                                <h5 >Cátalogo de Cuentas</h5>

                                                <a href="catalogocuentas.jsp" class="stretched-link"></a>
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