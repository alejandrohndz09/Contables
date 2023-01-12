/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*todo el codigo generado en este archivo sera aplicable solo a la vista de las partidas*/

var partida = new Object();
partida.actual = null;
var numpartida = -1;
var nombrec = '', codigoc = '';
var validasaldo = false;
var i = 0; //contador de errores

$(document).ready(function () {
    $('#partidas').on('click', 'a[data-type="btn-agregar"]', debehaber); /*agregamos debes y aber*/
    $('#partidas').on('click', 'a[data-type="quitar"]', quitarcuenta); /*quitamos debes y haber*/
    $('#buscarcuenta input[name=codigocuenta]').on('input focus', obtenervalor); /*busqueda instantanea de cuentas mediante focus*/
    $('#cuentasencontradas').on('click', 'div[data-type="result"]', mostrarvalor); /*para el focus de las cuentas encontradas*/
    $('#partidas').on('click', 'a[data-codigo]', cargarcuentas); /*cargara los valores de las cuentas seleccionadas a los <a>*/
    $('#registropartidas').on('submit', registrar); /*envio de los datos al servlet*/
    $('#limpiarbusqueda').on('click', 'a[data-type="btn-limpiar"]', limpiarbusquedaajax); /*limpiar busqueda ajax*/

    /*metodos para el focus de los saldos de los input*/
    $('#partidas').on('keyup', 'input[id=cargo]', comprobarSaldos);
    $('#partidas').on('keyup', 'input[id=abono]', comprobarSaldos);
    /*validacion de los saldos de las cuentas*/
    $('#partidas').on('blur', 'input[id=cargo]', validaCuentasSaldos);
    $('#partidas').on('blur', 'input[id=abono]', validaCuentasSaldos);
    $('#partidas').on('keyup', 'input[id=cargo]', validaCuentasSaldos);
    $('#partidas').on('keyup', 'input[id=abono]', validaCuentasSaldos);
    $('#partidas').on('click', 'a[data-codigo]', validaCuentasSaldos);
    agregarpartidas();
    obtenervalor();
});
function agregarpartidas() {
    $.post('nuevapartida.jsp', {'opcion': 'nuevapartida', 'num-partida': numpartida}, cargarpartida);
}

function cargarpartida(data) {
    $('#partidas').append(data);
    var div = $('#partidas div[data-type="partida"]:last');
    //console.log(div);
    var pos = $(div).offset().top;
    numpartida = parseInt($(div).attr('data-num-partida'));
    $('html, body').animate({scrollTop: pos - 100}, 500); // will take two seconds to scroll to the element
}

function debehaber(e) {
    e.preventDefault();
    partida.actual = $(e.currentTarget).parent().parent().parent(); /*haciendo referencia al
     * div que solo contiene nada mas los componentes de obtencioon de datos, ya que si se hace referencia al
     * div padre partidas, al ejecutar esta funcion el btn-agregar, agregara nuevos componentes a todos los
     * div existentes que solo contienen componentes de obtención de datos.
     * hacer pruebas con partida.actual = $(e.currentTarget).parent().parent().parent().parent();*/
    //console.log(partida.actual);
    $.post('nuevapartida.jsp', {'opcion': 'nuevodebehaber', 'num-partida': numpartida}, cargardebehaber);
}

function cargardebehaber(data) {
    var contenedor;
    //console.log(partida.actual);
    if (partida.actual !== null) {
        contenedor = $(partida.actual).find('div[data-type="lista-ca"]');
        $(contenedor).append(data);
    }
}

function limpiarbusquedaajax() {
    var busqueda = $('#buscarcuenta input[name="codigocuenta"]').val('');
    busqueda.focus();
    //console.log(busqueda);
    $.post('buscarcuenta.jsp', {'codcuenta': busqueda}, mostrarbusqueda);
}

function obtenervalor() {
    var busqueda = $('#codigocuenta').val();
    //console.log(busqueda);
    $.post('buscarcuenta.jsp', {'codcuenta': busqueda}, mostrarbusqueda);
}

function mostrarbusqueda(data) {
    var cuentasencontradas = $('#cuentasencontradas');
    $(cuentasencontradas).html(data);
}

function quitarcuenta(e) {
    e.preventDefault();
    var actual = e.currentTarget; /*obtengo el boton seleccionado*/
    //console.log(actual);/*comprobacion de lo que e obtenido en consola*/
    var partida = $(actual).parent().parent().parent().parent(); /*hago referencia a mi div que contiene el form*/
    var listaca = $(partida).find('div[data-type="lista-ca"] div[data-type="ca"]'); /*obtengo mi lista de debes y haber que 
     * se encuentran en el form*/
    //console.log(listaca.length);
    var seleccionado;
    for (var i = 0; i < listaca.length; i++) {
        if ($(listaca).length > 2) {
            seleccionado = $(listaca[i]).find('a[data-type="quitar"]');
            //console.log(seleccionado);
            if ($(seleccionado).is(actual)) {
                $(listaca[i]).remove();
            }
        } else {
            swal({
                title: "Error",
                text: "No se puede eliminar la cuenta, debe haber \n" +
                        "más de una cuenta para la transacción",
                icon: 'error'
            });
            return;
        }
    }
    comprobarSaldos();
}

function registrar(e) {
    /*variables a usar*/
    var ca = new Array(); /*alamacenara todos los montos debes y haber correspondientes del form*/
    var opca = new Array(); /*alamacenara las opciones de los ca, si son cargos o abonos*/
    var codcuentas = new Array(); /*alamacenara todos los codigos de las cuentas correspondientes*/
    var fecha; /*alamacenara la fecha del form de las partidas*/
    var destransaccion; /*almacenara todos las descripciones por cada transaccion*/
    var numpartida; /*almacenara el número de la partida a registrar*/

    /* asignadas a cada debe haber*/
    var arreglo2; /*almacenara el tamaño de debes*/
    var arreglo3; /*almacenara el tamaño de haber*/
    var arreglo1; /*almacenara el tamaño de codigos cuentas*/
    var arregloiconos; /*almacenara las cuenta que no pueden ser cargadas o abonadas por su saldo disponibles o simplemente
     * no realizan transacciones*/
    var estado = false, saldosceros = false; //si hay iconos de error el estado sera true
    var i = 0, rcuenta = 0; /*contador*/
    /*variables adicionales para las validaciones de los debe y haber y los codigos*/
    var s1, s2, s3, s4;
    /*variables para cuadrar las partidas*/
    var tothaber = 0, totdebe = 0, debe = 0, haber = 0, codigos;
    var mostrartotales;
    var partidaregistrar = $('#partidas div[data-type="partida"]');
    numpartida = $(partidaregistrar).find('span[data-type="numeropartida"]');
    fecha = $(partidaregistrar).find('input[name="fecharegistro"]');
    destransaccion = $(partidaregistrar).find('textarea[name="transaccion"]');
    arreglo1 = $(partidaregistrar).find('div[data-type="lista-ca"] div[data-type="ca"] a[data-codigo]');
    arreglo2 = $(partidaregistrar).find('div[data-type="lista-ca"] div[data-type="ca"] input[name="cargo"]');
    arreglo3 = $(partidaregistrar).find('div[data-type="lista-ca"] div[data-type="ca"] input[name="abono"]');
    arregloiconos = $(partidaregistrar).find('span[id=icono-mensaje]').find('li[class="fa fa-remove"]');
    console.log(arregloiconos);
    //console.log(partidaregistrar);

    /*validación de que por cada cuenta solo debe estar lleno un debe o haber, forzando
     * que ambas partidas sean contrarias ejemplo:
     *  cuenta                 |     debe       |    haber
     *  -----------------------------------------------------
     *  sueldo de vendedores   | $ 100.00       |
     *  bancos                 |                | $100.00
     * 
     *                               ó
     *                               
     *  cuenta                 |     debe       |    haber
     *  -----------------------------------------------------
     *  letras de cambio       | $ 100.00       |
     *  bancos                 | $ 100.00        | 
     *  clientes               |                | $200
     *  
     * el arreglo2 contiene el total de debes y el arreglo3 el total de haber
     * esta validacion es repetitiva pero es necesaria para la visualización
     * */

    if (arreglo2.length === arreglo3.length) {

        for (i = 0; i < arreglo2.length; i++) {/*recorremos el tamaño del arreglo2 o bien
         * puede ser el arreglo3 ya que ambos deben tener la misma cantidad de elementos*/
            debe = $(arreglo2[i]).val();
            haber = $(arreglo3[i]).val();
            //console.log(debe + ' ' + haber);
            if ((debe.length === 0 && haber.length === 0) || (debe.length > 0 && haber.length > 0)) {
                $(arreglo2[i]).addClass('is-invalid');
                $(arreglo3[i]).addClass('is-invalid');
            } else {
                $(arreglo2[i]).removeClass('is-invalid');
                $(arreglo3[i]).removeClass('is-invalid');
            }

            if (debe === '') {
                debe = 0;
            } else if (haber === '') {
                haber = 0;
            }
            debe = parseFloat(debe);
            haber = parseFloat(haber);
            if (isNaN(debe) || isNaN(haber)) {
                $(arreglo2[i]).addClass('is-invalid');
                $(arreglo3[i]).addClass('is-invalid');
            } else {
                if ((parseFloat(debe) <= 0 && $(arreglo2[i]).length === 0) || (parseFloat(haber) <= 0 && $(arreglo3[i]).length === 0)) {
                    $(arreglo2[i]).addClass('is-invalid');
                    $(arreglo3[i]).addClass('is-invalid');
                }
            }
            /*acumulamos los saldos obtenidos*/
            tothaber = tothaber + haber;
            totdebe = totdebe + debe;
        }
        mostrartotales = $(partidaregistrar).find('div[data-totales="totales"]');
        //console.log(mostrartotales);
        if (totdebe !== tothaber) {
            for (var i = 0; i < arreglo3.length; i++) {
                if (parseFloat($(arreglo2[i]).val()) > 0 && $(arreglo3[i]).val() === '') {
                    $(arreglo3[i]).removeClass('is-valid');
                    $(arreglo3[i]).addClass('is-invalid');
                    $(arreglo2[i]).addClass('is-invalid');
                } else if (parseFloat($(arreglo3[i]).val()) > 0 && $(arreglo2[i]).val() === '') {
                    $(arreglo2[i]).removeClass('is-valid');
                    $(arreglo2[i]).addClass('is-invalid');
                    $(arreglo3[i]).addClass('is-invalid');
                }
            }
            e.preventDefault();
            swal({
                title: "Error",
                text: "La transacción no esta cuadrada!!! Por \n\
                    favor revise los montos ingresados ",
                icon: 'error'
            });
            $(mostrartotales).find('div[data-debe="topdebe"]').text('Transacción fallida').addClass('text-danger');
            $(mostrartotales).find('div[data-haber="tophaber"]').text('Transacción fallida').addClass('text-danger');
            return;
        } else {
            for (var i = 0; i < arreglo3.length; i++) {
                if (parseFloat($(arreglo2[i]).val()) > 0 && $(arreglo3[i]).val() === '') {
                    $(arreglo2[i]).removeClass('is-invalid');
                    $(arreglo2[i]).addClass('is-valid');
                    $(arreglo3[i]).removeClass('is-valid');
                    saldosceros = false;
                } else if (parseFloat($(arreglo3[i]).val()) > 0 && $(arreglo2[i]).val() === '') {
                    $(arreglo3[i]).removeClass('is-invalid');
                    $(arreglo3[i]).addClass('is-valid');
                    $(arreglo2[i]).removeClass('is-valid');
                    saldosceros = false;
                } else if ((parseFloat($(arreglo2[i]).val()) === 0 && $(arreglo3[i]).val() === '') || parseFloat($(arreglo3[i]).val()) === 0 && $(arreglo2[i]).val() === '') {
                    $(arreglo3[i]).removeClass('is-valid');
                    $(arreglo2[i]).removeClass('is-valid');
                    $(arreglo3[i]).addClass('is-invalid');
                    $(arreglo2[i]).addClass('is-invalid');
                    saldosceros = true;
                }
            }
            if (saldosceros === false) {
                $(mostrartotales).find('div[data-debe="topdebe"]').removeClass('text-danger');
                $(mostrartotales).find('div[data-haber="tophaber"]').removeClass('text-danger');
                $(mostrartotales).find('div[data-debe="topdebe"]').text('$ ' + totdebe).addClass('text-success');
                $(mostrartotales).find('div[data-haber="tophaber"]').text('$ ' + tothaber).addClass('text-success');
            } else {
                e.preventDefault();
                swal({
                    title: "Error",
                    text: "Los montos ingresados son incorrectos.",
                    icon: 'error'
                });
                $(mostrartotales).find('div[data-debe="topdebe"]').text('Transaccion fallida').addClass('text-danger');
                $(mostrartotales).find('div[data-haber="tophaber"]').text('Transaccion fallida').addClass('text-danger');
                return;
            }
        }
    }

    /*
     * VALIDACIONES NADA MÁS SOLO DE LOS MONTOS 
     *        DEL FORMULARIO CON ALERTAS
     */

//console.log(fecha.length);
//console.log(destransaccion.length);
//console.log(arreglo1.length);/*comprobando el tamaño del arreglo cargo*/
//console.log(arreglo2.length);/*comprobando el tamaño del arreglo abono*/
//console.log(arreglo3.length);

    /*condición de si hay la misma cantidad de debes y haber y codigos y destransacciones*/
    if ((arreglo1.length !== arreglo2.length || arreglo1.length !== arreglo3.length || arreglo2.length !== arreglo3.length) || (arreglo1.length === 0 || arreglo2.length === 0 || arreglo3.length === 0 || fecha.length === 0 || destransaccion.length === 0)) {
        e.preventDefault();
        swal({
            title: "Error",
            text: "Se han eliminado elementos del formulario",
            icon: 'error'
        });
        return;
    }
    /*condicion de que solo debe estar lleno un debe o un haber de una cuenta en especifico*/
    for (i = 0; i < arreglo2.length; i++) {
        s1 = $(arreglo2[i]).val(); //debe
        s2 = $(arreglo3[i]).val(); //haber
        s4 = $(arreglo1[i]).attr('data-codigo'); //codigos
        if (s1.length === 0 && s2.length === 0) {/*si uno de los componentes correspondientes
         * a los saldos esta vacio se ejecutara esta condición*/
            e.preventDefault();
            swal({
                title: "Error",
                text: "Por favor llene los campos correspondientes a los montos",
                icon: 'error'
            });
            return;
        } else if (s1.length > 0 && s2.length > 0) {/*si los dos componentes correspondientes
         * a los saldos esta con datos se ejecutara esta condición*/
            e.preventDefault();
            swal({
                title: "Error",
                text: "Solo debe existir un cargo o un abono",
                icon: 'error'
            });
            return;
        }

        if (s4 === '' || s4.length === 0 || s4 === null) {
            e.preventDefault();
            swal({
                title: "Error",
                text: "Verifique que todos los cargos y abonos \n\
                        tengan su respectiva cuenta asignada",
                icon: 'error'
            });
            return;
        }

        for (var n = 0; n < arreglo1.length; n++) {
            if (s4 === $(arreglo1[n]).attr('data-codigo')) {
                rcuenta++;
                //console.log(s4);
            }
        }

//cargando y abonando las cuentas
        if (parseFloat(s1) > 0) {
            opca.push('c');
            s3 = parseFloat(s1);
        } else if (parseFloat(s2) > 0) {
            opca.push('a');
            s3 = parseFloat(s2);
        }
        ca.push(s3); /*almacenamos los cargos y los abonos en el array*/
        //codcuentas.push(s4);/*almacenamos los codigos respectivos al arreglo*/
    }



    /*
     *  VALIDACIÓN DE LAS CUENTAS,  solo puede existir una cuenta con el mismo nombre
     */
//console.log(rcuenta);
    if (rcuenta > arreglo1.length) {
        e.preventDefault();
        swal({
            title: "Error",
            text: "Las cuentas solo deben ser agregadas\n\
                         una vez, no pueden repetirse.",
            icon: 'error'
        });
        return;
    } else {
        for (i = 0; i < arreglo1.length; i++) {
            s4 = $(arreglo1[i]).attr('data-codigo'); //codigos
            codcuentas.push(s4); /*almacenamos los codigos respectivos al arreglo*/
        }
        rcuenta = 0;
    }

    /*
     *VALIADACION DE LOS SALDOS DISPONIBLES DE LAS CUENTAS PARA PODER CARGARLAS O ABONARLAS, Ó SOLO CARGARLAS O SOLO ABONARLAS 
     */

    if (arregloiconos.length > 0) {
        estado = true;
    } else {
        estado = false;
    }

    if (estado === true) {
        e.preventDefault();
        swal({
            title: "Error",
            text: "Alguna de las cuentas de la trasacción no cuenta con la disponibilidad de saldo para la operación, o la cuenta solo puede ser cargada ó solo abonada.",
            icon: 'error'
        });
        return;
    }
    if (destransaccion.val() === '') {
        e.preventDefault();
        swal({
            title: "Error",
            text: "Asigene una descripción a la transacción",
            icon: 'error'
        });
        return;
    }

    e.preventDefault();
    var data = {
        'codcuentas[]': codcuentas,
        'cargoabono[]': ca,
        'opcionca[]': opca,
        'numpartida': $(numpartida).text().trim(),
        'fecha': $(fecha).val(),
        'transaccion': $(destransaccion).val()
    };
    console.log(data);
    $.ajax({
        url: "CtrPartida",
        type: "POST",
        data: data,
        success: function (data) {
            var json = JSON.parse(data);
            ejecutamensaje(json.title, json.text, json.icon);
            setTimeout("location.href='" + "registrarpartidas.jsp" + "'", 2000);
            limpiarform();
        }
    });
}

function ejecutamensaje(title, text, icon) {
    swal({
        title: title,
        text: text,
        icon: icon
    });
}


function mostrarvalor(e) {
    /*
     * la variable divresult contiene al <div> de los resultados de las cuentas mediante ajax
     * codigoc y nombrec almacenaran los valores correspondientes en caso de que el <div data-type="result"> exista
     * @type $
     */
    var divresult = $(e.currentTarget);
    //console.log(divresult.length);
    codigoc = $(divresult).find('input[name="codcuenta"]').val();
    nombrec = $(divresult).find('input[name="nombrecuenta"]').val();
    //console.log(codigoc);
    //console.log(nombrec);
}

function cargarcuentas(e) {
    e.preventDefault(); /*obtenemos el elemento <a> clickeado*/
    /* codigocuenta y nombrecuenta obtendran los valores de las variables globales codigoc y nombrec
     * contrescuen obtendra el tamaño de las cuentas filtradas en caso de que se halla llevado acabo la búsqueda
     * @type String
     */
    var codigocuenta, nombrecuenta;
    var datacuenta = $(e.currentTarget);
    var contrescuen = $('#cuentasencontradas div[data-type="result"]'); /*agarramos el arreglo de 
     * cuentas encontradas mediante la busqueda de ajax*/

    codigocuenta = codigoc;
    nombrecuenta = nombrec;
    //console.log(codigocuenta + ' ' + nombrecuenta);
    //console.log(datacuenta);
    if (contrescuen.length > 0) {//si elcontrescuen es mayor a 0 quiere decir que se a procedido a buscar cuentas para la transacción

        if ((nombrec === '' || codigoc === '') || (typeof nombrec === 'undefined' || typeof codigoc === 'undefined')
                || (nombrec === null || codigoc === null)) {//validaciones respectivas de las variables globales
            swal({
                title: "Error",
                text: "Primero seleccione la cuenta a asignar",
                icon: 'error'
            });
            return;
        } else {
            $(datacuenta).attr('data-codigo', codigocuenta); /*agregamos el codigó ejemplo <a data-codigo="1101">*/
            $(datacuenta).text(nombrecuenta); /*seteamo el nombre al elemento <a> ejemplo <a data-type"1101">Caja</a>*/
        }

    } else {
        swal({
            title: "Error",
            text: "Antes debe buscar las cuentas necesarias\n\
                     para poder realizar la transacción",
            icon: 'error'
        });
        return;
    }
}

function  comprobarSaldos() {
    var arreglo2; /*almacenara el tamaño de debes*/
    var arreglo3; /*almacenara el tamaño de haber*/
    var tothaber = 0, totdebe = 0, debe = 0, haber = 0, codigos;
    var mostrartotales;
    var saldosceros = false;
    var partidaregistrar = $('#partidas div[data-type="partida"]');
    arreglo2 = $(partidaregistrar).find('div[data-type="lista-ca"] div[data-type="ca"] input[name="cargo"]');
    arreglo3 = $(partidaregistrar).find('div[data-type="lista-ca"] div[data-type="ca"] input[name="abono"]');
    if (arreglo2.length === arreglo3.length) {

        for (i = 0; i < arreglo2.length; i++) {/*recorremos el tamaño del arreglo2 o bien
         * puede ser el arreglo3 ya que ambos deben tener la misma cantidad de elementos*/
            debe = $(arreglo2[i]).val();
            haber = $(arreglo3[i]).val();
            //console.log(debe + ' ' + haber);
            if ((debe.length === 0 && haber.length === 0) || (debe.length > 0 && haber.length > 0)) {
                $(arreglo2[i]).addClass('is-invalid');
                $(arreglo3[i]).addClass('is-invalid');
            } else {
                $(arreglo2[i]).removeClass('is-invalid');
                $(arreglo3[i]).removeClass('is-invalid');
            }

            if (debe === '') {
                debe = 0;
            } else if (haber === '') {
                haber = 0;
            }
            debe = parseFloat(debe);
            haber = parseFloat(haber);
            if (isNaN(debe) || isNaN(haber)) {
                $(arreglo2[i]).addClass('is-invalid');
                $(arreglo3[i]).addClass('is-invalid');
            } else {
                if ((parseFloat(debe) <= 0 && $(arreglo2[i]).length === 0) || (parseFloat(haber) <= 0 && $(arreglo3[i]).length === 0)) {
                    $(arreglo2[i]).addClass('is-invalid');
                    $(arreglo3[i]).addClass('is-invalid');
                }
            }
            /*acumulamos los saldos obtenidos*/
            tothaber = tothaber + haber;
            totdebe = totdebe + debe;
        }
        mostrartotales = $(partidaregistrar).find('div[data-totales="totales"]');
        //console.log(mostrartotales);
        if (totdebe !== tothaber) {
            for (var i = 0; i < arreglo3.length; i++) {
                if (parseFloat($(arreglo2[i]).val()) > 0 && $(arreglo3[i]).val() === '') {
                    $(arreglo3[i]).removeClass('is-valid');
                    $(arreglo3[i]).addClass('is-invalid');
                    $(arreglo2[i]).addClass('is-invalid');
                } else if (parseFloat($(arreglo3[i]).val()) > 0 && $(arreglo2[i]).val() === '') {
                    $(arreglo2[i]).removeClass('is-valid');
                    $(arreglo2[i]).addClass('is-invalid');
                    $(arreglo3[i]).addClass('is-invalid');
                }
            }
            $(mostrartotales).find('div[data-debe="topdebe"]').text('Transaccion fallida').addClass('text-danger');
            $(mostrartotales).find('div[data-haber="tophaber"]').text('Transaccion fallida').addClass('text-danger');
            return;
        } else {
            for (var i = 0; i < arreglo3.length; i++) {
                if (parseFloat($(arreglo2[i]).val()) > 0 && $(arreglo3[i]).val() === '') {
                    $(arreglo2[i]).removeClass('is-invalid');
                    $(arreglo2[i]).addClass('is-valid');
                    $(arreglo3[i]).removeClass('is-valid');
                } else if (parseFloat($(arreglo3[i]).val()) > 0 && $(arreglo2[i]).val() === '') {
                    $(arreglo3[i]).removeClass('is-invalid');
                    $(arreglo3[i]).addClass('is-valid');
                    $(arreglo2[i]).removeClass('is-valid');
                } else if ((parseFloat($(arreglo2[i]).val()) === 0 && $(arreglo3[i]).val() === '') || parseFloat($(arreglo3[i]).val()) === 0 && $(arreglo2[i]).val() === '') {
                    $(arreglo3[i]).removeClass('is-valid');
                    $(arreglo2[i]).removeClass('is-valid');
                    $(arreglo3[i]).addClass('is-invalid');
                    $(arreglo2[i]).addClass('is-invalid');
                    saldosceros = true;
                }
            }
            if (saldosceros === false) {
                $(mostrartotales).find('div[data-debe="topdebe"]').removeClass('text-danger');
                $(mostrartotales).find('div[data-haber="tophaber"]').removeClass('text-danger');
                $(mostrartotales).find('div[data-debe="topdebe"]').text('$ ' + totdebe).addClass('text-success');
                $(mostrartotales).find('div[data-haber="tophaber"]').text('$ ' + tothaber).addClass('text-success');
            } else {
                $(mostrartotales).find('div[data-debe="topdebe"]').text('Transacción fallida').addClass('text-danger');
                $(mostrartotales).find('div[data-haber="tophaber"]').text('Transacción fallida').addClass('text-danger');
            }
        }
    }

}

function validaCuentasSaldos(e) {
    var sel = e.currentTarget;
    var padre = $(sel).parent().parent();
    var tiposaldo = '';
    var span = $(padre).find('span[id=icono-mensaje]');
    var alerta = $(padre).parent().parent().find('div[id=mensaje-error]');
    var codigocuenta = $(padre).find('a[data-codigo]').attr('data-codigo');
    var cargo = $(padre).find('input[id=cargo]').val();
    var abono = $(padre).find('input[id=abono]').val();
    if (codigocuenta !== '' && typeof codigocuenta !== 'undefined') {
        $(span).removeAttr('hidden');
        if (parseInt(cargo) > 0 && abono === '') {
            tiposaldo = 'cargo';
            var data = {
                codigo: codigocuenta,
                tiposaldo: tiposaldo,
                monto: parseFloat(cargo)
            };
            $.ajax({
                url: "CtrSaldo",
                type: "GET",
                data: data,
                success: function (data) {
                    var json = JSON.parse(data);
                    console.log(json);
                    if (json.estado === 'si') {
                        //$(alerta).attr('hidden', '');
                        $(span).removeClass('badge-danger');
                        $(span).addClass('badge-success');
                        $(span).find('li').removeClass('fa-remove');
                        $(span).find('li').addClass('fa-check');
                    } else if (json.estado === 'no') {
                        //$(alerta).attr('hidden', '');
                        $(span).removeClass('badge-success');
                        $(span).addClass('badge-danger');
                        $(span).find('li').removeClass('fa-check');
                        $(span).find('li').addClass('fa-remove');
                    } else if (json.estado === 'notransaccion') {
                        //$(alerta).removeAttr('hidden');
                        alertamensaje("Error!", "Las cuentas de cierre solo realizan operaciones al finalizar el ejercico.", "error");
                        $(span).removeClass('badge-success');
                        $(span).addClass('badge-danger');
                        $(span).find('li').removeClass('fa-check');
                        $(span).find('li').addClass('fa-remove');
                    }
                }
            });
        }
        if (parseInt(abono) > 0 && cargo === '') {
            tiposaldo = 'abono';
            var data = {
                codigo: codigocuenta,
                tiposaldo: tiposaldo,
                monto: parseFloat(abono)
            };
            $.ajax({
                url: "CtrSaldo",
                type: "GET",
                data: data,
                success: function (data) {
                    var json = JSON.parse(data);
                    console.log(json);
                    if (json.estado === 'si') {
                        //$(alerta).attr('hidden', '');
                        $(span).removeClass('badge-danger');
                        $(span).addClass('badge-success');
                        $(span).find('li').removeClass('fa-remove');
                        $(span).find('li').addClass('fa-check');
                    } else if (json.estado === 'no') {
                        //$(alerta).attr('hidden', '');
                        $(span).removeClass('badge-success');
                        $(span).addClass('badge-danger');
                        $(span).find('li').removeClass('fa-check');
                        $(span).find('li').addClass('fa-remove');
                    } else if (json.estado === 'notransaccion') {
                        //$(alerta).removeAttr('hidden');
                        alertamensaje("Error!", "Las cuentas de cierre solo realizan operaciones al finalizar el ejercico.", "error");
                        $(span).removeClass('badge-success');
                        $(span).addClass('badge-danger');
                        $(span).find('li').removeClass('fa-check');
                        $(span).find('li').addClass('fa-remove');
                    }
                }
            });
        }

        if ((parseInt(cargo) === 0 && parseInt(abono) === 0) || (cargo === '' && abono === '') || (cargo !== '' && abono !== '')) {
//$(alerta).attr('hidden', '');
            $(span).removeClass('badge-success');
            $(span).addClass('badge-danger');
            $(span).find('li').removeClass('fa-check');
            $(span).find('li').addClass('fa-remove');
            validasaldo = true;
        }
    }
}

function alertamensaje(titulo, texto, icono) {
    swal({
        title: titulo,
        text: texto,
        icon: icono
    });
}

function saldo(evt, input) {
// Backspace = 8, Enter = 13, ‘0′ = 48, ‘9′ = 57, ‘.’ = 46, ‘-’ = 43
    var key = window.Event ? evt.which : evt.keyCode;
    var chark = String.fromCharCode(key);
    var tempValue = input.value + chark;
    if (key >= 48 && key <= 57) {
        if (filter(tempValue) === false) {
            return false;
        } else {
            return true;
        }
    } else {
        if (key === 8 || key === 13 || key === 0) {
            return true;
        } else if (key === 46) {
            if (filter(tempValue) === false) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
function filter(__val__) {
    var preg = /^([0-9]+\.?[0-9]{0,2})$/;
    if (preg.test(__val__) === true) {
        return true;
    } else {
        return false;
    }
}

function limpiarform() {
    $('#abono').val('');
    $('#cargo').val('');
    $('#transaccion').val('');
    $('#codigocuenta').val('').focus();
}