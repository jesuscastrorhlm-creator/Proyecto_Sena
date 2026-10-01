package Tienda;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Guarda en este equipo las cuentas que el usuario decidió recordar
 * (marcando "Recordar mi cuenta"), para sugerirlas en el login.
 * Se guardan en el archivo cuentas_recordadas.properties, junto a tienda.db.
 */
public class CuentasRecordadas {

    private static final File ARCHIVO = new File("cuentas_recordadas.properties");

    private static Properties cargar() {
        Properties p = new Properties();
        if (ARCHIVO.exists()) {
            try (InputStreamReader in = new InputStreamReader(new FileInputStream(ARCHIVO), StandardCharsets.UTF_8)) {
                p.load(in);
            } catch (Exception e) {
                System.out.println("No se pudieron leer las cuentas recordadas: " + e.getMessage());
            }
        }
        return p;
    }

    private static void guardarArchivo(Properties p) {
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(ARCHIVO), StandardCharsets.UTF_8)) {
            p.store(out, "Cuentas recordadas - Galapa Express");
        } catch (Exception e) {
            System.out.println("No se pudieron guardar las cuentas recordadas: " + e.getMessage());
        }
    }

    /** Correos recordados, en orden alfabético. */
    public static List<String> listar() {
        List<String> correos = new ArrayList<>(cargar().stringPropertyNames());
        Collections.sort(correos);
        return correos;
    }

    /** Contraseña guardada para ese correo, o null si no existe. */
    public static String obtenerContrasena(String correo) {
        String valor = cargar().getProperty(correo.toLowerCase());
        if (valor == null) {
            return null;
        }
        try {
            return new String(Base64.getDecoder().decode(valor), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void guardar(String correo, String contrasena) {
        Properties p = cargar();
        p.setProperty(correo.toLowerCase(),
                Base64.getEncoder().encodeToString(contrasena.getBytes(StandardCharsets.UTF_8)));
        guardarArchivo(p);
    }

    public static void eliminar(String correo) {
        Properties p = cargar();
        if (p.remove(correo.toLowerCase()) != null) {
            guardarArchivo(p);
        }
    }

    public static void borrarTodas() {
        guardarArchivo(new Properties());
    }
}
