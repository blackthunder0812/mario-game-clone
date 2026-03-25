package mario.util;

/**
 * Game-wide constants for tuning physics, display, and gameplay.
 */
public final class Constants {

    private Constants() { }

    // Display
    public static final int WINDOW_WIDTH  = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int FPS           = 60;

    // Tile / grid
    public static final int TILE_SIZE = 32;

    // Player physics
    public static final double GRAVITY       = 0.6;
    public static final double MAX_FALL_SPEED = 10.0;
    public static final double JUMP_FORCE    = -11.0;
    public static final double MOVE_SPEED    = 4.0;

    // Enemy
    public static final double ENEMY_SPEED = 1.2;

    // Player dimensions (in pixels)
    public static final int PLAYER_WIDTH  = 28;
    public static final int PLAYER_HEIGHT = 44;

    // Scoring
    public static final int COIN_SCORE  = 100;
    public static final int ENEMY_SCORE = 200;
    public static final int FLAG_SCORE  = 1000;

    // Player
    public static final int STARTING_LIVES = 3;
}
