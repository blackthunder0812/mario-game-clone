package mario.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests verifying game Constants are correctly defined and sensible.
 */
class ConstantsTest {

    @Test
    @DisplayName("Window dimensions are positive")
    void windowDimensions() {
        assertTrue(Constants.WINDOW_WIDTH > 0);
        assertTrue(Constants.WINDOW_HEIGHT > 0);
    }

    @Test
    @DisplayName("FPS is a reasonable value (30-120)")
    void fpsReasonable() {
        assertTrue(Constants.FPS >= 30 && Constants.FPS <= 120);
    }

    @Test
    @DisplayName("Tile size is a positive power-related value")
    void tileSizePositive() {
        assertTrue(Constants.TILE_SIZE > 0);
    }

    @Test
    @DisplayName("Gravity is positive (pulls downward)")
    void gravityPositive() {
        assertTrue(Constants.GRAVITY > 0);
    }

    @Test
    @DisplayName("Jump force is negative (upward)")
    void jumpForceNegative() {
        assertTrue(Constants.JUMP_FORCE < 0);
    }

    @Test
    @DisplayName("Max fall speed is positive")
    void maxFallSpeedPositive() {
        assertTrue(Constants.MAX_FALL_SPEED > 0);
    }

    @Test
    @DisplayName("Move speed is positive")
    void moveSpeedPositive() {
        assertTrue(Constants.MOVE_SPEED > 0);
    }

    @Test
    @DisplayName("Enemy speed is positive")
    void enemySpeedPositive() {
        assertTrue(Constants.ENEMY_SPEED > 0);
    }

    @Test
    @DisplayName("Player dimensions are smaller than tile size * 2")
    void playerFitsInTiles() {
        assertTrue(Constants.PLAYER_WIDTH < Constants.TILE_SIZE * 2);
        assertTrue(Constants.PLAYER_HEIGHT < Constants.TILE_SIZE * 2);
    }

    @Test
    @DisplayName("Score values are positive")
    void scoresPositive() {
        assertTrue(Constants.COIN_SCORE > 0);
        assertTrue(Constants.ENEMY_SCORE > 0);
        assertTrue(Constants.FLAG_SCORE > 0);
    }

    @Test
    @DisplayName("Flag score is the highest scoring event")
    void flagScoreHighest() {
        assertTrue(Constants.FLAG_SCORE > Constants.ENEMY_SCORE);
        assertTrue(Constants.FLAG_SCORE > Constants.COIN_SCORE);
    }

    @Test
    @DisplayName("Starting lives is at least 1")
    void startingLivesPositive() {
        assertTrue(Constants.STARTING_LIVES >= 1);
    }
}
