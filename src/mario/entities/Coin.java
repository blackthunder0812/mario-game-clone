package mario.entities;

import mario.util.Constants;

import java.awt.*;

/**
 * A collectible coin that floats and spins.
 */
public class Coin {

    private final double x, y;
    private final int size = 20;
    private boolean collected;
    private int animFrame;

    public Coin(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (!collected) {
            animFrame++;
        }
    }

    public void collect() {
        collected = true;
    }

    public void draw(Graphics2D g, int camX) {
        if (collected) return;
        mario.util.SpriteRenderer.drawCoin(g, (int) x - camX, (int) y, size, animFrame);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }

    public boolean isCollected() { return collected; }
    public double getX() { return x; }
    public double getY() { return y; }
}
