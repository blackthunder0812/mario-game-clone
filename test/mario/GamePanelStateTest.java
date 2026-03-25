package mario;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for GamePanel state machine transitions and initialization.
 * These tests verify the game state enum and its role in the UI flow
 * without requiring a display (headless-safe).
 */
class GamePanelStateTest {

    @Test
    @DisplayName("GameState enum has all expected states")
    void allStatesExist() {
        GamePanel.GameState[] states = GamePanel.GameState.values();
        assertEquals(4, states.length);
        assertNotNull(GamePanel.GameState.TITLE);
        assertNotNull(GamePanel.GameState.PLAYING);
        assertNotNull(GamePanel.GameState.GAME_OVER);
        assertNotNull(GamePanel.GameState.WIN);
    }

    @Test
    @DisplayName("GameState valueOf round-trips correctly")
    void valueOfRoundTrip() {
        assertEquals(GamePanel.GameState.TITLE, GamePanel.GameState.valueOf("TITLE"));
        assertEquals(GamePanel.GameState.PLAYING, GamePanel.GameState.valueOf("PLAYING"));
        assertEquals(GamePanel.GameState.GAME_OVER, GamePanel.GameState.valueOf("GAME_OVER"));
        assertEquals(GamePanel.GameState.WIN, GamePanel.GameState.valueOf("WIN"));
    }

    @Test
    @DisplayName("GameState ordinal order: TITLE, PLAYING, GAME_OVER, WIN")
    void ordinalOrder() {
        assertTrue(GamePanel.GameState.TITLE.ordinal() < GamePanel.GameState.PLAYING.ordinal());
        assertTrue(GamePanel.GameState.PLAYING.ordinal() < GamePanel.GameState.GAME_OVER.ordinal());
        assertTrue(GamePanel.GameState.GAME_OVER.ordinal() < GamePanel.GameState.WIN.ordinal());
    }
}
