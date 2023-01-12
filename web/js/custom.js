/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*todo el codigo de este archivo es utilizado nada más solo para el catalogo de cuentas*/
var filetree = {

    name: '.file-tree',
    Node: null

};
$(document).ready(function () {

    //$(".file-tree").filetree();
    $('#form-cuenta').on('submit', guardarCuenta);
    $('#form-eliminar').on('submit', eliminarCuenta);
    // $('a').on('click', cargadoDatosForm);
    $(filetree.name).on('click', 'li a', delegacionevento); /*delegacion de evento al <ul> clickeado segun su clase*/
    iniciacatalogo();

});

function guardarCuenta(e) {

    e.preventDefault(); //Cancela el evento si este es cancelable
    var obj = {
        id: $('#idcuenta').val(),
        nombre: $('#nombre').val(),
        codigo: $('#codigo').val(),
        descripcion: $('#descripcion').val(),
        tiposaldo: $('#tiposaldo option:selected').val() /*obtenemos el valor del select*/
    };
    /*para el registro de los datos si el id esta vacio sera una nueva cuenta a guardar
     * caso contrario si el id lleva algo es edicion de la cuenta seleccionada*/

    //validacion de los datos obtenidos
    if (obj.id === '') {
        if (obj.nombre === '' || obj.codigo === '' || obj.tiposaldo === '') {

            swal({
                title: "Campos vacios",
                text: "Por favor complete los campos:\n - Nombre de la Cuenta\n - Código de la Cuenta\n - Descripción de la Cuenta\n - Saldo de la Cuenta\nSon Obligatorios",
                icon: 'error'
            });
            return;

        } else {

            var guardar = 'guardar';
            $.ajax({
                url: "CtrCuenta",
                type: "POST",
                data: {id: obj.id, guardar: guardar, nombre: obj.nombre, codigo: obj.codigo, descripcion: obj.descripcion, tiposaldo: obj.tiposaldo},
                success: function (datos) {
                    swal("La cuenta " + obj.nombre + " ha sido registrada\nsatisfactoriamente", {
                        icon: "success"
                    });
                    //$('#arbol-cuentas').html(datos);
//                    $('#arbol-cuentas').load('prueba.jsp #arbol-cuentas');
                    iniciacatalogo();
                }
            });

        }
    } else if (obj.id !== '') {

        if (obj.nombre === '' || obj.codigo === '' || obj.tiposaldo === '') {

            swal({
                title: "Campos vacios",
                text: "Por favor complete los campos:\n - Nombre de la Cuenta\n - Código de la Cuenta\n - Descripción de la Cuenta\n - Saldo de la Cuenta\nSon Obligatorios",
                icon: 'error'
            });
            return;

        } else {

            var buscar = true;
            buscar = valida(obj);/*llamamos la funcion valida para el caso de edicion de datos*/
            if (!buscar) {

                swal("El nombre/código de la cuenta que intenta registar ya existe", {
                    icon: "error"
                });
                return;

            } else {

                var guardar = 'editar';
                $.ajax({
                    url: "CtrCuenta",
                    type: "POST",
                    data: {id: obj.id, guardar: guardar, nombre: obj.nombre, codigo: obj.codigo, descripcion: obj.descripcion, tiposaldo: obj.tiposaldo},
                    success: function () {
                        swal("La cuenta " + obj.nombre + " ha sido modificada\nsatisfactoriamente", {
                            icon: "success"
                        });
                        iniciacatalogo();
                    }
                });

            }

        }

    }

    limpiarcamposcuenta();
}

function limpiarcamposcuenta() {
    /*
     *limpiando los campos del formulario 
     */
    $('#idcuenta').val('');
    $('#nombre').val('');
    $('#codigo').val('');
    $('#descripcion').val('');
    /*
     * deseleccionamos el valor del select al que hemos tenido acceso
     */
    $('#tiposaldo option:selected').prop('selected', false);
}

/*metodo de cargado de datos en los inputs correspondientes*/
function cargadoDatosForm(obj) {

    /*obteniendo los valores de la etiqueta 'a' clikeada para
     * pasarlos a los input correspondientes*/
    /*var nombrecuenta = $(this).data('nombrecuenta');
     var codigocuenta = $(this).data('codigocuenta');
     var descripcioncuenta = $(this).data('descripcioncuenta');
     var tipocuenta = $(this).data('tipocuenta');*/
    /*asignamos los valores a los input's correspondientes del objeto recibido*/

    var newcod = '';
    document.getElementById('idcuenta').value = obj.idcuenta;
    document.getElementById('nombre').value = obj.nombrecuenta;

    if (obj.codigocuenta.length === 1) {

        newcod = obj.codigocuenta.substr(0, 1);

    } else if (obj.codigocuenta.length === 2) {

        newcod = obj.codigocuenta.substr((obj.codigocuenta.length - 1), obj.codigocuenta.length);

    } else if (obj.codigocuenta.length > 2) {

        newcod = obj.codigocuenta.substr((obj.codigocuenta.length - 2), obj.codigocuenta.length);

    }

    document.getElementById('codigo').value = newcod;

    if (obj.descripcioncuenta === 'null') {

        document.getElementById('descripcion').value = "";

    } else {

        document.getElementById('descripcion').value = obj.descripcioncuenta;

    }

    if (obj.tipocuenta === '+') {

        $('#tiposaldo option:eq(1)').prop('selected', true);

    } else if (obj.tipocuenta === '-') {

        $('#tiposaldo option:eq(2)').prop('selected', true);

    } else if (obj.tipocuenta === '=') {

        $('#tiposaldo option:eq(3)').prop('selected', true);

    }
}

function delegacionevento(evt) {

    var actual = evt.currentTarget; //e.currentTarget se referira al padre <ul> en este contexto
    evt.preventDefault(); /*Cancela el evento si este es cancelable, sin detener el resto del 
     funcionamiento del evento, es decir, puede ser llamado de nuevo.*/
    seleccioncuenta(actual); /*mandamos como parametro a actual que hace referencia al <ul> padre*/

}

function seleccioncuenta(actual) {

    var obj = {
        idcuenta: $(actual).attr("data-idcuenta"),
        nombrecuenta: $(actual).attr("data-nombrecuenta"),
        codigocuenta: $(actual).attr("data-codigocuenta"),
        descripcioncuenta: $(actual).attr("data-descripcioncuenta"),
        tipocuenta: $(actual).attr('data-tipocuenta')
    };

    if (filetree.Node === null) {

        $(actual).addClass("focus");
        $(actual).focus();
        filetree.Node = actual;

    } else {

        $(filetree.Node).removeClass('focus');
        $(actual).addClass('focus');
        $(actual).focus();
        filetree.Node = actual;

    }
    cargadoDatosForm(obj);/*llamo el formulario para cargar los datos respectivos
     * segun se seleccione una cuenta*/
}
/*
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
*/
/*con esta fuccion removeremos la clase focus que es la que permite dejar seleccionada
 * la cuenta al clickearla*/
function deseleccionarCuenta() {
    $(filetree.name + ' .focus').removeClass('focus');/*removemos la clase focus*/
    filetree.Node = null;/*inicializamos el nodo en null*/
    limpiarcamposcuenta();/*llamamos la funcion para limpiar los campos al deseleccionar la cuenta*/
}

/*metodo para eliminar la cuenta que se seleccione del .file-tree*/
function eliminarCuenta(evt) {
    evt.preventDefault(); //Cancela el evento si este es cancelable
    var obj = {
        id: $('#idcuenta').val(),
        nombre: $('#nombre').val(),
        codigo: $('#codigo').val(),
        descripcion: $('#descripcion').val(),
        tiposaldo: $('#tiposaldo option:selected').val() /*obtenemos el valor del select*/
    };

    if (obj.id === '' || obj.nombre === '' || obj.codigo === '') {

        swal({
            text: "Seleccione una cuenta a eliminar",
            icon: 'error'
        });

    } else {

        swal({
            text: "Ha seleccionado una cuenta",
            icon: 'success'
        });

    }

}

function valida(obj) {
    /*VERIFICANDO QUE EL CÓDIGO O EL NOMBRE DE LA CUENTA  NO DEBEN EXISTIR AL AGREGAR LA CUENTA*/
    var catalogo = $(filetree.Node).parent().parent().find('> li > a');
    var buscar = true, code = '';
    for (i = 0; i < catalogo.length; i++) {
        if (filetree.Node !== catalogo[i]) {

            if ($(catalogo[i]).attr('data-codigocuenta').length === 1) {

                code = $(catalogo[i]).attr('data-codigocuenta').substr(0, 1);

            } else if ($(catalogo[i]).attr('data-codigocuenta').length === 2) {

                code = $(catalogo[i]).attr('data-codigocuenta').substr($(catalogo[i]).attr('data-codigocuenta').length - 1, $(catalogo[i]).attr('data-codigocuenta').length);

            } else if ($(catalogo[i]).attr('data-codigocuenta').length > 2) {

                code = $(catalogo[i]).attr('data-codigocuenta').substr($(catalogo[i]).attr('data-codigocuenta').length - 2, $(catalogo[i]).attr('data-codigocuenta').length);

            }

            if (obj.codigo === code) {

                buscar = false;

            }
            if (obj.nombre === $(catalogo[i]).attr('data-nombrecuenta')) {

                buscar = false;

            }
        }
    }
    return buscar;
}

function iniciacatalogo(){
    $.post('catalogo.jsp',cargarcatalogo);
}

function cargarcatalogo(data){
    var content=$('#cc');
    content.html(data);
}
