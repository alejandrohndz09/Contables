package entidades;
// Generated 29/10/2019 03:43:38 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Partida generated by hbm2java
 */
public class Partida  implements java.io.Serializable {


     private int idpartida;
     private Periodo periodo;
     private Date fecha;
     private String descripcion;
     private Integer numpartida;
     private Set debehabers = new HashSet(0);

    public Partida() {
    }
	
    public Partida(int idpartida) {
        this.idpartida = idpartida;
    }
    public Partida(int idpartida, Periodo periodo, Date fecha, String descripcion, Integer numpartida, Set debehabers) {
       this.idpartida = idpartida;
       this.periodo = periodo;
       this.fecha = fecha;
       this.descripcion = descripcion;
       this.numpartida = numpartida;
       this.debehabers = debehabers;
    }
   
    public int getIdpartida() {
        return this.idpartida;
    }
    
    public void setIdpartida(int idpartida) {
        this.idpartida = idpartida;
    }
    public Periodo getPeriodo() {
        return this.periodo;
    }
    
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Integer getNumpartida() {
        return this.numpartida;
    }
    
    public void setNumpartida(Integer numpartida) {
        this.numpartida = numpartida;
    }
    public Set getDebehabers() {
        return this.debehabers;
    }
    
    public void setDebehabers(Set debehabers) {
        this.debehabers = debehabers;
    }




}


