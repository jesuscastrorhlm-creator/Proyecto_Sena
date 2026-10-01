/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tienda.Interfaces;

import Tienda.Conexion;
import Tienda.Interfaces.PanelAdministrador;
import Tienda.PanelFondo;

/**
 *
 * @author CRISTIAN PC
 */
public class VentanaCliente extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaCliente.class.getName());

    /**
     * Creates new form VentanaCliente
     */
    private int idClienteSeleccionado = -1; // -1 si no tiene ficha en Cliente
    private int idUsuarioSeleccionado = -1; // -1 si no tiene cuenta de usuario

    public VentanaCliente() {
        setContentPane(new PanelFondo("/Tienda/Imagenes/fondo_abarrotes.png"));
    initComponents();
    cargarClientes();
    setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/fondo_abarrotes.png")).getImage());
    
    jTable1.getColumnModel().removeColumn(jTable1.getColumnModel().getColumn(7)); // id_usuario
    jTable1.getColumnModel().removeColumn(jTable1.getColumnModel().getColumn(6)); // id_cliente

    Tienda.EstiloUI.botonPrimario(btnAgregar);
    Tienda.EstiloUI.botonPrimario(btnActualizar);
    Tienda.EstiloUI.botonPeligro(btnEliminar);
    Tienda.EstiloUI.botonAcento(btnHacerAdmin);
    Tienda.EstiloUI.botonAcento(btnHacerCliente);
    Tienda.EstiloUI.botonNeutro(btnLimpiar);
    Tienda.EstiloUI.botonSecundario(btnVolver);

    }
    
        private void cargarClientes() {
    try {
        java.sql.Connection con = Conexion.conectar();
        java.sql.Statement st = con.createStatement();
        java.sql.ResultSet rs = st.executeQuery(
            "SELECT c.id_cliente AS id_cliente, u.id_usuario AS id_usuario, c.nombre AS nombre, " +
            "u.usuario AS correo, COALESCE(c.telefono, u.telefono) AS telefono, u.tipo AS rol, " +
            "u.contrasena AS contrasena " +
            "FROM Cliente c LEFT JOIN Usuario u ON c.id_usuario = u.id_usuario " +
            "UNION ALL " +
            "SELECT NULL AS id_cliente, u.id_usuario AS id_usuario, u.nombre AS nombre, " +
            "u.usuario AS correo, u.telefono AS telefono, u.tipo AS rol, " +
            "u.contrasena AS contrasena " +
            "FROM Usuario u " +
            "WHERE u.id_usuario NOT IN (SELECT id_usuario FROM Cliente WHERE id_usuario IS NOT NULL) " +
            "ORDER BY nombre"
        );

        javax.swing.table.DefaultTableModel modelo =
            (javax.swing.table.DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        while (rs.next()) {
            Object idCliente = rs.getObject("id_cliente");
            Object idUsuario = rs.getObject("id_usuario");
            String contrasena = rs.getString("contrasena");
            String contrasenaOculta = (contrasena != null && !contrasena.isEmpty()) ? "••••••" : "";

            modelo.addRow(new Object[]{
                idCliente != null ? idCliente : idUsuario,
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("telefono"),
                contrasenaOculta,
                rs.getString("rol"),
                idCliente,
                idUsuario
            });
        }
        con.close();
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
    }
}

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        idClienteSeleccionado = -1;
        idUsuarioSeleccionado = -1;
}
    
    private void cambiarRol(String nuevoRol) {
    if (idUsuarioSeleccionado == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Este registro no tiene una cuenta de usuario asociada");
        return;
    }
    if (txtCorreo.getText().trim().equalsIgnoreCase("admin@gmail.com")) {
        javax.swing.JOptionPane.showMessageDialog(this, "Este usuario es el administrador principal y su rol no se puede cambiar");
        return;
    }
    int confirmar = javax.swing.JOptionPane.showConfirmDialog(
        this, "¿Cambiar el rol de " + txtNombre.getText() + " a " + nuevoRol + "?",
        "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
    if (confirmar != javax.swing.JOptionPane.YES_OPTION) return;
    try {
        java.sql.Connection con = Conexion.conectar();
        java.sql.PreparedStatement st = con.prepareStatement("UPDATE Usuario SET tipo = ? WHERE id_usuario = ?");
        st.setString(1, nuevoRol);
        st.setInt(2, idUsuarioSeleccionado);
        st.executeUpdate();
        con.close();
        javax.swing.JOptionPane.showMessageDialog(this, "Rol actualizado correctamente");
        limpiarCampos();
        cargarClientes();
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        btnHacerAdmin = new javax.swing.JButton();
        btnHacerCliente = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Ubuntu Mono", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GESTION DE CLIENTES");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Nombre : ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Telefono : ");

        txtTelefono.addActionListener(this::txtTelefonoActionPerformed);

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/agregar.png"))); // NOI18N
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/actualizar.png"))); // NOI18N
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/_eliminar.png"))); // NOI18N
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/limpiar.png"))); // NOI18N
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/volver.png"))); // NOI18N
        btnVolver.addActionListener(this::btnVolverActionPerformed);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Correo", "Telefono", "Contraseña", "Rol", "id_Cliente", "id_Usuario"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/icono_galapa_.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Correo : ");

        btnHacerAdmin.setText("Admin");
        btnHacerAdmin.addActionListener(this::btnHacerAdminActionPerformed);

        btnHacerCliente.setText("Cliente");
        btnHacerCliente.addActionListener(this::btnHacerClienteActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addGap(104, 104, 104)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(44, 44, 44)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                                    .addComponent(txtCorreo))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(btnAgregar)
                .addContainerGap(71, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(51, 51, 51))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnVolver)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnActualizar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar)
                        .addGap(89, 89, 89)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnHacerCliente, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnHacerAdmin, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(91, 91, 91))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jLabel1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(btnAgregar)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLimpiar)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnHacerAdmin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnHacerCliente))
                            .addComponent(btnEliminar)
                            .addComponent(btnActualizar)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVolver)))
                .addGap(0, 24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
            return;
        }

        try {
            java.sql.Connection con = Conexion.conectar();
            String sql = "INSERT INTO Cliente (nombre, telefono) VALUES (?, ?)";
            java.sql.PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, nombre);
            st.setString(2, telefono);
            st.executeUpdate();
            con.close();

            javax.swing.JOptionPane.showMessageDialog(this, "Cliente agregado correctamente");
            limpiarCampos();
            cargarClientes();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
       if (idClienteSeleccionado == -1 && idUsuarioSeleccionado == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un registro de la tabla primero");
        return;
    }
    String nombre = txtNombre.getText().trim();
    String telefono = txtTelefono.getText().trim();
    if (nombre.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
        return;
    }
    try {
        java.sql.Connection con = Conexion.conectar();
        if (idClienteSeleccionado != -1) {
            java.sql.PreparedStatement st = con.prepareStatement(
                "UPDATE Cliente SET nombre = ?, telefono = ? WHERE id_cliente = ?");
            st.setString(1, nombre);
            st.setString(2, telefono);
            st.setInt(3, idClienteSeleccionado);
            st.executeUpdate();
        }
        if (idUsuarioSeleccionado != -1) {
            java.sql.PreparedStatement stU = con.prepareStatement(
                "UPDATE Usuario SET nombre = ?, telefono = ? WHERE id_usuario = ?");
            stU.setString(1, nombre);
            stU.setString(2, telefono);
            stU.setInt(3, idUsuarioSeleccionado);
            stU.executeUpdate();
        }
        con.close();
        javax.swing.JOptionPane.showMessageDialog(this, "Registro actualizado correctamente");
        limpiarCampos();
        cargarClientes();
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        if (idClienteSeleccionado == -1 && idUsuarioSeleccionado == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un registro de la tabla primero");
        return;
    }
    if (txtCorreo.getText().trim().equalsIgnoreCase("admin@gmail.com")) {
        javax.swing.JOptionPane.showMessageDialog(this, "No puedes eliminar al administrador principal");
        return;
    }
    int confirmar = javax.swing.JOptionPane.showConfirmDialog(
        this, "¿Seguro que quieres eliminar este registro? Esto borra también su cuenta de acceso si tiene una.",
        "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
    if (confirmar != javax.swing.JOptionPane.YES_OPTION) return;
    try {
        java.sql.Connection con = Conexion.conectar();
        if (idClienteSeleccionado != -1) {
            java.sql.PreparedStatement st = con.prepareStatement("DELETE FROM Cliente WHERE id_cliente = ?");
            st.setInt(1, idClienteSeleccionado);
            st.executeUpdate();
        }
        if (idUsuarioSeleccionado != -1) {
            java.sql.PreparedStatement stU = con.prepareStatement("DELETE FROM Usuario WHERE id_usuario = ?");
            stU.setInt(1, idUsuarioSeleccionado);
            stU.executeUpdate();
        }
        con.close();
        javax.swing.JOptionPane.showMessageDialog(this, "Registro eliminado");
        limpiarCampos();
        cargarClientes();
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        txtNombre.setText("");
    txtCorreo.setText("");
    txtTelefono.setText("");
    idClienteSeleccionado = -1;
    idUsuarioSeleccionado = -1;
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
         PanelAdministrador panel = new PanelAdministrador();
        panel.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked

    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int fila = jTable1.getSelectedRow();
    if (fila >= 0) {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        Object idCliente = modelo.getValueAt(fila, 6);
        Object idUsuario = modelo.getValueAt(fila, 7);
        idClienteSeleccionado = idCliente != null ? (int) idCliente : -1;
        idUsuarioSeleccionado = idUsuario != null ? (int) idUsuario : -1;

        txtNombre.setText(modelo.getValueAt(fila, 1).toString());
        Object correo = modelo.getValueAt(fila, 2);
        txtCorreo.setText(correo != null ? correo.toString() : "");
        Object telefono = modelo.getValueAt(fila, 3);
        txtTelefono.setText(telefono != null ? telefono.toString() : "");
    }
    }//GEN-LAST:event_jTable1MouseClicked

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void btnHacerAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHacerAdminActionPerformed
        // TODO add your handling code here:
        cambiarRol("ADMIN");
    }//GEN-LAST:event_btnHacerAdminActionPerformed

    private void btnHacerClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHacerClienteActionPerformed
        // TODO add your handling code here:
        cambiarRol("CLIENTE");
    }//GEN-LAST:event_btnHacerClienteActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VentanaCliente().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnHacerAdmin;
    private javax.swing.JButton btnHacerCliente;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
