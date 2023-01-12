/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var pageactual = '10', pageanterior = '0';//inicio de las paginaciones
var contador = 1;
var data = {
    accion: 'paginacionmayor',
    anterior: pageanterior,
    actual: pageactual,
    nivel: 4,
    nombrecuenta: '',
    codigoinicio: '',
    codigofinal: ''
};
$(document).ready(function () {
    $('#filtromayor').on('click', 'a[id=buscar]', busqueda);
    $('#filtromayor').on('click', 'a[id=rango]', busqueda);
    //focus
    //$('#filtromayor').on('change', 'input[id=nivel]', focusinput);
    $('#filtromayor').on('focus', 'input[id=nivel]', focusinput);
    $('#filtromayor').on('focus', 'input[id=cuenta]', focusinput);
    $('#filtromayor').on('focus', 'input[id=cinicio]', focusinput);
    $('#filtromayor').on('focus', 'input[id=cfin]', focusinput);
    //keyup
    $('#filtromayor').on('keyup', 'input[id=nivel]', inputvacio);
    $('#filtromayor').on('keyup', 'input[id=cuenta]', inputvacio);
    $('#filtromayor').on('keyup', 'input[id=cinicio]', inputvacio);
    $('#filtromayor').on('keyup', 'input[id=cfin]', inputvacio);
    paginacion(data);
    filtrosmayor(data);
});

function busqueda(e) {
    var sel = e.currentTarget;
    $(sel).removeAttr("href");
    var tipo = $(sel).attr('id');
    //un unico filtro
    var nivel = $("#nivel").val();
    var cuenta = $("#cuenta").val();
    //rangos
    var cinicio = $("#cinicio").val();
    var cfin = $("#cfin").val();
    if (tipo === 'buscar') {
        $("#cinicio").val('');
        $("#cfin").val('');
        if (nivel === '' && cuenta === '') {
            swal({
                text: "Especifique una búsqueda.",
                icon: 'error'
            });
            data.anterior = 0;
            data.actual = 10;
            data.nivel = 4;
            data.nombrecuenta = '';
            data.codigoinicio = '';
            data.codigofinal = '';
            paginacion(data);
            filtrosmayor(data);
            return;
        }
        if ((cuenta !== '' && typeof cuenta !== 'undefined') && (nivel !== '' && typeof nivel !== 'undefined')) {
            $("#cuenta").val('');
            $("#nivel").val('');
            return;
        }
        if ((cuenta !== '' && typeof cuenta !== 'undefined') && (nivel === '' || typeof nivel === 'undefined')) {
            $("#nivel").val('');
            data.anterior = 0;
            data.actual = 10;
            data.nivel = 0;
            data.nombrecuenta = cuenta;
            data.codigoinicio = '';
            data.codigofinal = '';
            console.log("cuenta:" + cuenta);
            paginacion(data);
            filtrosmayor(data);
        } else {

        }
        if ((nivel !== '' && typeof nivel !== 'undefined') && (cuenta === '' || typeof cuenta === 'undefined')) {
            if (!isNaN(nivel) && parseInt(nivel) > 0) {
                $("#cuenta").val('');
                data.anterior = 0;
                data.actual = 10;
                data.nivel = nivel;
                data.nombrecuenta = '';
                data.codigoinicio = '';
                data.codigofinal = '';
                console.log("nivel:" + nivel);
                paginacion(data);
                filtrosmayor(data);
            } else {
                swal({
                    text: "El nivel ingresado es incorrecto.",
                    icon: 'error'
                });
            }
        }
    } else if (tipo === 'rango') {
        //limpiamos los datos anteriores
        $("#cuenta").val('');
        $("#nivel").val('');
        if (cinicio === '' && cfin === '') {
            swal({
                text: "Especifique un rango de codigos para visualizar los saldos de las cuentas.",
                icon: 'error'
            });
            data.anterior = 0;
            data.actual = 10;
            data.nivel = 4;
            data.nombrecuenta = '';
            data.codigoinicio = '';
            data.codigofinal = '';
            paginacion(data);
            filtrosmayor(data);
            return;
        }
        if (cinicio !== '' && cfin !== '') {
            data.anterior = 0;
            data.actual = 10;
            data.nivel = 0;
            data.nombrecuenta = '';
            data.codigoinicio = cinicio;
            data.codigofinal = cfin;
            paginacion(data);
            filtrosmayor(data);
        }
    }
}

function filtrosmayor(data) {
    $.post('libromayoraux.jsp', data, cargarlibromayor);
}

function paginacion(data) {
    $.post('paginaciones.jsp', data, cargarpaginacion);
}

function cargarlibromayor(data) {
    var contentdiv = $('#libro-mayor');
    contentdiv.html(data);
}
function cargarpaginacion(data) {
    var contentdiv = $('#paginacionmayor');
    contentdiv.html(data);
    $('#paginationmayor').on('click', 'a[data-numpagination]', paginacionmayor);
}
function paginacionmayor(e) {
    e.preventDefault();
    var actual = e.currentTarget;/*obtengo el boton seleccionado*/
    //console.log(actual);
    var divpagination = $(actual).parent();
    //console.log(divpagination);
    var tama = $(divpagination).find('a[data-numpagination]');
    //console.log(tama.length);
    var seleccionado;

    /*variables para sacar los codigos de las pagianciones*/
    var codact = '', codant = '';
    /*variables a pasar por ajax*/
    var anterior, actuall;
    for (var i = 0; i < tama.length; i++) {
        seleccionado = $(tama[i]);
        $(seleccionado).removeClass('active');
        if ($(seleccionado).is(actual)) {
            codant = $(tama[i - 1]).attr('data-numpagination');
            codact = seleccionado.attr('data-numpagination');
            if (typeof codant === 'undefined' || codant === '') {
                anterior = '0';
            } else {
                anterior = codant;
            }
            if (typeof codact === 'undefined' || codact === '') {
                actuall = '0';
            } else {
                actuall = codact;
            }
            //$(actual).addClass('active');/*agregamos la clase active al elemento
            //* <a data-numpagination> seleccionado*/
        }
    }

    if ((typeof anterior === 'undefined' || anterior === '') || (typeof actuall === 'undefined' || actuall === '')) {
        e.preventDefault();
        return;
    }
    data.anterior = anterior;
    data.actual = actuall;
    filtrosmayor(data);
    paginacion(data);
}

function focusinput(e) {
    var actual = e.currentTarget;
    var accion = $(actual).attr("id");
    if (accion !== '' && typeof accion !== 'undefined') {
        if (accion === 'nivel') {
            if ($("#nivel").val() !== '') {
                if (data.anterior === 0 && data.actual === 10) {
                    data.anterior = 0;
                    data.actual = 10;
                    data.nivel = $("#nivel").val();
                    data.nombrecuenta = '';
                    data.codigoinicio = '';
                    data.codigofinal = '';
                    paginacion(data);
                    filtrosmayor(data);
                } else {
                    data.nivel = $("#nivel").val();
                    data.nombrecuenta = '';
                    data.codigoinicio = '';
                    data.codigofinal = '';
                    paginacion(data);
                    filtrosmayor(data);
                }
            } else {
                data.anterior = 0;
                data.actual = 10;
                data.nivel = 4;
                data.nombrecuenta = '';
                data.codigoinicio = '';
                data.codigofinal = '';
                paginacion(data);
                filtrosmayor(data);
            }
            $("#cuenta").val('');
            $("#cinicio").val('');
            $("#cfin").val('');
        } else if (accion === 'cuenta') {
            if ($("#cuenta").val() !== '') {
                if (data.anterior === 0 && data.actual === 10) {
                    data.anterior = 0;
                    data.actual = 10;
                    data.nivel = 0;
                    data.nombrecuenta = $("#cuenta").val();
                    data.codigoinicio = '';
                    data.codigofinal = '';
                    paginacion(data);
                    filtrosmayor(data);
                } else {
                    data.nivel = $("#cuenta").val();
                    data.nombrecuenta = '';
                    data.codigoinicio = '';
                    data.codigofinal = '';
                    paginacion(data);
                    filtrosmayor(data);
                }
            } else {
                data.anterior = 0;
                data.actual = 10;
                data.nivel = 4;
                data.nombrecuenta = '';
                data.codigoinicio = '';
                data.codigofinal = '';
                paginacion(data);
                filtrosmayor(data);
            }
            $("#nivel").val('');
            $("#cinicio").val('');
            $("#cfin").val('');
        } else {
            if ($("#cinicio").val() !== '' && $("#cfin").val() !== '') {
                if (data.anterior === 0 && data.actual === 10) {
                    data.anterior = 0;
                    data.actual = 10;
                    data.nivel = 0;
                    data.nombrecuenta = '';
                    data.codigoinicio = $("#cinicio").val();
                    data.codigofinal = $("#cfin").val();
                    paginacion(data);
                    filtrosmayor(data);
                } else {
                    data.nivel = 0;
                    data.nombrecuenta = '';
                    data.codigoinicio = $("#cinicio").val();
                    data.codigofinal = $("#cfin").val();
                    paginacion(data);
                    filtrosmayor(data);
                }
            } else {
                data.anterior = 0;
                data.actual = 10;
                data.nivel = 4;
                data.nombrecuenta = '';
                data.codigoinicio = '';
                data.codigofinal = '';
                paginacion(data);
                filtrosmayor(data);
            }
            $("#nivel").val('');
            $("#cuenta").val('');
        }
    }
}


function inputvacio(e) {
    var actual = e.currentTarget;
    var accion = $(actual).attr("id");
    if (accion !== '' && typeof accion !== 'undefined') {
        if ((accion === 'nivel' && $("#nivel").val() === '') || (accion === 'cuenta' && $("#cuenta").val() === '')) {
            data.anterior = 0;
            data.actual = 10;
            data.nivel = 4;
            data.nombrecuenta = '';
            data.codigoinicio = '';
            data.codigofinal = '';
            paginacion(data);
            filtrosmayor(data);

        } else if ((accion === 'cfin' && $("#cfin").val() === '') || (accion === 'cinicio' && $("#cinicio").val() === '')) {
            data.anterior = 0;
            data.actual = 10;
            data.nivel = 4;
            data.nombrecuenta = '';
            data.codigoinicio = '';
            data.codigofinal = '';
            paginacion(data);
            filtrosmayor(data);
        }
    }
}



