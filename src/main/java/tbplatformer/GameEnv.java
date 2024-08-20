package tbplatformer;

/** Models the game environment. */
class GameEnv {

    /** Gravity. */
    public static final double GRAVITY = 0.64;

    /** Initial abscissa of the player. */
    private static final int INITIAL_X = 50;

    /** Initial ordinate of the player. */
    private static final int INITIAL_Y = 50;

    /** The tile map where the character evolves. */
    private TileMap tileMap;

    /** The player (played character). */
    private Player player;

    GameEnv(TileMap tileMap) {

        this.tileMap = tileMap;

        this.player = new Player(tileMap);
        this.player.setLocation(INITIAL_X, INITIAL_Y);
    }

    public Player getPlayer() {
        return player;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void update() {
        tileMap.update();
        player.update();
    }
}