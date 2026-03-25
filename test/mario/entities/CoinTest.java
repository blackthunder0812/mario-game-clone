package mario.entities;

import org.junit.jupiter.api.*;
import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Coin collectible behaviour.
 */
class CoinTest {

    private Coin coin;

    @BeforeEach
    void setUp() {
        coin = new Coin(150, 250);
    }

    @Test
    @DisplayName("Coin spawns at given position")
    void initialPosition() {
        assertEquals(150, coin.getX());
        assertEquals(250, coin.getY());
    }

    @Test
    @DisplayName("Coin starts uncollected")
    void initiallyUncollected() {
        assertFalse(coin.isCollected());
    }

    @Test
    @DisplayName("collect() marks coin as collected")
    void collectCoin() {
        coin.collect();
        assertTrue(coin.isCollected());
    }

    @Test
    @DisplayName("update() advances animation frame (does not throw)")
    void updateDoesNotThrow() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) coin.update();
        });
    }

    @Test
    @DisplayName("Collected coin stops animating (no errors)")
    void collectedCoinUpdateSafe() {
        coin.collect();
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) coin.update();
        });
    }

    @Test
    @DisplayName("getBounds returns correct hitbox")
    void boundsCorrect() {
        Rectangle bounds = coin.getBounds();
        assertEquals((int) coin.getX(), bounds.x);
        assertEquals((int) coin.getY(), bounds.y);
        assertTrue(bounds.width > 0);
        assertTrue(bounds.height > 0);
    }
}
