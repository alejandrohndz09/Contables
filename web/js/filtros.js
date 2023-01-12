/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var pageactual = '10', pageanterior = '0';//inicio de las paginaciones
var data = {
    accion: 'paginaciondiario',
    anterior: pageanterior,
    actual: pageactual,
    numero: '',
    fecha: '',
    ninicio: '',
    nfin: '',
    finicio: '',
    ffin: ''
};
$(document).ready(function () {
    /*para los filtros*/
    $('#formfiltrobusqueda input[name=numero]').on('keyup', busqueda);
    $('#formfiltrobusqueda input[name=fecha]').on('change', busqueda);
    //rango de número de partidas
    $('#formfiltrobusqueda input[name=ninicio]').on('keyup', rangos);
    $('#formfiltrobusqueda input[name=nfin]').on('keyup', rangos);
    $('#formfiltrobusqueda input[name=finicio]').on('change', rangos);
    $('#formfiltrobusqueda input[name=ffin]').on('change', rangos);
    //para vaciar los demas campos
    $('#formfiltrobusqueda input[name=numero]').on('focus', focusinput);
    $('#formfiltrobusqueda input[name=fecha]').on('focus', focusinput);
    $('#formfiltrobusqueda input[name=ninicio]').on('focus', focusinput);
    $('#formfiltrobusqueda input[name=nfin]').on('focus', focusinput);
    $('#formfiltrobusqueda input[name=finicio]').on('focus', focusinput);
    $('#formfiltrobusqueda input[name=ffin]').on('focus', focusinput);
    paginacion(data);
    filtros(data);
});
/*
 *      FILTRO DE LA PAGINA librodiario.jsp
 *      el filtro se realizara mediante tres <input> con valores distintos
 */

function busqueda(e) {
    var numero = $('#numero').val();
    var fecha = $('#fecha').val();
    if (numero === '' && fecha === '') {
        data.anterior = 0;
        data.actual = 10;
        data.fecha = '';
        data.numero = '';
        data.ninicio = '';
        data.nfin = '';
        data.finicio = '';
        data.ffin = '';
        paginacion(data);
        filtros(data);
    }
    if (numero !== '' && fecha === '') {
        if (!isNaN(numero) && parseInt(numero) > 0) {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = '';
            data.numero = numero;
            data.ninicio = '';
            data.nfin = '';
            data.finicio = '';
            data.ffin = '';
            paginacion(data);
            filtros(data);
        } else {
            swal({
                text: "El número de partida ingresado es incorrecto.",
                icon: 'error'
            });
        }
    }

    if (fecha !== '' && numero === '') {
        data.anterior = 0;
        data.actual = 10;
        data.fecha = fecha;
        data.numero = '';
        data.ninicio = '';
        data.nfin = '';
        data.finicio = '';
        data.ffin = '';
        paginacion(data);
        filtros(data);
    }

    if (fecha !== '' && numero !== '') {
        if (!isNaN(numero) && parseInt(numero) > 0) {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = fecha;
            data.numero = numero;
            data.ninicio = '';
            data.nfin = '';
            data.finicio = '';
            data.ffin = '';
            paginacion(data);
            filtros(data);
        } else {
            swal({
                text: "El número de partida ingresado es incorrecto.",
                icon: 'error'
            });
        }
    }
}

function rangos(e) {
    var actual = e.currentTarget;
    var accion = $(actual).attr('data-accion');
    if (accion === 'niveles') {
        var ninicio = $('#ninicio').val();
        var nfin = $('#nfin').val();
        if (ninicio !== '' && nfin !== '') {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = '';
            data.numero = '';
            data.ninicio = ninicio;
            data.nfin = nfin;
            data.finicio = '';
            data.ffin = '';
            paginacion(data);
            filtros(data);
        } else {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = '';
            data.numero = '';
            data.ninicio = '';
            data.nfin = '';
            data.finicio = '';
            data.ffin = '';
            paginacion(data);
            filtros(data);
        }
    }

    if (accion === 'fechas') {
        var finicio = $('#finicio').val();
        var ffin = $('#ffin').val();
        if (finicio !== '' && ffin !== '') {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = '';
            data.numero = '';
            data.ninicio = '';
            data.nfin = '';
            data.finicio = finicio;
            data.ffin = ffin;
            paginacion(data);
            filtros(data);
        } else {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = '';
            data.numero = '';
            data.ninicio = '';
            data.nfin = '';
            data.finicio = '';
            data.ffin = '';
            paginacion(data);
            filtros(data);
        }
    }

}

function focusinput(e) {
    var actual = e.currentTarget;
    var accion = $(actual).attr("data-accion");
    if (accion === 'search') {
        if ($('#numero').val() !== '' || $('#fecha').val() !== '') {
            if (data.anterior === 0 && data.actual === 10) {
                data.anterior = 0;
                data.actual = 10;
                data.fecha = $('#fecha').val();
                data.numero = $('#numero').val();
                data.ninicio = '';
                data.nfin = '';
                data.finicio = '';
                data.ffin = '';
                paginacion(data);
                filtros(data);
            } else {
                data.fecha = $('#fecha').val();
                data.numero = $('#numero').val();
                data.ninicio = '';
                data.nfin = '';
                data.finicio = '';
                data.ffin = '';
                paginacion(data);
                filtros(data);
            }
        } else {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = '';
            data.numero = '';
            data.ninicio = '';
            data.nfin = '';
            data.finicio = '';
            data.ffin = '';
            paginacion(data);
            filtros(data);
        }

        $('#ninicio').val('');
        $('#nfin').val('');
        $('#finicio').val('');
        $('#ffin').val('');
    }
    if (accion === 'niveles') {
        if ($('#ninicio').val() !== '' && $('#nfin').val() !== '') {
            if (!isNaN($('#ninicio').val()) && !isNaN($('#nfin').val()) && parseInt($('#ninicio').val()) > 0 && parseInt($('#nfin').val()) > 0) {
                if (data.anterior === 0 && data.actual === 10) {
                    data.anterior = 0;
                    data.actual = 10;
                    data.ninicio = $('#ninicio').val();
                    data.nfin = $('#nfin').val();
                    data.fecha = '';
                    data.numero = '';
                    data.finicio = '';
                    data.ffin = '';
                    paginacion(data);
                    filtros(data);
                } else {
                    data.fecha = '';
                    data.numero = '';
                    data.ninicio = $('#ninicio').val();
                    data.nfin = $('#nfin').val();
                    data.finicio = '';
                    data.ffin = '';
                    paginacion(data);
                    filtros(data);
                }
            } else {
                data.anterior = 0;
                data.actual = 10;
                data.fecha = '';
                data.numero = '';
                data.ninicio = '';
                data.nfin = '';
                data.finicio = '';
                data.ffin = '';
                paginacion(data);
                filtros(data);
            }
        } else {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = '';
            data.numero = '';
            data.ninicio = '';
            data.nfin = '';
            data.finicio = '';
            data.ffin = '';
            paginacion(data);
            filtros(data);
        }
        $('#numero').val('');
        $('#fecha').val('');
        $('#finicio').val('');
        $('#ffin').val('');
    }

    if (accion === 'fechas') {
        if ($('#finicio').val() !== '' && $('#ffin').val() !== '') {
            if (data.anterior === 0 && data.actual === 10) {
                data.anterior = 0;
                data.actual = 10;
                data.fecha = '';
                data.numero = '';
                data.ninicio = '';
                data.nfin = '';
                data.finicio = $('#finicio').val();
                data.ffin = $('#ffin').val();
                paginacion(data);
                filtros(data);
            } else {
                data.fecha = '';
                data.numero = '';
                data.ninicio = '';
                data.nfin = '';
                data.finicio = $('#finicio').val();
                data.ffin = $('#ffin').val();
                paginacion(data);
                filtros(data);
            }
        } else {
            data.anterior = 0;
            data.actual = 10;
            data.fecha = '';
            data.numero = '';
            data.ninicio = '';
            data.nfin = '';
            data.finicio = '';
            data.ffin = '';
            paginacion(data);
            filtros(data);
        }
        $('#numero').val('');
        $('#fecha').val('');
        $('#ninicio').val('');
        $('#nfin').val('');
    }
}

function filtros(data) {
    $.post('librodiarioaux.jsp', data, cargarlibrodiario);
}

function cargarlibrodiario(data) {
    var contentdiv = $('#partidasregistradas');
    contentdiv.html(data);
}

function paginacion(data) {
    $.post('paginaciones.jsp', data, cargarpaginacion);
}

function cargarpaginacion(data) {
    var contentdiv = $('#paginaciondiario');
    contentdiv.html(data);
    $('#paginaciondiario').on('click', 'a[data-numpagination]', paginaciondiario);
}

function paginaciondiario(e) {
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
    filtros(data);
    paginacion(data);
}