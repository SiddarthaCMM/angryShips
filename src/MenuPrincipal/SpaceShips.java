package MenuPrincipal;


import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author aaa
 */
public class SpaceShips extends javax.swing.JFrame {

    /**
     * Creates new form Options
     */
    private Clip clip;
    public int fileNumberSpaceships;
    public String playerNameSpaceships;

    private void reproducirSonidos(String rutaArchivo) {
        try {
            // Abrir el archivo de audio como un stream de entrada
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(rutaArchivo));

            // Obtener un clip de sonido
            clip = AudioSystem.getClip();

            // Abrir el clip con el stream de audio
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public SpaceShips(int fileNumberRegister, String playerNameRegister) {
        fileNumberSpaceships = fileNumberRegister;
        playerNameSpaceships = playerNameRegister;
        initComponents();
        reproducirSonidos("src/Musica/MenuPrincipal.wav");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        Ship2 = new javax.swing.JButton();
        Ship1 = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/shipsSelectionImages/Pilot Select.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 610, 80));

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/shipsSelectionImages/ShipPurple.png"))); // NOI18N
        jButton3.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(153, 153, 255)));
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 280, 100, 90));

        Ship2.setBackground(new java.awt.Color(0, 0, 0));
        Ship2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/shipsSelectionImages/ShipWhite.png"))); // NOI18N
        Ship2.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(255, 255, 255)));
        Ship2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Ship2MouseClicked(evt);
            }
        });
        jPanel1.add(Ship2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 250, 110, 120));

        Ship1.setBackground(new java.awt.Color(0, 0, 0));
        Ship1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/shipsSelectionImages/ShipRed.png"))); // NOI18N
        Ship1.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(255, 0, 0)));
        Ship1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Ship1MouseClicked(evt);
            }
        });
        Ship1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ship1ActionPerformed(evt);
            }
        });
        jPanel1.add(Ship1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 120, 100));

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MenuImages/ChBackUni.jpg"))); // NOI18N
        jPanel1.add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 560));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Ship1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ship1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Ship1ActionPerformed

    private void Ship1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Ship1MouseClicked
        // TODO add your handling code here:
        new Dificultad("src/shipsSelectionImages/ShipRed.png", fileNumberSpaceships, playerNameSpaceships).setVisible(true);
        dispose();
        clip.stop();
    }//GEN-LAST:event_Ship1MouseClicked

    private void Ship2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Ship2MouseClicked
        // TODO add your handling code here:
        new Dificultad("src/shipsSelectionImages/ShipWhite.png", fileNumberSpaceships, playerNameSpaceships).setVisible(true);
        dispose();
        clip.stop();
    }//GEN-LAST:event_Ship2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        new Dificultad("src/shipsSelectionImages/ShipPurple.png", fileNumberSpaceships, playerNameSpaceships).setVisible(true);
        dispose();
        clip.stop();
    }//GEN-LAST:event_jButton3MouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JButton Ship1;
    private javax.swing.JButton Ship2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
