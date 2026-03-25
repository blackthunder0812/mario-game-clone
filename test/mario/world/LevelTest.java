package mario.world;

import mario.entities.Coin;
import mario.entities.Enemy;
import mario.util.Constants;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Level loading, block grid, entity spawning, and flag placement.
 */
class LevelTest {

    private Level level;

    @BeforeEach
    void setUp() {
        level = new Level();
    }

    // ── Dimensions ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Level has positive tile dimensions")
    void positiveDimensions() {
        assertTrue(level.getWidthInTiles() > 0);
        assertTrue(level.getHeightInTiles() > 0);
    }

    @Test
    @DisplayName("Pixel dimensions are tile dimensions * TILE_SIZE")
    void pixelDimensions() {
        assertEquals(level.getWidthInTiles() * Constants.TILE_SIZE, level.getPixelWidth());
        assertEquals(level.getHeightInTiles() * Constants.TILE_SIZE, level.getPixelHeight());
    }

    @Test
    @DisplayName("Level has 16 rows (from the MAP)")
    void levelHeight() {
        assertEquals(16, level.getHeightInTiles());
    }

    // ── Block Grid ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Bottom rows are solid ground blocks")
    void bottomRowsAreGround() {
        // Row 14 and 15 (0-indexed) should have ground blocks
        Block block = level.getBlock(0, 14);
        assertNotNull(block);
        assertEquals(Block.Type.GROUND, block.getType());
        assertTrue(block.isSolid());
    }

    @Test
    @DisplayName("Top rows are mostly empty (sky)")
    void topRowsAreEmpty() {
        Block block = level.getBlock(0, 0);
        assertNotNull(block);
        assertEquals(Block.Type.EMPTY, block.getType());
        assertFalse(block.isSolid());
    }

    @Test
    @DisplayName("Out-of-bounds getBlock returns null")
    void outOfBoundsReturnsNull() {
        assertNull(level.getBlock(-1, 0));
        assertNull(level.getBlock(0, -1));
        assertNull(level.getBlock(level.getWidthInTiles(), 0));
        assertNull(level.getBlock(0, level.getHeightInTiles()));
    }

    @Test
    @DisplayName("Level contains brick blocks")
    void containsBricks() {
        boolean hasBrick = false;
        for (int x = 0; x < level.getWidthInTiles(); x++) {
            for (int y = 0; y < level.getHeightInTiles(); y++) {
                Block b = level.getBlock(x, y);
                if (b != null && b.getType() == Block.Type.BRICK) {
                    hasBrick = true;
                    break;
                }
            }
            if (hasBrick) break;
        }
        assertTrue(hasBrick, "Level should contain at least one BRICK block");
    }

    @Test
    @DisplayName("Level contains question blocks")
    void containsQuestionBlocks() {
        boolean hasQuestion = false;
        for (int x = 0; x < level.getWidthInTiles(); x++) {
            for (int y = 0; y < level.getHeightInTiles(); y++) {
                Block b = level.getBlock(x, y);
                if (b != null && b.getType() == Block.Type.QUESTION) {
                    hasQuestion = true;
                    break;
                }
            }
            if (hasQuestion) break;
        }
        assertTrue(hasQuestion, "Level should contain at least one QUESTION block");
    }

    // ── Entity Spawning ────────────────────────────────────────────────

    @Test
    @DisplayName("Level spawns enemies")
    void enemiesSpawned() {
        assertFalse(level.getEnemies().isEmpty(), "Level should spawn at least one enemy");
    }

    @Test
    @DisplayName("All enemies spawn alive")
    void enemiesStartAlive() {
        for (Enemy e : level.getEnemies()) {
            assertTrue(e.isAlive());
        }
    }

    @Test
    @DisplayName("Enemies spawn within level bounds")
    void enemiesWithinBounds() {
        for (Enemy e : level.getEnemies()) {
            assertTrue(e.getX() >= 0 && e.getX() < level.getPixelWidth());
            assertTrue(e.getY() >= 0 && e.getY() < level.getPixelHeight());
        }
    }

    @Test
    @DisplayName("Level spawns coins")
    void coinsSpawned() {
        assertFalse(level.getCoins().isEmpty(), "Level should spawn at least one coin");
    }

    @Test
    @DisplayName("All coins start uncollected")
    void coinsStartUncollected() {
        for (Coin c : level.getCoins()) {
            assertFalse(c.isCollected());
        }
    }

    @Test
    @DisplayName("Coins spawn within level bounds")
    void coinsWithinBounds() {
        for (Coin c : level.getCoins()) {
            assertTrue(c.getX() >= 0 && c.getX() < level.getPixelWidth());
            assertTrue(c.getY() >= 0 && c.getY() < level.getPixelHeight());
        }
    }

    // ── Flag ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Level has a flag placed")
    void flagExists() {
        assertTrue(level.getFlagX() >= 0, "Flag X should be non-negative");
        assertTrue(level.getFlagY() >= 0, "Flag Y should be non-negative");
    }

    @Test
    @DisplayName("Flag is positioned within level bounds")
    void flagWithinBounds() {
        assertTrue(level.getFlagX() < level.getPixelWidth());
        assertTrue(level.getFlagY() < level.getPixelHeight());
    }

    // ── Gaps in ground ─────────────────────────────────────────────────

    @Test
    @DisplayName("Level has gaps in the ground (pits)")
    void levelHasGaps() {
        boolean hasGap = false;
        int groundRow = level.getHeightInTiles() - 2; // row 14
        for (int x = 0; x < level.getWidthInTiles(); x++) {
            Block b = level.getBlock(x, groundRow);
            if (b != null && b.getType() == Block.Type.EMPTY) {
                hasGap = true;
                break;
            }
        }
        assertTrue(hasGap, "Level should have at least one gap/pit in the ground");
    }
}
