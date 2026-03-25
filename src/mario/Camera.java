package mario;

import mario.util.Constants;

/**
 * Camera that follows the player horizontally for side-scrolling.
 */
public class Camera {

    private int x;
    private final int levelWidth;

    public Camera(int levelWidth) {
        this.levelWidth = levelWidth;
    }

    /** Centre the camera on the player, clamped to level bounds. */
    public void update(double playerX) {
        x = (int) playerX - Constants.WINDOW_WIDTH / 2;
        if (x < 0) x = 0;
        if (x > levelWidth - Constants.WINDOW_WIDTH) {
            x = levelWidth - Constants.WINDOW_WIDTH;
        }
    }

    public int getX() { return x; }
}
