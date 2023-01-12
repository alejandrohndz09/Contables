<%-- 
    Document   : slide
    Created on : 01-04-2023, 03:49:40 AM
    Author     : Flavio Molina
--%>

<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion" style="background: #061b22 !important">
        <div class="sb-sidenav-menu">
            <div class="nav">
                <div class="sb-sidenav-menu-heading">Inicio</div>
                <a class="nav-link" href="index.jsp">
                    <div class="sb-nav-link-icon"><i style="color: #F5A623 !important" class="fas fa-house"></i></div>
                    Dashboard
                </a>
                <div class="sb-sidenav-menu-heading">Menu</div>
                <a class="nav-link" href="catalogocuentas.jsp">
                    <div class="sb-nav-link-icon"><i style="color: #F5A623 !important" class="fas fa-credit-card"></i>
                    </div>
                    C�talogo de Cuentas
                </a>
                <a class="nav-link" href="librodiario.jsp">
                    <div class="sb-nav-link-icon"><i style="color: #F5A623 !important"
                            class="fas fa-money-bill-transfer"></i></div>
                    Libro Diario
                </a>
                <a class="nav-link" href="libromayor.jsp">
                    <div class="sb-nav-link-icon"><i style="color: #F5A623 !important" class="fas fa-book"></i></div>
                    Libro Mayor
                </a>
                
                <a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapsePages"
                    aria-expanded="false" aria-controls="collapsePages">
                    <div class="sb-nav-link-icon"><i style="color: #F5A623 !important" class="fas fa-list"></i>
                    </div>
                    Otras Acciones
                    <div class="sb-sidenav-collapse-arrow"><i style="color: #F5A623 !important"
                            class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapsePages" aria-labelledby="headingTwo"
                    data-bs-parent="#sidenavAccordion">
                    <nav class="sb-sidenav-menu-nested nav accordion" id="sidenavAccordionPages">
                        <a class="nav-link" href="#" data-toggle="modal" data-target="#generarcierre">
                            <div class="sb-nav-link-icon"><i style="color: #F5A623 !important" class="fa-regular fa-square-minus"></i></div>
                            Cierre del Ejercicio
                        </a>
                        <a class="nav-link" href="inicioperiodo.jsp">
                            <div class="sb-nav-link-icon"><i style="color: #F5A623 !important" class="fa-regular fa-square-plus"></i></div>
                            Iniciar Ejercicio
                        </a>
                    </nav>
                </div>
                <div class="sb-sidenav-menu-heading">Estados Financieros</div>
                <a class="nav-link" href="balanzacomprobacion.jsp">
                    <div class="sb-nav-link-icon"><i style="color: #F5A623 !important" class="fas fa-coins"></i></div>
                    Balanza de Comprobaci�n
                </a>
                <a class="nav-link" href="estadoresultado.jsp">
                    <div class="sb-nav-link-icon"><i style="color: #F5A623 !important"
                            class="fas fa-chart-column"></i></div>
                    Estado de Resultados
                </a>
                <a class="nav-link" href="balancegeneral.jsp">
                    <div class="sb-nav-link-icon"><i style="color: #F5A623 !important"
                            class="fas fa-scale-balanced"></i></div>
                    Balance General
                </a>
            </div>
        </div>
        
    </nav>
</div>
