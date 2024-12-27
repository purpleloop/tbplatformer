package tbplatformer.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import tbplatformer.Tile;
import tbplatformer.TileSet;

/** A panel where to choose tiles. */
public class TilesPanel extends JPanel {

    /** No selection. */
    public static final int NONE = -1;

    /** Serial tag. */
    private static final long serialVersionUID = -2153990238840151387L;

    /** Border width. */
    private static final int BORDER_WIDTH = 5;

    /** The tileset. */
    private TileSet tileSet;

    /** Panel width. */
    private int panelWidth;

    /** Panel height. */
    private int panelHeigth;

    /** The currently selected tile. */
    private int selectedTile = NONE;

    /** Bold stroke. */
    private BasicStroke boldStroke;

    /** Mouse handler. */
    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {

            int x = e.getX() - BORDER_WIDTH;
            int row = x / tileSet.getTileSize();

            selectedTile = row;

            repaint();
        }

    };

    /**
     * Constructor of the panel.
     * 
     * @param tileSet the tileset to use
     */
    public TilesPanel(TileSet tileSet) {
        this.tileSet = tileSet;

        int tileSize = tileSet.getTileSize();

        panelWidth = tileSet.getNumTilesAcross() * tileSize;
        panelHeigth = tileSize;
        setPreferredSize(
                new Dimension(panelWidth + 2 * BORDER_WIDTH, panelHeigth + 2 * BORDER_WIDTH));

        addMouseListener(mouseListener);

        boldStroke = new BasicStroke(2);

    }

    @Override
    public void paint(Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics;

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth() + 2 * BORDER_WIDTH, panelHeigth + 2 * BORDER_WIDTH);

        int numTilesAcross = tileSet.getNumTilesAcross();
        int tileSize = tileSet.getTileSize();

        int i = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < numTilesAcross; x++) {
                Tile tile = tileSet.getTileByIndex(y * numTilesAcross + x);
                if (tile != null) {
                    g.drawImage(tile.getImage(), i * tileSize + BORDER_WIDTH, BORDER_WIDTH, null);
                }
                i++;

            }
        }

        if (selectedTile != NONE) {
            g.setColor(Color.RED);
            g.setStroke(boldStroke);
            g.drawRect(selectedTile * tileSize + BORDER_WIDTH, 0 + BORDER_WIDTH, tileSize,
                    tileSize);
        }

    }

    /**
     * @return the selected tile.
     */
    public int getSelection() {
        return selectedTile;

    }

}
