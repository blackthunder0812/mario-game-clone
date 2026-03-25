package mario;

import mario.entities.Coin;
import mario.entities.Enemy;
import mario.entities.Player;
import mario.util.Constants;
import mario.util.SpriteRenderer;
import mario.world.Block;
import mario.world.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

/**
 * Main game panel — owns the game loop, input handling, physics,
 * collision detection, and rendering.
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {

    public enum GameState { TITLE, PLAYING, GAME_OVER, WIN }

    private GameState state = GameState.TITLE;

    private Level level;
    private Player player;
    private Camera camera;
    private int animFrame;

    // Input state
    private boolean leftPressed, rightPressed, jumpPressed;
    private boolean enterPressed;

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
    }

    private void initGame() {
        level  = new Level();
        player = new Player(3 * Constants.TILE_SIZE, 12 * Constants.TILE_SIZE);
        camera = new Camera(level.getPixelWidth());
    }

    /** Start the game thread. */
    public void start() {
        Thread thread = new Thread(this, "GameLoop");
        thread.setDaemon(true);
        thread.start();
    }

    // ── Game Loop ──────────────────────────────────────────────────────

    @Override
    public void run() {
        long nsPerFrame = 1_000_000_000L / Constants.FPS;
        long lastTime = System.nanoTime();
        while (true) {
            long now = System.nanoTime();
            if (now - lastTime >= nsPerFrame) {
                lastTime = now;
                update();
                repaint();
            } else {
                try { Thread.sleep(1); } catch (InterruptedException ignored) {}
            }
        }
    }

    // ── Update ─────────────────────────────────────────────────────────

    private void update() {
        animFrame++;

        switch (state) {
            case TITLE -> {
                if (enterPressed) {
                    state = GameState.PLAYING;
                    initGame();
                    enterPressed = false;
                }
            }
            case PLAYING -> updatePlaying();
            case GAME_OVER, WIN -> {
                if (enterPressed) {
                    state = GameState.TITLE;
                    enterPressed = false;
                }
            }
        }
    }

    private void updatePlaying() {
        player.update(leftPressed, rightPressed, jumpPressed);

        // Tile collision for player
        resolvePlayerTileCollisions();

        // Fell into a pit
        if (player.getY() > level.getPixelHeight()) {
            player.die();
            if (!player.isAlive()) {
                state = GameState.GAME_OVER;
            }
        }

        // Update enemies
        for (Iterator<Enemy> it = level.getEnemies().iterator(); it.hasNext(); ) {
            Enemy e = it.next();
            e.update();
            if (e.shouldRemove()) { it.remove(); continue; }
            if (e.isAlive()) {
                resolveEnemyTileCollisions(e);
                checkPlayerEnemyCollision(e);
            }
        }

        // Update coins
        for (Coin c : level.getCoins()) {
            c.update();
            if (!c.isCollected() && player.getBounds().intersects(c.getBounds())) {
                c.collect();
                player.addCoin();
            }
        }

        // Flag check
        if (level.getFlagX() >= 0) {
            Rectangle flagBounds = new Rectangle(level.getFlagX(), level.getFlagY(),
                    Constants.TILE_SIZE, Constants.TILE_SIZE * 4);
            if (player.getBounds().intersects(flagBounds)) {
                player.addScore(Constants.FLAG_SCORE);
                player.setLevelComplete(true);
                state = GameState.WIN;
            }
        }

        camera.update(player.getX());
    }

    // ── Collision Detection ────────────────────────────────────────────

    private void resolvePlayerTileCollisions() {
        int ts = Constants.TILE_SIZE;

        // Determine which tiles the player overlaps
        int leftTile   = (int) (player.getX() / ts);
        int rightTile  = (int) ((player.getX() + player.getWidth() - 1) / ts);
        int topTile    = (int) (player.getY() / ts);
        int bottomTile = (int) ((player.getY() + player.getHeight() - 1) / ts);

        player.setOnGround(false);

        for (int gx = leftTile; gx <= rightTile; gx++) {
            for (int gy = topTile; gy <= bottomTile; gy++) {
                Block block = level.getBlock(gx, gy);
                if (block == null || !block.isSolid()) continue;

                Rectangle blockRect = new Rectangle(gx * ts, gy * ts, ts, ts);
                Rectangle playerRect = player.getBounds();
                if (!playerRect.intersects(blockRect)) continue;

                // Calculate overlap on each axis
                double overlapX = Math.min(playerRect.getMaxX() - blockRect.getMinX(),
                                           blockRect.getMaxX() - playerRect.getMinX());
                double overlapY = Math.min(playerRect.getMaxY() - blockRect.getMinY(),
                                           blockRect.getMaxY() - playerRect.getMinY());

                if (overlapX < overlapY) {
                    // Horizontal resolution
                    if (player.getX() + player.getWidth() / 2.0 < gx * ts + ts / 2.0) {
                        player.setX(gx * ts - player.getWidth());
                    } else {
                        player.setX(gx * ts + ts);
                    }
                } else {
                    // Vertical resolution
                    if (player.getY() + player.getHeight() / 2.0 < gy * ts + ts / 2.0) {
                        // Landing on top
                        player.setY(gy * ts - player.getHeight());
                        player.setVelY(0);
                        player.setOnGround(true);
                    } else {
                        // Hitting from below
                        player.setY(gy * ts + ts);
                        player.setVelY(1);
                        block.hit();
                    }
                }
            }
        }
    }

    private void resolveEnemyTileCollisions(Enemy e) {
        int ts = Constants.TILE_SIZE;
        int gx = (int) ((e.getX() + e.getSize() / 2.0) / ts);
        int gy = (int) ((e.getY() + e.getSize()) / ts);

        // Check ground beneath
        Block below = level.getBlock(gx, gy);
        if (below == null || !below.isSolid()) {
            e.reverseDirection();
        }

        // Check wall ahead
        int ahead = (e.getVelX() < 0)
                ? (int) (e.getX() / ts)
                : (int) ((e.getX() + e.getSize()) / ts);
        int midY = (int) ((e.getY() + e.getSize() / 2.0) / ts);
        Block wall = level.getBlock(ahead, midY);
        if (wall != null && wall.isSolid()) {
            e.reverseDirection();
        }
    }

    private void checkPlayerEnemyCollision(Enemy e) {
        if (!player.getBounds().intersects(e.getBounds())) return;

        // Stomping: player's feet are above enemy's mid-point and falling
        if (player.getFeetBounds().intersects(e.getBounds()) && player.getVelY() > 0) {
            e.stomp();
            player.bounce();
            player.addScore(Constants.ENEMY_SCORE);
        } else {
            player.takeDamage();
            if (!player.isAlive()) {
                state = GameState.GAME_OVER;
            }
        }
    }

    // ── Rendering ──────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (state) {
            case TITLE     -> drawTitleScreen(g);
            case PLAYING   -> drawGame(g);
            case GAME_OVER -> { drawGame(g); drawOverlay(g, "GAME OVER", "Press ENTER to return to title"); }
            case WIN       -> { drawGame(g); drawOverlay(g, "LEVEL COMPLETE!", "Score: " + player.getScore() + "  —  Press ENTER"); }
        }
    }

    private void drawGame(Graphics2D g) {
        int camX = camera.getX();

        // Background
        SpriteRenderer.drawBackground(g, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Decorative hills and clouds (parallax)
        drawBackgroundDecor(g, camX);

        // Blocks
        drawBlocks(g, camX);

        // Flag
        if (level.getFlagX() >= 0) {
            SpriteRenderer.drawFlag(g, level.getFlagX() - camX, level.getFlagY(), Constants.TILE_SIZE * 4);
        }

        // Coins
        for (Coin c : level.getCoins()) c.draw(g, camX);

        // Enemies
        for (Enemy e : level.getEnemies()) e.draw(g, camX);

        // Player
        player.draw(g, camX);

        // HUD
        drawHUD(g);
    }

    private void drawBlocks(Graphics2D g, int camX) {
        int ts = Constants.TILE_SIZE;
        int startCol = camX / ts;
        int endCol = (camX + Constants.WINDOW_WIDTH) / ts + 1;

        for (int gx = startCol; gx <= endCol && gx < level.getWidthInTiles(); gx++) {
            for (int gy = 0; gy < level.getHeightInTiles(); gy++) {
                Block b = level.getBlock(gx, gy);
                if (b == null || b.getType() == Block.Type.EMPTY) continue;

                int px = gx * ts - camX;
                int py = gy * ts;

                switch (b.getType()) {
                    case GROUND   -> SpriteRenderer.drawGround(g, px, py, ts);
                    case BRICK    -> SpriteRenderer.drawBrick(g, px, py, ts);
                    case QUESTION -> {
                        if (b.isHit()) {
                            SpriteRenderer.drawBrick(g, px, py, ts);
                        } else {
                            SpriteRenderer.drawQuestionBlock(g, px, py, ts, animFrame);
                        }
                    }
                    default -> {}
                }
            }
        }
    }

    private void drawBackgroundDecor(Graphics2D g, int camX) {
        // Simple parallax clouds
        for (int i = 0; i < 8; i++) {
            int cx = i * 350 - (camX / 3) % 2800;
            SpriteRenderer.drawCloud(g, cx, 40 + (i % 3) * 30);
        }
        // Hills
        for (int i = 0; i < 6; i++) {
            int hx = i * 500 - (camX / 2) % 3000;
            SpriteRenderer.drawHill(g, hx, Constants.WINDOW_HEIGHT - 120, 200, 80);
        }
    }

    private void drawHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));

        g.drawString("MARIO", 20, 28);
        g.drawString(String.format("%06d", player.getScore()), 20, 46);

        String coinText = "COINS x" + player.getCoins();
        g.drawString(coinText, 200, 28);

        g.drawString("LIVES x" + player.getLives(), 400, 28);

        g.drawString("WORLD 1-1", 600, 28);
    }

    private void drawTitleScreen(Graphics2D g) {
        SpriteRenderer.drawBackground(g, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        for (int i = 0; i < 6; i++) {
            SpriteRenderer.drawCloud(g, 50 + i * 140, 60 + (i % 3) * 25);
        }

        g.setColor(new Color(0, 0, 0, 120));
        g.fillRoundRect(150, 120, 500, 360, 20, 20);

        g.setColor(new Color(255, 50, 50));
        g.setFont(new Font("Arial", Font.BOLD, 52));
        drawCenteredString(g, "SUPER MARIO", Constants.WINDOW_WIDTH / 2, 210);

        g.setColor(new Color(255, 215, 0));
        g.setFont(new Font("Arial", Font.BOLD, 28));
        drawCenteredString(g, "JAVA CLONE", Constants.WINDOW_WIDTH / 2, 255);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        drawCenteredString(g, "Arrow Keys / WASD  —  Move & Jump", Constants.WINDOW_WIDTH / 2, 330);
        drawCenteredString(g, "SPACE  —  Jump", Constants.WINDOW_WIDTH / 2, 358);

        g.setColor(new Color(255, 255, 100));
        g.setFont(new Font("Arial", Font.BOLD, 22));
        int blink = (animFrame / 30) % 2;
        if (blink == 0) {
            drawCenteredString(g, "Press ENTER to Start", Constants.WINDOW_WIDTH / 2, 430);
        }
    }

    private void drawOverlay(Graphics2D g, String title, String subtitle) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        drawCenteredString(g, title, Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2 - 20);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        drawCenteredString(g, subtitle, Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2 + 30);
    }

    private void drawCenteredString(Graphics2D g, String text, int cx, int cy) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, cx - fm.stringWidth(text) / 2, cy);
    }

    // ── Input ──────────────────────────────────────────────────────────

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A  -> leftPressed = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_SPACE -> jumpPressed = true;
            case KeyEvent.VK_ENTER -> enterPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A  -> leftPressed = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_SPACE -> jumpPressed = false;
            case KeyEvent.VK_ENTER -> enterPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}
