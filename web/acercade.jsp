<%-- 
    Document   : acercade
    Created on : 10/08/2019, 10:52:53 AM
    Author     : Xom
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta   http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Acerca de</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/acercade.css" rel="stylesheet" type="text/css"/>
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,300,400,500" rel="stylesheet">
        <script src="js/jquery-3.4.0.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body>
        <!--diseño del navbar-->
        <%@include file="navbar.jsp" %>

        <!--diseño del contenedor de los elementos-->
        <br><br><br>
        <div class="container">
            <div class="jumbotron acercade">
                <h4>
                    Es posible controlar las declaraciones en una transacción de una manera más 
                    granular por medio de puntos de recuperación (savepoints). Los puntos de recuperación 
                    permiten descartar selectivamente algunas partes de la transacción mientras las demás sí 
                    se ejecutan. Después de definir un punto de recuperación con SAVEPOINT, se puede volver a él si 
                    es necesario por medio de ROLLBACK TO. Todos los cambios de la base de datos hechos por la transacción
                    entre el punto de recuperación y 
                    el rollback se descartan, pero los cambios hechos antes del punto de recuperación se mantienen.
                </h4>    
            </div>
        </div>
        <br>
        <footer class="footer mt-3">
            <p class="copyright m-0 text-center py-3 text-light bg-dark" >
                Todos los derechos reservados &copy; 2019.
            </p>
        </footer>
    </body>
</html>
