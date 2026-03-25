package mario.entities;

import mario.util.Constants;

import java.awt.*;

/**
 * A Goomba-style enemy that walks back and forth.
 */
public class Enemy {

    private double x, y;
    private double velX;
    private final int size = Constants.TILE_SIZE;
    private boolean alive = true;
    private int deathTimer;

    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
        this.velX = -Constants.ENEMY_SPEED;
    }

    public void update() {
        if (!alive) {
            deathTimer--;
            return;
        }
        x += velX;
    }

    /** Reverse direction when hitting a wall or edge. */
    public void reverseDirection() {
        velX = -velX;
    }

    /** Called when stomped by the player. */
    public void stomp() {
        alive = false;
        deathTimer = 20; // show squish animation briefly
    }

    public boolean shouldRemove() {
        return !alive && deathTimer <= 0;
    }

    public void draw(Graphics2D g, int camX) {
        int drawX = (int) x - camX;
        int drawY = (int) y;
        if (!alive) {
            // Squished
            g.setColor(new Color(165, 80, 40));
            g.fillOval(drawX, drawY + size - 8, size, 8);
        } else {
            mario.util.SpriteRenderer.drawGoomba(g, drawX, drawY, size);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }

    // Getters
    public double getX()    { return x; }
    public double getY()    { return y; }
    public int getSize()    { return size; }
    public boolean isAlive() { return alive; }
    public double getVelX() { return velX; }

    // Setters
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
