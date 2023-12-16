package tbplatformer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/** A tile based platformer application. */
public class TbPlatformerApp {

    private TbPlatformerApp() {
        // Prevents constructor use
    }

    /**
     * Main entry point.
     * 
     * @param args command line args
     */
    public static void main(String[] args) {

        JFrame window = new JFrame("Tile based platformer");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        window.setContentPane(new GamePanel());
        window.pack();
        window.setVisible(true);
    }

}
