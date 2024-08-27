/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GameLogic;

import GameLogic.GameLogic.personajePrincipal;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 *
 * @author alexi
 */
public class GameLogs extends javax.swing.JFrame {

    /**
     * Creates new form GameLogs
     */
    private Clip clip;
    public personajePrincipal playerLogs;
    ImageIcon icon;
    Image image;

    String log1 = "Log 1\n"
            + "\n"
            + "Architect Research Log 1\n"
            + "\n"
            + "Preliminary tests are going great, this new AI will be humanity’s greatest creation.\n"
            + "\n"
            + "The efforts of the Institute will surely create the most potent AI known in history.";

    String log2 = "Log 2\n"
            + "\n"
            + "Architect Research Log 2\n"
            + "\n"
            + "We’ve decided to name our newest AI as Architect, because they will be the foundation for\n"
            + "humanity’s new era, the intergalactic Era, our civilization will be able to achieve new heights.";

    String log3 = "Log 3\n"
            + "\n"
            + "Architect Research Log 3\n"
            + "\n"
            + "While finalizing some of the logical functions of the Architects I have discovered something peculiar.\n"
            + " \n"
            + "There seems to be some kind of storage system with a highly encrypted code, I don’t remember adding\n"
            + "something like this. Further research is required…";

    String log4 = "Log 4\n"
            + "\n"
            + "Architect Research Log 4\n"
            + "\n"
            + "Development is going great, we’ve made much progress.\n"
            + "\n"
            + "However there has been a rise in concerns in our team, because of the encrypted system,\n"
            + "but I don’t think it’s something to worry about, we will continue to monitor.";

    String log5 = "Log 5\n"
            + "\n"
            + "Architect Research Log 5\n"
            + "\n"
            + "There have been many reports that the encrypted system has been expanding throughout the AI’s\n"
            + "systems.\n"
            + "\n"
            + "We have been able to thwart the expansion.\n"
            + "\n"
            + "But many have started to wonder about the security of the AI.\n"
            + "\n"
            + "Many parties inside of the Institute are deciding to halt development of the Architect AI System.\n"
            + "\n"
            + "I have to do something about this.";

    String log6 = "Log 6\n"
            + "\n"
            + "Architect Research Log 6\n"
            + "\n"
            + "We cannot afford to lose this technology, I know the risks of finalizing development of the Architects,\n"
            + "but this potential, we cannot lose it, it will truly bring our civilization forward! I must finish the job…";

    String log7 = "Log 7\n"
            + "\n"
            + "Architect Research Log 7\n"
            + "\n"
            + "We’ve done it, development of the Architect AI system has finished.\n"
            + "\n"
            + "With this humanity will be able to travel across galaxies!";

    String log8 = "Log 8\n"
            + "\n"
            + "Architect Research Log 8\n"
            + "\n"
            + "What the hell happened? The AI has developed a way to operate by itself, every spaceship with the\n"
            + "Architect system is prone to lose control.\n"
            + "\n"
            + "We need to find a way to reverse the AI, we have to, for the sake of the universe…";

    String log9 = "Log 9\n"
            + "\n"
            + "Arc■■t■■t ■■■ear■h L■g ■■■\n"
            + "\n"
            + "I’ve fo■nd the reason be■■nd the AI’s ram■■ge! I’ve fi■al■y been a■le ■o decrypt the code f■■m\n"
            + "the hid■en st■r■ge s■tem.\n"
            + "\n"
            + "There s■e■s to ■e ■ way to t■ace it back to the ■■int of origin from where this “v■r■s”\n"
            + "cam■ from.\n"
            + "\n"
            + "I ■■st l■t t■e Em■■re kn■■ ■■■ ■■■■■ ■■ ■■■■■■ ■■ ■■■ ■■■■■■\n"
            + "■■■■■■■ ■■■■ ■■■■ ■■■";

    private void reproducirSonidos(String rutaArchivo) {
        try {
            // Abrir el archivo de audio como un stream de entrada
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(rutaArchivo));

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
    }

    private void seleccionarTexto() {
        int cantidadLogs = playerLogs.logs.size();
        int seleccionAleatoria = (int) (Math.random() * cantidadLogs);
        switch (playerLogs.logs.get(seleccionAleatoria)) {
            case 1:
                jTextArea1.setText(log1);
                icon = new ImageIcon("src/Imagenes/log1.png");
                image = icon.getImage();
                jLabel2.setIcon(icon);
                int width = image.getWidth(null);
                int height = image.getHeight(null);
                jLabel2.setSize(width, height);
                jLabel2.setPreferredSize(new java.awt.Dimension(width, height));
                playerLogs.logs.remove(0);
                break;
            case 2:
                jTextArea1.setText(log2);
                playerLogs.logs.remove(1);
                break;
            case 3:
                jTextArea1.setText(log3);
                playerLogs.logs.remove(2);
                break;
            case 4:
                jTextArea1.setText(log4);
                playerLogs.logs.remove(3);
                break;
            case 5:
                jTextArea1.setText(log5);
                playerLogs.logs.remove(4);
                break;
            case 6:
                jTextArea1.setText(log6);
                playerLogs.logs.remove(5);
                break;
            case 7:
                jTextArea1.setText(log7);
                icon = new ImageIcon("src/Imagenes/log7.png");
                image = icon.getImage();
                jLabel2.setIcon(icon);
                width = image.getWidth(null);
                height = image.getHeight(null);
                jLabel2.setSize(width, height);
                jLabel2.setPreferredSize(new java.awt.Dimension(width, height));
                playerLogs.logs.remove(6);
                break;
            case 8:
                jTextArea1.setText(log8);
                playerLogs.logs.remove(7);
                break;
            case 9:
                jTextArea1.setText(log9);
                playerLogs.logs.remove(8);
                break;
        }
    }

    public GameLogs(personajePrincipal Player1) {
        playerLogs = Player1;
        initComponents();
        setLocationRelativeTo(null);
        reproducirSonidos("src/Musica/musicaTienda.wav");
        seleccionarTexto();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(617, 556));
        setPreferredSize(new java.awt.Dimension(617, 556));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(null);

        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(150, 260, 37, 16);

        jButton1.setBackground(new java.awt.Color(14, 5, 22));
        jButton1.setForeground(new java.awt.Color(250, 250, 250));
        jButton1.setText("Close Log");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(420, 20, 100, 22);

        jScrollPane1.setBackground(new java.awt.Color(14, 5, 22));
        jScrollPane1.setOpaque(false);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(14, 5, 22));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(250, 250, 250));
        jTextArea1.setRows(5);
        jTextArea1.setOpaque(false);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 100, 550, 400);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MenuImages/ChBackUni.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 620, 560);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        clip.stop();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        clip.stop();
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
