package tbplatformer.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import tbplatformer.TileMap;
import tbplatformer.TileSet;
import tbplatformer.model.LevelMap;

public class MapPanel extends JPanel {

    /** Serial tag. */
    private static final long serialVersionUID = 2594535800078055137L;

    /** The edited tilemap. */
    private TileMap tileMap;

    /** Width of the panel. */
    private int panelWidth;

    /** Height of the panel. */
    private int panelHeigth;

    /** Mouse handler. */
    private MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            setTileIn(x, y);
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            setTileIn(x, y);
        }

    };

    /** The tiles panel. */
    private TilesPanel tilesPanel;

    /** The tile set. */
    private TileSet tileSet;

    /**
     * Constructor of the panel.
     * 
     * @param tileSet the tile set to use
     * @param tilesPanel the associated tilesPanel
     */
    public MapPanel(TileSet tileSet, TilesPanel tilesPanel) {

        int tileSize = tileSet.getTileSize();

        this.tileSet = tileSet;

        this.tilesPanel = tilesPanel;
        panelWidth = EditorFrame.DEFAULT_WIDTH * tileSize;
        panelHeigth = EditorFrame.DEFAULT_HEIGHT * tileSize;
        setPreferredSize(new Dimension(panelWidth, panelHeigth));

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    protected void setTileIn(int x, int y) {
        int selection = tilesPanel.getSelection();

        if (selection != TilesPanel.NONE) {

            int tileSize = tileMap.getTileSize();
            int col = x / tileSize;
            int row = y / tileSize;

            LevelMap map = tileMap.getMap();

            if (map.isValid(row, col)) {

                System.out.println("Set tile in " + row + ", " + col);

                map.setTile(row, col, selection);
                repaint();
            }

        }

    }

    @Override
    public void paint(Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics;

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panelWidth, panelHeigth);

        tileMap.draw(g);

        int tileSize = tileSet.getTileSize();

        for (int row = 0; row < tileMap.getMap().getHeight(); row++) {
            for (int col = 0; col < tileMap.getMap().getWidth(); col++) {

                g.setColor(Color.gray);
                g.drawRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }

    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public TileMap getTileMap() {
        return this.tileMap;
    }

}
