package mario.world;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Block tile types and behaviour.
 */
class BlockTest {

    @Test
    @DisplayName("GROUND block is solid")
    void groundIsSolid() {
        Block block = new Block(Block.Type.GROUND, 0, 0);
        assertTrue(block.isSolid());
    }

    @Test
    @DisplayName("BRICK block is solid")
    void brickIsSolid() {
        Block block = new Block(Block.Type.BRICK, 1, 2);
        assertTrue(block.isSolid());
    }

    @Test
    @DisplayName("QUESTION block is solid")
    void questionIsSolid() {
        Block block = new Block(Block.Type.QUESTION, 3, 4);
        assertTrue(block.isSolid());
    }

    @Test
    @DisplayName("EMPTY block is not solid")
    void emptyIsNotSolid() {
        Block block = new Block(Block.Type.EMPTY, 0, 0);
        assertFalse(block.isSolid());
    }

    @Test
    @DisplayName("Block stores grid coordinates")
    void gridCoordinates() {
        Block block = new Block(Block.Type.GROUND, 5, 10);
        assertEquals(5, block.getGridX());
        assertEquals(10, block.getGridY());
    }

    @Test
    @DisplayName("QUESTION block starts not hit")
    void questionBlockStartsUnhit() {
        Block block = new Block(Block.Type.QUESTION, 0, 0);
        assertFalse(block.isHit());
    }

    @Test
    @DisplayName("Hitting a QUESTION block marks it as hit")
    void hitQuestionBlock() {
        Block block = new Block(Block.Type.QUESTION, 0, 0);
        block.hit();
        assertTrue(block.isHit());
    }

    @Test
    @DisplayName("Hitting a non-QUESTION block does nothing")
    void hitNonQuestionBlock() {
        Block ground = new Block(Block.Type.GROUND, 0, 0);
        ground.hit();
        assertFalse(ground.isHit());
    }

    @Test
    @DisplayName("Hitting an already-hit QUESTION block stays hit")
    void doubleHitQuestionBlock() {
        Block block = new Block(Block.Type.QUESTION, 0, 0);
        block.hit();
        block.hit();
        assertTrue(block.isHit());
    }

    @Test
    @DisplayName("Block type getter returns correct type")
    void typeGetter() {
        assertEquals(Block.Type.BRICK, new Block(Block.Type.BRICK, 0, 0).getType());
        assertEquals(Block.Type.EMPTY, new Block(Block.Type.EMPTY, 0, 0).getType());
    }
}
