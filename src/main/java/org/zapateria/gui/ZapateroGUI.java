package org.zapateria.gui;

import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.zapateria.logica.Reparacion;
import org.zapateria.mapper.ReparacionMapper;
import org.zapateria.utilidades.Constantes;

/**
 *
 * @author geiner
 */
public class ZapateroGUI extends javax.swing.JFrame {

    private Reparacion reparacion;
    private EntityManagerFactory emf;
    private static final int BOOLEAN_COLUMN = 5;
    private List<Reparacion> listaReparacion;
    
    /**
     * Creates new form clienteGUI
     */
    public ZapateroGUI() {
        this.emf = Persistence.createEntityManagerFactory(Constantes.CONTEXTO);
        initComponents();
    }
    
     /**
     *
     * @param persona
     */
    public ZapateroGUI(Reparacion reparacion) {
        this.emf = Persistence.createEntityManagerFactory(Constantes.CONTEXTO);
        this.reparacion = reparacion;
        initComponents();
        // establecerValores();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cambiarRol = new javax.swing.JButton();
        cerrarSession = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Zapateria-Zapatero");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            getListReparacion(),
            new String [] {
                "Fecha Reparación", "Cliente", "Calzado", "Estado", "Gestionar Reparación"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.getModel().addTableModelListener(new CheckBoxModelListener( this ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("Bienvenido a Zapateria Zapatero");

        cambiarRol.setText("Cambiar de rol");
        cambiarRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarRolActionPerformed(evt);
            }
        });

        cerrarSession.setText("Cerrar sesión");
        cerrarSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSessionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(cambiarRol, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(224, 224, 224)
                .addComponent(cerrarSession, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cerrarSession, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cambiarRol, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cerrarSessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarSessionActionPerformed
        Constantes.session.clear();
        LoginGUI loginGui = new LoginGUI();
        loginGui.setVisible(true);

        this.dispose();
    }//GEN-LAST:event_cerrarSessionActionPerformed

    private void cambiarRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarRolActionPerformed
        RolGUI rol = new RolGUI();
        rol.setVisible(Boolean.TRUE);
        
        this.dispose();
    }//GEN-LAST:event_cambiarRolActionPerformed

    /**
     * obtiene lista de reparacion
     * @return 
     */
    private Object[][] getListReparacion() {
        ReparacionMapper reparacionMapper = new ReparacionMapper(Persistence.createEntityManagerFactory(Constantes.CONTEXTO));
        String id_zapatero = "";
        listaReparacion = reparacionMapper.consultarReparacionesZapatero(id_zapatero);
        
        if ( Objects.isNull(listaReparacion))
            return null;
        
        int lenght = listaReparacion.size();
        Object[][] objectPersona = new Object[lenght][5];
        
        for ( int fila = 0; fila < lenght; fila++ ) {
             objectPersona[fila][0] = listaReparacion.get(fila).getFechaSolicitud().toString();
             objectPersona[fila][1] = listaReparacion.get(fila).getCliente().toString();
             objectPersona[fila][2] = listaReparacion.get(fila).getCalzado().toString();
             objectPersona[fila][3] = listaReparacion.get(fila).getEstadoReparacion().toString();
             objectPersona[fila][5] = new Boolean(false);
        }
        return objectPersona;
    }
    
     public class CheckBoxModelListener implements TableModelListener {

        private ZapateroGUI clienteGUI;
        
        public CheckBoxModelListener(ZapateroGUI clienteGUI){
            this.clienteGUI = clienteGUI;
        }
        
        @Override
        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            
            System.out.println(" row " + row);
            System.out.println(" column " + column);
            
            if (column == BOOLEAN_COLUMN) {
 
                TableModel model = (TableModel) e.getSource();
                String columnName = model.getColumnName(column);
                Boolean checked = (Boolean) model.getValueAt(row, column);
                if (checked) {
                    
                    Reparacion reparacion = listaReparacion.get(row);
                    DetalleClienteGUI personaGui = new DetalleClienteGUI(reparacion);
                    personaGui.setVisible(Boolean.TRUE);
                    
                    this.clienteGUI.dispose();
                } 
            }
        }
    }
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ZapateroGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ZapateroGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ZapateroGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ZapateroGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ZapateroGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cambiarRol;
    private javax.swing.JButton cerrarSession;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}