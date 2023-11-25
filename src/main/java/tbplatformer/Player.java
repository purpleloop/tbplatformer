package tbplatformer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {

    private double x;
    private double y;
    private double dx;
    private double dy;

    private int width;
    private int height;

    private boolean left;
    private boolean right;
    private boolean jumping;
    private boolean falling;

    private double moveSpeed;
    private double maxSpeed;
    private double maxFallingSpeed;
    private double stopSpeed;
    private double jumpStart;
    private double gravity;

    private TileMap tileMap;

    private boolean topLeft;
    private boolean topRight;
    private boolean bottomLeft;
    private boolean bottomRight;

    private Animation animation;
    private BufferedImage[] idleSprite;
    private BufferedImage[] walkingSprite;
    private BufferedImage[] jumpingSprite;
    private BufferedImage[] fallingSprite;
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

        idleSprite = new BufferedImage[1];
        jumpingSprite = new BufferedImage[1];
        fallingSprite = new BufferedImage[1];
        walkingSprite = new BufferedImage[6];

        try {
            idleSprite[0] = ImageIO.read(new File("graphics/player/kirbyidle.gif"));
            jumpingSprite[0] = ImageIO.read(new File("graphics/player/kirbyjump.gif"));
            fallingSprite[0] = ImageIO.read(new File("graphics/player/kirbyfall.gif"));
            BufferedImage image = ImageIO.read(new File("graphics/player/kirbywalk.gif"));

            for (int i = 0; i < walkingSprite.length; i++) {
                walkingSprite[i] = image.getSubimage(i * width + i, 0, width, height);
            }

        } catch (IOException e) {
            throw new GameException("Error while loading player sprites", e);
        }

        animation = new Animation();
        facingLeft = false;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJumping(boolean jumping) {
        if (!falling) {
            this.jumping = jumping;
        }
    }

    public void update() {

        // detetermine next position

        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right) {
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
            if (topLeft || topRight) {
                // Hit an upper tile
                dy = 0;
                tempy = currRow * tileMap.getTileSize() + height / 2;
            } else {
                tempy += dy;
            }
        }
        if (dy > 0) {

            // Falling
            if (bottomLeft || bottomRight) {
                // Hit the ground
                dy = 0;
                falling = false;
                tempy = (currRow + 1) * tileMap.getTileSize() - height / 2;
            } else {
                tempy += dy;
            }
        }

        calculateCorners(toX, y);

        if (dx < 0) {

            // Going left
            if (topLeft || bottomLeft) {
                // Hit left tile
                dx = 0;
                tempx = currCol * tileMap.getTileSize() + width / 2;
            } else {
                tempx += dx;
            }
        }
        if (dx > 0) {

            // Going right
            if (topRight || bottomRight) {
                // Hit right tile
                dx = 0;
                tempx = (currCol + 1) * tileMap.getTileSize() - width / 2;
            } else {
                tempx += dx;
            }
        }

        if (!falling) {
            calculateCorners(x, y + 1);
            if (!bottomLeft && !bottomRight) {
                falling = true;
            }
        }

        x = tempx;
        y = tempy;

        // move the map
        tileMap.setX((int) (GamePanel.WIDTH / 2 - x));
        tileMap.setY((int) (GamePanel.HEIGHT / 2 - y));

        // Sprite animation
        if (left || right) {
            animation.setFrames(walkingSprite);
            animation.setDelay(100);
        } else {
            animation.setFrames(idleSprite);
            animation.setDelay(-1);
        }
        if (dy < 0) {
            animation.setFrames(jumpingSprite);
            animation.setDelay(-1);
        }
        if (dy > 0) {
            animation.setFrames(fallingSprite);
            animation.setDelay(-1);
        }
        animation.update();
        if (dx < 0) {
            facingLeft = true;
        }
        if (dx > 0) {
            facingLeft = false;
        }

    }

    private void calculateCorners(double x, double y) {
        int leftTile = tileMap.getColTile((int) (x - width / 2));
        int rightTile = tileMap.getColTile((int) (x + width / 2) - 1);
        int topTile = tileMap.getRowTile((int) (y - height / 2));
        int bottomTile = tileMap.getRowTile((int) (y + height / 2) - 1);

        topLeft = tileMap.isBlocked(topTile, leftTile);
        topRight = tileMap.isBlocked(topTile, rightTile);
        bottomLeft = tileMap.isBlocked(bottomTile, leftTile);
        bottomRight = tileMap.isBlocked(bottomTile, rightTile);

    }

    public void draw(Graphics2D g) {

        int tx = tileMap.getX();
        int ty = tileMap.getY();

        if (facingLeft) {
            g.drawImage(animation.getImage(), (int) (tx + x - width / 2),
                    (int) (ty + y - height / 2), null);
        } else {
            g.drawImage(animation.getImage(), (int) (tx + x - width / 2 + width),
                    (int) (ty + y - height / 2), -width, height, null);
        }

    }
}
