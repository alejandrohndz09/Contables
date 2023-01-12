/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesos;

import entidades.Cuenta;
import entidades.Debehaber;
import entidades.Inventario;
import entidades.Partida;
import entidades.Periodo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import logica.logicaContables;
//ver rebajas y devoluciones  y gastos

/**
 *
 * @author Xom
 */
public class Cierre {

    logicaContables lc;
    private int i = 0;
    private ArrayList<Partida> lista;
    private ArrayList<Debehaber> dh;//arreglo que almacenara el listado de las cuentas que generaran las partidas de ajuste

    public Cierre() {
        this.lc = new logicaContables();
        this.lista = new ArrayList<>();
        this.dh = new ArrayList<>();
        this.i = lc.listapartidasperiodo("", "").get(lc.listapartidasperiodo("", "").size() - 1).getNumpartida();
    }

    public void generarPartidasCierreAjuste() throws ParseException {
        Mayor m, aux, adi, og, ge, god, gisr, oi, dg, oio, ie, iod;
        Cuenta may;
        ArrayList<Mayor> listadodebe, listadohaber, listadoauxiliar, listadoauxiliar2, listadoauxiliar3;//arreglo para las cuentas con saldo
        int tamadebe = 0, tamahaber = 0, tamaauxiliar;//cuantas cuentas estan con saldo
        BigDecimal acumula1, acumula2,
                acumula3, acumula4, acumula5;//acumula1=Acreedor, acumula2=Deudor


        /* 1 - LIQUIDACIÓN DE LA CUENTA IVA DEBITO FISCAL Y DETERMINACIÓN SI SE PAGARA IMPUESTO*******************SI***************************************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        listadohaber = new ArrayList<>();
        acumula1 = new BigDecimal("0.00");
        acumula2 = new BigDecimal("0.00");
        tamadebe = 0;
        tamahaber = 0;
        //proceso para el debito fiscal de los saldos
        listadohaber = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "2107");
        tamahaber = listadohaber.size();
        for (Mayor cuenta : listadohaber) {
            cuenta.issdsa();
            if (cuenta.getSaldoAcreedor().floatValue() != 0) {
                acumula1 = acumula1.add(new BigDecimal(Float.parseFloat(String.valueOf(cuenta.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP));
            }
        }
        //proceso para el credito fiscal de los saldos
        listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "1108");
        tamadebe = listadodebe.size();
        for (Mayor cuenta : listadodebe) {
            cuenta.issdsa();
            if (cuenta.getSaldoDeudor().floatValue() != 0) {
                acumula2 = acumula2.add(new BigDecimal(Float.parseFloat(String.valueOf(cuenta.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP));
            }
        }

        //obteniendo la diferencia de los saldos acreedor y deudor para saber si se pagara impuesto o no
        if (acumula1.floatValue() > acumula2.floatValue()) {//en caso de que el saldo del debito sea mayor entonces procedemos a asignar el saldo a la cuenta de impuesto por pagar
            may = this.lc.buscarcodigo("", "210401", 0);//recuperamos la cuenta IVA por Pagar
            listadohaber.stream().map((mayor) -> {
                //mayor.issdsa();
                return mayor;
            }).filter((mayor) -> (mayor.getSaldoAcreedor().floatValue() != 0)).forEachOrdered((mayor) -> {
                this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));//cargamos las subcuentas del debito
            });
            listadodebe.stream().map((mayor) -> {
                //mayor.issdsa();
                return mayor;
            }).filter((mayor) -> (mayor.getSaldoDeudor().floatValue() != 0)).forEachOrdered((mayor) -> {
                this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));//abonas las subcuentas del crédito
            });
            this.dh.add(new Debehaber(may, acumula1.subtract(acumula2).setScale(2, RoundingMode.HALF_UP), 'a'));//abonamos el impuesto por pagar
            registrarAjusteCierre("por liquidación de Iva debito fiscal", this.dh);
        } else {//caso contrario que nuestras ventas sean menores a nuestras compras entonces el iva credito hay que acumular el remanente
            //cargando y abonando ambas cuentas con el saldo del iva debito fiscal
            may = this.lc.buscarcodigo("", "110803", 0);//recuperamos la cuenta iva remanente
            listadohaber.stream().map((mayor) -> {
                //mayor.issdsa();
                return mayor;
            }).forEachOrdered((mayor) -> {
                this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));//cargamos las subcuentas del debito
            });
            this.dh.add(new Debehaber(may, acumula1.setScale(2, RoundingMode.HALF_UP), 'a'));//abonamos el saldo del iva debito a la cuenta IVA Remanente para acumular el saldo
            registrarAjusteCierre("por liquidación de Iva debito fiscal", this.dh);
        }
        /*FIN DE LIQUIDACIÓN IVA DEBITO FISCAL*/


 /* 2 - LIQUIDACIÓN DE LAS CUENTAS REBAJAS Y DEVOLUCIONES SOBRE VENTAS Y DETERMINAR VENTAS NETAS******************************SI*****************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        tamadebe = 0;
        acumula1 = new BigDecimal("0.00");
        //iniciando el calculo de saldos de ventas
        listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "5101");
        tamadebe = listadodebe.size();
        for (Mayor mayor : listadodebe) {
            mayor.issdsa();
            if (mayor.getSaldoAcreedor().floatValue() != 0) {
                acumula1 = acumula1.add(new BigDecimal(Float.parseFloat(String.valueOf(mayor.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP));
            }
        }
        //iniciando el calculo de saldos de devoluciones sobre ventas
        aux = this.lc.mayorizarCuenta("410301");
        aux.issdsa();
        //iniciamos el calculo de saldos para rebajas sobre ventas
        adi = this.lc.mayorizarCuenta("410401");
        adi.issdsa();
        //preocediendo a cargar y abonar las cuentas respectivas
        if (aux.getSaldoDeudor().floatValue() > 0 || adi.getSaldoDeudor().floatValue() > 0) {
            if (acumula1.floatValue() > aux.getSaldoDeudor().add(adi.getSaldoDeudor()).floatValue()) {
                if (listadodebe.size() > 1) {
                    if (listadodebe.get(0).getSaldoAcreedor().floatValue() > listadodebe.get(1).getSaldoAcreedor().floatValue()) {
                        if (listadodebe.get(1).getSaldoAcreedor().floatValue() > adi.getSaldoDeudor().floatValue()) {
                            if (aux.getSaldoDeudor().floatValue() > 0 && adi.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                                this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal(adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            } else if (adi.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue() + adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            } else if (aux.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue() + adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            }
                        } else {
                            if (aux.getSaldoDeudor().floatValue() > 0 && adi.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal((aux.getSaldoDeudor().floatValue() + adi.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP), 'c'));
                            } else if (adi.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            } else if (aux.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            }
                        }
                    } else {
                        if (listadodebe.get(0).getSaldoAcreedor().floatValue() > aux.getSaldoDeudor().floatValue()) {
                            if (aux.getSaldoDeudor().floatValue() > 0 && adi.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                                this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal(adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            } else if (adi.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue() + adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            } else if (aux.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue() + adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            }
                        } else {
                            if (aux.getSaldoDeudor().floatValue() > 0 && adi.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal((aux.getSaldoDeudor().floatValue() + adi.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP), 'c'));
                            } else if (adi.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal(adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            } else if (aux.getSaldoDeudor().floatValue() > 0) {
                                this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            }

                        }
                    }
                } else {
                    if (aux.getSaldoDeudor().floatValue() > 0 && adi.getSaldoDeudor().floatValue() > 0) {
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal((aux.getSaldoDeudor().floatValue() + adi.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP), 'c'));
                    } else if (adi.getSaldoDeudor().floatValue() > 0) {
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(adi.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                    } else if (aux.getSaldoDeudor().floatValue() > 0) {
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(aux.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                    }
                }
                if (aux.getSaldoDeudor().floatValue() > 0 && adi.getSaldoDeudor().floatValue() > 0) {
                    this.dh.add(new Debehaber(aux.getCuentamayor(), aux.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                    this.dh.add(new Debehaber(adi.getCuentamayor(), adi.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                } else if (aux.getSaldoDeudor().floatValue() > 0) {
                    this.dh.add(new Debehaber(aux.getCuentamayor(), aux.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                } else if (adi.getSaldoDeudor().floatValue() > 0) {
                    this.dh.add(new Debehaber(adi.getCuentamayor(), adi.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                }
                registrarAjusteCierre("por liquidación de las devoluciones, rebajas sobre ventas y generar las ventas netas", this.dh);
            }
        }
        /*FIN LIQUIDACIÓN DE LAS CUENTAS REBAJAS Y DEVOLUCIONES SOBRE VENTAS*/

 /* 3 - POR LIQUIDACIÓN DE LOS GASTOS SOBRE COMPRAS Y DETERMINAR COMPRAS TOTALES*******************Si********************************************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        acumula1 = new BigDecimal("0.00");
        listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "4101");
        for (Mayor mayor : listadodebe) {
            mayor.issdsa();
            if (mayor.getSaldoDeudor().floatValue() != 0) {
                acumula1 = acumula1.add(new BigDecimal(Float.parseFloat(String.valueOf(mayor.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP));
            }
        }
        m = this.lc.mayorizarCuenta("410201");
        m.issdsa();
        if (m.getSaldoDeudor().floatValue() > 0) {
            if (acumula1.floatValue() > m.getSaldoDeudor().floatValue()) {
                if (listadodebe.size() > 1) {
                    this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal((m.getSaldoDeudor().floatValue() / 2)).setScale(2, RoundingMode.HALF_UP), 'c'));
                    this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal((m.getSaldoDeudor().floatValue() / 2)).setScale(2, RoundingMode.HALF_UP), 'c'));
                } else {
                    this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), m.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'c'));
                }
                this.dh.add(new Debehaber(m.getCuentamayor(), m.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                registrarAjusteCierre("por liquidación de los gastos sobre compras y determinar las compras totales", this.dh);
            }
        }
        /*FIN LIQUIDACION DE LA CUENTA GASTOS SOBRE COMPRAS*/

 /* 4 - POR LIUIDACIÓN DE REBAJAS Y DEVOLUCIONES SOBRE COMPRAS Y DETERMINAR COMPRAS NETAS*******************************************************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        acumula1 = new BigDecimal("0.00");
        listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "4101");//arreglo de la cuenta compras
        if (listadodebe.size() > 0) {
            for (Mayor mayor : listadodebe) {
                mayor.issdsa();
                if (mayor.getSaldoDeudor().floatValue() != 0) {
                    acumula1 = acumula1.add(new BigDecimal(mayor.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP));
                }
            }
            m = this.lc.mayorizarCuenta("510201");//devoluciones sobre compras
            m.issdsa();
            adi = this.lc.mayorizarCuenta("510301");//rebajas sobre compras
            adi.issdsa();

            if (m.getSaldoAcreedor().floatValue()>0 || adi.getSaldoAcreedor().floatValue()>0) {
                if (acumula1.floatValue() > m.getSaldoAcreedor().add(adi.getSaldoAcreedor()).floatValue()) {
                    this.dh.add(new Debehaber(m.getCuentamayor(), m.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));
                    this.dh.add(new Debehaber(adi.getCuentamayor(), adi.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));
                    if (listadodebe.size() > 1) {
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), m.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'a'));
                        this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), adi.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'a'));
                    } else {
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), m.getSaldoAcreedor().add(adi.getSaldoAcreedor()).setScale(2, RoundingMode.HALF_UP), 'a'));
                    }
                    registrarAjusteCierre("por liquidación de las devoluciones, rebajas sobre compras y generar las compras netas", this.dh);
                }
            }
        }
        /*FIN LIQUIDACIÓN DE LAS CUENTAS REBAJAS Y DEVOLUCIONES SOBRE COMPRAS*/

 /* 5 - POR LIQUIDACIÓN DE LA CUENTA INVENTARIO INICIAL DE MERCADERÍA Y DETERMINAR LA MERCADERÍA DISPONIBLE************************************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        tamadebe = 0;
        listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "4101");
        tamadebe = listadodebe.size();
        m = this.lc.mayorizarCuenta("110601");
        m.issdsa();
        if (m.getSaldoDeudor().floatValue() > 0) {
            if (listadodebe.size() > 1) {
                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(m.getSaldoDeudor().floatValue() / tamadebe).setScale(2, RoundingMode.HALF_UP), 'c'));
                this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal(m.getSaldoDeudor().floatValue() / tamadebe).setScale(2, RoundingMode.HALF_UP), 'c'));
                this.dh.add(new Debehaber(m.getCuentamayor(), m.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
            } else {
                this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), m.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'c'));
                this.dh.add(new Debehaber(m.getCuentamayor(), m.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
            }
            registrarAjusteCierre("por liquidación del inventario inicial de mercaderia y determinar la mercadería disponible", this.dh);
        }
        /*FIN LIQUIDACIÓN DE LA CUENTA INVENTARIO INICIAL DE MERCADERIA*/

 /* 6 - POR APERTURA DE NUEVO INVENTARIO Y DETERMINAR EL COSTO DE VENTAS***********************************************************************************/
        Inventario i = this.lc.recuperaInventario("Inventario Final", this.lc.PeriodoenCurso().getIdperiodo());
        EstadodeResultado er = new EstadodeResultado(i.getValor());
        if (i != null) {
            this.dh = new ArrayList<>();
            listadodebe = new ArrayList<>();
            tamadebe = 0;
            listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "4101");
            tamadebe = listadodebe.size();
            if (tamadebe > 0) {
                if (Float.parseFloat(String.valueOf(er.getInventariofinal())) > 0) {
                    if (listadodebe.size() > 1) {
                        this.dh.add(new Debehaber(this.lc.buscarcodigo("", "110601", 0), er.getInventariofinal().setScale(2, RoundingMode.HALF_UP), 'c'));
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal((er.getInventariofinal().floatValue() / tamadebe)).setScale(2, RoundingMode.HALF_UP), 'a'));
                        this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal((er.getInventariofinal().floatValue() / tamadebe)).setScale(2, RoundingMode.HALF_UP), 'a'));
                    } else {
                        this.dh.add(new Debehaber(this.lc.buscarcodigo("", "110601", 0), er.getInventariofinal().setScale(2, RoundingMode.HALF_UP), 'c'));
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), er.getInventariofinal().setScale(2, RoundingMode.HALF_UP), 'a'));
                    }
                    registrarAjusteCierre("por apertura del inventario para el siguiente periodo", this.dh);
                }
            }
        }
        /*FIN APERTURA DE INVENTARIO Y DETERMINACIÓN DEL COSTO DE VENTAS*/

 /* 7 - POR LIQUIDACIÓN DE LA CUENTA COMPRAS Y DETERMINAR LA UTILIDAD BRUTA**********************************************************************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        listadohaber = new ArrayList<>();
        acumula1 = new BigDecimal("0.00");
        acumula2 = new BigDecimal("0.00");
        acumula3 = new BigDecimal("0.00");
        tamadebe = 0;
        tamahaber = 0;
        listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "5101");//ventas, en la posición cero esta contado y credito en la 1
        listadohaber = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "4101");//compras
        tamadebe = listadodebe.size();
        tamahaber = listadohaber.size();
        for (Mayor mayor : listadohaber) {//acumulación del saldos deudor de compras para liquidación
            mayor.issdsa();
            if (mayor.getSaldoDeudor().floatValue() > 0) {
                acumula1 = acumula1.add(new BigDecimal(mayor.getSaldoDeudor().floatValue()).setScale(2, RoundingMode.HALF_UP));//acumulando el saldo
            }
        }
        for (Mayor sm : listadodebe) {//necesario solo para generar los saldos de cada posicion
            sm.issdsa();
        }
        if (acumula1.floatValue() > 0) {//para compras
            if (tamahaber > 1) {//si estan las dos cuentas acumulamos el saldo en variables diferentes
                acumula2 = acumula2.add(new BigDecimal(listadohaber.get(0).getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                acumula3 = acumula2.add(new BigDecimal(listadohaber.get(1).getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
            } else {//si no acumulamos en una sola
                acumula2 = acumula2.add(new BigDecimal(listadohaber.get(0).getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
            }

            if (tamadebe > 1) {//si esta ventas contado y credito
                if (tamahaber > 1) {//si estan las dos cuentas de compras
                    if (listadodebe.get(0).getSaldoAcreedor().floatValue() > listadodebe.get(1).getSaldoAcreedor().floatValue()) {//verificando que el saldo de vcontado sea mayor vcredito
                        if (listadodebe.get(1).getSaldoAcreedor().floatValue() > acumula3.floatValue()) {
                            this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), acumula3.setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'a'));
                            this.dh.add(new Debehaber(listadohaber.get(1).getCuentamayor(), acumula3.setScale(2, RoundingMode.HALF_UP), 'a'));
                        } else {
                            this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), acumula2.add(acumula3).setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'a'));
                            this.dh.add(new Debehaber(listadohaber.get(1).getCuentamayor(), acumula3.setScale(2, RoundingMode.HALF_UP), 'a'));
                        }
                    } else {//caso contrario el vcredito mayor a vcontado
                        if (listadodebe.get(0).getSaldoAcreedor().floatValue() > acumula2.floatValue()) {
                            this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), acumula3.setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'a'));
                            this.dh.add(new Debehaber(listadohaber.get(1).getCuentamayor(), acumula3.setScale(2, RoundingMode.HALF_UP), 'a'));
                        } else {
                            this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), acumula2.add(acumula3).setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'a'));
                            this.dh.add(new Debehaber(listadohaber.get(1).getCuentamayor(), acumula3.setScale(2, RoundingMode.HALF_UP), 'a'));
                        }
                    }
                } else {//si solo esta una cuenta compras
                    if (listadodebe.get(0).getSaldoAcreedor().floatValue() > listadodebe.get(1).getSaldoAcreedor().floatValue()) {
                        if (listadodebe.get(1).getSaldoAcreedor().floatValue() > (acumula2.floatValue() / tamadebe)) {
                            this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal((acumula2.floatValue() / tamadebe)).setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal((acumula2.floatValue() / tamadebe)).setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula1.setScale(2, RoundingMode.HALF_UP), 'a'));
                        } else {
                            this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal(acumula2.floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula1.setScale(2, RoundingMode.HALF_UP), 'a'));
                        }
                    } else {
                        if (listadodebe.get(0).getSaldoAcreedor().floatValue() > (acumula2.floatValue() / tamadebe)) {
                            this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), new BigDecimal((acumula2.floatValue() / tamadebe)).setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal((acumula2.floatValue() / tamadebe)).setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula1.setScale(2, RoundingMode.HALF_UP), 'a'));
                        } else {
                            this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), new BigDecimal(acumula2.floatValue()).setScale(2, RoundingMode.HALF_UP), 'c'));
                            this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula1.setScale(2, RoundingMode.HALF_UP), 'a'));
                        }
                    }
                }
            } else {
                if (tamahaber > 1) {
                    this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), acumula2.add(acumula3).setScale(2, RoundingMode.HALF_UP), 'c'));
                    this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'a'));
                    this.dh.add(new Debehaber(listadohaber.get(1).getCuentamayor(), acumula3.setScale(2, RoundingMode.HALF_UP), 'a'));
                } else {
                    this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'c'));
                    this.dh.add(new Debehaber(listadohaber.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'a'));
                }
            }
            registrarAjusteCierre("por liquidación de la cuenta compras y determinar la utilidad bruta", this.dh);
        }
        /*FIN DE LIQUIDACIÓN DE LA CUENTAS COMPRAS*/

 /* 8 - POR LIQUIDACIÓN DE LA CUENTA VENTAS Y DETERMINAR EL ISR*********************************************************************************************/
        if (i != null) {
            this.dh = new ArrayList<>();
            if (Float.parseFloat(String.valueOf(er.getInventariofinal())) > 0) {
                listadodebe = new ArrayList<>();
                tamadebe = 0;
                acumula1 = new BigDecimal("0.00");
                acumula2 = new BigDecimal("0.00");
                acumula3 = new BigDecimal("0.00");
                listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "5101");//listado de cuentas de ventas
                tamadebe = listadodebe.size();
                for (Mayor mayor : listadodebe) {
                    mayor.issdsa();
                    if (mayor.getSaldoAcreedor().floatValue() != 0) {
                        acumula1 = acumula1.add(new BigDecimal(mayor.getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                    }
                }
                if (acumula1.floatValue() > 0) {
                    if (tamadebe > 1) {
                        acumula2 = acumula2.add(new BigDecimal(listadodebe.get(0).getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                        acumula3 = acumula3.add(new BigDecimal(listadodebe.get(1).getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                    } else {
                        acumula2 = acumula2.add(new BigDecimal(listadodebe.get(0).getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                    }

                    if (tamadebe > 1) {
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'c'));
                        this.dh.add(new Debehaber(listadodebe.get(1).getCuentamayor(), acumula3.setScale(2, RoundingMode.HALF_UP), 'c'));
                    } else {
                        this.dh.add(new Debehaber(listadodebe.get(0).getCuentamayor(), acumula2.setScale(2, RoundingMode.HALF_UP), 'c'));
                    }
                    this.dh.add(new Debehaber(this.lc.buscarcodigo("", "210402", 0), er.impuestoSobreLaRenta().setScale(2, RoundingMode.HALF_UP), 'a'));
                    this.dh.add(new Debehaber(this.lc.buscarcodigo("", "610101", 0), acumula1.subtract(er.impuestoSobreLaRenta()).setScale(2, RoundingMode.HALF_UP), 'a'));
                    registrarAjusteCierre("por liquidacion de la cuenta ventas y determinar el Impuesto", this.dh);
                }
            }
        }
        /*FIN LIQUIDACIÓN DE LA CUENTA VENTAS*/

 /* 9 - LIQUIDACIÓN DE LA CUENTA INGRESOS NO DE OPERACION CON TODAS LAS HIJAS Y SUBHIJAS*********************************************************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        listadohaber = new ArrayList<>();
        listadoauxiliar = new ArrayList<>();
        listadoauxiliar2 = new ArrayList<>();
        listadoauxiliar3 = new ArrayList<>();
        acumula1 = new BigDecimal("0.00");
        acumula2 = new BigDecimal("0.00");
        acumula3 = new BigDecimal("0.00");
        acumula4 = new BigDecimal("0.00");
        acumula5 = new BigDecimal("0.00");
        //recogemos las cuentas que poseen saldo en caso de que así sea
        listadodebe = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "5201");
        listadohaber = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "5202");
        listadoauxiliar = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "5203");
        listadoauxiliar2 = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "5204");
        listadoauxiliar3 = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "5205");
        if (listadodebe.size() > 0 || listadohaber.size() > 0 || listadoauxiliar.size() > 0 || listadoauxiliar2.size() > 0 || listadoauxiliar3.size() > 0) {
            for (Mayor mayor : listadodebe) {
                mayor.issdsa();
                if (mayor.getSaldoAcreedor().floatValue() > 0) {
                    acumula1 = acumula1.add(new BigDecimal(mayor.getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));
                }
            }
            for (Mayor mayor : listadohaber) {
                mayor.issdsa();
                if (mayor.getSaldoAcreedor().floatValue() > 0) {
                    acumula2 = acumula2.add(new BigDecimal(mayor.getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));
                }
            }
            for (Mayor mayor : listadoauxiliar) {
                mayor.issdsa();
                if (mayor.getSaldoAcreedor().floatValue() > 0) {
                    acumula3 = acumula3.add(new BigDecimal(mayor.getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));
                }
            }
            for (Mayor mayor : listadoauxiliar2) {
                mayor.issdsa();
                if (mayor.getSaldoAcreedor().floatValue() > 0) {
                    acumula4 = acumula4.add(new BigDecimal(mayor.getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));
                }
            }
            for (Mayor mayor : listadoauxiliar3) {
                mayor.issdsa();
                if (mayor.getSaldoAcreedor().floatValue() > 0) {
                    acumula5 = acumula5.add(new BigDecimal(mayor.getSaldoAcreedor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));
                }
            }
            this.dh.add(new Debehaber(this.lc.buscarcodigo("", "610101", 0), acumula1.add(acumula2.add(acumula3.add(acumula4.add(acumula5)))).setScale(2, RoundingMode.HALF_UP), 'a'));
            registrarAjusteCierre("por liquidación de otros ingresos no operacionales", this.dh);
        }
        /*FIN LIQUIDACIÓN DE OTROS INGRESOS NO OPERACIONALES*/


 /* 10 - POR LIQUIDACIÓN DE LOS GASTOS DE OPERACION Y TRASPASO DE LA RESERVA COMO PROVISIÓN******************************************************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        listadohaber = new ArrayList<>();
        listadoauxiliar = new ArrayList<>();
        acumula1 = new BigDecimal("0.00");
        acumula2 = new BigDecimal("0.00");
        acumula3 = new BigDecimal("0.00");
        listadodebe = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "4301");
        listadohaber = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "4302");
        listadoauxiliar = (ArrayList<Mayor>) this.lc.mayorizarCuenta(6, "4303");
        if (i != null) {
            if (Float.parseFloat(String.valueOf(er.getInventariofinal())) > 0) {
                if (listadodebe.size() > 0 || listadohaber.size() > 0 || listadoauxiliar.size() > 0) {
                    for (Mayor mayor : listadodebe) {
                        mayor.issdsa();
                        if (mayor.getSaldoDeudor().floatValue() > 0) {
                            acumula1 = acumula1.add(new BigDecimal(mayor.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                    for (Mayor mayor : listadohaber) {
                        mayor.issdsa();
                        if (mayor.getSaldoDeudor().floatValue() > 0) {
                            acumula2 = acumula2.add(new BigDecimal(mayor.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                    for (Mayor mayor : listadoauxiliar) {
                        mayor.issdsa();
                        if (mayor.getSaldoDeudor().floatValue() > 0) {
                            acumula3 = acumula3.add(new BigDecimal(mayor.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                    //registrando la liquidación de las cuentas
                    this.dh.add(new Debehaber(this.lc.buscarcodigo("", "610101", 0), acumula1.add(acumula2.add(acumula3.add(er.reservaLegal()))).setScale(2, RoundingMode.HALF_UP), 'c'));
                    for (Mayor mayor : listadodebe) {
                        if (mayor.getSaldoDeudor().floatValue() > 0) {
                            this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                        }
                    }
                    for (Mayor mayor : listadohaber) {
                        if (mayor.getSaldoDeudor().floatValue() > 0) {
                            this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                        }
                    }
                    for (Mayor mayor : listadoauxiliar) {
                        if (mayor.getSaldoDeudor().floatValue() > 0) {
                            this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                        }
                    }
                    if (Float.parseFloat(String.valueOf(er.reservaLegal())) > 0) {
                        this.dh.add(new Debehaber(this.lc.buscarcodigo("", "310201", 0), er.reservaLegal().setScale(2, RoundingMode.HALF_UP), 'a'));
                    }
                    registrarAjusteCierre("por liquidación de los gastos de operación", this.dh);
                }
            }
        }
        /*FIN LIQUIDACIÓN DE GASTOS DE OPERACION*/

 /* 11 - LQUIDACIÓN DE LAS DEMAS CUENTAS DE GASTOS DE OPERACIÓN*********************************************************************************************/
        this.dh = new ArrayList<>();
        listadodebe = new ArrayList<>();
        listadohaber = new ArrayList<>();
        listadoauxiliar = new ArrayList<>();
        listadoauxiliar2 = new ArrayList<>();
        acumula1 = new BigDecimal("0.00");
        acumula2 = new BigDecimal("0.00");
        acumula3 = new BigDecimal("0.00");
        acumula4 = new BigDecimal("0.00");

        listadodebe = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "4304");
        listadohaber = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "4305");
        listadoauxiliar = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "4306");
        listadoauxiliar2 = (ArrayList<Mayor>) lc.mayorizarCuenta(6, "4307");
        if (listadodebe.size() > 0 || listadohaber.size() > 0 || listadoauxiliar.size() > 0 || listadoauxiliar2.size() > 0) {
            for (Mayor mayor : listadodebe) {
                mayor.issdsa();
                if (mayor.getSaldoDeudor().floatValue() > 0) {
                    acumula1 = acumula1.add(new BigDecimal(mayor.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                }
            }

            for (Mayor mayor : listadohaber) {
                mayor.issdsa();
                if (mayor.getSaldoDeudor().floatValue() > 0) {
                    acumula2 = acumula2.add(new BigDecimal(mayor.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                }
            }

            for (Mayor mayor : listadoauxiliar) {
                mayor.issdsa();
                if (mayor.getSaldoDeudor().floatValue() > 0) {
                    acumula3 = acumula3.add(new BigDecimal(mayor.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                }
            }

            for (Mayor mayor : listadoauxiliar2) {
                mayor.issdsa();
                if (mayor.getSaldoDeudor().floatValue() > 0) {
                    acumula4 = acumula4.add(new BigDecimal(mayor.getSaldoDeudor().floatValue())).setScale(2, RoundingMode.HALF_UP);
                }
            }
            this.dh.add(new Debehaber(this.lc.buscarcodigo("", "6101", 0), acumula1.add(acumula2.add(acumula3.add(acumula4))).setScale(2, RoundingMode.HALF_UP), 'c'));
            for (Mayor mayor : listadodebe) {
                if (mayor.getSaldoDeudor().floatValue() > 0) {
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                }
            }
            for (Mayor mayor : listadohaber) {
                if (mayor.getSaldoDeudor().floatValue() > 0) {
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                }
            }
            for (Mayor mayor : listadoauxiliar) {
                if (mayor.getSaldoDeudor().floatValue() > 0) {
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                }
            }
            for (Mayor mayor : listadoauxiliar2) {
                if (mayor.getSaldoDeudor().floatValue() > 0) {
                    this.dh.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
                }
            }

            registrarAjusteCierre("por liquidación de otros gastos no operacionales", this.dh);
        }
        /*FIN LIQUIDACIÓN DE CUENTAS DE OTROS GASTOS DE OPERACIÓN*/

 /* 12 - DETERMINACIÓN DE LA UTILIDAD DEL EJERCICIO***********************************************************************************************************/
        this.dh = new ArrayList<>();
        m = this.lc.mayorizarCuenta("610101");
        m.issdsa();
        if (Float.parseFloat(String.valueOf(m.getSaldoAcreedor())) > 0 || Float.parseFloat(String.valueOf(m.getSaldoDeudor())) > 0) {
            if (Float.parseFloat(String.valueOf(m.getSaldoAcreedor())) > 0) {
                this.dh.add(new Debehaber(m.getCuentamayor(), m.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'c'));
                this.dh.add(new Debehaber(this.lc.buscarcodigo("", "310302", 0), m.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP), 'a'));
            }
            if (Float.parseFloat(String.valueOf(m.getSaldoDeudor())) > 0) {
                this.dh.add(new Debehaber(m.getCuentamayor(), m.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'c'));
                this.dh.add(new Debehaber(this.lc.buscarcodigo("", "310302", 0), m.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP), 'a'));
            }
            registrarAjusteCierre("por determinar la utilidad del ejercicio", this.dh);
        }
        /*FIN DETERMINACIÓN UTILIDAD DEL EJERCICIO*/

 /*13 - PARTIDAD DE CIERRE DEL EJERCICIO*********************************************************************************************************************/
        CierreEjercicio();
        /*FIN PARTIDA DE CIERRE*/

 /*FINALIZACIÓN DEL PERIODO Y APERTURAMOS EL NUEVO**************************************************************************************************************/
        finalizaIniciaPeriodo();
        /*FIN FINALIZA E INICIA PERIODO*/
    }

    public void registrarAjusteCierre(String descripcion, ArrayList<Debehaber> dh) {
        //this.lc.ordenarDH(dh);//opcional para el metodo de ordenamiento
        Partida p = new Partida();
        if (lc.listapartidasperiodo("", "") != null) {
            if (i == 0) {
                i = 1;
            } else {
                i++;
            }
        } else {
            i = 0;
        }
        p.setDescripcion(descripcion);
        p.setNumpartida(i);
        p.setPeriodo(lc.PeriodoenCurso());
        p.setFecha(Date.valueOf(LocalDate.now()));
        dh.stream().map((debehaber) -> {
            debehaber.setPartida(p);
            return debehaber;
        }).forEachOrdered(p.getDebehabers()::add);
        this.lc.registrarPartida(p);
        this.lista.add(p);
    }

    private void CierreEjercicio() {
        ArrayList<Debehaber> caF = new ArrayList<>();
        ArrayList<Debehaber> caI = new ArrayList<>();
        ArrayList<Mayor> may = (ArrayList<Mayor>) lc.listadoMayorBG(6, "11");
        may.addAll(lc.listadoMayorBG(6, "12"));
        may.addAll(lc.listadoMayorBG(6, "21"));
        may.addAll(lc.listadoMayorBG(6, "22"));
        may.addAll(lc.listadoMayorBG(6, "31"));

        for (Mayor mayor : may) {
            mayor.issdsa();
            if ("-".equals(mayor.getCuentamayor().getTipocuenta()) && Float.parseFloat(String.valueOf(mayor.getSaldoAcreedor())) != 0) {
                caF.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor(), 'c'));
                caI.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoAcreedor(), 'a'));
            } else {
                if ("+".equals(mayor.getCuentamayor().getTipocuenta()) && Float.parseFloat(String.valueOf(mayor.getSaldoDeudor())) != 0) {
                    caF.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor(), 'a'));
                    caI.add(new Debehaber(mayor.getCuentamayor(), mayor.getSaldoDeudor(), 'c'));
                }
            }
        }

        registrarAjusteCierre("por cierre del ejercicio", caF);
        //caI = lc.ordenarDH(caI);//opcional para el metodo de ordenamiento
        Partida p = new Partida();
        p.setPeriodo(null);
        p.setFecha(null);
        p.setDescripcion("Balance Inicial");
        caI.stream().map((cA) -> {
            cA.setPartida(p);
            return cA;
        }).forEachOrdered(p.getDebehabers()::add);
        lc.registrarPartida(p);
    }

    public void finalizaIniciaPeriodo() throws ParseException {
        Periodo p = lc.PeriodoenCurso();
        Date fechainicio = (Date) p.getFechainicial();
        Date fechafinal = (Date) p.getFechafinal();
        p.setEnproceso(false);
        p.setTerminado(true);
        lc.actualizarPeriodo(p);
        /*todo la codificacion del nuevo periodo*/

        //Nuevo periodo, para el siguiente año, cuando el cierre se efectue se apertura el siguiente año,
        //dando como paso el nuevo periodo con la nueva partida
        //obtenemos las fechas del anterior y aumentamos el año
        Periodo aux, periodo;//para verificacion de insercion de datos en la db

        String fecha = String.valueOf(fechainicio);
        String nueva[] = fecha.split("-");
        int año = Integer.parseInt(nueva[0]);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fi = ("01/" + "01/" + (año + 1));
        String ff = ("31/" + "12/" + (año + 1));

        java.util.Date date = sdf.parse(fi);
        java.util.Date date1 = sdf.parse(ff);
        java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
        java.sql.Date sqlStartDate1 = new java.sql.Date(date1.getTime());
        Date f1 = sqlStartDate;
        Date f2 = sqlStartDate1;
        aux = lc.bp(f1, f2);
        if (aux == null) {
            periodo = new Periodo(f1, f2, true, false);
            lc.guardarPeriodo(periodo);
            if (lc.obtenerUltima() != null) {//si esta condicion se cumple quiere decir que ya ha habido periodos anteriores
                //por lo cual hay que recuperar la ultima partida registrada, faltan procesos aun
                Partida primera;
                primera = lc.obtenerUltima();
                primera.setNumpartida(1);
                primera.setFecha(periodo.getFechainicial());
                primera.setPeriodo(lc.PeriodoenCurso());
                lc.actualizarPartida(primera);
            }

        }

    }

    public ArrayList<Partida> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Partida> lista) {
        this.lista = lista;
    }

}
