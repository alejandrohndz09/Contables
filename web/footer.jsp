<%-- 
    Document   : footer
    Created on : 10/08/2019, 04:24:57 PM
    Author     : Xom
--%>

<%@page import="java.time.LocalDate"%>
<br>
<footer class="footer mt-3">
    <p class="copyright m-0 text-center py-3 text-light bg-dark" >
        Todos los derechos reservados &copy; <%= LocalDate.now().getYear() %>.
    </p>
</footer>
