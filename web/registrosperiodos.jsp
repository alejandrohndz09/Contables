<%-- 
    Document   : registrosperiodos
    Created on : 2/11/2019, 05:34:55 PM
    Author     : Xom
--%>
<%@page import="java.text.SimpleDateFormat"%>
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

        <title>Registros de periodos</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/diseño.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">

        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <!--script de sweetalert para los mensajes de registros de los datos-->
        <script src="sweetalert-master/docs/assets/sweetalert/sweetalert.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            function modalfade(idperiodo) {
                $.post("modalperiodo.jsp", {'idperiodo': idperiodo}, datos);
                $('#modalperiodo').modal('toggle');
            }

            function datos(data) {
                var contennt = $('#informacion');
                contennt.html(data);
            }
        </script>
    </head>
    <body>
        <!--diseño del navbar-->
        <%@include file="navbar.jsp" %>

        <!--diseño del contenedor de los elementos-->
        <br><br><br>
        <div class="container">
            <div class="card-box">
                <div class="tab-content" id="myTabContent">
                    <div class="tab-pane fade show active bg-white py-2">
                        <br>
                        <div class="container">
                            <div class="text-center text-white bg-info py-2 font-weight-bold">
                                <h1 class="font-weight-bold">Listado de Periodos</h1>
                            </div>
                        </div>
                        <br><br>
                        <div class="container text-center">
                            <div class="row px-5">
                                <div class="col-md-12 text-center">
                                    <table id="tablaperiodos" class="table table-borderedtable-responsive-lg text-center">
                                        <thead class="table table-info">
                                            <tr>
                                                <th>Fecha de Inicio</th>
                                                <th>Fecha de Finalización</th>
                                                <th>Descripción</th>
                                                <th>Valor</th>
                                                <th>En proceso</th>
                                                <th>Finalizado</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                logicaContables lc = new logicaContables();
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
                                                ArrayList<Periodo> i = (ArrayList<Periodo>) lc.listaPeriodos();
                                                ArrayList<Inventario> in = (ArrayList<Inventario>) lc.listInventarios();
                                                if (i.size() > 0) {
                                                    int con = 0;

                                                    for (Periodo x : i) {
                                            %>
                                            <tr>
                                                <td><%= sdf.format(x.getFechainicial()) %></td>
                                                <td><%= sdf.format(x.getFechafinal()) %></td>
                                                <%
                                                    if (in.size() > con) {
                                                        if (lc.listInventarios().get(con).getPeriodo().getIdperiodo() == x.getIdperiodo()) {
                                                            for (Inventario o : in) {
                                                                if (o.getPeriodo().getIdperiodo() == x.getIdperiodo()) {
                                                %>
                                                <td><strong><%= o.getDescripcion()%></strong></td>
                                                <td><strong><%= o.getValor()%></strong></td>
                                                <%
                                                        }

                                                    }
                                                    con++;
                                                } else {%>
                                                <td><strong>Asignar</strong></td>
                                                <td><strong>Asignar</strong></td>
                                                <%}
                                                } else {%>
                                                <td><strong>Asignar</strong></td>
                                                <td><strong>Asignar</strong></td>
                                                <%}%>
                                                <td><%=(x.getEnproceso()) ? "<span class='text-success'><strong>Si</strong></span>" : "<span class='text-danger'><strong>No</strong></span>"%></td>
                                                <td><%=(x.getTerminado()) ? "<span class='text-success'><strong>Si</strong></span>" : "<span class='text-danger'><strong>No</strong></span>"%></td>
                                                <td><button class="btn btn-outline-info" onclick="modalfade(<%= x.getIdperiodo()%>)"><span class="fa fa-eye"></span></button></td>
                                            </tr>
                                            <%
                                                }
                                            } else {%>
                                            <tr class="alert-warning"><td colspan="7">No hay periodos registrados</td></tr>
                                            <%}%>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <br><br><br><br>
                    </div>
                </div>
            </div>
        </div>


        <div class="modal fade" id="modalperiodo">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-info">
                        <h4 class="modal-title text-white"><strong>Información del Periodo</strong></h4>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                    </div>
                    <div class="box-body">
                        <div id="informacion">

                        </div>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal-dialog -->
        </div>

        <!--footer exportado-->
        <%@include file="footer.jsp" %>
    </body>
</html>

