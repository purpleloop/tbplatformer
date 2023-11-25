package tbplatformer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import tbplatformer.model.LevelMap;
import tbplatformer.storage.ResourceFileStorage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    /** Serial tag. */
    private static final long serialVersionUID = -8481917967658589859L;

    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    private Thread thread;
    private boolean running;

    public static final int FPS = 30;
    public static final long TARGET_TIME = 1000 / FPS;

    private BufferedImage image;
    private Graphics2D g;
    private double averageFPS;

    private TileMap tileMap;
    private Player player;

    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setFocusable(true);
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }

        addKeyListener(this);
    }

    @Override
    public void run() {

        init();

        long startTime;
        long urdTime;
        long waitTime;
        int totalTime = 0;

        int frameCount = 0;
        int maxFrameCount = 30;

        // GAME LOOP
        while (running) {

            startTime = System.nanoTime();

            update();
            render();
            draw();

            urdTime = (System.nanoTime() - startTime) / 1000000;
            waitTime = TARGET_TIME - urdTime;

            if (waitTime <= 0) {

                waitTime = 5;
                System.out.println("gameLoop targetTime : " + TARGET_TIME);
                System.out.println("gameLoop URDTimeMillis : " + urdTime);
            }
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new GameException("Game thread has been interrupted", e);
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == maxFrameCount) {
                averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000.0);
                frameCount = 0;
                totalTime = 0;
            }
        }

        draw();

    }

    private void init() {
        running = true;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();

        ResourceFileStorage resourceFileStorage = new ResourceFileStorage();
        LevelMap map = resourceFileStorage.getMapByName("testmap2");
        tileMap = new TileMap(map);

        tileMap.loadTiles("graphics/tileset.gif");
        player = new Player(tileMap);
        player.setX(50);
        player.setY(50);
    }

    private void update() {
        tileMap.update();
        player.update();
    }

    private void render() {

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        displayStats();

        tileMap.draw(g);
        player.draw(g);
    }

    private void displayStats() {
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + averageFPS, 10, 10);
    }

    private void draw() {
        Graphics2D g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            player.setLeft(true);
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            player.setRight(true);
        }
        if (keyCode == KeyEvent.VK_W) {
            player.setJumping(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            player.setLeft(false);
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            player.setRight(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}