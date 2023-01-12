/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*función para generar el arbol de carpetas*/
(function ($) {
    $.fn.filetree = function (method) {

        var settings = {// settings to expose
            animationSpeed: 'fast',
            collapsed: true,
            console: false
        }
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
                            .on('dblclick', 'a', function (e) { // Agregaremos una anulación de dobleclic para los enlaces raíz de la carpeta
                                e.preventDefault();
                                $(this).parent().toggleClass('closed').toggleClass('open');
                                return false;
                            });

                    //alert(options.animationSpeed); Are the settings coming in

                });


            }
        }

        if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.on("error", function () {
                console.log(method + " no existe en el complemento explorador de archivos");
            });
        }
    }

}(jQuery));