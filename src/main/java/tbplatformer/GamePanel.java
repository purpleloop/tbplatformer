package tbplatformer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import tbplatformer.model.LevelMap;
import tbplatformer.storage.ResourceFileStorage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    /** Represents the graphical view where to do the game environment rendering */
    public class GameView {
    
        /** View width. */
        public static final int WIDTH = 400;
    
        /** View height. */
        public static final int HEIGHT = 400;
    
        /** Are stats visible ? */
        private boolean statsVisible = false;
    
        /** The render image associated to the view. */
        private BufferedImage renderImage;
    
        /** The render graphics used to draw the view. */
        private Graphics2D renderGraphics;
    
        GameView() {
    
            renderImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            renderGraphics = renderImage.createGraphics();
        }
    
        private void render() {
    
            // Draw background
            renderGraphics.setColor(Color.BLACK);
            renderGraphics.fillRect(0, 0, WIDTH, HEIGHT);
    
            // Draw tiles and character
            gameEnv.getTileMap().draw(renderGraphics);
            gameEnv.getPlayer().draw(renderGraphics);
    
            if (statsVisible) {
                displayStats();
            }
        }
    
        private void displayStats() {
            renderGraphics.setColor(Color.PINK);
            renderGraphics.drawString("FPS: " + averageFPS, 10, 10);
        }
    
        public Image getImage() {
            return renderImage;
        }
    
    }

    /** Serial tag. */
    private static final long serialVersionUID = -8481917967658589859L;
    
    /** The targeted rendering rate in frames par second. */
    public static final int TARGET_FPS = 30;

    /** Targeted rendering time in seconds. */
    public static final long TARGET_TIME = 1000 / TARGET_FPS;
    
    private Thread thread;
    private boolean running;

    private double averageFPS;

    private GameEnv gameEnv;

    private GameView gameView;

    public GamePanel() {
        super();
        setPreferredSize(new Dimension(GameView.WIDTH, GameView.HEIGHT));

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

            gameEnv.update();
            gameView.render();
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

    private void draw() {
        Graphics2D g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(gameView.getImage(), 0, 0, null);
        g2.dispose();
    }

    private void init() {
        running = true;

        gameView = new GameView();

        ResourceFileStorage resourceFileStorage = new ResourceFileStorage();
        LevelMap map = resourceFileStorage.getMapByName("testmap2");
        TileSet loadTiles = resourceFileStorage.getTileSetByName("tileset");
        TileMap tileMap = new TileMap(map, loadTiles);
        gameEnv = new GameEnv(tileMap);

    }

    @Override
    public void keyPressed(KeyEvent e) {

        Player player = gameEnv.getPlayer();

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

        Player player = gameEnv.getPlayer();

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
