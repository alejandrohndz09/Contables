
<%@page import="java.util.ArrayList"%>
<%@page import="logica.logicaContables"%>
<%@page import="entidades.Cuenta"%>
<%
    logicaContables lc = new logicaContables();
    ArrayList<Cuenta> c = (ArrayList<Cuenta>) lc.listadocuentas();
    if (c.size() > 0) {
        for (Cuenta cuenta : c) {
            recursivo(cuenta, out, lc);
        }
    } else {
        out.print("No hay cuentas registradas");
    }
%>
<%!
    public void recursivo(Cuenta c, JspWriter out, logicaContables lc) {
        try {           
            if (c != null) {
                out.print("<li>"
                        + "<a  data-type=\"cuenta\" class='noselect'"
                        + "data-idcuenta=\"" + c.getIdcuenta() + "\""
                            + " data-codigocuenta=\"" + c.getCodigocuenta() + "\""
                            + " data-nombrecuenta=\"" + c.getNombrecuenta() + "\""
                            + " data-descripcioncuenta=\"" + c.getDescripcioncuenta() + "\""
                            + " data-tipocuenta=\"" + c.getTipocuenta() + "\">");
                    if (lc.buscarcodigo("", c.getCodigocuenta() + "R", 0) != null) {
                        out.print( "<i class=\"fa fa-folder\" style=\"color:#061b22 !important\"></i> "+
                                   c.getCodigocuenta() + " - " + "R" + c.getNombrecuenta() +"</a>");

                    } else {
                        out.print("<i class=\"fa fa-folder\" style=\"color:#061b22 !important\"></i> " +c.getCodigocuenta() + " - " +  c.getNombrecuenta()+ "</a>");
                    }
                    if (c.getCuentinternas().size() > 0) {
                        out.print("<ul>");
                        for (Object obj : c.getCuentinternas()) {
                            recursivo((Cuenta) obj, out, lc);
                        }
                        out.print("</ul>");
                    }
                    out.print("</li>");
            }
        } catch (Exception e) {
            /*no se puede poner mensaje de error*/
        }
    }
%>

<script type="text/javascript">
    $(document).ready(function () {
        /*Esta codificación de javascript es necesaria para esta pagina.jsp incluida dentro de ella,
         * ya que cada vez que agreguemos o eliminemos o editemos esta pagina sera llamada via ajax
         * por lo cual si este script esta en el custom no sera funcionable ya que custom hace
         * referencia a la pagina original donde sera incluida esta.*/
        $(".file-tree").filetree();

    });
    $.fn.filetree = function (method) {
        var settings = {// settings to expose
            animationSpeed: 'fast',
            collapsed: true,
            console: false
        };
        var methods = {
            init: function (options) {
// Obtenga configuraciones estándar y fusione valores pasados
                var options = $.extend(settings, options);
                // Esto servira para cada arbol encontrado en catalagocuentas.jsp
                return this.each(function () {

                    var $fileList = $(this);
                    $fileList
                            .addClass('file-list')
                            .find('li')
                            .has('ul') // Cualquier li que tenga una lista dentro es una raíz de carpeta
                            .addClass('folder-root closed')
                            .on('dblclick', 'a[data-type="cuenta"]', function (e) { // Agregaremos una anulación de dobleclic para los enlaces raíz de la carpeta
                                e.preventDefault();
                                $(this).parent().toggleClass('closed').toggleClass('open');
                                console.log(e);
                                return false;
                            });
                });
            }
        };

        if (typeof method === 'object' || !method) {

            return methods.init.apply(this, arguments);

        } else {

            $.on("error", function () {
                console.log(method + " no existe en el complemento para generar el catalogo");
            });

        }
    };
</script>