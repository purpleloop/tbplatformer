package tbplatformer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Game {

    public static void main(String[] args) {

        JFrame window = new JFrame("Tile based platformer");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        window.setContentPane(new GamePanel());
        window.pack();
        window.setVisible(true);
    }

}