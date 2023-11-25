package tbplatformer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileMap {

    private int x;
    private int y;

    private int tileSize;
    private int[][] map;
    private int mapWidth;
    private int mapHeight;

    private BufferedImage tileSet;
    private Tile[][] tiles;

    private int minx;
    private int miny;
    private int maxx = 0;
    private int maxy = 0;

    public TileMap(String s, int tileSize) {
        this.tileSize = tileSize;

        try (BufferedReader br = new BufferedReader(new FileReader(s));) {

            mapWidth = Integer.parseInt(br.readLine());

            mapHeight = Integer.parseInt(br.readLine());

            map = new int[mapHeight][mapWidth];

            minx = GamePanel.WIDTH - mapWidth * tileSize;
            miny = GamePanel.HEIGHT - mapHeight * tileSize;

            String delimiters = "\\s+";

            for (int row = 0; row < mapHeight; row++) {
                String line = br.readLine();
                String[] tokens = line.split(delimiters);

                for (int col = 0; col < mapWidth; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }

            }

        } catch (IOException e) {
            throw new GameException("Error while reading the map", e);
        }

    }

    public void loadTiles(String s) {

        try {
            tileSet = ImageIO.read(new File(s));
            int numTilesAcross = (tileSet.getWidth() + 1) / (tileSize + 1);
            tiles = new Tile[2][numTilesAcross];

            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileSet.getSubimage(col * tileSize + col, 0, tileSize, tileSize);
                tiles[0][col] = new Tile(subimage, false);
                subimage = tileSet.getSubimage(col * tileSize + col, tileSize + 1, tileSize,
                        tileSize);
                tiles[1][col] = new Tile(subimage, true);

            }

        } catch (IOException e) {
            throw new GameException("Error while reading the tileset image", e);
        }

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
        int rc = map[row][col];
        int r = rc / tiles[0].length;
        int c = rc % tiles[0].length;
        return tiles[r][c].isBlocked();
    }

    public int getColTile(int x) {
        return x / tileSize;
    }

    public int getRowTile(int y) {
        return y / tileSize;
    }

    public int getTile(int row, int col) {
        return map[row][col];
    }

    public int getTileSize() {
        return tileSize;
    }

    public void update() {

    }

    public void draw(Graphics2D g) {
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                int rc = map[row][col];

                int r = rc / tiles[0].length;
                int c = rc % tiles[0].length;

                g.drawImage(tiles[r][c].getImage(), x + col * tileSize, y + row * tileSize, null);

            }

        }
    }
}
