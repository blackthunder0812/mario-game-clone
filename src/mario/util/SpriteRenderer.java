package mario.util;

import java.awt.*;

/**
 * Draws game entities as simple colored shapes (no sprite images needed).
 */
public final class SpriteRenderer {

    private SpriteRenderer() { }

    /** Draw Mario as a simple character shape. */
    public static void drawPlayer(Graphics2D g, int x, int y, int w, int h, boolean facingRight) {
        // Body (red)
        g.setColor(new Color(220, 30, 30));
        g.fillRect(x + 4, y + 14, w - 8, h - 14);

        // Head (skin tone)
        g.setColor(new Color(255, 200, 150));
        g.fillOval(x + 4, y, w - 8, 18);

        // Hat (red)
        g.setColor(new Color(220, 30, 30));
        g.fillRect(x + (facingRight ? 2 : 6), y, w - 8, 8);

        // Eyes
        g.setColor(Color.BLACK);
        int eyeX = facingRight ? x + w - 12 : x + 8;
        g.fillRect(eyeX, y + 8, 3, 3);

        // Shoes (brown)
        g.setColor(new Color(139, 69, 19));
        g.fillRect(x + 2, y + h - 6, (w - 4) / 2, 6);
        g.fillRect(x + w / 2, y + h - 6, (w - 4) / 2, 6);
    }

    /** Draw a Goomba enemy. */
    public static void drawGoomba(Graphics2D g, int x, int y, int size) {
        // Body (brown)
        g.setColor(new Color(165, 80, 40));
        g.fillOval(x, y + 4, size, size - 4);

        // Head (darker brown)
        g.setColor(new Color(130, 60, 30));
        g.fillArc(x - 2, y, size + 4, size / 2 + 4, 0, 180);

        // Eyes
        g.setColor(Color.WHITE);
        g.fillOval(x + 6, y + 8, 7, 7);
        g.fillOval(x + size - 13, y + 8, 7, 7);
        g.setColor(Color.BLACK);
        g.fillOval(x + 9, y + 10, 3, 3);
        g.fillOval(x + size - 10, y + 10, 3, 3);

        // Feet
        g.setColor(new Color(80, 40, 20));
        g.fillOval(x, y + size - 8, size / 2, 8);
        g.fillOval(x + size / 2, y + size - 8, size / 2, 8);
    }

    /** Draw a coin. */
    public static void drawCoin(Graphics2D g, int x, int y, int size, int frame) {
        g.setColor(new Color(255, 215, 0));
        int stretch = (int) (size * 0.4 * Math.abs(Math.sin(frame * 0.1)));
        g.fillOval(x + (size - stretch) / 2, y + 2, Math.max(stretch, 4), size - 4);
        g.setColor(new Color(255, 235, 100));
        g.fillOval(x + (size - stretch) / 2 + 2, y + 6, Math.max(stretch - 4, 2), size - 12);
    }

    /** Draw a ground block. */
    public static void drawGround(Graphics2D g, int x, int y, int size) {
        g.setColor(new Color(139, 90, 43));
        g.fillRect(x, y, size, size);
        g.setColor(new Color(100, 65, 30));
        g.drawRect(x, y, size, size);
        g.drawLine(x, y + size / 2, x + size, y + size / 2);
        g.drawLine(x + size / 2, y, x + size / 2, y + size);
    }

    /** Draw a brick block. */
    public static void drawBrick(Graphics2D g, int x, int y, int size) {
        g.setColor(new Color(200, 100, 50));
        g.fillRect(x, y, size, size);
        g.setColor(new Color(160, 75, 35));
        // Brick pattern
        for (int row = 0; row < 2; row++) {
            int by = y + row * (size / 2);
            int offset = (row % 2 == 0) ? 0 : size / 4;
            for (int col = -1; col < 3; col++) {
                int bx = x + offset + col * (size / 2);
                g.drawRect(bx, by, size / 2, size / 2);
            }
        }
    }

    /** Draw a question block. */
    public static void drawQuestionBlock(Graphics2D g, int x, int y, int size, int frame) {
        float pulse = 0.8f + 0.2f * (float) Math.sin(frame * 0.08);
        g.setColor(new Color((int) (255 * pulse), (int) (200 * pulse), 0));
        g.fillRect(x, y, size, size);
        g.setColor(new Color(180, 130, 0));
        g.drawRect(x, y, size, size);
        // Question mark
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, size / 2));
        FontMetrics fm = g.getFontMetrics();
        String q = "?";
        g.drawString(q, x + (size - fm.stringWidth(q)) / 2, y + size / 2 + fm.getAscent() / 2 - 2);
    }

    /** Draw the flag pole. */
    public static void drawFlag(Graphics2D g, int x, int y, int height) {
        // Pole
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x + 14, y, 4, height);

        // Ball on top
        g.setColor(Color.GREEN);
        g.fillOval(x + 10, y - 6, 12, 12);

        // Flag
        g.setColor(Color.GREEN);
        int[] xPts = {x + 18, x + 50, x + 18};
        int[] yPts = {y + 6, y + 20, y + 34};
        g.fillPolygon(xPts, yPts, 3);
    }

    /** Draw a pipe. */
    public static void drawPipe(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(0, 170, 0));
        g.fillRect(x, y, w, h);
        g.setColor(new Color(0, 200, 0));
        g.fillRect(x - 4, y, w + 8, 16);
        g.setColor(new Color(0, 140, 0));
        g.drawRect(x, y, w, h);
        g.drawRect(x - 4, y, w + 8, 16);
    }

    /** Draw sky / background gradient. */
    public static void drawBackground(Graphics2D g, int width, int height) {
        GradientPaint sky = new GradientPaint(0, 0, new Color(107, 140, 255),
                0, height, new Color(170, 200, 255));
        g.setPaint(sky);
        g.fillRect(0, 0, width, height);
    }

    /** Draw a cloud. */
    public static void drawCloud(Graphics2D g, int x, int y) {
        g.setColor(new Color(255, 255, 255, 200));
        g.fillOval(x, y + 10, 40, 25);
        g.fillOval(x + 15, y, 35, 30);
        g.fillOval(x + 35, y + 8, 40, 28);
    }

    /** Draw a hill in the background. */
    public static void drawHill(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(90, 180, 60));
        int[] xPts = {x, x + w / 2, x + w};
        int[] yPts = {y + h, y, y + h};
        g.fillPolygon(xPts, yPts, 3);
    }
}
