/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tienda.Interfaces;

import Tienda.VentanaVentasGuardadas;
import Tienda.Conexion;
import Tienda.Interfaces.PanelAdministrador;
import Tienda.PanelFondo;

/**
 *
 * @author CRISTIAN PC
 */
public class VentanaDetalleVenta extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaDetalleVenta.class.getName());

    /**
     * Creates new form VentanaDetalleVenta
     */
    private java.util.List<Integer> idsVentaEnTabla = new java.util.ArrayList<>();

    public VentanaDetalleVenta() {
        setContentPane(new PanelFondo("/Tienda/Imagenes/fondo_abarrotes.png"));
    initComponents();
    setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/fondo_abarrotes.png")).getImage());

    Tienda.EstiloUI.botonSecundario(btnVolver);

        crearControlesDeCierre();
        cargarVentas();
    }

    // ---------- Total del día y guardado de ventas por fecha ----------

    private javax.swing.JLabel lblTotalVentas;
    private javax.swing.JButton btnGuardarDia;
    private javax.swing.JButton btnVerGuardadas;

    /**
     * Agrega el total de ventas y los botones "Guardar ventas del día" y
     * "Ver ventas guardadas", y reacomoda la ventana para que quepan.
     * (Está fuera del código generado para que el editor visual no lo borre.)
     */
    private void crearControlesDeCierre() {

        lblTotalVentas = new javax.swing.JLabel("TOTAL DE VENTAS: $ 0");
        lblTotalVentas.setFont(new java.awt.Font("Segoe UI", 1, 22));
        lblTotalVentas.setForeground(Tienda.EstiloUI.AMARILLO);

        btnGuardarDia = new javax.swing.JButton("GUARDAR VENTAS DEL DÍA");
        btnGuardarDia.setFont(new java.awt.Font("Segoe UI", 0, 16));
        Tienda.EstiloUI.botonPrimario(btnGuardarDia);
        btnGuardarDia.addActionListener(e -> guardarVentasDelDia());

        btnVerGuardadas = new javax.swing.JButton("VER VENTAS GUARDADAS");
        btnVerGuardadas.setFont(new java.awt.Font("Segoe UI", 0, 16));
        Tienda.EstiloUI.botonAcento(btnVerGuardadas);
        btnVerGuardadas.addActionListener(e -> {
            new VentanaVentasGuardadas().setVisible(true);
            this.dispose();
        });

        jScrollPane2.setPreferredSize(new java.awt.Dimension(609, 230));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(609, 123));
        btnGuardarDia.setPreferredSize(new java.awt.Dimension(290, 50));
        btnVerGuardadas.setPreferredSize(new java.awt.Dimension(290, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

               layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(77, 77, 77)
                        .addComponent(jLabel1))
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalVentas)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnVolver)
                        .addGap(20, 20, 20)
                        .addComponent(btnGuardarDia, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnVerGuardadas, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );


        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel1)))
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblTotalVentas)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnVolver)
                    .addComponent(btnGuardarDia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVerGuardadas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void guardarVentasDelDia() {

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(this,
                "Se guardarán las ventas actuales por fecha y el historial quedará vacío.\n"
                + "El total volverá a $ 0.\n\n¿Deseas continuar?",
                "Guardar ventas del día",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (respuesta != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int fechas = Tienda.GestorCierres.guardarPendientes();

            if (fechas == 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "No hay ventas para guardar.");
                return;
            }

            cargarVentas();
            ((javax.swing.table.DefaultTableModel) tablaDetalle.getModel()).setRowCount(0);

            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ventas guardadas correctamente.\nPuedes consultarlas en \"Ver ventas guardadas\".");

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No se pudieron guardar las ventas: " + e.getMessage());
        }
    }
    
        private void cargarVentas() {
        try {
            java.sql.Connection con = Conexion.conectar();
            Tienda.GestorCierres.asegurarEsquema(con);
            java.sql.Statement st = con.createStatement();

            // Solo las ventas que aún no se han guardado
            String sql = "SELECT Venta.id_venta, Venta.fecha, Cliente.nombre, Venta.total " +
                         "FROM Venta " +
                         "INNER JOIN Cliente ON Venta.id_cliente = Cliente.id_cliente " +
                         "WHERE Venta.id_cierre IS NULL " +
                         "ORDER BY Venta.id_venta DESC";

            java.sql.ResultSet rs = st.executeQuery(sql);

            javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) tablaVentas.getModel();
            modelo.setRowCount(0);
            idsVentaEnTabla.clear();
            double totalVentas = 0;

            while (rs.next()) {
                totalVentas += rs.getDouble("total");
                modelo.addRow(new Object[]{
                    rs.getInt("id_venta"),
                    rs.getString("fecha"),
                    rs.getString("nombre"),
                    rs.getDouble("total")
                });
                idsVentaEnTabla.add(rs.getInt("id_venta"));
            }

            lblTotalVentas.setText("TOTAL DE VENTAS: " + Tienda.GestorCierres.dinero(totalVentas));

            con.close();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + e.getMessage());
        }
    }

    private void cargarDetalle(int idVenta) {
        try {
            java.sql.Connection con = Conexion.conectar();

            String sql = "SELECT Producto.nombre, DetalleVenta.cantidad, DetalleVenta.precio_unitario, DetalleVenta.subtotal " +
                         "FROM DetalleVenta " +
                         "INNER JOIN Producto ON DetalleVenta.id_producto = Producto.id_producto " +
                         "WHERE DetalleVenta.id_venta = ?";

            java.sql.PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, idVenta);
            java.sql.ResultSet rs = st.executeQuery();

            javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) tablaDetalle.getModel();
            modelo.setRowCount(0);

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("subtotal")
                });
            }

            con.close();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar el detalle: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaDetalle = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Ubuntu Mono", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("HISTORIAL DE VENTAS");

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Venta", "Fecha", "Cliente", "Total"
            }
        ));
        tablaVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaVentasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaVentas);

        tablaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio Unitario", "SubTotal"
            }
        ));
        tablaDetalle.setEnabled(false);
        jScrollPane3.setViewportView(tablaDetalle);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Detalle de la venta seleccionada");

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/volver.png"))); // NOI18N
        btnVolver.addActionListener(this::btnVolverActionPerformed);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/icono_galapa_.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(77, 77, 77)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(btnVolver)))
                .addContainerGap(444, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jLabel1)))
                .addGap(5, 5, 5)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnVolver)
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        PanelAdministrador panel = new PanelAdministrador();
        panel.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void tablaVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaVentasMouseClicked
        // TODO add your handling code here:
        int fila = tablaVentas.getSelectedRow();

        if (fila >= 0) {
            int idVenta = idsVentaEnTabla.get(fila);
            cargarDetalle(idVenta);
        }
    
    }//GEN-LAST:event_tablaVentasMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaDetalleVenta().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tablaDetalle;
    private javax.swing.JTable tablaVentas;
    // End of variables declaration//GEN-END:variables
}
