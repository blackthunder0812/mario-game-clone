package mario.entities;

import mario.util.Constants;

import java.awt.*;

/**
 * The player character (Mario). Handles movement, jumping, and physics.
 */
public class Player {

    private double x, y;
    private double velX, velY;
    private final int width  = Constants.PLAYER_WIDTH;
    private final int height = Constants.PLAYER_HEIGHT;
    private boolean onGround;
    private boolean facingRight = true;
    private boolean alive = true;
    private int lives = Constants.STARTING_LIVES;
    private int score;
    private int coins;
    private boolean levelComplete;
    private double spawnX, spawnY;

    // Invincibility frames after taking damage
    private int invincibleTimer;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.spawnX = x;
        this.spawnY = y;
    }

    public void update(boolean leftPressed, boolean rightPressed, boolean jumpPressed) {
        if (!alive) return;

        if (invincibleTimer > 0) invincibleTimer--;

        // Horizontal movement
        if (leftPressed) {
            velX = -Constants.MOVE_SPEED;
            facingRight = false;
        } else if (rightPressed) {
            velX = Constants.MOVE_SPEED;
            facingRight = true;
        } else {
            velX = 0;
        }

        // Jumping
        if (jumpPressed && onGround) {
            velY = Constants.JUMP_FORCE;
            onGround = false;
        }

        // Gravity
        velY += Constants.GRAVITY;
        if (velY > Constants.MAX_FALL_SPEED) {
            velY = Constants.MAX_FALL_SPEED;
        }

        x += velX;
        y += velY;

        // Prevent going left of level start
        if (x < 0) x = 0;
    }

    public void draw(Graphics2D g, int camX) {
        if (!alive) return;

        // Blink during invincibility
        if (invincibleTimer > 0 && (invincibleTimer / 3) % 2 == 0) return;

        mario.util.SpriteRenderer.drawPlayer(g,
                (int) x - camX, (int) y, width, height, facingRight);
    }

    /** Called when Mario falls into a pit or gets hit. */
    public void die() {
        lives--;
        if (lives <= 0) {
            alive = false;
        } else {
            respawn();
        }
    }

    public void takeDamage() {
        if (invincibleTimer > 0) return;
        die();
    }

    public void respawn() {
        x = spawnX;
        y = spawnY;
        velX = 0;
        velY = 0;
        invincibleTimer = 90; // 1.5 seconds of invincibility
    }

    public void bounce() {
        velY = Constants.JUMP_FORCE * 0.6;
    }

    public void addScore(int points) { score += points; }
    public void addCoin() { coins++; addScore(Constants.COIN_SCORE); }

    // Getters
    public double getX()      { return x; }
    public double getY()      { return y; }
    public double getVelY()   { return velY; }
    public int getWidth()     { return width; }
    public int getHeight()    { return height; }
    public boolean isOnGround()  { return onGround; }
    public boolean isAlive()     { return alive; }
    public int getLives()        { return lives; }
    public int getScore()        { return score; }
    public int getCoins()        { return coins; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isLevelComplete() { return levelComplete; }
    public boolean isInvincible()    { return invincibleTimer > 0; }

    // Setters
    public void setX(double x)      { this.x = x; }
    public void setY(double y)      { this.y = y; }
    public void setVelY(double v)   { this.velY = v; }
    public void setOnGround(boolean g) { this.onGround = g; }
    public void setLevelComplete(boolean c) { this.levelComplete = c; }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /** Smaller hitbox for feet — used to detect stomping enemies. */
    public Rectangle getFeetBounds() {
        return new Rectangle((int) x + 4, (int) y + height - 8, width - 8, 8);
    }
}
