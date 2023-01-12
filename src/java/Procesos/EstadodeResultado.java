/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import logica.logicaContables;

/**
 *
 * @author Xom
 */
public class EstadodeResultado {

    /*variables solo para generar los procesos de saldos obtenidos de la base de datos de las cuentas
    correspondientes*/
    private BigDecimal ventas;
    private BigDecimal rebajassobreventas;
    private BigDecimal devolucionessobreventas;
    private BigDecimal compras;
    private BigDecimal gastosobrecompras;
    private BigDecimal rebajassobrecompras;
    private BigDecimal devolucionessobrecompras;
    private BigDecimal inventarioinicial;
    private BigDecimal inventariofinal;
    /*gastos de operacion, en esta parte; en la vista se es visible las cuentas correspondientes a cada gasto*/
    private BigDecimal gastosadmon;
    private BigDecimal gastosventa;
    private BigDecimal gastosfinancieros;
    /*otros*/
    private BigDecimal otrosgastos;
    private BigDecimal otrosingresos;
    
    logicaContables lc;
    
    public EstadodeResultado() {
    }
    
    public EstadodeResultado(BigDecimal ventas, BigDecimal rebajassobreventas, BigDecimal devolucionessobreventas, BigDecimal compras, BigDecimal gastosobrecompras, BigDecimal rebajassobrecompras, BigDecimal devolucionessobrecompras, BigDecimal inventarioinicial, BigDecimal inventariofinal, BigDecimal gastosadmon, BigDecimal gastosventa, BigDecimal gastosfinancieros, BigDecimal otrosgastos, BigDecimal otrosingresos) {
        this.ventas = ventas;
        this.rebajassobreventas = rebajassobreventas;
        this.devolucionessobreventas = devolucionessobreventas;
        this.compras = compras;
        this.gastosobrecompras = gastosobrecompras;
        this.rebajassobrecompras = rebajassobrecompras;
        this.devolucionessobrecompras = devolucionessobrecompras;
        this.inventarioinicial = inventarioinicial;
        this.inventariofinal = inventariofinal;
        this.gastosadmon = gastosadmon;
        this.gastosventa = gastosventa;
        this.gastosfinancieros = gastosfinancieros;
        this.otrosgastos = otrosgastos;
        this.otrosingresos = otrosingresos;
    }
    
    public EstadodeResultado(String inf) throws ParseException {
        Mayor v, rsv, dsv, c, gsc, rsc, dsc, ini, ga, gv, gf, og, ge, god, gisr, oi, dg, oio, ie, iod;
        this.lc = new logicaContables();

        /*mayor de la cuenta ventas*/
        v = lc.mayorizarCuenta("5101");
        v.issdsa();
        this.ventas = v.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta rebajas sobre ventas*/
        rsv = lc.mayorizarCuenta("4104");
        rsv.issdsa();
        this.rebajassobreventas = rsv.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta devoluciones sobre ventas*/
        dsv = lc.mayorizarCuenta("4103");
        dsv.issdsa();
        this.devolucionessobreventas = dsv.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta compras*/
        c = lc.mayorizarCuenta("4101");
        c.issdsa();
        this.compras = c.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta gastos sobre compras*/
        gsc = lc.mayorizarCuenta("4102");
        gsc.issdsa();
        this.gastosobrecompras = gsc.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta rebajas sobre compras*/
        rsc = lc.mayorizarCuenta("5103");
        rsc.issdsa();
        this.rebajassobrecompras = rsc.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta devoluciones sobre compras*/
        dsc = lc.mayorizarCuenta("5102");
        dsc.issdsa();
        this.devolucionessobrecompras = dsc.getSaldoAcreedor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta inventario*/
        ini = lc.mayorizarCuenta("1106");
        ini.issdsa();
        this.inventarioinicial = ini.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP);

        /*inventario final*/
        this.inventariofinal = new BigDecimal(inf).setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta gastos de administracion*/
        ga = lc.mayorizarCuenta("4302");
        ga.issdsa();
        this.gastosadmon = ga.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta gastos de venta*/
        gv = lc.mayorizarCuenta("4301");
        gv.issdsa();
        this.gastosventa = gv.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta gastos financieros*/
        gf = lc.mayorizarCuenta("4303");
        gf.issdsa();
        this.gastosfinancieros = gf.getSaldoDeudor().setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta otros gastos*/
        og = lc.mayorizarCuenta("4304");
        og.issdsa();
        ge = lc.mayorizarCuenta("4305");
        ge.issdsa();
        god = lc.mayorizarCuenta("4306");
        god.issdsa();
        gisr = lc.mayorizarCuenta("4307");
        gisr.issdsa();
        this.otrosgastos = og.getSaldoDeudor().add(ge.getSaldoDeudor().add(god.getSaldoDeudor().add(gisr.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);

        /*mayor de la cuenta otros ingresos*/
        oi = lc.mayorizarCuenta("5201");
        oi.issdsa();
        dg = lc.mayorizarCuenta("5202");
        dg.issdsa();
        oio = lc.mayorizarCuenta("5203");
        oio.issdsa();
        ie = lc.mayorizarCuenta("5204");
        ie.issdsa();
        iod = lc.mayorizarCuenta("5205");
        iod.issdsa();
        this.otrosingresos = oi.getSaldoAcreedor().add(dg.getSaldoAcreedor().add(oio.getSaldoAcreedor().add(ie.getSaldoAcreedor().add(iod.getSaldoAcreedor())))).setScale(2, RoundingMode.HALF_UP);
    }

    /*metodos de calculos respectivos para el estado de resultado partiendo de los saldos de las cuentas*/
    public BigDecimal getVentas() {
        return ventas.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setVentas(BigDecimal ventas) {
        this.ventas = ventas;
    }
    
    public BigDecimal getRebajassobreventas() {
        return rebajassobreventas.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setRebajassobreventas(BigDecimal rebajassobreventas) {
        this.rebajassobreventas = rebajassobreventas;
    }
    
    public BigDecimal getDevolucionessobreventas() {
        return devolucionessobreventas.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setDevolucionessobreventas(BigDecimal devolucionessobreventas) {
        this.devolucionessobreventas = devolucionessobreventas;
    }
    
    public BigDecimal getCompras() {
        return compras.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setCompras(BigDecimal compras) {
        this.compras = compras;
    }
    
    public BigDecimal getGastosobrecompras() {
        return gastosobrecompras.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setGastosobrecompras(BigDecimal gastosobrecompras) {
        this.gastosobrecompras = gastosobrecompras;
    }
    
    public BigDecimal getRebajassobrecompras() {
        return rebajassobrecompras.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setRebajassobrecompras(BigDecimal rebajassobrecompras) {
        this.rebajassobrecompras = rebajassobrecompras;
    }
    
    public BigDecimal getDevolucionessobrecompras() {
        return devolucionessobrecompras.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setDevolucionessobrecompras(BigDecimal devolucionessobrecompras) {
        this.devolucionessobrecompras = devolucionessobrecompras;
    }
    
    public BigDecimal getInventarioinicial() {
        return inventarioinicial.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setInventarioinicial(BigDecimal inventarioinicial) {
        this.inventarioinicial = inventarioinicial;
    }
    
    public BigDecimal getInventariofinal() {
        return inventariofinal.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setInventariofinal(BigDecimal inventariofinal) {
        this.inventariofinal = inventariofinal;
    }
    
    public BigDecimal getGastosadmon() {
        return gastosadmon.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setGastosadmon(BigDecimal gastosadmon) {
        this.gastosadmon = gastosadmon;
    }
    
    public BigDecimal getGastosventa() {
        return gastosventa.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setGastosventa(BigDecimal gastosventa) {
        this.gastosventa = gastosventa;
    }
    
    public BigDecimal getGastosfinancieros() {
        return gastosfinancieros.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setGastosfinancieros(BigDecimal gastosfinancieros) {
        this.gastosfinancieros = gastosfinancieros;
    }
    
    public BigDecimal getOtrosgastos() {
        return otrosgastos.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setOtrosgastos(BigDecimal otrosgastos) {
        this.otrosgastos = otrosgastos;
    }
    
    public BigDecimal getOtrosingresos() {
        return otrosingresos.setScale(2, RoundingMode.HALF_UP);
    }
    
    public void setOtrosingresos(BigDecimal otrosingresos) {
        this.otrosingresos = otrosingresos;
    }
    
    public BigDecimal ventasNetas() {
        
        return this.ventas.subtract(this.rebajassobreventas.add(this.devolucionessobreventas)).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal comprasTotales() {
        return this.compras.add(this.gastosobrecompras).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal comprasNetas() {
        return comprasTotales().subtract(this.devolucionessobrecompras.add(this.rebajassobrecompras)).setScale(2, RoundingMode.HALF_UP);
        
    }
    
    public BigDecimal mercaderiaDisponible() {
        return comprasNetas().add(this.inventarioinicial).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal costoVentas() {
        return mercaderiaDisponible().subtract(this.inventariofinal).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal utilidadBruta() {
        return ventasNetas().subtract(costoVentas()).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal gastosOperacion() {
        return this.gastosadmon.add(this.gastosventa.add(this.gastosfinancieros)).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal utilidadOperacion() {
        return utilidadBruta().subtract(gastosOperacion()).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal utilidadAntesImpuestoReservas() {
        return utilidadOperacion().subtract(this.otrosgastos).add(this.otrosingresos).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal reservaLegal() {
        return utilidadAntesImpuestoReservas().multiply(new BigDecimal("0.07")).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal utilidadAntesdelImpuesto() {
        return utilidadAntesImpuestoReservas().subtract(reservaLegal()).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal impuestoSobreLaRenta() {
        if (this.ventas.doubleValue() <= 150000) {
            return utilidadAntesdelImpuesto().multiply(new BigDecimal("0.25")).setScale(2, RoundingMode.HALF_UP);
        } else {
            return utilidadAntesdelImpuesto().multiply(new BigDecimal("0.30")).setScale(2, RoundingMode.HALF_UP);
        }
    }
    
    public BigDecimal utilidadDelEjercicio() {
        return utilidadAntesdelImpuesto().subtract(impuestoSobreLaRenta()).setScale(2, RoundingMode.HALF_UP);
    }
}
