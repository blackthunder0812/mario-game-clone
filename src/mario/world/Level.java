package mario.world;

import mario.entities.Coin;
import mario.entities.Enemy;
import mario.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the level layout using a character-grid map.
 * <p>
 * Legend:
 *   G = ground, B = brick, ? = question block,
 *   E = enemy (Goomba), C = coin, F = flag,
 *   P = pipe (2 wide, placed from top), . = empty
 */
public class Level {

    private Block[][] grid;
    private int widthInTiles;
    private int heightInTiles;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private int flagX = -1, flagY = -1;

    // Default level map
    private static final String[] MAP = {
        //          1111111111222222222233333333334444444444555555555566666666667777777777
        // 1234567890123456789012345678901234567890123456789012345678901234567890123456789
        "..............................................................................",
        "..............................................................................",
        "..............................................................................",
        "..............................................................F...............",
        "..............................................................................",
        "..............................................................................",
        "..............................................................................",
        "..........C.C.C......C.........C.C.C............C.............................",
        "..............................................................................",
        ".....................BBB?B...............................BB?BB.................",
        "..............................................................................",
        "..........................E..........B?B...C.C........E.......................",
        "..............................................................................",
        "..................................E...................E.........E...............",
        "GGGGGGGGGGGGGGGGGGGG...GGGGGGGGGGGGGGGGGGG...GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG",
        "GGGGGGGGGGGGGGGGGGGG...GGGGGGGGGGGGGGGGGGG...GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG",
    };

    public Level() {
        loadMap(MAP);
    }

    private void loadMap(String[] map) {
        heightInTiles = map.length;
        widthInTiles = 0;
        for (String row : map) {
            widthInTiles = Math.max(widthInTiles, row.length());
        }

        grid = new Block[widthInTiles][heightInTiles];

        for (int row = 0; row < heightInTiles; row++) {
            String line = map[row];
            for (int col = 0; col < widthInTiles; col++) {
                char ch = col < line.length() ? line.charAt(col) : '.';
                int px = col * Constants.TILE_SIZE;
                int py = row * Constants.TILE_SIZE;

                switch (ch) {
                    case 'G' -> grid[col][row] = new Block(Block.Type.GROUND, col, row);
                    case 'B' -> grid[col][row] = new Block(Block.Type.BRICK, col, row);
                    case '?' -> grid[col][row] = new Block(Block.Type.QUESTION, col, row);
                    case 'E' -> {
                        grid[col][row] = new Block(Block.Type.EMPTY, col, row);
                        enemies.add(new Enemy(px, py));
                    }
                    case 'C' -> {
                        grid[col][row] = new Block(Block.Type.EMPTY, col, row);
                        coins.add(new Coin(px + 6, py + 6));
                    }
                    case 'F' -> {
                        grid[col][row] = new Block(Block.Type.EMPTY, col, row);
                        flagX = px;
                        flagY = py;
                    }
                    default  -> grid[col][row] = new Block(Block.Type.EMPTY, col, row);
                }
            }
        }
    }

    public Block getBlock(int gridX, int gridY) {
        if (gridX < 0 || gridX >= widthInTiles || gridY < 0 || gridY >= heightInTiles) {
            return null;
        }
        return grid[gridX][gridY];
    }

    // Getters
    public int getWidthInTiles()   { return widthInTiles; }
    public int getHeightInTiles()  { return heightInTiles; }
    public int getPixelWidth()     { return widthInTiles * Constants.TILE_SIZE; }
    public int getPixelHeight()    { return heightInTiles * Constants.TILE_SIZE; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<Coin> getCoins()    { return coins; }
    public int getFlagX()           { return flagX; }
    public int getFlagY()           { return flagY; }
}
