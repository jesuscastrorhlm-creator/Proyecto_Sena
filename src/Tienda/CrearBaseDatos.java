package Tienda;

import java.sql.Connection;
import java.sql.Statement;

public class CrearBaseDatos {

    public static void main(String[] args) {

        Connection conexion = Conexion.conectar();

        if (conexion != null) {

            try {

                Statement sentencia = conexion.createStatement();

                // Tabla Usuario
                sentencia.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Usuario (" +
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "usuario TEXT NOT NULL," +
                    "contrasena TEXT NOT NULL," +
                    "tipo TEXT NOT NULL)"
                );

                // Tabla Producto
                sentencia.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Producto (" +
                    "id_producto INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "precio REAL NOT NULL," +
                    "stock INTEGER NOT NULL," +
                    "categoria TEXT)"
                );

                // Tabla Cliente
                sentencia.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Cliente (" +
                    "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "telefono TEXT)"
                );

                // Tabla Venta
                sentencia.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Venta (" +
                    "id_venta INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fecha TEXT NOT NULL," +
                    "id_cliente INTEGER NOT NULL," +
                    "total REAL NOT NULL," +
                    "FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente))"
                );

                // Tabla DetalleVenta
                sentencia.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS DetalleVenta (" +
                    "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_venta INTEGER NOT NULL," +
                    "id_producto INTEGER NOT NULL," +
                    "cantidad INTEGER NOT NULL," +
                    "precio_unitario REAL NOT NULL," +
                    "subtotal REAL NOT NULL," +
                    "FOREIGN KEY (id_venta) REFERENCES Venta(id_venta)," +
                    "FOREIGN KEY (id_producto) REFERENCES Producto(id_producto))"
                );
                // Agregar columnas necesarias si no existen
try {
    sentencia.executeUpdate(
        "ALTER TABLE Usuario ADD COLUMN telefono TEXT"
    );
    System.out.println("Columna telefono agregada a Usuario.");
} catch (Exception e) {
    System.out.println("La columna telefono ya existe en Usuario.");
}

try {
    sentencia.executeUpdate(
        "ALTER TABLE Cliente ADD COLUMN id_usuario INTEGER"
    );
    System.out.println("Columna id_usuario agregada a Cliente.");
} catch (Exception e) {
    System.out.println("La columna id_usuario ya existe en Cliente.");
}
// Agregar productos de ejemplo
sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Aceite Vegetal 1L', 6500, 20, 'Aceites y Condimentos' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Aceite Vegetal 1L')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Leche Entera 1L', 4500, 25, 'Lácteos y Huevos' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Leche Entera 1L')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Huevos x12', 8500, 15, 'Lácteos y Huevos' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Huevos x12')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Coca Cola 1.5L', 6000, 20, 'Bebidas' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Coca Cola 1.5L')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Agua 600ml', 2000, 30, 'Bebidas' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Agua 600ml')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Detergente 1kg', 7000, 15, 'Aseo del Hogar' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Detergente 1kg')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Jabón de lavar', 3000, 20, 'Aseo del Hogar' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Jabón de lavar')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Papas Margarita', 2500, 25, 'Snacks y Dulces' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Papas Margarita')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Chocolate', 3000, 20, 'Snacks y Dulces' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Chocolate')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Salchichas', 6500, 15, 'Carnes y Embutidos' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Salchichas')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Chorizo', 8000, 15, 'Carnes y Embutidos' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Chorizo')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Banano', 500, 50, 'Frutas y Verduras' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Banano')"
);

sentencia.executeUpdate(
    "INSERT INTO Producto (nombre, precio, stock, categoria) " +
    "SELECT 'Tomate', 800, 40, 'Frutas y Verduras' " +
    "WHERE NOT EXISTS (SELECT 1 FROM Producto WHERE nombre = 'Tomate')"
);

                GestorCierres.asegurarEsquema(conexion);

                System.out.println("Tablas creadas correctamente.");

                conexion.close();

            } catch (Exception e) {

                System.out.println("Error al crear las tablas: " + e.getMessage());

            }

        }
    }
}