package mario.entities;

import mario.util.Constants;
import org.junit.jupiter.api.*;
import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Enemy (Goomba) patrol behaviour, stomping, and removal.
 */
class EnemyTest {

    private Enemy enemy;

    @BeforeEach
    void setUp() {
        enemy = new Enemy(200, 300);
    }

    // ── Initial State ──────────────────────────────────────────────────

    @Test
    @DisplayName("Enemy spawns at the given position")
    void initialPosition() {
        assertEquals(200, enemy.getX());
        assertEquals(300, enemy.getY());
    }

    @Test
    @DisplayName("Enemy starts alive")
    void initialAliveState() {
        assertTrue(enemy.isAlive());
    }

    @Test
    @DisplayName("Enemy initially moves left")
    void initialDirection() {
        assertTrue(enemy.getVelX() < 0);
    }

    @Test
    @DisplayName("Enemy size matches TILE_SIZE")
    void enemySize() {
        assertEquals(Constants.TILE_SIZE, enemy.getSize());
    }

    // ── Movement ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Enemy moves left by ENEMY_SPEED each update")
    void movesLeft() {
        double startX = enemy.getX();
        enemy.update();
        assertEquals(startX - Constants.ENEMY_SPEED, enemy.getX(), 0.01);
    }

    @Test
    @DisplayName("reverseDirection flips movement direction")
    void reverseDirection() {
        double originalVel = enemy.getVelX();
        enemy.reverseDirection();
        assertEquals(-originalVel, enemy.getVelX(), 0.01);
    }

    @Test
    @DisplayName("Enemy moves right after reversing")
    void movesRightAfterReverse() {
        enemy.reverseDirection();
        double startX = enemy.getX();
        enemy.update();
        assertEquals(startX + Constants.ENEMY_SPEED, enemy.getX(), 0.01);
    }

    @Test
    @DisplayName("Double reverse restores original direction")
    void doubleReverseRestoresDirection() {
        double originalVel = enemy.getVelX();
        enemy.reverseDirection();
        enemy.reverseDirection();
        assertEquals(originalVel, enemy.getVelX(), 0.01);
    }

    // ── Stomping & Death ───────────────────────────────────────────────

    @Test
    @DisplayName("stomp() kills the enemy")
    void stompKillsEnemy() {
        enemy.stomp();
        assertFalse(enemy.isAlive());
    }

    @Test
    @DisplayName("Dead enemy should not move")
    void deadEnemyDoesNotMove() {
        enemy.stomp();
        double x = enemy.getX();
        enemy.update();
        assertEquals(x, enemy.getX(), 0.01);
    }

    @Test
    @DisplayName("Enemy is not immediately removable after stomp (death animation)")
    void notImmediatelyRemovableAfterStomp() {
        enemy.stomp();
        assertFalse(enemy.shouldRemove());
    }

    @Test
    @DisplayName("Enemy becomes removable after death timer expires")
    void removableAfterDeathTimer() {
        enemy.stomp();
        // Tick through the 20-frame death animation
        for (int i = 0; i < 20; i++) {
            assertFalse(enemy.shouldRemove());
            enemy.update();
        }
        assertTrue(enemy.shouldRemove());
    }

    @Test
    @DisplayName("Alive enemy is never marked for removal")
    void aliveEnemyNotRemovable() {
        for (int i = 0; i < 100; i++) {
            enemy.update();
            assertFalse(enemy.shouldRemove());
        }
    }

    // ── Hitbox ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("getBounds returns correct hitbox")
    void boundsCorrect() {
        Rectangle bounds = enemy.getBounds();
        assertEquals((int) enemy.getX(), bounds.x);
        assertEquals((int) enemy.getY(), bounds.y);
        assertEquals(enemy.getSize(), bounds.width);
        assertEquals(enemy.getSize(), bounds.height);
    }

    @Test
    @DisplayName("Bounds update after movement")
    void boundsUpdateAfterMovement() {
        enemy.update();
        Rectangle bounds = enemy.getBounds();
        assertEquals((int) enemy.getX(), bounds.x);
    }

    // ── Setters ────────────────────────────────────────────────────────

    @Test
    @DisplayName("setX / setY update position")
    void settersWork() {
        enemy.setX(999);
        enemy.setY(888);
        assertEquals(999, enemy.getX());
        assertEquals(888, enemy.getY());
    }
}
