package tbplatformer.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import tbplatformer.TileMap;
import tbplatformer.TileSet;
import tbplatformer.model.LevelMap;
import tbplatformer.storage.ResourceFileStorage;

/** The editor main frame. */
public class EditorFrame extends JFrame {

    /** Serial tag. */
    private static final long serialVersionUID = -6811264538663146209L;

    /** Default width of a new map. */
    public static final int DEFAULT_WIDTH = 30;

    /** Default height of a new map. */
    public static final int DEFAULT_HEIGHT = 20;

    /** The panel for editing the map. */
    private MapPanel mapPanel;

    /** The tile set. */
    private TileSet tileSet;

    /** Dialog used to load and save files. */
    private JFileChooser fileChooser;

    /** Resource storage using the filesystem. */
    private ResourceFileStorage resourceStorage;

    /** Open action. */
    private Action openAction = new AbstractAction("Open") {

        /** Serial tag. */
        private static final long serialVersionUID = -6526174050620903110L;

        @Override
        public void actionPerformed(ActionEvent e) {
            openMap();
        }
    };

    /** Save action. */
    private Action saveAction = new AbstractAction("Save") {

        /** Serial tag. */
        private static final long serialVersionUID = 6543072360413937523L;

        @Override
        public void actionPerformed(ActionEvent e) {
            saveMap();
        }
    };

    /** Creates the editor. */
    public EditorFrame() {

        super("Platformer map editor");

        fileChooser = new JFileChooser(".");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        ResourceFileStorage storage = new ResourceFileStorage();

        tileSet = storage.getTileSetByName("tileset");

        TilesPanel tilesPanel = new TilesPanel(tileSet);
        mainPanel.add(tilesPanel, BorderLayout.NORTH);

        this.mapPanel = new MapPanel(tileSet, tilesPanel);

        LevelMap levelMap = getDefaultLevelMap();

        TileMap tileMap = new TileMap(levelMap, tileSet);

        mapPanel.setTileMap(tileMap);

        mainPanel.add(mapPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem openMenuItem = new JMenuItem(openAction);
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem(saveAction);
        fileMenu.add(saveMenuItem);

        resourceStorage = new ResourceFileStorage();

        pack();
    }

    private LevelMap getDefaultLevelMap() {
        LevelMap levelMap = new LevelMap(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        for (int y = 0; y < DEFAULT_HEIGHT; y++) {
            for (int x = 0; x < DEFAULT_WIDTH; x++) {
                levelMap.setTile(2, 2, 0);
            }

        }

        return levelMap;
    }

    protected void openMap() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            LevelMap levelMap = resourceStorage.readMapFile(file);

            TileMap tileMap = new TileMap(levelMap, tileSet);

            mapPanel.setTileMap(tileMap);

            repaint();

        }

    }

    protected void saveMap() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            TileMap tileMap = mapPanel.getTileMap();
            LevelMap levelMap = tileMap.getLevelMap();

            resourceStorage.saveMapFile(levelMap, file);
        }

    }

    public static void main(String[] args) {
        EditorFrame editorFrame = new EditorFrame();
        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        editorFrame.setVisible(true);
    }

}
