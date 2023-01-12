/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesos;

import entidades.Periodo;
import logica.logicaContables;

/**
 *
 * @author Xom
 */
public abstract class ValidaPeriodo {

    public static boolean encurso() {
        logicaContables lc = new logicaContables();
        Periodo periodo = lc.PeriodoenCurso();
        if (periodo == null) {
            return false;
        }
        return true;
    }
    
    public static boolean terminado() {
        logicaContables lc = new logicaContables();
        Periodo periodo = lc.PeriodoenCurso();
        if (periodo == null) {
            return false;
        }else if(periodo.getEnproceso()==true && periodo.getTerminado()==true){
            return false;
        }else if(periodo.getEnproceso()==true && periodo.getTerminado()==false){
            return true;
        }
        return true;
    }
}
