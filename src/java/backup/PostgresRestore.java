/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xom
 */
public class PostgresRestore {

    public PostgresRestore() {
    }

    public int restore(String puerto, String usuario, String password, String dbpgadmin, String nombrearchivo) {
        final List<String> comandos = new ArrayList<>();
        comandos.add("C:\\Program Files\\PostgreSQL\\9.1\\bin\\pg_restore.exe"); // Direccion de carpeta donde se instala pgAdmin en disco local C y su .exe de instalacion
        comandos.add("-h");
        comandos.add("localhost");
        comandos.add("-p");
        comandos.add(puerto);//puerto de pgadmin
        comandos.add("-U");
        comandos.add(usuario); //nombre de usuario en pgAdmin
        comandos.add("-c");
        comandos.add("-d");
        comandos.add(dbpgadmin); // nombre de la base de datos en pgAdmin
        comandos.add("-v");
        comandos.add("C:\\Respaldo Contabilidad\\" + nombrearchivo);//el nombre del archivo seleccionado debe ser igual al nombre de los backup que se poseen en esta carpeta
        ProcessBuilder pb = new ProcessBuilder(comandos);
        pb.environment().put("PGPASSWORD", password); // contraseña de pgAdmin
        try {
            final Process process = pb.start();
            final BufferedReader r = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String line = r.readLine();
            while (line != null) {
                System.err.println(line);
                line = r.readLine();
            }
            r.close();
            process.waitFor();
            process.destroy();
            return 1;
            //JOptionPane.showMessageDialog(null, "Restauracion realizada con EXITO!!");
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            return 0;
        }

    }

}
