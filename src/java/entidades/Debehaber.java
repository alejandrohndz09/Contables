package entidades;
// Generated 29/10/2019 03:43:38 AM by Hibernate Tools 4.3.1


import java.math.BigDecimal;

/**
 * Debehaber generated by hbm2java
 */
public class Debehaber  implements java.io.Serializable {


     private int iddebehaber;
     private Cuenta cuenta;
     private Partida partida;
     private BigDecimal montopartida;
     private Character tipotransaccion;

    public Debehaber() {
    }

	
    public Debehaber(int iddebehaber) {
        this.iddebehaber = iddebehaber;
    }
    public Debehaber(int iddebehaber, Cuenta cuenta, Partida partida, BigDecimal montopartida, Character tipotransaccion) {
       this.iddebehaber = iddebehaber;
       this.cuenta = cuenta;
       this.partida = partida;
       this.montopartida = montopartida;
       this.tipotransaccion = tipotransaccion;
    }

    public Debehaber(Cuenta cuenta, Partida partida, BigDecimal montopartida, Character tipotransaccion) {
        this.cuenta = cuenta;
        this.partida = partida;
        this.montopartida = montopartida;
        this.tipotransaccion = tipotransaccion;
    }

    public Debehaber(Cuenta cuenta, BigDecimal montopartida, Character tipotransaccion) {
        this.cuenta = cuenta;
        this.montopartida = montopartida;
        this.tipotransaccion = tipotransaccion;
    }
   
    public int getIddebehaber() {
        return this.iddebehaber;
    }
    
    public void setIddebehaber(int iddebehaber) {
        this.iddebehaber = iddebehaber;
    }
    public Cuenta getCuenta() {
        return this.cuenta;
    }
    
    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
    public Partida getPartida() {
        return this.partida;
    }
    
    public void setPartida(Partida partida) {
        this.partida = partida;
    }
    public BigDecimal getMontopartida() {
        return this.montopartida;
    }
    
    public void setMontopartida(BigDecimal montopartida) {
        this.montopartida = montopartida;
    }
    public Character getTipotransaccion() {
        return this.tipotransaccion;
    }
    
    public void setTipotransaccion(Character tipotransaccion) {
        this.tipotransaccion = tipotransaccion;
    }




}


