package mario.world;

/**
 * Represents a single tile/block in the game world.
 */
public class Block {

    public enum Type {
        EMPTY,
        GROUND,
        BRICK,
        QUESTION,
        PIPE_TOP_LEFT,
        PIPE_TOP_RIGHT,
        PIPE_BODY_LEFT,
        PIPE_BODY_RIGHT
    }

    private Type type;
    private final int gridX, gridY;
    private boolean hit; // for question blocks that have been hit

    public Block(Type type, int gridX, int gridY) {
        this.type = type;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public boolean isSolid() {
        return type != Type.EMPTY;
    }

    public void hit() {
        if (type == Type.QUESTION && !hit) {
            hit = true;
        }
    }

    // Getters
    public Type getType() { return type; }
    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public boolean isHit() { return hit; }
}
