/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#formbackup').on('submit', crearrespaldo);
    $('#formimport').on('submit', importarrespaldo);
});

function crearrespaldo(e) {
    var gestor = $('#gestor').val();
    var puerto = $('#puerto').val();
    var database = $('#database').val();
    var usuario = $('#usuario').val();
    var contraseña = $('#pass').val();
    if (gestor === '' || puerto === '' || database === '' || usuario === '' || contraseña === '') {
        e.preventDefault();
        swal({
            title: "Campos vacíos",
            text: "Todos los campos son obligatorios, por favor llene el formulario, para poder crear el respaldo de la información",
            icon: 'error'
        });
        return;
    }
    e.preventDefault();
    var data = {
        'accion': "respaldo",
        'gestor': gestor,
        'puerto': puerto,
        'database': database,
        'usuario': usuario,
        'contraseña': contraseña
    };

    console.log(data);
    swal({
        title: "Advertencia",
        text: "Asegurese de haber creado una carpeta en el disco local C con el nombre Respaldo Contabilidad, para poder realizar el respaldo de la información",
        icon: "warning",
        buttons: true,
        dangerMode: true
    }).then((willDelete) => {
        if (willDelete) {
            $.ajax({
                url: "CtrRespaldo",
                type: "POST",
                data: data,
                success: function (data) {
                    swal("Respaldo de datos creado correctamente", {
                        icon: "success"
                    });
                    empty();
                }
            });
        } else {
            swal("El respaldo de datos ha sido cancelado!", {
                icon: 'error'
            });
        }
    });
}

function empty() {
    $('#gestor option:selected').prop('selected', false);
    $('#puerto option:selected').prop('selected', false);
    $('#database').val('');
    $('#usuario').val('');
    $('#pass').val('');
}

function  importarrespaldo(e) {
    e.preventDefault();
    var puerto = $('#puertoimport').val();
    var db = $('#databaseimport').val();
    var archivo = $('#backup').val();
    var user = $('#usuarioimport').val();
    var pass = $('#passimport').val();
    if (puerto === '' || db === '' || archivo === '' || user === '' || pass === '') {
        e.preventDefault();
        swal({
            title: "Campos vacíos",
            text: "Todos los campos son obligatorios, por favor llene el formulario, para poder importar la información",
            icon: 'error'
        });
        return;
    }
    if (puerto !== '' && db !== '' && archivo !== '' && user !== '' && pass !== '') {
        var archivoname = document.getElementById('backup').files[0].name;
        var data = {
            accion: "importar",
            puerto: puerto,
            database: db,
            archivoname: archivoname,
            usuario: user,
            password: pass
        };
        console.log(data);
        swal({
            title: "Advertencia",
            text: "Asegurese de haber seleccionado el archivo de la carpeta Respaldo Contabilidad del disoc local C, de no ser asi, cancele la operación y vuelva a intentarlo.",
            icon: "warning",
            buttons: true,
            dangerMode: true
        }).then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    url: "CtrRespaldo",
                    type: "POST",
                    data: data,
                    success: function (data) {
                        var json = JSON.parse(data);
                        if (json.estado === '1') {
                            swal("Restauración de datos realizada correctamente", {
                                icon: "success"
                            });
                            limpiar();
                        } else {
                            swal("La restauración de la información ha fallado.", {
                                icon: "success"
                            });
                        }
                    }
                });
            } else {
                swal("La restauración de datos ha sido cancelado!", {
                    icon: 'error'
                });
            }
        });
    } else {
        e.preventDefault();
        swal({
            title: "Advertencia",
            text: "La información del servidor a sido alterada.",
            icon: 'warning'
        });
        return;
    }
}

function limpiar() {
    $('#puertoimport').val('');
    $('#databaseimport').val('');
    $('#backup').val('');
    $('#usuarioimport').val('');
    $('#passimport').val('');
}
