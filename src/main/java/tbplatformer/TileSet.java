package tbplatformer;

import java.awt.image.BufferedImage;

/** Models a tileset. */
public class TileSet {

    /** Tile set size. */
    private static final int TILE_SIZE = 32;

    /** Number of tiles per line in the image. */
    private static final int NUM_TILES_ACROSS = 13;

    /** Available tiles to build a level map. */
    private Tile[][] tiles;

    /**
     * Creates a tileSet from a given image.
     * 
     * @param tileSetImage the source image
     */
    public TileSet(BufferedImage tileSetImage) {

        tiles = new Tile[2][NUM_TILES_ACROSS];

        for (int col = 0; col < NUM_TILES_ACROSS; col++) {

            // Get non blocking tiles images
            createTile(tileSetImage, col, 0, false);

        }

        // Get blocking tiles images (floor)
        createTile(tileSetImage, 0, 1, true);
        createTile(tileSetImage, 1, 1, true);

        // Door
        createTile(tileSetImage, 2, 1, false);

    }

    /** Creates a tile from a tileSet image.
     * @param tileSetImage the tile set image
     * @param col column
     * @param row row
     * @param blocking is the tile blocking ?
     */
    private void createTile(BufferedImage tileSetImage, int col, int row, boolean blocking) {
        BufferedImage subimage = tileSetImage.getSubimage(col * TILE_SIZE + col,
                row * (TILE_SIZE + 1), TILE_SIZE, TILE_SIZE);
        tiles[row][col] = new Tile(subimage, blocking);
    }

    /** @return the tile size */
    public int getTileSize() {
        return TILE_SIZE;
    }

    /**
     * Get the tile for a given tile index.
     * 
     * @param tileIndex the tile index
     * @return the requested tile
     */
    public Tile getTileByIndex(int tileIndex) {
        int spriteRow = tileIndex / NUM_TILES_ACROSS;
        int spriteColumn = tileIndex % NUM_TILES_ACROSS;

        return tiles[spriteRow][spriteColumn];
    }

    public int getNumTilesAcross() {
        return NUM_TILES_ACROSS;
    }

}
