package mario.entities;

import mario.util.Constants;
import org.junit.jupiter.api.*;
import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Player movement, physics, scoring, lives, and hitbox behaviour.
 */
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(100, 200);
    }

    // ── Initial State ──────────────────────────────────────────────────

    @Test
    @DisplayName("Player starts at given position")
    void initialPosition() {
        assertEquals(100, player.getX());
        assertEquals(200, player.getY());
    }

    @Test
    @DisplayName("Player starts alive with default lives")
    void initialLivesAndState() {
        assertTrue(player.isAlive());
        assertEquals(Constants.STARTING_LIVES, player.getLives());
    }

    @Test
    @DisplayName("Player starts with zero score and coins")
    void initialScoreAndCoins() {
        assertEquals(0, player.getScore());
        assertEquals(0, player.getCoins());
    }

    @Test
    @DisplayName("Player faces right initially")
    void initialFacingDirection() {
        assertTrue(player.isFacingRight());
    }

    @Test
    @DisplayName("Player starts not on ground and level not complete")
    void initialGroundAndLevelState() {
        assertFalse(player.isOnGround());
        assertFalse(player.isLevelComplete());
    }

    @Test
    @DisplayName("Player dimensions match constants")
    void playerDimensions() {
        assertEquals(Constants.PLAYER_WIDTH, player.getWidth());
        assertEquals(Constants.PLAYER_HEIGHT, player.getHeight());
    }

    // ── Horizontal Movement ────────────────────────────────────────────

    @Test
    @DisplayName("Moving right increases X position")
    void moveRight() {
        double startX = player.getX();
        player.update(false, true, false);
        assertEquals(startX + Constants.MOVE_SPEED, player.getX(), 0.01);
        assertTrue(player.isFacingRight());
    }

    @Test
    @DisplayName("Moving left decreases X position")
    void moveLeft() {
        double startX = player.getX();
        player.update(true, false, false);
        assertEquals(startX - Constants.MOVE_SPEED, player.getX(), 0.01);
        assertFalse(player.isFacingRight());
    }

    @Test
    @DisplayName("No input means no horizontal movement")
    void noHorizontalMovement() {
        double startX = player.getX();
        double startY = player.getY();
        player.update(false, false, false);
        // X unchanged (gravity still affects Y)
        assertEquals(startX, player.getX(), 0.01);
    }

    @Test
    @DisplayName("Player cannot move left past x=0")
    void cannotMoveLeftOfZero() {
        Player edgePlayer = new Player(1, 200);
        edgePlayer.update(true, false, false);
        assertTrue(edgePlayer.getX() >= 0);
    }

    // ── Jumping & Gravity ──────────────────────────────────────────────

    @Test
    @DisplayName("Player can jump when on ground")
    void jumpWhenOnGround() {
        player.setOnGround(true);
        player.update(false, false, true);
        // Jump force is applied then gravity acts in the same frame
        double expected = Constants.JUMP_FORCE + Constants.GRAVITY;
        assertEquals(expected, player.getVelY(), 0.01);
        assertFalse(player.isOnGround());
    }

    @Test
    @DisplayName("Player cannot jump when in air")
    void cannotJumpInAir() {
        player.setOnGround(false);
        player.setVelY(0);
        player.update(false, false, true);
        // Velocity should only be affected by gravity, not jump
        assertNotEquals(Constants.JUMP_FORCE, player.getVelY(), 0.01);
    }

    @Test
    @DisplayName("Gravity increases downward velocity each frame")
    void gravityApplied() {
        player.setVelY(0);
        player.update(false, false, false);
        assertTrue(player.getVelY() > 0);
    }

    @Test
    @DisplayName("Fall speed is capped at MAX_FALL_SPEED")
    void maxFallSpeed() {
        player.setVelY(Constants.MAX_FALL_SPEED + 10);
        player.update(false, false, false);
        // After update, velY should be capped (gravity adds to it, but it was set above max)
        assertTrue(player.getVelY() <= Constants.MAX_FALL_SPEED + Constants.GRAVITY);
    }

    @Test
    @DisplayName("Bounce sets upward velocity (for enemy stomp)")
    void bounce() {
        player.bounce();
        double expected = Constants.JUMP_FORCE * 0.6;
        assertEquals(expected, player.getVelY(), 0.01);
    }

    // ── Lives & Death ──────────────────────────────────────────────────

    @Test
    @DisplayName("die() reduces lives by one")
    void dieLosesLife() {
        int startLives = player.getLives();
        player.die();
        assertEquals(startLives - 1, player.getLives());
    }

    @Test
    @DisplayName("Player respawns at spawn point after dying with lives remaining")
    void respawnAfterDeath() {
        player.setX(500);
        player.setY(300);
        player.die();
        // Should respawn at original position (100, 200)
        assertEquals(100, player.getX(), 0.01);
        assertEquals(200, player.getY(), 0.01);
    }

    @Test
    @DisplayName("Player becomes invincible after respawn")
    void invincibleAfterRespawn() {
        player.die();
        assertTrue(player.isInvincible());
    }

    @Test
    @DisplayName("Player is dead after losing all lives")
    void deadAfterAllLivesLost() {
        for (int i = 0; i < Constants.STARTING_LIVES; i++) {
            player.die();
        }
        assertFalse(player.isAlive());
    }

    @Test
    @DisplayName("takeDamage has no effect during invincibility")
    void invincibilityProtectsDamage() {
        player.die(); // lose 1 life, become invincible
        int livesAfterDeath = player.getLives();
        player.takeDamage(); // should be ignored
        assertEquals(livesAfterDeath, player.getLives());
    }

    @Test
    @DisplayName("Dead player does not update")
    void deadPlayerNoUpdate() {
        for (int i = 0; i < Constants.STARTING_LIVES; i++) {
            player.die();
        }
        double x = player.getX();
        double y = player.getY();
        player.update(true, false, false);
        assertEquals(x, player.getX(), 0.01);
        assertEquals(y, player.getY(), 0.01);
    }

    // ── Scoring ────────────────────────────────────────────────────────

    @Test
    @DisplayName("addScore increments score correctly")
    void addScore() {
        player.addScore(500);
        assertEquals(500, player.getScore());
        player.addScore(300);
        assertEquals(800, player.getScore());
    }

    @Test
    @DisplayName("addCoin increments coin count and adds COIN_SCORE to score")
    void addCoin() {
        player.addCoin();
        assertEquals(1, player.getCoins());
        assertEquals(Constants.COIN_SCORE, player.getScore());
    }

    @Test
    @DisplayName("Collecting multiple coins accumulates correctly")
    void multipleCoins() {
        player.addCoin();
        player.addCoin();
        player.addCoin();
        assertEquals(3, player.getCoins());
        assertEquals(3 * Constants.COIN_SCORE, player.getScore());
    }

    // ── Hitboxes ───────────────────────────────────────────────────────

    @Test
    @DisplayName("getBounds returns full body hitbox at current position")
    void fullBounds() {
        Rectangle bounds = player.getBounds();
        assertEquals((int) player.getX(), bounds.x);
        assertEquals((int) player.getY(), bounds.y);
        assertEquals(player.getWidth(), bounds.width);
        assertEquals(player.getHeight(), bounds.height);
    }

    @Test
    @DisplayName("getFeetBounds is smaller than full bounds")
    void feetBoundsSmaller() {
        Rectangle full = player.getBounds();
        Rectangle feet = player.getFeetBounds();
        assertTrue(feet.width < full.width);
        assertTrue(feet.height < full.height);
    }

    @Test
    @DisplayName("Feet hitbox is at the bottom of the player")
    void feetBoundsAtBottom() {
        Rectangle feet = player.getFeetBounds();
        int expectedBottomY = (int) player.getY() + player.getHeight();
        assertEquals(expectedBottomY, feet.y + feet.height);
    }

    // ── Setters ────────────────────────────────────────────────────────

    @Test
    @DisplayName("setOnGround / setLevelComplete work correctly")
    void settersWork() {
        player.setOnGround(true);
        assertTrue(player.isOnGround());
        player.setLevelComplete(true);
        assertTrue(player.isLevelComplete());
    }
}
