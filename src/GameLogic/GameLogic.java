package GameLogic;

import MenuPrincipal.MenuPrincipal;
import Tienda.Tienda;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author alexi
 */
public class GameLogic extends javax.swing.JFrame {

    //Creación del personaje principal
    public personajePrincipal Player1;

    //Creacion de frame de mejoras de personaje
    public mejorasPlayer frameMejoras;

    //Rectangulo de baseespacial
    public Rectangle baseEspacialBounds;

    //Condicional de la tienda
    private boolean tiendaDisponible;

    //Movimiento de nave en X y Y
    private int xPos;
    private int yPos;

    //Velocidad de movimiento en X y Y y velocidad minima
    private double dxPlayer; // Velocidad en x
    private double dyPlayer; // Velocidad en y
    private final float minVelocity = 0.15f;

    //Iniciacion de momento de disparo
    private long lastShotTime = 0; // Tiempo del último disparo

    //Booleanos de movimiento y rotacion
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    //Velocidad y friccion
    private final float friction = 0.95f; // Factor de fricción

    //Timers de actualización
    private final Timer timer;
    private final Timer enemyUpdateTimer;
    private final Timer statsUpdate;
    private Timer explosionEnemigo;

    //Rotacion de la imagen
    private BufferedImage originalImage;
    private BufferedImage rotatedImage;
    private double rotationAngleShip = 0; // Ángulo de rotación actual

    //jLabel de disparos
    private final String laserImagePath = "src/Imagenes/Laser.png";
    private final ImageIcon bulletIcon;
    ArrayList<Disparo> disparos = new ArrayList<>();
    ArrayList<DisparoEnemigo> disparosEnemigos = new ArrayList<>();

    //Variables de generacion de enemigos
    private int enemyGenerationInterval = 5000; // Intervalo de 5 segundos
    private long lastEnemyGenerationTime = 0; // Tiempo del último enemigo generado
    private int levelDiff;
    public String pathSeleccionEnemigo;
    public String pathSeleccionBoss;
    private final ArrayList<Enemigo> enemigos = new ArrayList<>();
    private final ArrayList<Boss> bosses = new ArrayList<>();
    private final ArrayList<Enemigo> enemigosExplosion = new ArrayList<>();

    //Base espacial
    private final javax.swing.JLabel baseEspacial;

    //Puntuación temporal
    private int puntuacion = 0;

    //Sonidos
    private Clip musicaFondo;
    private Clip clip;

    //Cambio de ronda
    public int ronda = 1;
    public int enemiesKilled = 0;
    public int enemiesNecessary = ronda * 5;
    public int maxEnemies = 10;
    public boolean actualizacionBackground;
    public boolean generarBoss;

    //savefile
    public int saveFile;
    public String tipoNave;

    //Records
    public ArrayList<String> records;

    //Dificultad
    public int selectorDificultad;

    /**
     * Creates new form GameLogic
     */
    //Clases del escenario
    //Métodos del gameplay
    private void generarEnemigos(double nivelPlayer) {

        if (enemigos.size() < maxEnemies) {
            long currentTime = System.currentTimeMillis();
            double nivelEnemy = Math.random() * levelDiff + 1;
            double positivo_negativo = Math.random() * 2;
            double nivelLocal;

            if (positivo_negativo == 0) {
                nivelLocal = nivelPlayer - nivelEnemy;
                if (nivelLocal < 0) {
                    nivelLocal = 0;
                }
            } else {
                nivelLocal = nivelEnemy + nivelPlayer;
            }

            if (currentTime - lastEnemyGenerationTime >= enemyGenerationInterval) {
                Random rand = new Random();
                int xPosEnemigo, yPosEnemigo;

                // Obtener la posición de la vista actual
                Point viewPosition = jScrollPane1.getViewport().getViewPosition();
                int viewportWidth = jScrollPane1.getViewport().getWidth();
                int viewportHeight = jScrollPane1.getViewport().getHeight();

                // Generar una posición aleatoria en las orillas de la vista actual
                int edge = 10; // Margen desde el borde de la vista para la aparición
                int side = rand.nextInt(4); // Elegir un lado al azar

                switch (side) {
                    case 0: // Arriba
                        xPosEnemigo = viewPosition.x + rand.nextInt(viewportWidth);
                        yPosEnemigo = viewPosition.y - edge;
                        break;
                    case 1: // Abajo
                        xPosEnemigo = viewPosition.x + rand.nextInt(viewportWidth);
                        yPosEnemigo = viewPosition.y + viewportHeight + edge;
                        break;
                    case 2: // Izquierda
                        xPosEnemigo = viewPosition.x - edge;
                        yPosEnemigo = viewPosition.y + rand.nextInt(viewportHeight);
                        break;
                    case 3: // Derecha
                        xPosEnemigo = viewPosition.x + viewportWidth + edge;
                        yPosEnemigo = viewPosition.y + rand.nextInt(viewportHeight);
                        break;
                    default:
                        xPosEnemigo = viewPosition.x;
                        yPosEnemigo = viewPosition.y;
                        break;
                }

                // Generar dirección aleatoria para el enemigo
                double dxEnemigo = (rand.nextDouble() - 0.5) * 2;
                double dyEnemigo = (rand.nextDouble() - 0.5) * 2;

                int tipoNaveSeleccion = (int) (Math.random() * 4) + 1;

                switch (tipoNaveSeleccion) {
                    case 1:
                        pathSeleccionEnemigo = "src/Imagenes/NaveEnemigaComun.png";
                        break;
                    case 2:
                        pathSeleccionEnemigo = "src/Imagenes/NaveEnemigaComun2.png";
                        break;
                    case 3:
                        pathSeleccionEnemigo = "src/Imagenes/NaveEnemigaComun3.png";
                        break;
                    case 4:
                        pathSeleccionEnemigo = "src/Imagenes/NaveEnemigaComun4.png";
                        break;

                }

                // Crear y agregar el nuevo enemigo
                Enemigo nuevoEnemigo = new Enemigo(xPosEnemigo, yPosEnemigo, dxEnemigo, dyEnemigo, pathSeleccionEnemigo, nivelLocal);
                enemigos.add(nuevoEnemigo);
                nuevoEnemigo.setDaño();
                nuevoEnemigo.setDefensa();
                nuevoEnemigo.setExperiencia();
                nuevoEnemigo.setScrap();
                nuevoEnemigo.setVelocidadAtaque();
                nuevoEnemigo.setVelocidadNave();
                nuevoEnemigo.setVida();
                jLabel1.add(nuevoEnemigo.jlabel);
                lastEnemyGenerationTime = currentTime;
            }
        } else {
            long currentTime = System.currentTimeMillis();
            lastEnemyGenerationTime = currentTime;
        }
    }

    private void generarBoss(double nivelPlayer) {
        double nivelEnemy = Math.random() * levelDiff + 1;
        double positivo_negativo = Math.random() * 2;
        double nivelLocalBoss;

        if (positivo_negativo == 0) {
            nivelLocalBoss = nivelPlayer - nivelEnemy;
            if (nivelLocalBoss < 0) {
                nivelLocalBoss = 0;
            }
        } else {
            nivelLocalBoss = nivelPlayer + nivelEnemy;
        }

        Random rand = new Random();
        int xPosEnemigo, yPosEnemigo;

        // Obtener la posición de la vista actual
        Point viewPosition = jScrollPane1.getViewport().getViewPosition();
        int viewportWidth = jScrollPane1.getViewport().getWidth();
        int viewportHeight = jScrollPane1.getViewport().getHeight();

        // Generar una posición aleatoria en las orillas de la vista actual
        int edge = 10; // Margen desde el borde de la vista para la aparición
        int side = rand.nextInt(4); // Elegir un lado al azar

        switch (side) {
            case 0: // Arriba
                xPosEnemigo = viewPosition.x + rand.nextInt(viewportWidth);
                yPosEnemigo = viewPosition.y - edge;
                break;
            case 1: // Abajo
                xPosEnemigo = viewPosition.x + rand.nextInt(viewportWidth);
                yPosEnemigo = viewPosition.y + viewportHeight + edge;
                break;
            case 2: // Izquierda
                xPosEnemigo = viewPosition.x - edge;
                yPosEnemigo = viewPosition.y + rand.nextInt(viewportHeight);
                break;
            case 3: // Derecha
                xPosEnemigo = viewPosition.x + viewportWidth + edge;
                yPosEnemigo = viewPosition.y + rand.nextInt(viewportHeight);
                break;
            default:
                xPosEnemigo = viewPosition.x;
                yPosEnemigo = viewPosition.y;
                break;
        }

        // Generar dirección aleatoria para el enemigo
        double dxEnemigo = (rand.nextDouble() - 0.5) * 2;
        double dyEnemigo = (rand.nextDouble() - 0.5) * 2;

        int tipoNaveSeleccion = (int) (Math.random() * 2) + 1;

        switch (tipoNaveSeleccion) {
            case 1:
                pathSeleccionBoss = "src/Imagenes/NaveEnemiga.png";
                break;
            case 2:
                pathSeleccionBoss = "src/Imagenes/NaveEnemiga2.png";
                break;

        }

        // Crear y agregar el nuevo enemigo
        Boss nuevoBoss = new Boss(xPosEnemigo, yPosEnemigo, dxEnemigo, dyEnemigo, pathSeleccionBoss, nivelLocalBoss);
        bosses.add(nuevoBoss);
        nuevoBoss.setDaño();
        nuevoBoss.setDefensa();
        nuevoBoss.setExperiencia();
        nuevoBoss.setScrap();
        nuevoBoss.setVelocidadAtaque();
        nuevoBoss.setVelocidadNave();
        nuevoBoss.setVida();
        jLabel1.add(nuevoBoss.jlabel);
    }

    private void calcularEnemigosNecesarios() {
        enemiesNecessary = ronda * 5;
        if (enemiesKilled % 25 == 0) {
            actualizacionBackground = true;
        }
        if (enemiesKilled % 10 == 0) {
            generarBoss = true;
        }
    }

    private boolean debeEliminarEnemigo(Enemigo enemigo) {
        boolean puntuacionCambiada = false;

        for (Disparo disparo : disparos) {
            Rectangle r1 = disparo.getLaserBounds();
            Rectangle r2 = enemigo.getEnemyBounds();

            if (r1.intersects(r2)) {
                jLabel1.remove(disparo.jlabel);
                enemigo.dañoVida(Player1.getDaño() - (enemigo.getDefensa() / 100) * Player1.getDaño());
                if (enemigo.getVida() <= 0) {
                    reproducirSonidos("src/Musica/enemigoMuerto.wav");
                    enemiesKilled += 1;
                    int logFound = (int) (Math.random() * 99) + 1;
                    if (logFound == 1) {
                        GameLogs gameLogs = new GameLogs(Player1);
                        gameLogs.setVisible(true);
                        gameLogs.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowOpened(WindowEvent e) {
                                pausarJuego();
                            }

                            @Override
                            public void windowClosed(WindowEvent e) {
                                reanudarJuego();
                            }
                        });
                    }
                    if (enemiesKilled == enemiesNecessary) {
                        ronda += 1;
                        // jLabel5.setText(String.valueOf(ronda));
                        calcularEnemigosNecesarios();
                    }
                    puntuacion += enemigo.getPuntuacion();
                    Player1.setScrap(enemigo.getScrap());
                    Player1.setExperiencia(enemigo.getExperiencia());
                    if (Player1.getExperiencia() >= Player1.experienciaSiguiente) {
                        reproducirSonidos("src/Musica/subirNivel.wav");
                        Player1.subirNivel();
                    }
                    enemiesKO.setText(String.valueOf("Puntuacion: " + puntuacion));
                    puntuacionCambiada = true;
                }
                disparos.remove(disparo);
                break; // Salir del bucle si se ha eliminado el enemigo
            }
        }
        return puntuacionCambiada;
    }

    private boolean debeEliminarBoss(Boss boss) {
        boolean puntuacionCambiada = false;

        for (Disparo disparo : disparos) {
            Rectangle r1 = disparo.getLaserBounds();
            Rectangle r2 = boss.getEnemyBounds();

            if (r1.intersects(r2)) {
                jLabel1.remove(disparo.jlabel);
                boss.dañoVida(Player1.getDaño() - (boss.getDefensa() / 100) * Player1.getDaño());
                if (boss.getVida() <= 0) {
                    reproducirSonidos("src/Musica/enemigoMuerto.wav");
                    enemiesKilled += 1;
                    if (enemiesKilled == enemiesNecessary) {
                        ronda += 1;
                        // jLabel5.setText(String.valueOf(ronda));
                        calcularEnemigosNecesarios();
                    }
                    puntuacion += boss.getPuntuacion();
                    Player1.setScrap(boss.getScrap());
                    Player1.setExperiencia(boss.getExperiencia());
                    if (Player1.getExperiencia() >= Player1.experienciaSiguiente) {
                        reproducirSonidos("src/Musica/subirNivel.wav");
                        Player1.subirNivel();
                    }
                    enemiesKO.setText(String.valueOf("Puntuacion: " + puntuacion));
                    puntuacionCambiada = true;
                }
                disparos.remove(disparo);
                break; // Salir del bucle si se ha eliminado el enemigo
            }
        }
        return puntuacionCambiada;
    }

    private void amenuPrincipal() {
        new MenuPrincipal().setVisible(true);
        this.dispose();
        pausarJuego();
    }

    private static ArrayList<String> cargarRecords(String path) {
        ArrayList<String> recordsTemporal = new ArrayList<>();

        File archivo = new File(path);
        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + path);
            return recordsTemporal;
        }

        try (Scanner reader = new Scanner(archivo)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim(); // Elimina espacios en blanco al inicio y final
                if (!line.isEmpty()) { // Solo agrega líneas que no estén vacías
                    recordsTemporal.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no se encontró: " + e.getMessage());
        }

        return recordsTemporal;
    }

    private void debeEliminarPersonaje() {

        for (Enemigo enemigo : enemigos) {

            for (DisparoEnemigo disparo : disparosEnemigos) {
                Rectangle r1 = disparo.getLaserBounds();
                Rectangle r2 = Player1.getBounds();

                if (r1.intersects(r2)) {
                    jLabel1.remove(disparo.jlabel);
                    Player1.dañoVida(Player1.getDaño() - (Player1.getDefensa() / 100) * enemigo.getDaño());
                    if (Player1.getVida() <= 0) {

                        File archivo1 = new File("src/Guardados/" + saveFile + ".txt");
                        archivo1.delete();

                        // Cargar registros existentes
                        ArrayList<String> recordsTemporal = cargarRecords("src/Guardados/Records.txt");

                        // Agregar la nueva entrada
                        recordsTemporal.add("Puntuación: " + String.valueOf(puntuacion) + " Jugador " + String.valueOf(Player1.playerName + " dificultad " + selectorDificultad));

                        // Ordenar los registros de mayor a menor por puntuación
                        recordsTemporal.sort(new Comparator<String>() {
                            @Override
                            public int compare(String s1, String s2) {
                                // Extraer la puntuación de cada cadena
                                int puntuacion1 = extraerPuntuacion(s1);
                                int puntuacion2 = extraerPuntuacion(s2);

                                // Comparar las puntuaciones (de mayor a menor)
                                return Integer.compare(puntuacion2, puntuacion1);
                            }

                            private int extraerPuntuacion(String s) {
                                try {
                                    // Extraer el número después de "Puntuación: " y antes de " Jugador"
                                    String parteNumerica = s.substring(s.indexOf("Puntuación: ") + "Puntuación: ".length(), s.indexOf(" Jugador")).trim();
                                    return Integer.parseInt(parteNumerica);
                                } catch (NumberFormatException e) {
                                    System.out.println("Error al extraer puntuación: " + e.getMessage());
                                    return 0; // Valor por defecto en caso de error
                                }
                            }
                        });

                        // Guardar la lista ordenada en el archivo
                        try (FileWriter wr = new FileWriter("src/Guardados/Records.txt")) {
                            // Construir el texto final con cada registro en una línea separada
                            for (String record : recordsTemporal) {
                                wr.write(record + System.lineSeparator());
                            }
                            System.out.println("Successful");
                        } catch (IOException e) {
                            System.out.println("An error occurred: " + e.getMessage());
                        }

                        amenuPrincipal();
                    }
                    disparosEnemigos.remove(disparo);
                    break;
                }
            }
        }
    }

    private void verificarPosicionNave() {
        if (!Player1.shipBounds.intersects(baseEspacialBounds)) {
            // Reiniciar la bandera cuando la nave sale de los límites
            tiendaDisponible = true;
        }
    }

    private void pausarJuego() {
        timer.stop();
        enemyUpdateTimer.stop();
        statsUpdate.stop();
        leftPressed = false;
        rightPressed = false;
        downPressed = false;
        upPressed = false;
        musicaFondo.stop();
    }

    private void reanudarJuego() {
        timer.start();
        enemyUpdateTimer.start();
        statsUpdate.start();
        musicaFondo.start();
    }

    private void reproducirSonidos(String rutaArchivo) {
        try {
            // Abrir el archivo de audio como un stream de entrada
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(rutaArchivo));

            // Abrir el clip con el stream de audio
            switch (rutaArchivo) {
                case "src/Musica/musicaGamePlay.wav":
                    musicaFondo = AudioSystem.getClip();
                    musicaFondo.open(audioStream);
                    musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
                    musicaFondo.start();
                    break;
                default:
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    break;

            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
    }

    private void guardarJuego() {
        ArrayList<String> arrayGuardado = new ArrayList<>();
        //Caracteristicos
        arrayGuardado.add(Player1.playerName); //Cargado 0
        arrayGuardado.add(tipoNave); //Cargado 1
        //Jugabilidad
        arrayGuardado.add(String.valueOf(ronda)); //Cargado 2
        arrayGuardado.add(String.valueOf(enemiesKilled)); //Cargado 3
        arrayGuardado.add(String.valueOf(puntuacion)); //Cargado 4
        //Atributos
        arrayGuardado.add(String.valueOf(Player1.dañoBase)); //Cargado 5
        arrayGuardado.add(String.valueOf(Player1.multiplicadorDaño)); //Cargado 6
        arrayGuardado.add(String.valueOf(Player1.defensaBase)); //Cargado 7
        arrayGuardado.add(String.valueOf(Player1.multiplicadorDefensa)); //Cargado 8
        arrayGuardado.add(String.valueOf(Player1.velocidadAtaqueBase)); //Cargado 9
        arrayGuardado.add(String.valueOf(Player1.multiplicadorVelocidadAtaque)); //Cargado 10
        arrayGuardado.add(String.valueOf(Player1.velocidadNaveBase)); //Cargado 11
        arrayGuardado.add(String.valueOf(Player1.multiplicadorVelocidadNave)); //Cargado 12
        arrayGuardado.add(String.valueOf(Player1.vida)); //Cargado 13
        arrayGuardado.add(String.valueOf(Player1.vidaBase)); //Cargado 14
        arrayGuardado.add(String.valueOf(Player1.multiplicadorVida)); //Cargado 15
        arrayGuardado.add(String.valueOf(Player1.experiencia)); //Cargado 16
        arrayGuardado.add(String.valueOf(Player1.experienciaBase)); //Cargado 17
        arrayGuardado.add(String.valueOf(Player1.nivel)); //Cargado 18
        arrayGuardado.add(String.valueOf(Player1.scrap)); //Cargado 19
        arrayGuardado.add(String.valueOf(saveFile)); //Cargado 20

        //Frame de Mejoras
        arrayGuardado.add(String.valueOf(frameMejoras.getMejoraAtaque())); //Cargado 21
        arrayGuardado.add(String.valueOf(frameMejoras.getMejoraDefensa())); //Cargado 22
        arrayGuardado.add(String.valueOf(frameMejoras.getMejoraVida())); //Cargado 23
        arrayGuardado.add(String.valueOf(frameMejoras.getMejoraVelocidadAtaque())); //Cargado 24
        arrayGuardado.add(String.valueOf(frameMejoras.getMejoraVelocidadNave())); //Cargado 25
        arrayGuardado.add(String.valueOf(frameMejoras.getMejoraMultAtaque())); //Cargado 26
        arrayGuardado.add(String.valueOf(frameMejoras.getMejoraMultDefensa())); //Cargado 27
        arrayGuardado.add(String.valueOf(frameMejoras.getMejoraMultVida())); //Cargado 28

        arrayGuardado.add(String.valueOf(Player1.logs)); //Cargado 29

        arrayGuardado.add(String.valueOf(selectorDificultad)); //Cargado 30

        try {
            FileWriter wr = new FileWriter("src/Guardados/" + saveFile + ".txt");
            wr.write(arrayGuardado.toString());
            wr.close();
            System.out.println("Sucessful");
        } catch (IOException e) {
            System.out.println("An error ocurred");
        }
    }

    private void cargarJuego(ArrayList<String> datosCargados) {
        round.setText(String.valueOf(datosCargados.get(2)));
        enemiesKO.setText(String.valueOf(datosCargados.get(3)));
        score.setText(String.valueOf(datosCargados.get(4)));
        Player1.setDañoBaseFile(Double.parseDouble(datosCargados.get(5)));
        Player1.setMultiplicadorDañoFile(Double.parseDouble(datosCargados.get(6)));
        Player1.setDefensaBaseFile(Double.parseDouble(datosCargados.get(7)));
        Player1.setMultiplicadorDefensaFile(Double.parseDouble(datosCargados.get(8)));
        Player1.setVelocidadAtaqueBaseFile(Double.parseDouble(datosCargados.get(9)));
        Player1.setMultiplicadorVelocidadAtaqueFile(Double.parseDouble(datosCargados.get(10)));
        Player1.setVelocidadNaveBaseFile(Double.parseDouble(datosCargados.get(11)));
        Player1.setMultiplicadorVelocidadNaveFile(Double.parseDouble(datosCargados.get(12)));
        Player1.setVidaActualFile(Double.parseDouble(datosCargados.get(13)));
        Player1.setVidaBaseFile(Double.parseDouble(datosCargados.get(14)));
        Player1.setMultiplicadorVidaFile(Double.parseDouble(datosCargados.get(15)));
        Player1.setExperienciaFile(Double.parseDouble(datosCargados.get(16)));
        Player1.setExperienciaBaseFile(Double.parseDouble(datosCargados.get(17)));
        Player1.setNivelFile(Double.parseDouble(datosCargados.get(18)));
        Player1.setScrapFile(Double.parseDouble(datosCargados.get(19)));

        //Cargar Frame de mejoras
        frameMejoras.setMejoraAtaqueFile(Integer.parseInt(String.valueOf(datosCargados.get(21))));
        frameMejoras.setMejoraDefensaFile(Integer.parseInt(String.valueOf(datosCargados.get(22))));
        frameMejoras.setMejoraVidaFile(Integer.parseInt(String.valueOf(datosCargados.get(23))));
        frameMejoras.setMejoraVelocidadAtaqueFile(Integer.parseInt(String.valueOf(datosCargados.get(24))));
        frameMejoras.setMejoraVelocidadNaveFile(Integer.parseInt(String.valueOf(datosCargados.get(25))));
        frameMejoras.setMejoraMultAtaqueFile(Integer.parseInt(String.valueOf(datosCargados.get(26))));
        frameMejoras.setMejoraMultDefensaFile(Integer.parseInt(String.valueOf(datosCargados.get(27))));
        frameMejoras.setMejoraMultVidaFile(Integer.parseInt(String.valueOf(datosCargados.get(28))));

        String str = datosCargados.get(29);
        str = str.replace("[", "").replace("]", "");
        String[] strArray = str.split(",\\s*");
        Player1.logs.clear();
        for (String s : strArray) {
            Player1.logs.add(Integer.valueOf(s));
        }
    }

    private void actualizarHUB() {
        Ataque.setText(String.valueOf(Player1.getDaño()));
        defensa.setText(String.valueOf(Player1.getDefensa()));
        velocidadAtaque.setText(String.valueOf(Player1.getVelocidadAtaque()));
        velocidadNave.setText(String.valueOf(Player1.getVelocidadNave()));
        nivel.setText(String.valueOf(Player1.getNivel()));
        enemiesKO.setText(String.valueOf(enemiesKilled));
        round.setText(String.valueOf(ronda));
        score.setText(String.valueOf(puntuacion));
        barraVida.setMaximum((int) (Math.floor(Player1.getVidaMax())));
        barraVida.setValue((int) (Math.floor(Player1.getVida())));

        barraExperiencia.setMaximum((int) (Math.floor(Player1.experienciaSiguiente)));
        barraExperiencia.setValue((int) (Math.floor(Player1.getExperiencia())));
        if (!musicaFondo.isRunning()) {
            reproducirSonidos("src/Musica/musicaGamePlay.wav");
        }

    }

    private void cambiarBase() {
        double posicionBaseWidth = Math.random() * (jLabel1.getWidth() - 500);
        double posicionBaseHeight = Math.random() * (jLabel1.getHeight() - 500);
        baseEspacial.setLocation((int) posicionBaseWidth, (int) posicionBaseHeight);
        baseEspacial.setSize(baseEspacial.getPreferredSize()); // Asegúrate de ajustar el tamaño si es necesario
        baseEspacialBounds = baseEspacial.getBounds();
        baseEspacialBounds.setLocation((int) posicionBaseWidth, (int) posicionBaseHeight);
    }

    private void cambiarBackground() {
        int backGroundRandom = (int) (Math.floor((Math.random() * 4) + 1));
        switch (backGroundRandom) {
            case 1:
                jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Fondo_Espacio.jpg")));
                jLabel1.revalidate();
                jLabel1.repaint();
                jLabel1.setSize(jLabel1.getPreferredSize());
                xPos = 310;
                yPos = 345;
                jLabel2.setLocation(310, 345);
                cambiarBase();
                break;
            case 2:
                jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Fondo_Espacio2.jpg")));
                jLabel1.revalidate();
                jLabel1.repaint();
                jLabel1.setSize(jLabel1.getPreferredSize());
                xPos = 310;
                yPos = 345;
                jLabel2.setLocation(310, 345);
                cambiarBase();
                break;
            case 3:
                jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Fondo_Espacio3.jpg")));
                jLabel1.revalidate();
                jLabel1.repaint();
                jLabel1.setSize(jLabel1.getPreferredSize());
                xPos = 310;
                yPos = 345;
                jLabel2.setLocation(310, 345);
                cambiarBase();
                break;
            case 4:
                jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Fondo_Espacio4.jpg")));
                jLabel1.revalidate();
                jLabel1.repaint();
                jLabel1.setSize(jLabel1.getPreferredSize());
                xPos = 310;
                yPos = 345;
                jLabel2.setLocation(310, 345);
                cambiarBase();
                break;

        }
    }

    private void explosionEnemigo() {

        for (Enemigo enemigo : enemigosExplosion) {
            explosionEnemigo = new Timer(1000, (ActionEvent e) -> {
                jLabel1.remove(enemigo.jlabel);
                if (enemigosExplosion.isEmpty()) {
                    explosionEnemigo.stop();
                }
            });
            explosionEnemigo.start();
        }
    }

    private void explosionBoss(Boss boss) {
        explosionEnemigo = new Timer(1000, (ActionEvent e) -> {
            jLabel1.remove(boss.jlabel);
            explosionEnemigo.stop();
        });
        explosionEnemigo.start();

    }

    //Métodos y clases de personaje principal
    public class personajePrincipal {

        //Atributos
        public double daño;
        public double dañoBase;
        public double multiplicadorDaño = 0.5;

        public double defensa;
        public double defensaBase;
        public double multiplicadorDefensa = 0.25;

        public double velocidadAtaque;
        public double velocidadAtaqueBase;
        public double multiplicadorVelocidadAtaque = 20;

        public double velocidadNave;
        public double velocidadNaveBase;
        public double multiplicadorVelocidadNave = 0.005f;

        public double experiencia;
        public double experienciaSiguiente;
        public double experienciaBase;

        public double vida;
        public double vidaMax;
        public double vidaBase;
        public double multiplicadorVida = 1.5;

        public double nivel;
        public double nivelBase;

        public double scrap;
        public double scrapBase;

        public String playerName;

        LinkedList<Integer> logs = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        private Rectangle shipBounds = jLabel2.getBounds();

        public Rectangle getBounds() {
            return this.shipBounds;
        }

        public void setBounds() {
            this.shipBounds = jLabel2.getBounds();
        }

        //File setters
        public void setDañoBaseFile(double valor) {
            this.dañoBase = valor;
        }

        public void setMultiplicadorDañoFile(double valor) {
            this.multiplicadorDaño = valor;
        }

        public void setDefensaBaseFile(double valor) {
            this.defensaBase = valor;
        }

        public void setMultiplicadorDefensaFile(double valor) {
            this.multiplicadorDefensa = valor;
        }

        public void setVelocidadAtaqueBaseFile(double valor) {
            this.velocidadAtaqueBase = valor;
        }

        public void setMultiplicadorVelocidadAtaqueFile(double valor) {
            this.multiplicadorVelocidadAtaque = valor;
        }

        public void setVelocidadNaveBaseFile(double valor) {
            this.velocidadNaveBase = valor;
        }

        public void setMultiplicadorVelocidadNaveFile(double valor) {
            this.multiplicadorVelocidadNave = valor;
        }

        public void setVidaActualFile(double valor) {
            this.vida = valor;
        }

        public void setVidaBaseFile(double valor) {
            this.vidaBase = valor;
        }

        public void setMultiplicadorVidaFile(double valor) {
            this.multiplicadorVida = valor;
        }

        public void setExperienciaFile(double valor) {
            this.experiencia = valor;
        }

        public void setExperienciaBaseFile(double valor) {
            this.experienciaBase = valor;
        }

        public void setNivelFile(double valor) {
            this.nivel = valor;
        }

        public void setScrapFile(double valor) {
            this.scrap = valor;
        }

        //Normal setters
        public void setDaño(double mejora) {
            this.dañoBase += mejora;
        }

        public void setDaño() {
            this.daño = (this.multiplicadorDaño) * (this.nivel) + this.dañoBase;
        }

        public double getDaño() {
            return this.daño;
        }

        public void setMultiplicadorDaño(double mejora) {
            this.multiplicadorDaño += mejora;
        }

        public double getMultiplicadorDaño() {
            return this.multiplicadorDaño;
        }

        public void setDefensa(double mejora) {
            this.defensaBase += mejora;
        }

        public void setDefensa() {
            double defensaProvisional = (this.multiplicadorDefensa) * (this.nivel) + this.defensaBase;
            if (defensaProvisional <= 50) {
                this.defensa = defensaProvisional;
            }
        }

        public double getDefensa() {
            return this.defensa;
        }

        public void setMultiplicadorDefensa(double mejora) {
            this.multiplicadorDefensa += mejora;
        }

        public double getMultiplicadorDefensa() {
            return this.multiplicadorDefensa;
        }

        public void setVelocidadAtaque(double mejora) {
            double velocidadAtaqueProvisional = this.velocidadAtaqueBase - mejora;
            if (velocidadAtaqueProvisional >= 350) {
                this.velocidadAtaqueBase = velocidadAtaqueProvisional;
            }
        }

        public void setVelocidadAtaque() {
            double velocidadAtaqueProvisional = this.velocidadAtaqueBase - ((this.multiplicadorVelocidadAtaque) * (this.nivel)) + 20;
            if (velocidadAtaqueProvisional >= 350) {
                this.velocidadAtaque = velocidadAtaqueProvisional;
            }
        }

        public double getVelocidadAtaque() {
            return this.velocidadAtaque;
        }

        public void setMultiplicadorVelocidadAtaque(double mejora) {
            this.multiplicadorVelocidadAtaque += mejora;
        }

        public double getMultiplicadorVelocidadAtaque() {
            return this.multiplicadorVelocidadAtaque;
        }

        public void setVelocidadNave(double mejora) {
            double velocidadNaveProvisional = this.velocidadNaveBase + mejora;
            if (velocidadNaveProvisional >= 0.4f) {
                this.velocidadNaveBase = velocidadNaveProvisional;
            }
        }

        public void setVelocidadNave() {
            double velocidadNaveProvisional = (this.multiplicadorVelocidadNave) * (this.nivel) + this.velocidadNaveBase;
            if (velocidadNaveProvisional >= 0.4f) {
                this.velocidadNave = velocidadNaveProvisional;
            }
        }

        public double getVelocidadNave() {
            return this.velocidadNave;
        }

        public void setMultiplicadorVelocidadNave(double mejora) {
            this.multiplicadorVelocidadNave += mejora;
        }

        public double getMutliplicadorVelocidadNave() {
            return this.multiplicadorVelocidadNave;
        }

        public void setExperiencia(double ganancia) {
            this.experiencia += ganancia;
        }

        public void setExperienciaSiguiente() {
            this.experienciaSiguiente = 3 * this.nivel + 30;
        }

        public double getExperiencia() {
            return this.experiencia;
        }

        public void setVida(double mejora) {
            this.vidaBase += mejora;
        }

        public void dañoVida(double daño) {
            this.vida -= daño;
        }

        public void setVida() {
            this.vida = (this.multiplicadorVida) * (this.nivel) + (this.vidaBase);
        }

        public double getVida() {
            return this.vida;
        }

        public double getVidaMax() {
            return (((this.multiplicadorVida) * (this.nivel)) + (this.vidaBase));
        }

        public void setMultiplicadorVida(double mejora) {
            this.multiplicadorVida += mejora;
        }

        public double getMultiplicadorVida() {
            return this.multiplicadorVida;
        }

        public void setNivel() {
            this.nivel = 0;
        }

        public double getNivel() {
            return this.nivel;
        }

        public void setScrap(double ganancia) {
            if (ganancia <= 0) {
                this.scrap += ganancia;
            } else {
                this.scrap += ganancia;
            }
        }

        public double getScrap() {
            return this.scrap;
        }

        public void subirNivel() {
            this.nivel += 1;
            Player1.setDaño();
            Player1.setDefensa();
            Player1.setVelocidadAtaque();
            Player1.setVelocidadNave();
            Player1.setVida();
            Player1.setExperienciaSiguiente();
            Player1.experiencia = 0;
        }

        //Constructor
        public personajePrincipal(String Path) {
            switch (Path) {
                case "src/shipsSelectionImages/ShipRed.png":
                    this.dañoBase = 4;
                    this.defensaBase = 2;
                    this.velocidadAtaqueBase = 2000;
                    this.velocidadAtaque = 2000;
                    this.velocidadNaveBase = 0.25f;
                    this.velocidadNave = 0.25f;
                    this.experiencia = 0;
                    this.vidaBase = 15;
                    this.nivel = 1;
                    this.scrap = 0;
                    break;
                case "src/shipsSelectionImages/ShipWhite.png":
                    this.dañoBase = 6;
                    this.defensaBase = 4;
                    this.velocidadAtaqueBase = 3000;
                    this.velocidadAtaque = 3000;
                    this.velocidadNaveBase = 0.2f;
                    this.velocidadNave = 0.2f;
                    this.experiencia = 0;
                    this.vidaBase = 20;
                    this.nivel = 1;
                    this.scrap = 0;
                    break;
                case "src/shipsSelectionImages/ShipPurple.png":
                    this.dañoBase = 3;
                    this.defensaBase = 1;
                    this.velocidadAtaqueBase = 1000;
                    this.velocidadAtaque = 1000;
                    this.velocidadNaveBase = 0.3f;
                    this.velocidadNave = 0.3;
                    this.experiencia = 0;
                    this.vidaBase = 10;
                    this.nivel = 1;
                    this.scrap = 0;
                    break;
                default:
                    break;
            }
        }

        //Meter atributos del personaje principal
        public void personajeDisparo(double clickX, double clickY) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShotTime >= this.velocidadAtaque) {
                reproducirSonidos("src/Musica/laserPersonajePrincipal.wav");
                // Coordenadas del centro de la nave
                // Coordenadas del centro de la nave
                double centerX = jLabel2.getX() + jLabel2.getWidth() / 2.0;
                double centerY = jLabel2.getY() + jLabel2.getHeight() / 2.0;

                // Calcular dirección del disparo basado en el clic del mouse
                double dx = clickX - centerX;
                double dy = clickY - centerY;

                // Calcular la distancia
                double distance = Math.sqrt(dx * dx + dy * dy);

                // Velocidad del disparo
                double bulletSpeed = 5.0;
                double dxb = (dx / distance) * bulletSpeed;
                double dyb = (dy / distance) * bulletSpeed;

                // Calcular el ángulo de disparo en grados
                double angle = Math.toDegrees(Math.atan2(dy, dx));
                if (angle > -90 && angle < 0) {
                    angle += 90;
                } else if (angle > 0 && angle < 180) {
                    angle += 90;
                } else if (angle < -90 && angle > -180) {
                    angle += 450;
                } else if (angle == 0) {
                    angle = 0;
                }

                //0° Arriba
                //90° Derecha
                //180° Abajo
                //270° Izquierda
                // Crear y agregar nuevo disparo a la lista
                Disparo nuevoDisparo = new Disparo(centerX, centerY, dxb, dyb, angle);
                disparos.add(nuevoDisparo);
                jLabel1.add(nuevoDisparo.jlabel);

                // Actualizar el tiempo del último disparo
                lastShotTime = currentTime;

            }
        }

        private void movimientoPJPrincipal() {
            boolean moved = false;

            double radians = Math.toRadians(rotationAngleShip); // Convertir ángulo de rotación a radianes

            // Ajustar velocidad según las teclas presionadas
            if (upPressed) {
                dxPlayer += Math.sin(radians) * (velocidadNave);
                dyPlayer -= Math.cos(radians) * (velocidadNave);
                //El usar seno y coseno nos da la dirección en diagonal en caso de que la nave se encuentre rotando, por ejemplo si los radianes
                //son iguales a 45° entonces el resultado de ambas ecuaciones nos va a dar 0.707 aproximadamente, lo que hace
                //que el movimiento sea 0.707 que corresponde a la dirección de 45°
                moved = true;
            }
            if (downPressed) {
                dxPlayer -= Math.sin(radians) * (velocidadNave);
                dyPlayer += Math.cos(radians) * (velocidadNave);
                moved = true;
            }

            if (this.shipBounds.intersects(baseEspacialBounds) && tiendaDisponible) {
                // Pausar el juego
                pausarJuego();

                // Crear y mostrar la ventana de la tienda
                Tienda tienda = new Tienda(Player1, frameMejoras);
                tienda.setVisible(true);
                tienda.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        pausarJuego();
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                        reanudarJuego();
                    }
                });
                tiendaDisponible = false; // Marcar que la tienda no está disponible para abrir

                tienda.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        // Reanudar el juego cuando la tienda se cierra
                        reanudarJuego();
                    }

                });
            }

            verificarPosicionNave();

            // Aplicar fricción en la dirección del movimiento
            dxPlayer *= friction;
            dyPlayer *= friction;

            // Actualizar posición
            xPos += dxPlayer;
            yPos += dyPlayer;

            if (Math.abs(dxPlayer) < minVelocity) {
                dxPlayer = 0;
            }
            if (Math.abs(dyPlayer) < minVelocity) {
                dyPlayer = 0;
            }

            // Obtener el tamaño del jLabel2
            int playerWidth = jLabel2.getWidth();
            int playerHeight = jLabel2.getHeight();

            // Asegurarse de que la nave no se salga de los límites del fondo
            xPos = Math.max(0, Math.min(xPos, jLabel1.getWidth() - playerWidth));
            yPos = Math.max(0, Math.min(yPos, jLabel1.getHeight() - playerHeight));

            // Actualizar la posición de la nave en pantalla
            jLabel2.setLocation((int) xPos, (int) yPos);

            Player1.shipBounds.setLocation(jLabel2.getX(), jLabel2.getY());

            if (moved) {
                centrarVistaenNave();
            }
        }

        private void rotarNave() {
            if (leftPressed) {
                rotationAngleShip -= 2; // Rotar a la izquierda
            }
            if (rightPressed) {
                rotationAngleShip += 2; // Rotar a la derecha
            }

            // Asegurandose de que se encuentra entre 0 y 360
            rotationAngleShip = rotationAngleShip % 360;

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = rotatedImage.createGraphics();
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(rotationAngleShip), width / 2, height / 2);
            transform.translate((width - originalImage.getWidth()) / 2, (height - originalImage.getHeight()) / 2);

            g2d.drawImage(originalImage, transform, null);
            g2d.dispose();

            jLabel2.setIcon(new ImageIcon(rotatedImage));
        }

        private void cargarNavePrincipal(String Path) {
            try {
                originalImage = ImageIO.read(new File(Path));
                rotatedImage = originalImage;
                jLabel2.setIcon(new ImageIcon(originalImage));
                jLabel2.setSize(jLabel2.getPreferredSize());
                Player1.shipBounds = jLabel2.getBounds();
            } catch (IOException e) {
            }
        }

        private void centrarVistaenNave() {
            int viewX = xPos - (jScrollPane1.getWidth() / 2) + (jLabel2.getWidth() / 2);
            int viewY = yPos - (jScrollPane1.getHeight() / 2) + (jLabel2.getHeight() / 2);

            viewX = Math.max(0, Math.min(viewX, jLabel1.getWidth() - jScrollPane1.getWidth()));
            viewY = Math.max(0, Math.min(viewY, jLabel1.getHeight() - jScrollPane1.getHeight()));

            jScrollPane1.getViewport().setViewPosition(new Point(viewX, viewY));
        }
    }

    public class Disparo {

        public JLabel jlabel; // La clase disparo es representada por un JLabel como objeto
        public double x, y;   // Posición del disparo
        public double dx, dy; // Velocidad del disparo
        public double angle;
        public Rectangle LaserPlayerBounds;

        public Rectangle getLaserBounds() {
            return LaserPlayerBounds;
        }

        public Disparo(double x, double y, double dx, double dy, double angle) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.angle = angle;
            // Rotar imagen del disparo
            BufferedImage laserImage = rotateBulletImage(bulletIcon.getImage(), angle);
            this.jlabel = new JLabel(new ImageIcon(laserImage));

            // Establecer el tamaño del JLabel según el tamaño de la imagen rotada
            this.jlabel.setSize(laserImage.getWidth(), laserImage.getHeight());
            this.jlabel.setLocation((int) x, (int) y);

            LaserPlayerBounds = jlabel.getBounds();
        }

        public void mover() {
            x += dx;
            y += dy;
            jlabel.setLocation((int) x, (int) y);
            LaserPlayerBounds = jlabel.getBounds();
        }

        private BufferedImage rotateBulletImage(Image image, double angle) {
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            // Convertir el ángulo de grados a radianes
            double radianAngle = Math.toRadians(angle);

            //0° Arriba
            //90° Derecha
            //180° Abajo
            //270° Izquierda
            double cos = Math.abs(Math.cos(radianAngle));
            double sin = Math.abs(Math.sin(radianAngle));
            int newWidth = (int) Math.ceil(width * cos + height * sin);
            int newHeight = (int) Math.ceil(height * cos + width * sin);

            // Crear una nueva imagen con las dimensiones adecuadas
            BufferedImage rotatedImageBullet = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rotatedImageBullet.createGraphics();

            // Realizar la rotación
            AffineTransform transform = new AffineTransform();
            transform.translate((newWidth - width) / 2, (newHeight - height) / 2);
            transform.rotate(radianAngle, width / 2, height / 2);

            g2d.drawImage(image, transform, null);
            g2d.dispose();

            return rotatedImageBullet;
        }

    }

    private void actualizarDisparos() {
        // Obtener el área visible del JScrollPane
        int viewportWidth = jScrollPane1.getViewport().getWidth();
        int viewportHeight = jScrollPane1.getViewport().getHeight();
        Point viewportPosition = jScrollPane1.getViewport().getViewPosition();

        ArrayList<Disparo> disparosToRemove = new ArrayList<>();
        for (Disparo disparo : disparos) {
            disparo.mover();

            // Verificar si el disparo está dentro del área visible del JScrollPane
            int disparoX = (int) disparo.x - viewportPosition.x;
            int disparoY = (int) disparo.y - viewportPosition.y;

            if (disparoX < 0 || disparoX > viewportWidth || disparoY < 0 || disparoY > viewportHeight) {
                disparosToRemove.add(disparo);
            }
        }

        // Elimina los disparos que están fuera de la vista
        for (Disparo disparo : disparosToRemove) {
            jLabel1.remove(disparo.jlabel);
            disparos.remove(disparo);
        }

        ArrayList<DisparoEnemigo> disparosEnemigosToRemove = new ArrayList<>();
        for (DisparoEnemigo disparoEnemigo : disparosEnemigos) {
            disparoEnemigo.mover();

            // Verificar si el disparo enemigo está dentro del área visible del JScrollPane
            int disparoEnemigoX = (int) disparoEnemigo.x - viewportPosition.x;
            int disparoEnemigoY = (int) disparoEnemigo.y - viewportPosition.y;

            if (disparoEnemigoX < 0 || disparoEnemigoX > viewportWidth || disparoEnemigoY < 0 || disparoEnemigoY > viewportHeight) {
                disparosEnemigosToRemove.add(disparoEnemigo);
            }
        }

        // Elimina los disparos enemigos que están fuera de la vista
        for (DisparoEnemigo disparoEnemigo : disparosEnemigosToRemove) {
            jLabel1.remove(disparoEnemigo.jlabel);
            disparosEnemigos.remove(disparoEnemigo);

        }

        // Actualiza la vista para reflejar los cambios
    }

    public class mejorasPlayer {

        public int mejoraAtaque = 1;
        public int mejoraDefensa = 1;
        public int mejoraVida = 1;
        public int mejoraVelocidadAtaque = 1;
        public int mejoraVelocidadNave = 1;
        public int mejoraMultAtaque = 1;
        public int mejoraMultDefensa = 1;
        public int mejoraMultVida = 1;

        public int getMejoraAtaque() {
            return mejoraAtaque;
        }

        public void setMejoraAtaque() {
            this.mejoraAtaque += 1;
            System.out.println(mejoraAtaque);
        }

        public void setMejoraAtaqueFile(int valor) {
            this.mejoraAtaque = valor;
        }

        public int getMejoraDefensa() {
            return mejoraDefensa;
        }

        public void setMejoraDefensa() {
            this.mejoraDefensa += 1;
        }

        public void setMejoraDefensaFile(int valor) {
            this.mejoraDefensa = valor;
        }

        public int getMejoraVida() {
            return mejoraVida;
        }

        public void setMejoraVida() {
            this.mejoraVida += 1;
        }

        public void setMejoraVidaFile(int valor) {
            this.mejoraVida = valor;
        }

        public int getMejoraVelocidadAtaque() {
            return mejoraVelocidadAtaque;
        }

        public void setMejoraVelocidadAtaque() {
            this.mejoraVelocidadAtaque += 1;
        }

        public void setMejoraVelocidadAtaqueFile(int valor) {
            this.mejoraVelocidadAtaque = valor;
        }

        public int getMejoraVelocidadNave() {
            return mejoraVelocidadNave;
        }

        public void setMejoraVelocidadNave() {
            this.mejoraVelocidadNave += 1;
        }

        public void setMejoraVelocidadNaveFile(int valor) {
            this.mejoraVelocidadNave = valor;
        }

        public int getMejoraMultAtaque() {
            return mejoraMultAtaque;
        }

        public void setMejoraMultAtaque() {
            this.mejoraMultAtaque += 1;
        }

        public void setMejoraMultAtaqueFile(int valor) {
            this.mejoraMultAtaque = valor;
        }

        public int getMejoraMultDefensa() {
            return mejoraMultDefensa;
        }

        public void setMejoraMultDefensa() {
            this.mejoraMultDefensa += 1;
        }

        public void setMejoraMultDefensaFile(int valor) {
            this.mejoraMultDefensa = valor;
        }

        public int getMejoraMultVida() {
            return mejoraMultVida;
        }

        public void setMejoraMultVida() {
            this.mejoraMultVida += 1;
        }

        public void setMejoraMultVidaFile(int valor) {
            this.mejoraMultVida = valor;
        }

        public mejorasPlayer() {
            this.mejoraAtaque = 1;
            this.mejoraDefensa = 1;
            this.mejoraVida = 1;
            this.mejoraVelocidadAtaque = 1;
            this.mejoraVelocidadNave = 1;
            this.mejoraMultAtaque = 1;
            this.mejoraMultDefensa = 1;
            this.mejoraMultVida = 1;
        }
    }

//Métodos y clases de enemigos
    public class Enemigo {

        //Atributos funcionales
        int x, y;
        private int width, height;
        public double dx, dy;
        JLabel jlabel;
        private long lastEnemyShotTime = 0; // Tiempo del último disparo del enemigo
        private final String enemyLaserImagePath = "src/Imagenes/LaserEnemigo.png";
        private final Rectangle enemyBounds;
        private final int puntuacion = 5;
        private final double puntuacionMultiplicador = 0.5;

        //Atributos de gameplay
        public double daño;
        public double dañoBase = 4;
        public double dañoMultiplicador = 0.6;

        public double defensa;
        public double defensaBase = 5;
        public double defensaMultiplicador = 0.5;

        public double velocidadAtaque;
        public double velocidadAtaqueBase = 3000;
        public double velocidadAtaqueMultiplicador = 25;

        public int velocidadNave;
        public int velocidadNaveBase = 1;
        public double velocidadNaveMultiplicador = 0.05;

        public double experiencia;
        public double experienciaBase = 3;
        public double experienciaMultiplicador = 0.5;

        public double vida;
        public double vidaBase = 7;
        public double vidaMultiplicador = 0.5;

        public double nivel;

        public double scrap;
        public double scrapBase = 3;
        public double scrapMultiplicador = 0.5;

        //Getters and setters funcionales
        public int getPuntuacion() {
            return this.puntuacion;
        }

        public double getPuntuacionMultiplicador() {
            return this.puntuacionMultiplicador;
        }

        public Rectangle getEnemyBounds() {
            return enemyBounds;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void setLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        //Getters and setters de gameplay
        public void setDaño() {
            this.daño = (this.dañoMultiplicador * this.nivel) + this.dañoBase;
        }

        public double getDaño() {
            return this.daño;
        }

        public void setDefensa() {
            double defensaProvisional = (this.defensaMultiplicador * this.nivel) + this.defensaBase;
            if (defensaProvisional <= 50) {
                this.defensa = defensaProvisional;
            }
        }

        public double getDefensa() {
            return this.defensa;
        }

        public void setVelocidadAtaque() {
            double velocidadAtaqueProvisional = -(this.velocidadAtaqueMultiplicador * this.nivel) + this.velocidadAtaqueBase;
            if (velocidadAtaqueProvisional >= 350) {
                this.velocidadAtaque = velocidadAtaqueProvisional;
            }
        }

        public double getVelocidadAtaque() {
            return this.velocidadAtaque;
        }

        public void setVelocidadNave() {
            double resultado = (this.velocidadNaveMultiplicador * this.nivel) + this.velocidadNaveBase;
            if (resultado <= 3) {
                this.velocidadNave = (int) Math.floor(resultado);
            }
        }

        public int getVelocidadNave() {
            return this.velocidadNave;
        }

        public void setExperiencia() {
            this.experiencia = (this.experienciaMultiplicador * this.nivel) + this.experienciaBase;
        }

        public double getExperiencia() {
            return this.experiencia;
        }

        public void setVida() {
            this.vida = (this.vidaMultiplicador * this.nivel) + this.vidaBase;
        }

        public void dañoVida(double daño) {
            this.vida -= daño;
        }

        public double getVida() {
            return this.vida;
        }

        public double getNivel() {
            return this.nivel;
        }

        public void setScrap() {
            this.scrap = (this.scrapMultiplicador * this.nivel) + this.scrapBase;
        }

        public double getScrap() {
            return this.scrap;
        }

        //Constructor
        public Enemigo(int x, int y, double dx, double dy, String imagePath, double nivelPlayer) {

            //Asignaciones de atributos funcionales
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.jlabel = new JLabel(new ImageIcon(imagePath));
            this.jlabel.setSize(jlabel.getPreferredSize()); // Asegúrate de que el tamaño sea adecuado
            this.jlabel.setLocation(x, y);
            enemyBounds = jlabel.getBounds();

            //Asignaciones de atributos de gameplay
            this.nivel = nivelPlayer;
        }

        //Funciones
        public void moverHaciaPersonaje(Enemigo enemigo) {
            // Calcular las coordenadas centrales del personaje principal
            int playerCenterX = jLabel2.getX() + jLabel2.getWidth() / 2;
            int playerCenterY = jLabel2.getY() + jLabel2.getHeight() / 2;

            // Calcular las coordenadas centrales del enemigo
            double enemyCenterX = enemigo.getX() + enemigo.getWidth() / 2.0;
            double enemyCenterY = enemigo.getY() + enemigo.getHeight() / 2.0;

            // Calcular la diferencia en las coordenadas X e Y entre el enemigo y el personaje
            double dxE = playerCenterX - enemyCenterX;
            double dyE = playerCenterY - enemyCenterY;

            // Calcular la distancia euclidiana entre el enemigo y el personaje
            double distance = Math.sqrt(dxE * dxE + dyE * dyE);

            // Si la distancia es mayor a 0, mover al enemigo
            if (distance > 0) {
                // Normalizar el vector de dirección
                double moveX = (dxE / distance) * this.velocidadNave;
                double moveY = (dyE / distance) * this.velocidadNave;

                // Calcular la nueva posición en punto flotante
                double newX = enemigo.getX() + moveX;
                double newY = enemigo.getY() + moveY;

                // Redondear la nueva posición a enteros antes de actualizar
                int roundedX = (int) Math.round(newX);
                int roundedY = (int) Math.round(newY);

                // Mover al enemigo en la dirección calculada
                enemigo.setLocation(roundedX, roundedY);

                // Actualiza la posición del JLabel del enemigo
                enemigo.jlabel.setLocation(roundedX, roundedY);

                // Actualizar la posición de los bounds del enemigo
                enemyBounds.setLocation(roundedX, roundedY);
            }

            // Revalidar el fondo para asegurar que los cambios en la interfaz se reflejen
            jLabel1.revalidate();
            jLabel1.repaint();
        }

        public void disparar() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastEnemyShotTime >= this.velocidadAtaque) {
                int centerX = x + jlabel.getWidth() / 2;
                int centerY = y + jlabel.getHeight() / 2;

                double dxE = jLabel2.getX() + jLabel2.getWidth() / 2 - centerX;
                double dyE = jLabel2.getY() + jLabel2.getHeight() / 2 - centerY;

                double angle = Math.atan2(dyE, dxE);
                double bulletSpeed = 3.0; // Ajusta la velocidad si es necesario

                double dxBullet = bulletSpeed * Math.cos(angle);
                double dyBullet = bulletSpeed * Math.sin(angle);

                DisparoEnemigo nuevoDisparo = new DisparoEnemigo(centerX, centerY, dxBullet, dyBullet, Math.toDegrees(angle), enemyLaserImagePath);
                disparosEnemigos.add(nuevoDisparo);
                jLabel1.add(nuevoDisparo.jlabel);

                lastEnemyShotTime = currentTime;
            }
        }
    }

    public class Boss {

        //Atributos funcionales
        int x, y;
        private int width, height;
        public double dx, dy;
        JLabel jlabel;
        private long lastEnemyShotTime = 0; // Tiempo del último disparo del enemigo
        private final String enemyLaserImagePath = "src/Imagenes/LaserEnemigo.png";
        private final Rectangle enemyBounds;
        private final int puntuacion = 20;
        private final double puntuacionMultiplicador = 1;

        //Atributos de gameplay
        public double daño;
        public double dañoBase = 8;
        public double dañoMultiplicador = 1.2;

        public double defensa;
        public double defensaBase = 10;
        public double defensaMultiplicador = 1;

        public double velocidadAtaque;
        public double velocidadAtaqueBase = 1500;
        public double velocidadAtaqueMultiplicador = 25;

        public int velocidadNave;
        public int velocidadNaveBase = 1;
        public double velocidadNaveMultiplicador = 0.05;

        public double experiencia;
        public double experienciaBase = 10;
        public double experienciaMultiplicador = 1;

        public double vida;
        public double vidaBase = 14;
        public double vidaMultiplicador = 1;

        public double nivel;

        public double scrap;
        public double scrapBase = 30;
        public double scrapMultiplicador = 1;

        //Getters and setters funcionales
        public int getPuntuacion() {
            return this.puntuacion;
        }

        public double getPuntuacionMultiplicador() {
            return this.puntuacionMultiplicador;
        }

        public Rectangle getEnemyBounds() {
            return enemyBounds;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void setLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        //Getters and setters de gameplay
        public void setDaño() {
            this.daño = (this.dañoMultiplicador * this.nivel) + this.dañoBase;
        }

        public double getDaño() {
            return this.daño;
        }

        public void setDefensa() {
            double defensaProvisional = (this.defensaMultiplicador * this.nivel) + this.defensaBase;
            if (defensaProvisional <= 50) {
                this.defensa = defensaProvisional;
            }
        }

        public double getDefensa() {
            return this.defensa;
        }

        public void setVelocidadAtaque() {
            double velocidadAtaqueProvisional = -(this.velocidadAtaqueMultiplicador * this.nivel) + this.velocidadAtaqueBase;
            if (velocidadAtaqueProvisional >= 350) {
                this.velocidadAtaque = velocidadAtaqueProvisional;
            }
        }

        public double getVelocidadAtaque() {
            return this.velocidadAtaque;
        }

        public void setVelocidadNave() {
            double resultado = (this.velocidadNaveMultiplicador * this.nivel) + this.velocidadNaveBase;
            if (resultado <= 3) {
                this.velocidadNave = (int) Math.floor(resultado);
            }
        }

        public int getVelocidadNave() {
            return this.velocidadNave;
        }

        public void setExperiencia() {
            this.experiencia = (this.experienciaMultiplicador * this.nivel) + this.experienciaBase;
        }

        public double getExperiencia() {
            return this.experiencia;
        }

        public void setVida() {
            this.vida = (this.vidaMultiplicador * this.nivel) + this.vidaBase;
        }

        public void dañoVida(double daño) {
            this.vida -= daño;
        }

        public double getVida() {
            return this.vida;
        }

        public double getNivel() {
            return this.nivel;
        }

        public void setScrap() {
            this.scrap = (this.scrapMultiplicador * this.nivel) + this.scrapBase;
        }

        public double getScrap() {
            return this.scrap;
        }

        //Constructor
        public Boss(int x, int y, double dx, double dy, String imagePath, double nivelPlayer) {

            //Asignaciones de atributos funcionales
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.jlabel = new JLabel(new ImageIcon(imagePath));
            this.jlabel.setSize(jlabel.getPreferredSize()); // Asegúrate de que el tamaño sea adecuado
            this.jlabel.setLocation(x, y);
            enemyBounds = jlabel.getBounds();

            //Asignaciones de atributos de gameplay
            this.nivel = nivelPlayer;
        }

        //Funciones
        public void moverHaciaPersonaje(Boss boss) {
            // Calcular las coordenadas centrales del personaje principal
            int playerCenterX = jLabel2.getX() + jLabel2.getWidth() / 2;
            int playerCenterY = jLabel2.getY() + jLabel2.getHeight() / 2;

            // Calcular las coordenadas centrales del Boss
            double bossCenterX = boss.getX() + boss.getWidth() / 2.0;
            double bossCenterY = boss.getY() + boss.getHeight() / 2.0;

            // Calcular la diferencia en las coordenadas X e Y entre el Boss y el personaje
            double dxB = playerCenterX - bossCenterX;
            double dyB = playerCenterY - bossCenterY;

            // Calcular la distancia euclidiana entre el Boss y el personaje
            double distance = Math.sqrt(dxB * dxB + dyB * dyB);

            // Si la distancia es mayor a 0, mover al Boss
            if (distance > 0) {
                // Normalizar el vector de dirección
                double moveX = (dxB / distance) * this.velocidadNave;
                double moveY = (dyB / distance) * this.velocidadNave;

                // Calcular la nueva posición en punto flotante
                double newX = boss.getX() + moveX;
                double newY = boss.getY() + moveY;

                // Redondear la nueva posición a enteros antes de actualizar
                int roundedX = (int) Math.round(newX);
                int roundedY = (int) Math.round(newY);

                // Mover al Boss en la dirección calculada
                boss.setLocation(roundedX, roundedY);

                // Actualiza la posición del JLabel del Boss
                boss.jlabel.setLocation(roundedX, roundedY);

                // Actualizar la posición de los bounds del Boss
                enemyBounds.setLocation(roundedX, roundedY);
            }

            // Revalidar el fondo para asegurar que los cambios en la interfaz se reflejen
            jLabel1.revalidate();
            jLabel1.repaint();
        }

        public void disparar() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastEnemyShotTime >= this.velocidadAtaque) {
                int centerX = x + jlabel.getWidth() / 2;
                int centerY = y + jlabel.getHeight() / 2;

                double dxb = jLabel2.getX() + jLabel2.getWidth() / 2 - centerX;
                double dyb = jLabel2.getY() + jLabel2.getHeight() / 2 - centerY;

                double angle = Math.atan2(dyb, dxb);
                double bulletSpeed = 3.0; // Ajusta la velocidad si es necesario

                double dxBullet = bulletSpeed * Math.cos(angle);
                double dyBullet = bulletSpeed * Math.sin(angle);

                DisparoEnemigo nuevoDisparo = new DisparoEnemigo(centerX, centerY, dxBullet, dyBullet, Math.toDegrees(angle), enemyLaserImagePath);
                disparosEnemigos.add(nuevoDisparo);
                jLabel1.add(nuevoDisparo.jlabel);

                lastEnemyShotTime = currentTime;
            }
        }

    }

    private void actualizarEnemigos() {
        // Iterador para actualizar los enemigos
        Iterator<Enemigo> iterador = enemigos.iterator();

        while (iterador.hasNext()) {
            Enemigo enemigo = iterador.next();
            // Actualiza el estado del enemigo
            enemigo.moverHaciaPersonaje(enemigo);
            enemigo.disparar();

            enemigo.enemyBounds.setLocation(enemigo.getX(), enemigo.getY());

            // Comprueba si el enemigo debe ser eliminado
            if (debeEliminarEnemigo(enemigo)) {
                enemigo.jlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ExplosionNave.png")));
                enemigo.jlabel.revalidate();
                enemigo.jlabel.repaint();
                enemigo.jlabel.setSize(enemigo.jlabel.getPreferredSize());
                enemigosExplosion.add(enemigo);
                explosionEnemigo();
                reproducirSonidos("src/Musica/enemigoMuerto.wav");
                iterador.remove(); // Elimina el enemigo de la lista usando el iterador

            }
        }
    }

    private void actualizarBosses() {
        // Iterador para actualizar los enemigos
        Iterator<Boss> iterador = bosses.iterator();

        while (iterador.hasNext()) {
            Boss boss = iterador.next();
            // Actualiza el estado del enemigo
            boss.moverHaciaPersonaje(boss);
            boss.disparar();

            boss.enemyBounds.setLocation(boss.getX(), boss.getY());

            // Comprueba si el enemigo debe ser eliminado
            if (debeEliminarBoss(boss)) {
                boss.jlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ExplosionNave.png")));
                boss.jlabel.revalidate();
                boss.jlabel.repaint();
                boss.jlabel.setSize(boss.jlabel.getPreferredSize());
                explosionBoss(boss);
                reproducirSonidos("src/Musica/enemigoMuerto.wav");
                iterador.remove(); // Elimina el enemigo de la lista usando el iterador

            }
        }
    }

    public class DisparoEnemigo {

        public JLabel jlabel;
        public double x, y;
        public double dx, dy;
        public Rectangle laserEnemyBounds;

        public Rectangle getLaserBounds() {
            return this.laserEnemyBounds;
        }

        private BufferedImage rotateBulletImage2(Image image, double angle) {
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            double rad = Math.toRadians(angle);

            // Calculo del tamaño necesario para la imagen rotada
            double sin = Math.abs(Math.sin(rad));
            double cos = Math.abs(Math.cos(rad));
            int newWidth = (int) Math.floor(width * cos + height * sin);
            int newHeight = (int) Math.floor(width * sin + height * cos);

            // Creacion de una nueva imagen con el tamaño adecuado
            BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rotatedImage.createGraphics();

            // Configura el punto de rotación y aplica la rotación
            g2d.translate((newWidth - width) / 2, (newHeight - height) / 2);
            g2d.rotate(rad, width / 2, height / 2);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            return rotatedImage;
        }

        public DisparoEnemigo(double x, double y, double dx, double dy, double angle, String imagePath) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;

            try {
                BufferedImage laserImage = ImageIO.read(new File(imagePath));
                laserImage = rotateBulletImage2(laserImage, angle); // Usa el ángulo calculado para rotar la imagen
                this.jlabel = new JLabel(new ImageIcon(laserImage));
                this.jlabel.setSize(laserImage.getWidth(), laserImage.getHeight());
                this.jlabel.setLocation((int) x, (int) y);
            } catch (IOException e) {
            }
            laserEnemyBounds = this.jlabel.getBounds();
        }

        public void mover() {
            x += dx;
            y += dy;
            jlabel.setLocation((int) x, (int) y);
            laserEnemyBounds = jlabel.getBounds();
        }
    }

//Funcion de frame principal
    public GameLogic(String Path, int fileNumberSpaceships, String playerNameSpaceships, int difficulty) {
        initComponents();

        //Creacion de objeto de personaje principal
        Player1 = new personajePrincipal(Path);
        Player1.playerName = playerNameSpaceships;
        Player1.setDaño();
        Player1.setDefensa();
        Player1.setVelocidadAtaque();
        Player1.setVelocidadNave();
        Player1.setVida();
        Player1.setExperienciaSiguiente();
        nombre.setText(Player1.playerName);

        //Selector de dificultad
        selectorDificultad = difficulty;
        switch (selectorDificultad) {
            case 1:
                enemyGenerationInterval = 5000;
                maxEnemies = 10;
                levelDiff = 3;
                break;
            case 2:
                enemyGenerationInterval = 4000;
                maxEnemies = 15;
                levelDiff = 4;
                break;
            case 3:
                enemyGenerationInterval = 3000;
                maxEnemies = 20;
                levelDiff = 5;
                break;
        }

        //Frame de mejoras de personae
        frameMejoras = new mejorasPlayer();

        //Creacion de variable con savefile
        saveFile = fileNumberSpaceships;
        tipoNave = Path;

        //Centrado de ventana en la pantalla
        setLocationRelativeTo(null);
        Player1.cargarNavePrincipal(Path);
        //Cargamos la imagen del disparo
        bulletIcon = new ImageIcon(laserImagePath);

        //Base espacial
        baseEspacial = new javax.swing.JLabel();
        baseEspacial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Spacebase.png")));

        // Cambia la ubicación
        double posicionBaseWidth = 500;//Math.random() * (jLabel1.getWidth() - 500);
        double posicionBaseHeight = 500;//Math.random() * (jLabel1.getHeight() - 500);
        baseEspacial.setLocation((int) posicionBaseWidth, (int) posicionBaseHeight);
        baseEspacial.setSize(baseEspacial.getPreferredSize()); // Asegúrate de ajustar el tamaño si es necesario
        baseEspacialBounds = baseEspacial.getBounds();
        baseEspacialBounds.setLocation((int) posicionBaseWidth, (int) posicionBaseHeight);
        jLabel1.add(baseEspacial);

        xPos = 310;
        yPos = 345;
        jLabel2.setLocation(310, 345);  // Establecer la posición inicial de la nave
        jLabel1.add(jLabel2);

        timer = new Timer(10, (ActionEvent e) -> {
            Player1.movimientoPJPrincipal();
            Player1.rotarNave();
            Player1.centrarVistaenNave();
            actualizarDisparos();
        });

        enemyUpdateTimer = new Timer(10, (ActionEvent e) -> {
            generarEnemigos(Player1.getNivel());
            actualizarEnemigos();
            actualizarBosses();
        });

        statsUpdate = new Timer(10, (ActionEvent e) -> {
            actualizarHUB();
            debeEliminarPersonaje();
            if (actualizacionBackground) {
                cambiarBase();
                cambiarBackground();
                actualizacionBackground = false;
            }
            if (generarBoss) {
                generarBoss(Player1.getNivel());
                generarBoss = false;
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        enemiesKO.setDoubleBuffered(true);

        reproducirSonidos("src/Musica/musicaGamePlay.wav");

        jButton1.setFocusable(false);
    }

    public GameLogic(ArrayList<String> datosPartida) {
        initComponents();

        //Creacion de objeto de personaje principal
        Player1 = new personajePrincipal(datosPartida.get(1));
        Player1.playerName = datosPartida.get(0);

        //Frame de mejoras de personae
        frameMejoras = new mejorasPlayer();

        //Función para cargar datos
        cargarJuego(datosPartida);

        //Selector de dificultad
        selectorDificultad = Integer.parseInt(datosPartida.get(30));
        switch (selectorDificultad) {
            case 1:
                enemyGenerationInterval = 5000;
                maxEnemies = 10;
                levelDiff = 3;
                break;
            case 2:
                enemyGenerationInterval = 4000;
                maxEnemies = 15;
                levelDiff = 4;
                break;
            case 3:
                enemyGenerationInterval = 3000;
                maxEnemies = 20;
                levelDiff = 5;
                break;
        }

        //Datos cargados automaticos
        Player1.setDaño();
        Player1.setDefensa();
        Player1.setVelocidadAtaque();
        Player1.setVelocidadNave();
        Player1.setExperienciaSiguiente();
        nombre.setText(Player1.playerName);

        //Creacion de variable con savefile
        saveFile = Integer.parseInt(datosPartida.get(20));
        tipoNave = datosPartida.get(1);

        //Centrado de ventana en la pantalla
        setLocationRelativeTo(null);
        Player1.cargarNavePrincipal(datosPartida.get(1));
        //Cargamos la imagen del disparo
        bulletIcon = new ImageIcon(laserImagePath);

        //Base espacial
        baseEspacial = new javax.swing.JLabel();
        baseEspacial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Spacebase.png")));

        // Cambia la ubicación
        double posicionBaseWidth = Math.random() * (jLabel1.getWidth() - 500);//Math.random() * 3840;
        double posicionBaseHeight = Math.random() * (jLabel1.getHeight() - 500);//Math.random() * 2480;
        baseEspacial.setLocation((int) posicionBaseWidth, (int) posicionBaseHeight);
        baseEspacial.setSize(baseEspacial.getPreferredSize()); // Asegúrate de ajustar el tamaño si es necesario
        baseEspacialBounds = baseEspacial.getBounds();
        baseEspacialBounds.setLocation((int) posicionBaseWidth, (int) posicionBaseHeight);
        jLabel1.add(baseEspacial);

        xPos = 310;
        yPos = 345;
        jLabel2.setLocation(310, 345);  // Establecer la posición inicial de la nave
        jLabel1.add(jLabel2);

        timer = new Timer(10, (ActionEvent e) -> {
            if (upPressed || downPressed) {
                Player1.movimientoPJPrincipal();
            }
            if (leftPressed || rightPressed) {
                Player1.rotarNave();
            }
            Player1.centrarVistaenNave();
            actualizarDisparos();
        });

        enemyUpdateTimer = new Timer(10, (ActionEvent e) -> {
            generarEnemigos(Player1.getNivel());
            actualizarEnemigos();
            actualizarBosses();
        });

        statsUpdate = new Timer(10, (ActionEvent e) -> {
            actualizarHUB();
            debeEliminarPersonaje();
            if (actualizacionBackground) {
                cambiarBase();
                cambiarBackground();
                actualizacionBackground = false;
            }
            if (generarBoss) {
                generarBoss(Player1.getNivel());
                generarBoss = false;
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        enemiesKO.setDoubleBuffered(true);

        reproducirSonidos("src/Musica/musicaGamePlay.wav");

        jButton1.setFocusable(false);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        Pilot = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        barraVida = new javax.swing.JProgressBar();
        barraExperiencia = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        Ataque = new javax.swing.JLabel();
        velocidadAtaque = new javax.swing.JLabel();
        defensa = new javax.swing.JLabel();
        velocidadNave = new javax.swing.JLabel();
        nivel = new javax.swing.JLabel();
        nombre = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        enemiesKO = new javax.swing.JLabel();
        round = new javax.swing.JLabel();
        score = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        BackHud = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(885, 684));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        getContentPane().setLayout(null);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(1974, 1282, 0, 0);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Fondo_Espacio.jpg"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jLabel1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(200, 0, 700, 700);

        jPanel1.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(240, 240, 240));
        jLabel10.setText("Atack");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(40, 410, 32, 16);

        Pilot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/PilotHUD (1).png"))); // NOI18N
        Pilot.setText("jLabel6");
        jPanel1.add(Pilot);
        Pilot.setBounds(10, 60, 180, 200);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setText("LVL");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 380, 30, 20);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel6.setText("NAME");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(30, 270, 50, 16);
        jPanel1.add(barraVida);
        barraVida.setBounds(50, 340, 146, 20);
        jPanel1.add(barraExperiencia);
        barraExperiencia.setBounds(50, 310, 146, 20);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setText("EXP");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 310, 30, 16);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/hearth-removebg-preview (1).png"))); // NOI18N
        jLabel9.setText("jLabel9");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(0, 320, 50, 50);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(240, 240, 240));
        jLabel11.setText("SpeedAtack");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(40, 440, 66, 16);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(240, 240, 240));
        jLabel12.setText("Defense");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(40, 470, 46, 16);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(240, 240, 240));
        jLabel13.setText("Speed");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(40, 500, 34, 16);

        Ataque.setForeground(new java.awt.Color(242, 242, 242));
        jPanel1.add(Ataque);
        Ataque.setBounds(140, 410, 50, 20);

        velocidadAtaque.setForeground(new java.awt.Color(242, 242, 242));
        jPanel1.add(velocidadAtaque);
        velocidadAtaque.setBounds(140, 440, 50, 20);

        defensa.setForeground(new java.awt.Color(242, 242, 242));
        jPanel1.add(defensa);
        defensa.setBounds(140, 470, 50, 20);

        velocidadNave.setForeground(new java.awt.Color(242, 242, 242));
        jPanel1.add(velocidadNave);
        velocidadNave.setBounds(140, 500, 50, 20);

        nivel.setForeground(new java.awt.Color(242, 242, 242));
        nivel.setText("A");
        jPanel1.add(nivel);
        nivel.setBounds(60, 380, 90, 16);

        nombre.setForeground(new java.awt.Color(242, 242, 242));
        jPanel1.add(nombre);
        nombre.setBounds(80, 270, 50, 20);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(242, 242, 242));
        jLabel4.setText("Enemies killed");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 540, 80, 16);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(242, 242, 242));
        jLabel5.setText("Round");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 570, 40, 16);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(242, 242, 242));
        jLabel14.setText("Score");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(10, 600, 30, 16);

        enemiesKO.setForeground(new java.awt.Color(242, 242, 242));
        enemiesKO.setText("0");
        jPanel1.add(enemiesKO);
        enemiesKO.setBounds(120, 540, 70, 16);

        round.setForeground(new java.awt.Color(242, 242, 242));
        round.setText("0");
        jPanel1.add(round);
        round.setBounds(120, 570, 70, 16);

        score.setForeground(new java.awt.Color(242, 242, 242));
        score.setText("0");
        jPanel1.add(score);
        score.setBounds(70, 600, 110, 16);

        jButton1.setText("Save game");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(0, 0, 90, 22);

        BackHud.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/BackHud (1).png"))); // NOI18N
        BackHud.setOpaque(true);
        jPanel1.add(BackHud);
        BackHud.setBounds(0, 0, 200, 680);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 210, 680);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_W:
                upPressed = true;
                break;
        }
        if (!timer.isRunning()) {
            timer.start();
            statsUpdate.start();
        }
        if (!enemyUpdateTimer.isRunning()) {
            enemyUpdateTimer.start();
        }
        setFocusable(true);
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_W:
                upPressed = false;
                break;
        }
    }//GEN-LAST:event_formKeyReleased

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        double targetX = evt.getX();
        double targetY = evt.getY();

        Player1.personajeDisparo(targetX, targetY);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        guardarJuego();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Ataque;
    private javax.swing.JLabel BackHud;
    private javax.swing.JLabel Pilot;
    private javax.swing.JProgressBar barraExperiencia;
    private javax.swing.JProgressBar barraVida;
    private javax.swing.JLabel defensa;
    private javax.swing.JLabel enemiesKO;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nivel;
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel round;
    private javax.swing.JLabel score;
    private javax.swing.JLabel velocidadAtaque;
    private javax.swing.JLabel velocidadNave;
    // End of variables declaration//GEN-END:variables
}
