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
 * @author Aprendiz
 */
public class VentanaProducto extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaProducto.class.getName());

    /**
     * Creates new form VentanaProducto
     */
        private int idSeleccionado = -1;

        public VentanaProducto() {
        setContentPane(new PanelFondo("/Tienda/Imagenes/fondo_abarrotes.png"));
        initComponents();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/fondo_abarrotes.png")).getImage());

        Tienda.EstiloUI.botonPrimario(btnAgregar);
        Tienda.EstiloUI.botonPrimario(btnActualizar);
        Tienda.EstiloUI.botonPeligro(btnEliminar);
        Tienda.EstiloUI.botonNeutro(btnLimpiar);
        Tienda.EstiloUI.botonSecundario(btnVolver);

        configurarTabla();
        cargarProductos();
        cargarCategorias();

                comboCategoria.addActionListener(e -> {
            if ("+ Agregar nueva categoria...".equals(comboCategoria.getSelectedItem())) {
                agregarNuevaCategoria();
            } else if ("- Eliminar categoria...".equals(comboCategoria.getSelectedItem())) {
                eliminarCategoria();
            }
        });
    }

        private void cargarCategorias() {
        try {
            java.sql.Connection con = Conexion.conectar();
            java.sql.Statement st = con.createStatement();
            java.sql.ResultSet rs = st.executeQuery("SELECT nombre FROM Categoria ORDER BY nombre");

            javax.swing.DefaultComboBoxModel<String> modelo = new javax.swing.DefaultComboBoxModel<>();
            while (rs.next()) {
                modelo.addElement(rs.getString("nombre"));
            }
            modelo.addElement("+ Agregar nueva categoria...");
            modelo.addElement("- Eliminar categoria...");

            comboCategoria.setModel(modelo);
            con.close();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar categorias: " + e.getMessage());
        }
    }

    private void agregarNuevaCategoria() {
        String nueva = javax.swing.JOptionPane.showInputDialog(this, "Nombre de la nueva categoria:");

        if (nueva == null || nueva.trim().isEmpty()) {
            comboCategoria.setSelectedIndex(0);
            return;
        }

        nueva = nueva.trim();

        try {
            java.sql.Connection con = Conexion.conectar();
            java.sql.PreparedStatement st = con.prepareStatement("INSERT INTO Categoria (nombre) VALUES (?)");
            st.setString(1, nueva);
            st.executeUpdate();
            con.close();

            cargarCategorias();
            comboCategoria.setSelectedItem(nueva);

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Esa categoria ya existe o hubo un error: " + e.getMessage());
            comboCategoria.setSelectedIndex(0);
        }
    }
    
            private void eliminarCategoria() {
        try {
            java.sql.Connection con = Conexion.conectar();
            java.sql.Statement st = con.createStatement();
            java.sql.ResultSet rs = st.executeQuery("SELECT nombre FROM Categoria ORDER BY nombre");

            java.util.List<String> lista = new java.util.ArrayList<>();
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
            con.close();

            if (lista.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "No hay categorias para eliminar");
                comboCategoria.setSelectedIndex(0);
                return;
            }

            String elegida = (String) javax.swing.JOptionPane.showInputDialog(
                this, "Selecciona la categoria a eliminar:", "Eliminar categoria",
                javax.swing.JOptionPane.PLAIN_MESSAGE, null,
                lista.toArray(), lista.get(0)
            );

            if (elegida == null) {
                comboCategoria.setSelectedIndex(0);
                return;
            }

            int confirmar = javax.swing.JOptionPane.showConfirmDialog(
                this, "¿Seguro que quieres eliminar la categoria \"" + elegida + "\"?",
                "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirmar != javax.swing.JOptionPane.YES_OPTION) {
                comboCategoria.setSelectedIndex(0);
                return;
            }

            java.sql.Connection con2 = Conexion.conectar();
            java.sql.PreparedStatement st2 = con2.prepareStatement("DELETE FROM Categoria WHERE nombre = ?");
            st2.setString(1, elegida);
            st2.executeUpdate();
            con2.close();

            javax.swing.JOptionPane.showMessageDialog(this, "Categoria eliminada");
            cargarCategorias();
            comboCategoria.setSelectedIndex(0);

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            comboCategoria.setSelectedIndex(0);
        }
    }
    
        private void configurarTabla() {
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
            new Object[]{"ID", "Nombre", "Precio", "Stock", "Categoria"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProductosagg.setModel(modelo);
    }

    private void cargarProductos() {
        try {
            java.sql.Connection con = Conexion.conectar();
            java.sql.Statement st = con.createStatement();
            java.sql.ResultSet rs = st.executeQuery("SELECT * FROM Producto");

            javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) tblProductosagg.getModel();
            modelo.setRowCount(0);

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getString("categoria")
                });
            }

            con.close();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
    txtNombre.setText("");
    txtPrecio.setText("");
    txtStock.setText("");
    comboCategoria.setSelectedIndex(0);
    idSeleccionado = -1;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        lblCategorias = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProductosagg = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        comboCategoria = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitulo.setFont(new java.awt.Font("Ubuntu Mono", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("GESTION DE PRODUCTOS");

        lblNombre.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNombre.setText("Nombre :");

        lblPrecio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPrecio.setText("Precio :");

        lblStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStock.setText("Stock :");

        lblCategorias.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCategorias.setText("Categorias :");

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/agregar.png"))); // NOI18N
        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarMouseClicked(evt);
            }
        });
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/actualizar.png"))); // NOI18N
        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarMouseClicked(evt);
            }
        });
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/_eliminar.png"))); // NOI18N
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/limpiar.png"))); // NOI18N
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseClicked(evt);
            }
        });
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/volver.png"))); // NOI18N
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVolverMouseClicked(evt);
            }
        });
        btnVolver.addActionListener(this::btnVolverActionPerformed);

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        tblProductosagg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Precio", "Stock", "Categoria"
            }
        ));
        tblProductosagg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosaggMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblProductosagg);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tienda/Imagenes/icono_galapa_.png"))); // NOI18N

        comboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lacteos o huevo", "Carnes y Embutidos", "Bebidas", "Frutas", "Verduras", "Snacks y Dulces", "Aseo del Hogar", "Aceites y Condimentos" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVolver)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAgregar)
                                .addGap(36, 36, 36)
                                .addComponent(btnActualizar)
                                .addGap(31, 31, 31)
                                .addComponent(btnLimpiar)
                                .addGap(28, 28, 28)
                                .addComponent(btnEliminar))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 952, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblStock)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNombre))
                                .addGap(133, 133, 133)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblPrecio)
                                    .addComponent(txtPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                    .addComponent(lblCategorias)
                                    .addComponent(comboCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addComponent(lblTitulo)))))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(lblTitulo)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLimpiar)
                            .addComponent(btnEliminar)
                            .addComponent(btnActualizar)
                            .addComponent(btnAgregar)
                            .addComponent(btnVolver))
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPrecio)
                            .addComponent(lblNombre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCategorias)
                            .addComponent(lblStock))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:                                         
        PanelAdministrador panel = new PanelAdministrador();
        panel.setVisible(true);
        this.dispose();
  
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        String nombre = txtNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto = txtStock.getText().trim();
        String categoria = (String) comboCategoria.getSelectedItem();

        if (nombre.isEmpty() || precioTexto.isEmpty() || stockTexto.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Completa nombre, precio y stock");
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);

            java.sql.Connection con = Conexion.conectar();
            String sql = "INSERT INTO Producto (nombre, precio, stock, categoria) VALUES (?, ?, ?, ?)";
            java.sql.PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, nombre);
            st.setDouble(2, precio);
            st.setInt(3, stock);
            st.setString(4, categoria);
            st.executeUpdate();
            con.close();

            javax.swing.JOptionPane.showMessageDialog(this, "Producto agregado correctamente");
            limpiarCampos();
            cargarProductos();

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Precio y Stock deben ser numeros");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        if (idSeleccionado == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla primero");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto = txtStock.getText().trim();
        String categoria = (String) comboCategoria.getSelectedItem();

        if (nombre.isEmpty() || precioTexto.isEmpty() || stockTexto.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Completa nombre, precio y stock");
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);

            java.sql.Connection con = Conexion.conectar();
            String sql = "UPDATE Producto SET nombre = ?, precio = ?, stock = ?, categoria = ? WHERE id_producto = ?";
            java.sql.PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, nombre);
            st.setDouble(2, precio);
            st.setInt(3, stock);
            st.setString(4, categoria);
            st.setInt(5, idSeleccionado);
            st.executeUpdate();
            con.close();

            javax.swing.JOptionPane.showMessageDialog(this, "Producto actualizado correctamente");
            limpiarCampos();
            cargarProductos();

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Precio y Stock deben ser numeros");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        if (idSeleccionado == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla primero");
            return;
        }

        int confirmar = javax.swing.JOptionPane.showConfirmDialog(
            this, "¿Seguro que quieres eliminar este producto?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmar != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        try {
            java.sql.Connection con = Conexion.conectar();
            String sql = "DELETE FROM Producto WHERE id_producto = ?";
            java.sql.PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, idSeleccionado);
            st.executeUpdate();
            con.close();

            javax.swing.JOptionPane.showMessageDialog(this, "Producto eliminado");
            limpiarCampos();
            cargarProductos();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarMouseClicked

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVolverMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVolverMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void tblProductosaggMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosaggMouseClicked
        // TODO add your handling code here:
         int fila = tblProductosagg.getSelectedRow();

        if (fila >= 0) {
            idSeleccionado = (int) tblProductosagg.getValueAt(fila, 0);
            txtNombre.setText(tblProductosagg.getValueAt(fila, 1).toString());
            txtPrecio.setText(tblProductosagg.getValueAt(fila, 2).toString());
            txtStock.setText(tblProductosagg.getValueAt(fila, 3).toString());
            comboCategoria.setSelectedItem(tblProductosagg.getValueAt(fila, 4).toString());
        }
    }//GEN-LAST:event_tblProductosaggMouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane2MouseClicked

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
        java.awt.EventQueue.invokeLater(() -> new VentanaProducto().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCategorias;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblProductosagg;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
