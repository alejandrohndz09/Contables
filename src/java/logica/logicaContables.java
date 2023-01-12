/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import Procesos.Mayor;
import entidades.Cuenta;
import entidades.Debehaber;
import entidades.Inventario;
import entidades.Partida;
import entidades.Periodo;
import hibernateutil.HibernateUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Xom
 */
public class logicaContables {
    
    private SessionFactory sf;
    private Session ses;
    private Transaction tra;
    Query q, q2;
    
    public logicaContables() {
    }

    /*metodos de acceso*/
    private void iniciaOperacion() throws HibernateException {
        sf = HibernateUtil.getSessionFactory();
        ses = sf.openSession();
        tra = ses.beginTransaction();
    }
    
    private void manejaExcepcion(HibernateException e) throws HibernateException {
        tra.rollback();
        throw new HibernateException("Ocurrio un error", e);
    }

    /*
     * ************************************************************************
     *                      METODOS SOLO PARA LAS CUENTAS
     * *************************************************************************
     *
     */
 /*metodo para guardar la cuenta*/
    public void guardarcuenta(Cuenta c) throws HibernateException {
        try {
            iniciaOperacion();
            ses.save(c);
            tra.commit();
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
    }

    /*metodo para modificar los datos de la cuenta*/
    public void actualizarcuenta(Cuenta c) throws HibernateException {
        try {
            
            iniciaOperacion();
            ses.update(c);
            tra.commit();
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
    }
    
    public void eliminarCuenta(Cuenta c) throws HibernateException {
        try {
            iniciaOperacion();
            ses.delete(c);
            tra.commit();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
    }

    /*metodo para obtener las cuentas, retornando como arreglo solo con las cuentas padres*/
    public List<Cuenta> setearpadres() throws HibernateException {
        List<Cuenta> listadocuentas = null;
        try {
            iniciaOperacion();
            listadocuentas = ses.createQuery("from Cuenta as c order by c.codigocuenta asc").list();
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        return listadocuentas;
        
    }
    
    public List<Cuenta> listadocuentas() throws HibernateException {
        List<Cuenta> listadocuentas = null;
        try {
            iniciaOperacion();
            listadocuentas = ses.createQuery("from Cuenta as c order by c.codigocuenta asc").list();
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        /*si esta validacion no se pone entonces dara error en la ejecución
        de la vista debido a que no habra ninguna cuenta registrada y al pasar
        como parametro la lista al arbol ira null lo cual generaría el error,
        sin obtención alguna de las cuentas padres por parte del arbol*/
        if (listadocuentas.size() > 0) {
            return arbolcuentas(listadocuentas);
        } else {
            return listadocuentas;
        }
        
    }

    /*metodo que retornara las cuentas ya en su jerarquía correspondiente*/
    private List<Cuenta> arbolcuentas(List<Cuenta> cuentas) throws HibernateException {
        List<Cuenta> aux = ordenarCuentas((ArrayList<Cuenta>) cuentas);
        List<Cuenta> arbc = new ArrayList();/*ira con los datos de las cuentas ya ordenadas*/
 /*variables correspondientes para los respecitvos movimientos de las cuentas dentro del arbol*/
        Cuenta act = null;
        Cuenta sig = null;
        Cuenta tempo = null;
        String codA = null;
        String codS = null;
        int tamA = 0;
        int tamS = 0;
        int nivel = 0;
        arbc.add(aux.get(0));/*agregamos al arbol la primera cuenta del arreglo*/
        
        for (int i = 0; i < aux.size() - 1; i++) {/*recorremos el tamaño del arreglo recibido como parametro*/
            act = aux.get(i);
            act.setCuentinternas(new ArrayList<>());
            sig = aux.get(i + 1);
            codA = act.getCodigocuenta();
            codS = sig.getCodigocuenta();
            tamA = codA.length();
            tamS = codS.length();

            /*validacion para las cuentas donde se MOD sea diferente de cero,
            es decir son cuentas que tienen asignada una letra por lo cual hay que quitarselas
            para poder jerarquizar los niveles correctamente
            if (tamA % 2 != 0) {
                tamA = tamA - 1;
            } else if (tamS % 2 != 0) {
                tamS = tamS - 1;
            }*/
            if (tamS > tamA) {/*verificamos que si la siguiente cuenta su codigo es mayor a la anterior*/
                if (act.getCuentinternas() != null) {
                    if (act.getCuentinternas().isEmpty()) {
                        act.setCuentinternas(new ArrayList<>());
                    }
                }
                act.getCuentinternas().add(sig);
                sig.setCuenta(act);
            } else if ((tamS == tamA && (tamS != 1 && tamA != 1))) {/*si la longitud de los codigos son iguales, pero
                diferente de 1; entonces el valor de sig debe de agregarse despues del arreglo que contiene las hijas
                de la variable act*/
                tempo = act.getCuenta();
                sig.setCuenta(tempo);
                tempo.getCuentinternas().add(sig);
            } else {
                nivel = (int) (Math.round((tamA - tamS) / (float) 2) + 1);
                tempo = act;
                for (int f = 1; f <= nivel; f++) {
                    tempo = tempo.getCuenta();
                }
                if (tempo == null) {
                    arbc.add(sig);
                    sig.setCuenta(null);
                } else {
                    tempo.getCuentinternas().add(sig);
                    sig.setCuenta(tempo);
                    
                }
                
            }
            
        }
        return arbc;/*este arreglo me retornara solo las cuentas padres nada más*/
    }
    
    public List<Cuenta> buscacuentas() throws HibernateException {
        List<Cuenta> listadocuentas = null;
        try {
            iniciaOperacion();
            listadocuentas = ses.createQuery("from Cuenta as c order by c.codigocuenta asc").list();
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        return listadocuentas;
    }

    /*metodo que buscara un codigo ingresado en caso de un nuevo registro o edicion, si el codgigo existe retornara el objeto cargado
    con los datos respectivos*/
    public Cuenta buscarcodigo(String cuent, String cod, int id) {
        for (Cuenta c : buscacuentas()) {
            if (c.getCodigocuenta().equals(cod) || c.getNombrecuenta().compareTo(cuent) == 0 || c.getIdcuenta() == id) {
                return c;
            }
        }
        return null;
    }

    /*metodo para el ajax de busqueda de cuenta, retornara un arreglo con todas las cuentas encontradas dentro de la DB*/
    public List<Cuenta> nombrecodigo(String nombrecodigo) throws HibernateException {
        List<Cuenta> cuentasencontradas = new ArrayList();
        try { //and (LENGTH(c.codigocuenta)=6 or LENGTH(c.codigocuenta)=7)
            iniciaOperacion();
            //q = ses.createQuery("from Cuenta c where (c.codigocuenta LIKE ?) or (lower(c.nombrecuenta) LIKE lower(?))");
            q = ses.createQuery("from Cuenta c where (c.codigocuenta LIKE ? or lower(c.nombrecuenta) LIKE lower(?)) and (LENGTH(c.codigocuenta)>=6 or LENGTH(c.codigocuenta)-1>=6))");
            q.setString(0, "%" + nombrecodigo + "%");
            q.setString(1, "%" + nombrecodigo + "%");
            cuentasencontradas = q.list();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        return cuentasencontradas;
    }
    
    public Cuenta padre(String c) throws HibernateException {
        Cuenta p = null;
        try {
            iniciaOperacion();
            q = ses.createQuery("FROM Cuenta c where c.codigocuenta = ?");
            q.setString(0, c);
            p = (Cuenta) q.uniqueResult();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        return p;
    }

    /*
     * ************************************************************************
     *                  METODOS SOLO PARA LAS PARTIDAS
     * *************************************************************************
     */
    public void guardarpartidacargoabono(Partida p, ArrayList<Debehaber> listaca) throws HibernateException {
        Serializable id;//variable que se seteara a la partida a guardar para la insercion de los cargos y abonos
        try {
            /*insercion de la partida a la bd*/
            Periodo per = PeriodoenCurso();
            if (p != null) {
                p.setPeriodo(per);
                iniciaOperacion();
                id = ses.save(p);
                p.setIdpartida((int) id);
                tra.commit();
            }

            /*insercion de los cargos y abonos a la bd*/
            if (listaca.size() > 0) {
                iniciaOperacion();
                for (int i = 0; i < listaca.size(); i++) {
                    ses.save(listaca.get(i));
                }
                tra.commit();
            }
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
    }

    /*registrando las partidas de ajuste y de cierre*/
    public void registrarPartida(Partida partida) {
        Serializable id;
        try {
            /*registro de las partidas*/
            iniciaOperacion();
            id = ses.save(partida);
            partida.setIdpartida((Integer) id);
            tra.commit();
            /*registro de los cargos y abonos*/
            if (partida.getDebehabers().size() > 0) {
                iniciaOperacion();
                for (Debehaber ca : ordenarCierre(partida.getDebehabers())) {
                    ca.setPartida(partida);
                    ses.save(ca);
                }
                tra.commit();
            }
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
    }
    
    public void actualizarPartida(Partida p) throws HibernateException {
        try {
            
            iniciaOperacion();
            ses.update(p);
            tra.commit();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
    }
    
    public List<Partida> listapartidasperiodo(String numpartida, String fecha) throws HibernateException {
        List<Partida> listap = null;
        try {
            Periodo p = PeriodoenCurso();
            iniciaOperacion();
            q = ses.createQuery("from Partida as p where (cast(p.numpartida as string) LIKE ? or cast(p.fecha as string) LIKE ?) and (p.periodo.idperiodo=? ) order by p.idpartida asc");
            q.setString(0, "%" + numpartida + "%");
            q.setString(1, "%" + fecha + "%");
            q.setInteger(2, p.getIdperiodo());
            listap = q.list();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        if (listap == null) {
            return new ArrayList<>();
        } else {
            return listap;
        }
    }
    
    public List<Partida> listapartidasperiodo(int ninicio, int nfin, String fechainicio, String fechafin) throws HibernateException {
        List<Partida> listap = null;
        try {
            Periodo p = PeriodoenCurso();
            iniciaOperacion();
            q = ses.createQuery("from Partida as p where ((p.numpartida BETWEEN ? and ? or p.numpartida BETWEEN ? and ?) or (cast(p.fecha as string) BETWEEN ? and ? or cast(p.fecha as string) BETWEEN ? and ?)) and (p.periodo.idperiodo=? ) order by p.idpartida asc");
            q.setInteger(0, ninicio);
            q.setInteger(1, nfin);
            q.setInteger(2, nfin);
            q.setInteger(3, ninicio);
            q.setString(4, fechainicio);
            q.setString(5, fechafin);
            q.setString(6, fechafin);
            q.setString(7, fechainicio);
            q.setInteger(8, p.getIdperiodo());
            listap = q.list();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        if (listap == null) {
            return new ArrayList<>();
        } else {
            return listap;
        }
    }

    /*este metodo es opcional, en caso de que se requiera mayor seguridad, para recuperar los ca de cada partida debe llamarse
    en la vista librodiario.jsp enviando comò parametro el id de la partida correspondiente*/
    public List<Debehaber> debehaberporpartida(int id) throws HibernateException {
        List<Debehaber> listadh = null;
        
        try {
            iniciaOperacion();
            q = ses.createQuery("from Debehaber as dh join fetch dh.partida as p join fetch dh.cuenta as c where p.idpartida=?");
            q.setInteger(0, id);
            listadh = ordenarDH((ArrayList<Debehaber>) q.list());
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        if (listadh == null) {
            return new ArrayList<>();
        } else {
            return listadh;
        }
    }
    
    public ArrayList<Debehaber> ordenarDH(Set debehaber) {
        ArrayList<Debehaber> debe = new ArrayList();
        ArrayList<Debehaber> haber = new ArrayList();
        Debehaber aux;
        for (Object ca : debehaber) {
            aux = (Debehaber) ca;
            if (aux.getTipotransaccion().equals('c')) {
                debe.add(aux);
            } else if (aux.getTipotransaccion().equals('a')) {
                haber.add(aux);
            }
        }
        debe.addAll(haber);
        Collections.sort(debe, (Debehaber o1, Debehaber o2) -> new Integer(o1.getIddebehaber()).compareTo(o2.getIddebehaber()));
        
        return debe;
    }

    /*metodo para buscar partidas existentes en el periodo */
    public Partida buscapartidanumeroperiodoexistente(int numpartida) {
        for (Partida p : listapartidasperiodo("", "")) {
            if (p.getNumpartida() == numpartida) {
                return p;
            }
        }
        return null;
    }

    /*Busca detalle de dos partidas mas ingresadas, para poder agregarlas en caso de que el periodo este finalizado*/
    public Partida buscaDescripcion(String d) {
        for (Partida p : listapartidasperiodo("", "")) {
            if (p.getDescripcion().equals(d)) {
                return p;
            }
        }
        return null;
    }
    
    public int obtenerNumeroPartidaUltima() throws HibernateException {
        int npartidamax = 0;
        try {
            if (!listaPeriodos().isEmpty()) {
                iniciaOperacion();
                q = ses.createQuery("select max(p.numpartida) from Partida p");
                npartidamax = (Integer) q.uniqueResult();
            }
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        return npartidamax;
    }
    
    public Partida obtenerUltima() throws HibernateException {
        Partida ultima = null;
        try {
            iniciaOperacion();
            q = ses.createQuery("from Partida where idpartida = (select max(p.idpartida) from Partida p)");
            ultima = (Partida) q.uniqueResult();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        return ultima;
    }

    /*
    ********************************************************************************
    *   METODOS PARA LA MAYORIZACIÓN Y BALANZA DE SUMAS Y SALDOS DE LAS CUENTAS,
    *               PARA OBTENER SUS SALDOS Y PODER ACUMULARLOS
    ********************************************************************************
     */

 /*mayorizamiento para las cuentas por nivel*/
    public List<Mayor> listadoMayor(int lenc) throws HibernateException {
        ArrayList<Mayor> listam = new ArrayList<>();
        ArrayList<Mayor> aux = new ArrayList<>();
        Mayor mayor;
        List<Cuenta> listac = null;
        
        try {
            iniciaOperacion();
            q = ses.createQuery("from Cuenta c where LENGTH(c.codigocuenta) = " + lenc + " or LENGTH(c.codigocuenta)-1=" + lenc + " order by c.codigocuenta asc");
            listac = q.list();
            if (listac != null) {
                for (Cuenta x : listac) {
                    mayor = new Mayor(x);
                    recogerdebehaber(x, mayor);
                    aux.add(mayor);
                }
                if (aux.size() > 0) {
                    for (Mayor m : aux) {
                        if (m.getDh().size() > 0) {
                            listam.add(m);
                        }
                    }
                }
            }
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        if (listam.size() > 0) {
            return listam;
        } else {
            return new ArrayList<>();
        }
    }

    /*mayorización de las cuentas por nombre o letras en común*/
    public List<Mayor> listadoMayor(String nombre) throws HibernateException {
        ArrayList<Mayor> listam = new ArrayList<>();
        ArrayList<Mayor> aux = new ArrayList<>();
        Mayor mayor;
        List<Cuenta> listac = null;
        
        try {
            iniciaOperacion();
            q = ses.createQuery("from Cuenta c where (c.nombrecuenta LIKE? or c.codigocuenta=?) order by c.codigocuenta asc");
            q.setString(0, "%" + nombre + "%");
            q.setString(1, nombre);
            listac = q.list();
            if (listac != null) {
                for (Cuenta x : listac) {
                    mayor = new Mayor(x);
                    recogerdebehaber(x, mayor);
                    aux.add(mayor);
                }
                if (aux.size() > 0) {
                    for (Mayor m : aux) {
                        if (m.getDh().size() > 0) {
                            listam.add(m);
                        }
                    }
                }
            }
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        if (listam.size() > 0) {
            return listam;
        } else {
            return new ArrayList<>();
        }
    }

    /*mayorización de las cuentas por rangos de los codigos*/
    public List<Mayor> listadoMayor(String codigoinicio, String codigofinal) throws HibernateException {
        ArrayList<Mayor> listam = new ArrayList<>();
        ArrayList<Mayor> aux = new ArrayList<>();
        Mayor mayor;
        List<Cuenta> listac = null;
        
        try {
            iniciaOperacion();
            q = ses.createQuery("from Cuenta c where ((c.codigocuenta  BETWEEN '" + codigoinicio + "' and '" + codigofinal + "' ) or ((c.codigocuenta  BETWEEN '" + codigofinal + "' and '" + codigoinicio + "' ))) order by c.codigocuenta asc");
            listac = q.list();
            if (listac != null) {
                for (Cuenta x : listac) {
                    mayor = new Mayor(x);
                    recogerdebehaber(x, mayor);
                    aux.add(mayor);
                }
                if (aux.size() > 0) {
                    for (Mayor m : aux) {
                        if (m.getDh().size() > 0) {
                            listam.add(m);
                        }
                    }
                }
            }
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        if (listam.size() > 0) {
            return listam;
        } else {
            return new ArrayList<>();
        }
    }
    
    public List<Mayor> listaperiodocuentas(List<Mayor> lista) {
        List<Mayor> original = new ArrayList();
        for (Mayor m : lista) {
            m.liquidacion();
            if ((m.getSaldoAcreedor().floatValue() >= 0 || m.getSaldoDeudor().floatValue() >= 0) && m.getTama() > 0) {
                original.add(m);
            }
        }
        return original;
    }

    /**
     * ****************************************************************************
     * METODOS PARA EL PERIODO
     * ****************************************************************************
     */
    /*metodo para inicios de periodos para transacciones*/
    public void guardarPeriodo(Periodo p) throws HibernateException {
        try {
            iniciaOperacion();
            ses.save(p);
            tra.commit();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
    }

    /*metodo para la modificacion de los datos del periodo*/
    public void actualizarPeriodo(Periodo p) throws HibernateException {
        try {
            iniciaOperacion();
            ses.update(p);
            tra.commit();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
    }
    
    public Periodo PeriodoenCurso() {
        Periodo periodo = null;
        try {
            iniciaOperacion();
            q = ses.createQuery("FROM Periodo p where p.enproceso = true");
            periodo = (Periodo) q.uniqueResult();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        return periodo;
    }
    
    public List<Periodo> listaPeriodos() throws HibernateException {
        List<Periodo> listar = null;
        try {
            
            iniciaOperacion();
            q = ses.createQuery("from Periodo order by idperiodo asc");
            listar = q.list();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        return listar;
    }
    
    public Periodo buscaPeriodo(int id) {
        Periodo p = null;
        try {
            iniciaOperacion();
            q = ses.createQuery("FROM Periodo p WHERE p.idperiodo=?");
            q.setInteger(0, id);
            p = (Periodo) q.uniqueResult();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        return p;
    }
    
    public Periodo bp(Date fi, Date ff) throws HibernateException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        for (Periodo p : listaPeriodos()) {
            if (f.format(p.getFechainicial()).equals(f.format(fi)) && f.format(p.getFechafinal()).equals(f.format(ff))) {
                return p;
            }
        }
        return null;
    }

    /**
     * ****************************************************************************
     * METODOS PARA EL ESTADO DE RESULTADO
     * ****************************************************************************
     */
    public void guardarInventario(Inventario i) throws HibernateException {
        try {
            
            iniciaOperacion();
            ses.save(i);
            tra.commit();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
    }
    
    public void modificaInventario(Inventario i) throws HibernateException {
        try {
            
            iniciaOperacion();
            ses.update(i);
            tra.commit();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
    }
    
    public List<Inventario> listInventarios() throws HibernateException {
        List<Inventario> listar = null;
        try {
            
            iniciaOperacion();
            q = ses.createQuery("from Inventario as i order by i.periodo.idperiodo asc");
            listar = q.list();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        return listar;
    }

    /*
     *******************************************************************************************************************
     *METODOS PARA LAS PARTIDAS DE AJUSTE
     *******************************************************************************************************************
     */
    public List<Mayor> mayorizarCuenta(int nivel, String codigo) {
        ArrayList<Mayor> listam = new ArrayList<>();
        ArrayList<Mayor> aux = new ArrayList<>();
        Mayor mayor;
        List<Cuenta> listac = null;
        
        try {
            iniciaOperacion();
            q = ses.createQuery("from Cuenta c where (LENGTH(c.codigocuenta) = ? or LENGTH(c.codigocuenta)-1 = ?) and c.codigocuenta like? order by c.codigocuenta asc");
            q.setInteger(0, nivel);
            q.setInteger(1, nivel);
            q.setString(2, "%" + codigo + "%");
            listac = q.list();
            if (listac != null) {
                for (Cuenta x : listac) {
                    mayor = new Mayor(x);
                    recogerdebehaber(x, mayor);
                    aux.add(mayor);
                }
                if (aux.size() > 0) {
                    for (Mayor m : aux) {
                        if (m.getDh().size() > 0) {
                            listam.add(m);
                        }
                    }
                }
            }
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        if (listam.size() > 0) {
            return listam;
        } else {
            return new ArrayList<>();
        }
    }

    /*public Mayor mayorizarCuenta(int nivel,String codigo) {
        Mayor m = new Mayor();
        Cuenta c;
        try {
            iniciaOperacion();
            q = ses.createQuery("from Cuenta c where (LENGTH(c.codigocuenta) = ? or LENGTH(c.codigocuenta)-1 = ?) and c.codigocuenta like? order by c.codigocuenta asc");
            q.setInteger(0, nivel);
            q.setInteger(1, nivel);
            q.setString(2, "%" + codigo + "%");
            c = (Cuenta) q.uniqueResult();
            m.setCuentamayor(c);
            recogerdebehaber(c, m);

        } catch (HibernateException ex) {
            manejaExcepcion(ex);
            throw ex;
        } finally {
            ses.close();
        }
        return m;
    }*/
    //no borrar metodo ocupado antes de mayorizar al nivel ultimo
    public Mayor mayorizarCuenta(String codigo) {
        Mayor m = new Mayor();
        Cuenta c;
        try {
            iniciaOperacion();
            q = ses.createQuery("from Cuenta as c where c.codigocuenta=?");
            q.setString(0, codigo);
            c = (Cuenta) q.uniqueResult();
            m.setCuentamayor(c);
            recogerdebehaber(c, m);
            
        } catch (HibernateException ex) {
            manejaExcepcion(ex);
            throw ex;
        } finally {
            ses.close();
        }
        return m;
    }
    
    public Inventario recuperaInventario(String detalle, int periodo) {
        Inventario p = null;
        try {
            iniciaOperacion();
            q = ses.createQuery("from Inventario p WHERE (p.descripcion = ? and p.periodo.idperiodo = ?)");
            q.setString(0, detalle);
            q.setInteger(1, periodo);
            p = (Inventario) q.uniqueResult();
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        return p;
    }

    /*
    ********************************************************************************
    *   METODOS PARA LA MAYORIZACIÓN Y EL BALANCE GENERAL
    ********************************************************************************
     */
    public List<Mayor> listadoMayorBG(int lenc, String n) throws HibernateException {
        ArrayList<Mayor> listam = new ArrayList<>();
        ArrayList<Mayor> aux = new ArrayList<>();
        Mayor mayor;
        List<Cuenta> listac = null;
        
        try {
            iniciaOperacion();
            q = ses.createQuery("from Cuenta c where (LENGTH(c.codigocuenta) = ? or LENGTH(c.codigocuenta)-1 = ?) and SUBSTRING(c.codigocuenta,1,2) like? order by c.codigocuenta asc");
            q.setInteger(0, lenc);
            q.setInteger(1, lenc);
            q.setString(2, "%" + n + "%");
            listac = q.list();
            if (listac != null) {
                for (Cuenta x : listac) {
                    mayor = new Mayor(x);
                    recogerdebehaber(x, mayor);
                    aux.add(mayor);
                }
                if (aux.size() > 0) {
                    for (Mayor m : aux) {
                        if (m.getDh().size() > 0) {
                            listam.add(m);
                        }
                    }
                }
            }
            
        } catch (HibernateException e) {
            manejaExcepcion(e);
            throw e;
        } finally {
            ses.close();
        }
        
        if (listam.size() > 0) {
            return listam;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * **************************************************************************
     * METODOS DE ORDENAMIENTO
     * **************************************************************************
     */

    /*metodo para ordenar las cuentas de la mayorizacion mediante el iddebehaber*/
    public ArrayList<Debehaber> ordenarDH(ArrayList<Debehaber> debehaber) {
        ArrayList<Debehaber> debe = new ArrayList();
        ArrayList<Debehaber> haber = new ArrayList();
        debehaber.forEach((dh) -> {
            if (dh.getTipotransaccion().equals('c')) {
                debe.add(dh);
            } else if (dh.getTipotransaccion().equals('a')) {
                haber.add(dh);
            }
        });
        debe.addAll(haber);
        Collections.sort(debe, (Debehaber o1, Debehaber o2) -> new Integer(o1.getIddebehaber()).compareTo(o2.getIddebehaber()));
        
        return debe;
    }
    
    public ArrayList<Debehaber> ordenarCierre(Set debehaber) {
        ArrayList<Debehaber> debe = new ArrayList();
        ArrayList<Debehaber> haber = new ArrayList();
        Debehaber aux;
        for (Object ca : debehaber) {
            aux = (Debehaber) ca;
            if (aux.getTipotransaccion().equals('c')) {
                debe.add(aux);
            } else if (aux.getTipotransaccion().equals('a')) {
                haber.add(aux);
            }
        }
        debe.addAll(haber);
        return debe;
    }
    
    public ArrayList<Cuenta> ordenarCuentas(ArrayList<Cuenta> c) {
        ArrayList<Cuenta> cuenta = new ArrayList();
        String aux;
        for (int i = 0; i < c.size(); i++) {
            if (c.get(i).getCodigocuenta().length() % 2 != 0 && c.get(i).getCodigocuenta().length() > 1) {
                aux = c.get(i).getCodigocuenta().substring(0, c.get(i).getCodigocuenta().length() - 1);
                c.get(i).setCodigocuenta(aux);
                cuenta.add(c.get(i));
            } else {
                cuenta.add(c.get(i));
            }
        }
        Collections.sort(cuenta, (Cuenta o1, Cuenta o2) -> new String(o1.getCodigocuenta()).compareTo(o2.getCodigocuenta()));
        
        return cuenta;
    }

    /*este metodo servira para recoger los debe/haber de cada cuenta, de igual manera servira para recoger todos los debe/haber
     en caso de que haya cuentas hijas en la cuenta especificada en caso de que la mayorizacion se haga en un nivel respectivo o
    se especifique una cuenta en especifico a mostrar o para mostrar los rangos que se especifiquen de una cuenta hasta otra.*/
    public void recogerdebehaber(Cuenta c, Mayor m) {
        if (c != null) {
            m.getDh().addAll(c.getDebehabers());
            if (c.getCuentas().size() > 0) {
                c.getCuentas().forEach((Object t) -> {
                    recogerdebehaber((Cuenta) t, m);
                    
                });
            }
        }
    }
    
    public Cuenta arbol(Cuenta c, List<Cuenta> hijas) {
        Cuenta aux;
        for (Cuenta x : hijas) {
            if (c.getIdcuenta() == x.getCuenta().getIdcuenta()) {
                c.getCuentas().add(x.getCuenta());
            } else {
                aux = c;
                c = aux;
            }
        }
        return c;
    }
    
}