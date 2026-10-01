package Tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Maneja el "guardado de ventas por fecha".
 *
 * Las ventas nunca se borran: al guardar, cada venta pendiente
 * (Venta.id_cierre IS NULL) se asigna a un registro de CierreVentas
 * (uno por fecha). Así el historial actual queda vacío y el total en cero,
 * pero las ventas siguen existiendo para consultarlas después y para el
 * historial personal de cada cliente.
 */
public class GestorCierres {

    /** Crea la tabla CierreVentas y la columna Venta.id_cierre si no existen. */
    public static void asegurarEsquema(Connection con) throws Exception {

        try (Statement st = con.createStatement()) {

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS CierreVentas (" +
                "id_cierre INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fecha TEXT NOT NULL," +
                "fecha_guardado TEXT NOT NULL," +
                "num_ventas INTEGER NOT NULL," +
                "total REAL NOT NULL)"
            );

            boolean existeColumna = false;
            try (ResultSet rs = st.executeQuery("PRAGMA table_info(Venta)")) {
                while (rs.next()) {
                    if ("id_cierre".equalsIgnoreCase(rs.getString("name"))) {
                        existeColumna = true;
                    }
                }
            }

            if (!existeColumna) {
                st.executeUpdate("ALTER TABLE Venta ADD COLUMN id_cierre INTEGER");
            }
        }
    }

    /**
     * Guarda todas las ventas pendientes, agrupadas por su fecha.
     * Si ya existe un registro guardado para esa fecha, le suma las ventas nuevas.
     *
     * @return cantidad de fechas guardadas (0 si no había ventas pendientes)
     */
    public static int guardarPendientes() throws Exception {

        Connection con = Conexion.conectar();
        if (con == null) {
            throw new Exception("No se pudo conectar a la base de datos.");
        }

        try {
            asegurarEsquema(con);
            con.setAutoCommit(false);

            List<String> fechas = new ArrayList<>();

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(
                    "SELECT DISTINCT date(fecha, 'localtime') AS dia " +
                    "FROM Venta WHERE id_cierre IS NULL ORDER BY dia")) {
                while (rs.next()) {
                    fechas.add(rs.getString("dia"));
                }
            }

            for (String dia : fechas) {

                int cantidad;
                double total;

                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT COUNT(*) AS n, COALESCE(SUM(total), 0) AS t " +
                        "FROM Venta WHERE id_cierre IS NULL " +
                        "AND date(fecha, 'localtime') = ?")) {
                    ps.setString(1, dia);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        cantidad = rs.getInt("n");
                        total = rs.getDouble("t");
                    }
                }

                int idCierre = -1;

                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT id_cierre FROM CierreVentas WHERE fecha = ?")) {
                    ps.setString(1, dia);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            idCierre = rs.getInt("id_cierre");
                        }
                    }
                }

                if (idCierre == -1) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO CierreVentas " +
                            "(fecha, fecha_guardado, num_ventas, total) " +
                            "VALUES (?, datetime('now', 'localtime'), ?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, dia);
                        ps.setInt(2, cantidad);
                        ps.setDouble(3, total);
                        ps.executeUpdate();
                        try (ResultSet claves = ps.getGeneratedKeys()) {
                            if (!claves.next()) {
                                throw new Exception("No se pudo obtener el ID del registro guardado.");
                            }
                            idCierre = claves.getInt(1);
                        }
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE CierreVentas SET " +
                            "num_ventas = num_ventas + ?, total = total + ?, " +
                            "fecha_guardado = datetime('now', 'localtime') " +
                            "WHERE id_cierre = ?")) {
                        ps.setInt(1, cantidad);
                        ps.setDouble(2, total);
                        ps.setInt(3, idCierre);
                        ps.executeUpdate();
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE Venta SET id_cierre = ? " +
                        "WHERE id_cierre IS NULL AND date(fecha, 'localtime') = ?")) {
                    ps.setInt(1, idCierre);
                    ps.setString(2, dia);
                    ps.executeUpdate();
                }
            }

            con.commit();
            return fechas.size();

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ignorar) {
            }
            throw e;

        } finally {
            try {
                con.close();
            } catch (Exception ignorar) {
            }
        }
    }

    /** Formato de dinero, por ejemplo: $ 21.000 */
    public static String dinero(double valor) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "CO"));
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return "$ " + nf.format(valor);
    }
}
