package tbplatformer;

import java.awt.Graphics2D;
import java.io.IOException;

import tbplatformer.animation.CharacterAnimation;
import tbplatformer.animation.CharacterAnimation.Posture;

/** Represents the character. */
public class Player {

    /** Abscissa in the level. */
    private double x;

    /** Ordinate in the level. */
    private double y;

    /** Horizontal speed. */
    private double dx;

    /** Vertical speed. */
    private double dy;

    private int width;
    private int height;

    private boolean movingLeft;
    private boolean movingRight;
    private boolean jumping;
    private boolean falling;

    private double moveSpeed;
    private double maxSpeed;
    private double maxFallingSpeed;
    private double stopSpeed;
    private double jumpStart;
    private double gravity;

    private TileMap tileMap;

    /** Collision flags - top left. */
    private boolean collideTopLeft;

    /** Collision flags - Top right. */
    private boolean collideTopRight;

    /** Collision flags - bottom left. */
    private boolean collideBottomLeft;

    /** Collision flags - bottom right. */
    private boolean collideBottomRight;

    private CharacterAnimation characterAnimation;
    private boolean facingLeft;

    public Player(TileMap tileMap) {
        this.tileMap = tileMap;

        width = 22;
        height = 22;

        moveSpeed = 0.6;
        maxSpeed = 4.2;
        maxFallingSpeed = 12;
        stopSpeed = 0.3;
        jumpStart = -11.0;

        gravity = 0.64;

        try {
            characterAnimation = new CharacterAnimation(width, height);

        } catch (IOException e) {
            throw new GameException("Error while loading player sprites", e);
        }

        facingLeft = false;
    }

    /**
     * Sets the character location.
     * 
     * @param x Abscissa in the level.
     * @param y Ordinate in the level.
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Makes the character move to left.
     * 
     * @param left true if character is required to move left, false otherwise
     */
    public void setMovingLeft(boolean left) {
        this.movingLeft = left;
    }

    /**
     * Makes the character move to right.
     * 
     * @param right true if character is required to move right, false otherwise
     */
    public void setMovingRight(boolean right) {
        this.movingRight = right;
    }

    /**
     * Makes the character jump.
     * 
     * @param jumping true if character is required to jump, false otherwise
     */
    public void setJumping(boolean jumping) {
        if (!falling) {
            this.jumping = jumping;
        }
    }

    public void update() {

        updatePosition();

        // Check collisions
        int currCol = tileMap.getColTile((int) x);
        int currRow = tileMap.getRowTile((int) y);

        double toX = x + dx;
        double toY = y + dy;
        double tempx = x;
        double tempy = y;

        calculateCorners(x, toY);

        if (dy < 0) {

            // Jumping
            if (collideTopLeft || collideTopRight) {
                // Hit an upper tile
                dy = 0;
                tempy = currRow * tileMap.getTileSize() + height / 2.0;
            } else {
                tempy += dy;
            }
        }
        if (dy > 0) {

            // Falling
            if (collideBottomLeft || collideBottomRight) {
                // Hit the ground
                dy = 0;
                falling = false;
                tempy = (currRow + 1) * tileMap.getTileSize() - height / 2.0;
            } else {
                tempy += dy;
            }
        }

        calculateCorners(toX, y);

        if (dx < 0) {

            // Going left
            if (collideTopLeft || collideBottomLeft) {
                // Hit left tile
                dx = 0;
                tempx = currCol * tileMap.getTileSize() + width / 2.0;
            } else {
                tempx += dx;
            }
        }
        if (dx > 0) {

            // Going right
            if (collideTopRight || collideBottomRight) {
                // Hit right tile
                dx = 0;
                tempx = (currCol + 1) * tileMap.getTileSize() - width / 2.0;
            } else {
                tempx += dx;
            }
        }

        if (!falling) {
            calculateCorners(x, y + 1);
            if (!collideBottomLeft && !collideBottomRight) {
                falling = true;
            }
        }

        x = tempx;
        y = tempy;

        moveMap();

        updateAnimation();

    }

    /** Moves the map view. */
    private void moveMap() {
        tileMap.setX((int) (GamePanel.GameView.WIDTH / 2.0 - x));
        tileMap.setY((int) (GamePanel.GameView.HEIGHT / 2.0 - y));
    }

    /** Udpdate the character animation. */
    private void updateAnimation() {
        if (movingLeft || movingRight) {
            characterAnimation.setPosture(Posture.WALKING, 100);
        } else {
            characterAnimation.setPosture(Posture.IDLE, -1);
        }
        if (dy < 0) {
            characterAnimation.setPosture(Posture.JUMPING, -1);
        }
        if (dy > 0) {
            characterAnimation.setPosture(Posture.FALLING, -1);
        }
        characterAnimation.update();
        if (dx < 0) {
            facingLeft = true;
        }
        if (dx > 0) {
            facingLeft = false;
        }
    }

    /** Determine next position of the character. */
    private void updatePosition() {

        if (movingLeft) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (movingRight) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            // Stop moving
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        if (jumping) {
            dy = jumpStart;
            falling = true;
            jumping = false;
        }

        if (falling) {
            dy += gravity;
            if (dy > maxFallingSpeed) {
                dy = maxFallingSpeed;
            }
        } else {
            dy = 0;
        }
    }

    private void calculateCorners(double x, double y) {
        int leftTileCol = tileMap.getColTile((int) (x - width / 2.0));
        int rightTileCol = tileMap.getColTile((int) (x + width / 2.0) - 1);
        int topTileRow = tileMap.getRowTile((int) (y - height / 2.0));
        int bottomTileRow = tileMap.getRowTile((int) (y + height / 2.0) - 1);

        collideTopLeft = tileMap.isBlocked(topTileRow, leftTileCol);
        collideTopRight = tileMap.isBlocked(topTileRow, rightTileCol);
        collideBottomLeft = tileMap.isBlocked(bottomTileRow, leftTileCol);
        collideBottomRight = tileMap.isBlocked(bottomTileRow, rightTileCol);

    }

    public void draw(Graphics2D g) {

        int tx = tileMap.getX();
        int ty = tileMap.getY();

        if (facingLeft) {
            g.drawImage(characterAnimation.getImage(), (int) (tx + x - width / 2.0),
                    (int) (ty + y - height / 2.0), null);
        } else {
            g.drawImage(characterAnimation.getImage(), (int) (tx + x - width / 2.0 + width),
                    (int) (ty + y - height / 2.0), -width, height, null);
        }

    }
}
