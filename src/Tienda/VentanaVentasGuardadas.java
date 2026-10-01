package Tienda;

import Tienda.Conexion;
import Tienda.EstiloUI;
import Tienda.GestorCierres;
import Tienda.Interfaces.VentanaDetalleVenta;
import Tienda.PanelFondo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Muestra las ventas que ya fueron guardadas por fecha (cierres del día).
 * Arriba: un registro por fecha. Al seleccionar una fecha se ven sus ventas,
 * y al seleccionar una venta se ve su detalle.
 */
public class VentanaVentasGuardadas extends JFrame {

    private final List<Integer> idsCierre = new ArrayList<>();
    private final List<Integer> idsVenta = new ArrayList<>();

    private JTable tablaCierres;
    private JTable tablaVentas;
    private JTable tablaDetalle;
    private JTextField txtDesde;
    private JTextField txtHasta;
    private JLabel lblTotalPeriodo;

    public VentanaVentasGuardadas() {

        setTitle("Ventas guardadas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new PanelFondo("/Tienda/Imagenes/fondo_abarrotes.png"));
        setIconImage(new ImageIcon(getClass().getResource("/Tienda/Imagenes/fondo_abarrotes.png")).getImage());

        construirInterfaz();
        cargarCierres(null, null);

        setSize(900, 760);
        setLocationRelativeTo(null);
    }

    // ---------- Construcción de la interfaz ----------

    private void construirInterfaz() {

        getContentPane().setLayout(new BorderLayout(0, 8));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 30, 15, 30));

        // Encabezado
        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        encabezado.setOpaque(false);

        JLabel logo = new JLabel(new ImageIcon(getClass().getResource("/Tienda/Imagenes/icono_galapa_.png")));
        JLabel titulo = new JLabel("VENTAS GUARDADAS");
        titulo.setFont(new Font("Ubuntu Mono", Font.BOLD, 40));
        titulo.setForeground(Color.WHITE);

        encabezado.add(logo);
        encabezado.add(titulo);
        getContentPane().add(encabezado, BorderLayout.NORTH);

        // Centro
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        // Filtro por fechas
        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filtro.setOpaque(false);
        filtro.setAlignmentX(LEFT_ALIGNMENT);

        txtDesde = new JTextField(9);
        txtHasta = new JTextField(9);
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnTodo = new JButton("Ver todo");
        EstiloUI.botonPrimario(btnFiltrar);
        EstiloUI.botonNeutro(btnTodo);
        btnFiltrar.setPreferredSize(new Dimension(100, 30));
        btnTodo.setPreferredSize(new Dimension(100, 30));

        filtro.add(etiqueta("Desde (AAAA-MM-DD):"));
        filtro.add(txtDesde);
        filtro.add(etiqueta("Hasta:"));
        filtro.add(txtHasta);
        filtro.add(btnFiltrar);
        filtro.add(btnTodo);

        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnTodo.addActionListener(e -> {
            txtDesde.setText("");
            txtHasta.setText("");
            cargarCierres(null, null);
        });

        centro.add(filtro);
        centro.add(Box.createVerticalStrut(10));

        // Tabla de fechas guardadas
        centro.add(etiqueta("Días guardados (selecciona uno para ver sus ventas)"));
        tablaCierres = crearTabla(new String[]{"Fecha", "Guardado el", "N° ventas", "Total del día"});
        centro.add(envolver(tablaCierres, 190));
        centro.add(Box.createVerticalStrut(10));

        // Ventas del día seleccionado
        centro.add(etiqueta("Ventas de la fecha seleccionada"));
        tablaVentas = crearTabla(new String[]{"ID Venta", "Fecha y hora", "Cliente", "Total"});
        centro.add(envolver(tablaVentas, 170));
        centro.add(Box.createVerticalStrut(10));

        // Detalle de la venta
        centro.add(etiqueta("Detalle de la venta seleccionada"));
        tablaDetalle = crearTabla(new String[]{"Producto", "Cantidad", "Precio Unitario", "SubTotal"});
        centro.add(envolver(tablaDetalle, 120));

        getContentPane().add(centro, BorderLayout.CENTER);

        // Pie
        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);

        lblTotalPeriodo = new JLabel(" ");
        lblTotalPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalPeriodo.setForeground(EstiloUI.AMARILLO);

        JButton btnVolver = new JButton(new ImageIcon(getClass().getResource("/Tienda/Imagenes/volver.png")));
        EstiloUI.botonSecundario(btnVolver);
        btnVolver.addActionListener(e -> {
            new VentanaDetalleVenta().setVisible(true);
            dispose();
        });

        JButton btnEliminar = new JButton("Eliminar día seleccionado");
        EstiloUI.botonNeutro(btnEliminar);
        btnEliminar.setPreferredSize(new Dimension(200, 30));
        btnEliminar.addActionListener(e -> eliminarCierreSeleccionado());

        pie.add(lblTotalPeriodo, BorderLayout.CENTER);
        pie.add(btnVolver, BorderLayout.WEST);
        pie.add(btnEliminar, BorderLayout.EAST);
        getContentPane().add(pie, BorderLayout.SOUTH);

        // Selección: fecha -> ventas -> detalle
        tablaCierres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaCierres.getSelectedRow();
                if (fila >= 0 && fila < idsCierre.size()) {
                    cargarVentasDeCierre(idsCierre.get(fila));
                }
            }
        });

        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaVentas.getSelectedRow();
                if (fila >= 0 && fila < idsVenta.size()) {
                    cargarDetalle(idsVenta.get(fila));
                }
            }
        });
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setForeground(Color.WHITE);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JTable crearTabla(String[] columnas) {
        DefaultTableModel modelo = new DefaultTableModel(new Object[0][0], columnas) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        return new JTable(modelo);
    }

    private JScrollPane envolver(JTable tabla, int alto) {
        JScrollPane sp = new JScrollPane(tabla);
        sp.setAlignmentX(LEFT_ALIGNMENT);
        sp.setPreferredSize(new Dimension(700, alto));
        sp.setMinimumSize(new Dimension(100, alto));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, alto));
        return sp;
    }

    // ---------- Datos ----------

    private void aplicarFiltro() {

        String desde = txtDesde.getText().trim();
        String hasta = txtHasta.getText().trim();

        try {
            if (!desde.isEmpty()) {
                LocalDate.parse(desde);
            }
            if (!hasta.isEmpty()) {
                LocalDate.parse(hasta);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Escribe las fechas con el formato AAAA-MM-DD, por ejemplo 2026-09-28.");
            return;
        }

        if (!desde.isEmpty() && !hasta.isEmpty() && desde.compareTo(hasta) > 0) {
            JOptionPane.showMessageDialog(this, "La fecha 'Desde' no puede ser mayor que 'Hasta'.");
            return;
        }

        cargarCierres(desde.isEmpty() ? null : desde, hasta.isEmpty() ? null : hasta);
    }

    private void cargarCierres(String desde, String hasta) {

        DefaultTableModel modelo = (DefaultTableModel) tablaCierres.getModel();
        modelo.setRowCount(0);
        idsCierre.clear();
        limpiar(tablaVentas, idsVenta);
        ((DefaultTableModel) tablaDetalle.getModel()).setRowCount(0);

        double sumaTotal = 0;
        int sumaVentas = 0;

        try (java.sql.Connection con = Conexion.conectar()) {

            GestorCierres.asegurarEsquema(con);

            StringBuilder sql = new StringBuilder(
                    "SELECT id_cierre, fecha, fecha_guardado, num_ventas, total " +
                    "FROM CierreVentas WHERE 1 = 1");
            if (desde != null) {
                sql.append(" AND fecha >= ?");
            }
            if (hasta != null) {
                sql.append(" AND fecha <= ?");
            }
            sql.append(" ORDER BY fecha DESC");

            try (java.sql.PreparedStatement ps = con.prepareStatement(sql.toString())) {

                int i = 1;
                if (desde != null) {
                    ps.setString(i++, desde);
                }
                if (hasta != null) {
                    ps.setString(i++, hasta);
                }

                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        modelo.addRow(new Object[]{
                            rs.getString("fecha"),
                            rs.getString("fecha_guardado"),
                            rs.getInt("num_ventas"),
                            GestorCierres.dinero(rs.getDouble("total"))
                        });
                        idsCierre.add(rs.getInt("id_cierre"));
                        sumaTotal += rs.getDouble("total");
                        sumaVentas += rs.getInt("num_ventas");
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las ventas guardadas: " + e.getMessage());
        }

        if (idsCierre.isEmpty()) {
            lblTotalPeriodo.setText("No hay ventas guardadas para mostrar.");
        } else {
            lblTotalPeriodo.setText("TOTAL MOSTRADO: " + GestorCierres.dinero(sumaTotal)
                    + "  (" + sumaVentas + " ventas)");
        }
    }

    /** Elimina el día guardado seleccionado junto con sus ventas y detalles. */
    private void eliminarCierreSeleccionado() {

        int fila = tablaCierres.getSelectedRow();
        if (fila < 0 || fila >= idsCierre.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona un día para eliminar.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(this,
                "Se eliminará este día y todas sus ventas. Esta acción no se puede deshacer.\n¿Continuar?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        int idCierre = idsCierre.get(fila);

        try (java.sql.Connection con = Conexion.conectar()) {

            con.setAutoCommit(false);

            try (java.sql.PreparedStatement p1 = con.prepareStatement(
                    "DELETE FROM DetalleVenta WHERE id_venta IN " +
                    "(SELECT id_venta FROM Venta WHERE id_cierre = ?)");
                 java.sql.PreparedStatement p2 = con.prepareStatement(
                    "DELETE FROM Venta WHERE id_cierre = ?");
                 java.sql.PreparedStatement p3 = con.prepareStatement(
                    "DELETE FROM CierreVentas WHERE id_cierre = ?")) {

                p1.setInt(1, idCierre);
                p1.executeUpdate();
                p2.setInt(1, idCierre);
                p2.executeUpdate();
                p3.setInt(1, idCierre);
                p3.executeUpdate();

                con.commit();

            } catch (Exception ex) {
                con.rollback();
                throw ex;
            }

            cargarCierres(null, null);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void limpiar(JTable tabla, List<Integer> ids) {
        ((DefaultTableModel) tabla.getModel()).setRowCount(0);
        ids.clear();
    }

    private void cargarVentasDeCierre(int idCierre) {

        DefaultTableModel modelo = (DefaultTableModel) tablaVentas.getModel();
        limpiar(tablaVentas, idsVenta);
        ((DefaultTableModel) tablaDetalle.getModel()).setRowCount(0);

        try (java.sql.Connection con = Conexion.conectar();
             java.sql.PreparedStatement ps = con.prepareStatement(
                "SELECT Venta.id_venta, Venta.fecha, Cliente.nombre, Venta.total " +
                "FROM Venta INNER JOIN Cliente ON Venta.id_cliente = Cliente.id_cliente " +
                "WHERE Venta.id_cierre = ? ORDER BY Venta.id_venta DESC")) {

            ps.setInt(1, idCierre);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("id_venta"),
                        rs.getString("fecha"),
                        rs.getString("nombre"),
                        GestorCierres.dinero(rs.getDouble("total"))
                    });
                    idsVenta.add(rs.getInt("id_venta"));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las ventas: " + e.getMessage());
        }
    }

    private void cargarDetalle(int idVenta) {

        DefaultTableModel modelo = (DefaultTableModel) tablaDetalle.getModel();
        modelo.setRowCount(0);

        try (java.sql.Connection con = Conexion.conectar();
             java.sql.PreparedStatement ps = con.prepareStatement(
                "SELECT Producto.nombre, DetalleVenta.cantidad, " +
                "DetalleVenta.precio_unitario, DetalleVenta.subtotal " +
                "FROM DetalleVenta INNER JOIN Producto " +
                "ON DetalleVenta.id_producto = Producto.id_producto " +
                "WHERE DetalleVenta.id_venta = ?")) {

            ps.setInt(1, idVenta);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        GestorCierres.dinero(rs.getDouble("precio_unitario")),
                        GestorCierres.dinero(rs.getDouble("subtotal"))
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el detalle: " + e.getMessage());
        }
    }
}