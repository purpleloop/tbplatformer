package tbplatformer;

import java.awt.Graphics2D;

import tbplatformer.model.LevelMap;

public class TileMap {

    private int x;
    private int y;

    /** Description of the level (in particular, in terms of tiles). */
    private LevelMap map;

    /** The available tiles to compose levels. */
    private TileSet tileSet;

    private int minx;
    private int miny;
    private int maxx = 0;
    private int maxy = 0;

    /**
     * @param map Description of the level
     * @param tileSet The available tiles to compose levels.
     */
    public TileMap(LevelMap map, TileSet tileSet) {

        this.map = map;
        this.tileSet = tileSet;

        minx = GamePanel.WIDTH - map.getWidth() * tileSet.getTileSize();
        miny = GamePanel.HEIGHT - map.getHeight() * tileSet.getTileSize();

    }

    public LevelMap getLevelMap() {
        return this.map;
    }

    public int getX() {
        return x;
    }

    public void setX(int nx) {
        this.x = nx;
        if (x < minx) {
            x = minx;
        }
        if (x > maxx) {
            x = maxx;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int ny) {
        this.y = ny;
        if (y < miny) {
            y = miny;
        }
        if (y > maxy) {
            y = maxy;
        }
    }

    public boolean isBlocked(int row, int col) {
        return tileSet.getTileByIndex(map.getTile(row, col)).isBlocked();
    }

    public int getColTile(int x) {
        return x / tileSet.getTileSize();
    }

    public int getRowTile(int y) {
        return y / tileSet.getTileSize();
    }

    public int getTileSize() {
        return tileSet.getTileSize();
    }

    public void update() {

    }

    public void draw(Graphics2D g) {

        int tileSize = tileSet.getTileSize();

        for (int row = 0; row < map.getHeight(); row++) {
            for (int col = 0; col < map.getWidth(); col++) {
                int tileIndex = map.getTile(row, col);
                g.drawImage(tileSet.getTileByIndex(tileIndex).getImage(), x + col * tileSize,
                        y + row * tileSize, null);
            }
        }
    }

    public LevelMap getMap() {
        return map;
    }

}
