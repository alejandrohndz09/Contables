/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesos;

import entidades.Cuenta;
import entidades.Debehaber;
import entidades.Partida;
import entidades.Periodo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import logica.logicaContables;

/**
 *
 * @author Xom
 */
public class Mayor {

    logicaContables lc = new logicaContables();
    private ArrayList<Debehaber> dh;
    private Cuenta cuentamayor;
    /*serviran para la balanza de sumas y saldos*/
    private BigDecimal debe;
    private BigDecimal haber;
    /*serviran para el libro mayor y la balanza de sumas y saldos*/
    private BigDecimal saldoDeudor;
    private BigDecimal saldoAcreedor;
    private int tama;

    public Mayor() {
        this.debe = new BigDecimal("0.00");
        this.haber = new BigDecimal("0.00");
        this.saldoDeudor = new BigDecimal("0.00");
        this.saldoAcreedor = new BigDecimal("0.00");
        this.dh = new ArrayList<>();
        this.tama=0;
    }

    public Mayor(Cuenta cuentamayor) {
        this.debe = new BigDecimal("0.00");
        this.haber = new BigDecimal("0.00");
        this.saldoDeudor = new BigDecimal("0.00");
        this.saldoAcreedor = new BigDecimal("0.00");
        this.dh = new ArrayList<>();
        this.cuentamayor = cuentamayor;
        this.tama=0;
    }

    public Mayor(ArrayList<Debehaber> dh, Cuenta cuentamayor, BigDecimal debe, BigDecimal haber, BigDecimal saldoDeudor, BigDecimal saldoAcreedor,int tama) {
        this.dh = dh;
        this.cuentamayor = cuentamayor;
        this.debe = debe;
        this.haber = haber;
        this.saldoDeudor = saldoDeudor;
        this.saldoAcreedor = saldoAcreedor;
        this.tama=tama;
    }

    public ArrayList<Debehaber> getDh() {
        return dh;
    }

    public void setDh(ArrayList<Debehaber> dh) {
        this.dh = dh;
    }

    public Cuenta getCuentamayor() {
        return cuentamayor;
    }

    public void setCuentamayor(Cuenta cuentamayor) {
        this.cuentamayor = cuentamayor;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public BigDecimal getSaldoDeudor() {
        return saldoDeudor;
    }

    public void setSaldoDeudor(BigDecimal saldoDeudor) {
        this.saldoDeudor = saldoDeudor;
    }

    public BigDecimal getSaldoAcreedor() {
        return saldoAcreedor;
    }

    public void setSaldoAcreedor(BigDecimal saldoAcreedor) {
        this.saldoAcreedor = saldoAcreedor;
    }

    public int getTama() {
        return tama;
    }

    public void setTama(int tama) {
        this.tama = tama;
    }

    public void liquidacion() {
        int i=0;//ojo xd,para los colores de los saldos, y de igual manera servira para no agarrar cuentas que no correspondan al periodo en curso
        //en el que se encuentra el sistema
        BigDecimal sd = BigDecimal.valueOf(this.saldoDeudor.floatValue());
        BigDecimal sa = BigDecimal.valueOf(this.saldoAcreedor.floatValue());
        Periodo p = lc.PeriodoenCurso();
        ArrayList<Debehaber> lista = lc.ordenarDH(dh);

        for (Debehaber d : lista) {
            if (p != null) {
                if (d.getPartida().getPeriodo().getIdperiodo() == p.getIdperiodo() && d.getPartida() != null && !d.getPartida().getDescripcion().equals("por cierre del ejercicio")) {
                    if (d.getTipotransaccion().equals('c')) {
                        sd = sd.add(new BigDecimal(Float.parseFloat(String.valueOf(d.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
                    } else if (d.getTipotransaccion().equals('a')) {
                        sa = sa.add(new BigDecimal(Float.parseFloat(String.valueOf(d.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
                    }
                    i++;
                }
            }
        }
        /*movimientos (cargo/abono) acumulados para cada cuenta, se utilizaran para la balanza
        de sumas y saldos*/
        this.debe = sd;
        this.haber = sa;
        this.tama=i;
        /*determinamos el tipo de saldo si es deudor o acreedor para el mayor y la balanza de comprobación
        de sumas y saldos*/
        if (sd.floatValue() > 0 || sa.floatValue() > 0) {
            if (sd.floatValue() > sa.floatValue()) {
                this.saldoDeudor = BigDecimal.valueOf(sd.floatValue() - sa.floatValue()).setScale(2, RoundingMode.HALF_UP);
            } else if (sd.floatValue() < sa.floatValue()) {
                this.saldoAcreedor = BigDecimal.valueOf(sa.floatValue() - sd.floatValue()).setScale(2, RoundingMode.HALF_UP);
            } else {
                this.saldoDeudor = BigDecimal.valueOf(sd.floatValue() - sa.floatValue()).setScale(2, RoundingMode.HALF_UP);
                this.saldoAcreedor = BigDecimal.valueOf(sa.floatValue() - sd.floatValue()).setScale(2, RoundingMode.HALF_UP);
            }
        } else {
            this.saldoDeudor = sd;
            this.saldoAcreedor = sa;
        }
    }

    public void issdsa() {
        BigDecimal sd = BigDecimal.valueOf(this.saldoDeudor.floatValue());
        BigDecimal sa = BigDecimal.valueOf(this.saldoAcreedor.floatValue());
        Periodo p = lc.PeriodoenCurso();
        Partida aux1, aux2;
        this.dh = lc.ordenarDH(dh);

        if (p.getTerminado() == true) {
            int max = lc.obtenerNumeroPartidaUltima();
            int tama = this.dh.size() - 1;
            int inferior = 0;
            int superior = 0;
            int auxnum;
            ArrayList<Debehaber> listauxiliar = new ArrayList<>();
            aux1 = lc.buscaDescripcion("por liquidación de otros ingresos no operacionales");
            aux2 = lc.buscaDescripcion("por liquidación de otros gastos no operacionales");

            if (aux1 != null && aux2 == null) {
                inferior = max - 12;
            }
            if (aux1 == null && aux2 != null) {
                inferior = max - 12;
            }
            if (aux1 != null && aux2 != null) {
                inferior = max - 13;
            }
            if (aux1 == null && aux2 == null) {
                inferior = max - 11;
            }
            superior = max;// para este ejemplo superior es 25 e inferior sera 14

            if (inferior > 0) {
                for (int i = 0; i < tama; i++) {
                    if (this.dh.get(i).getPartida() != null) {
                        auxnum = this.dh.get(i).getPartida().getNumpartida();
                        if (!(auxnum > inferior && auxnum <= superior)) {
                            listauxiliar.add(this.dh.get(i));
                        }
                    }
                }
                this.dh = listauxiliar;
            }

        }

        for (Debehaber d : this.dh) {
            if (p != null) {
                if (d.getPartida().getPeriodo().getIdperiodo() == p.getIdperiodo()) {
                    if (d.getTipotransaccion().equals('c')) {
                        sd = sd.add(new BigDecimal(Float.parseFloat(String.valueOf(d.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
                    } else if (d.getTipotransaccion().equals('a')) {
                        sa = sa.add(new BigDecimal(Float.parseFloat(String.valueOf(d.getMontopartida()))).setScale(2, RoundingMode.HALF_UP));
                    }
                }
            }
        }
        /*movimientos (cargo/abono) acumulados para cada cuenta, se utilizaran para la balanza
        de sumas y saldos*/
        this.debe = sd;
        this.haber = sa;
        /*determinamos el tipo de saldo si es deudor o acreedor para el mayor y la balanza de comprobación
        de sumas y saldos*/
        if (sd.floatValue() > 0 || sa.floatValue() > 0) {
            if (sd.floatValue() > sa.floatValue()) {
                this.saldoDeudor = BigDecimal.valueOf(sd.floatValue() - sa.floatValue()).setScale(2, RoundingMode.HALF_UP);
            } else if (sd.floatValue() < sa.floatValue()) {
                this.saldoAcreedor = BigDecimal.valueOf(sa.floatValue() - sd.floatValue()).setScale(2, RoundingMode.HALF_UP);
            }
        } else {
            this.saldoDeudor = sd;
            this.saldoAcreedor = sa;
        }
    }

}
