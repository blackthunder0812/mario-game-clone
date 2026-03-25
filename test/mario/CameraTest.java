package mario;

import mario.util.Constants;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Camera side-scrolling and clamping behaviour.
 */
class CameraTest {

    // Use a level width wider than the window
    private static final int LEVEL_WIDTH = 2560;

    private Camera camera;

    @BeforeEach
    void setUp() {
        camera = new Camera(LEVEL_WIDTH);
    }

    @Test
    @DisplayName("Camera starts at x=0")
    void initialPosition() {
        assertEquals(0, camera.getX());
    }

    @Test
    @DisplayName("Camera centres on player when player is in the middle of the level")
    void centresOnPlayer() {
        double playerX = 1000;
        camera.update(playerX);
        int expected = (int) playerX - Constants.WINDOW_WIDTH / 2;
        assertEquals(expected, camera.getX());
    }

    @Test
    @DisplayName("Camera is clamped to 0 at the left edge")
    void clampedLeft() {
        camera.update(100); // near start of level
        assertTrue(camera.getX() >= 0);
    }

    @Test
    @DisplayName("Camera is clamped at right edge of level")
    void clampedRight() {
        camera.update(LEVEL_WIDTH - 10); // near end
        int maxCamX = LEVEL_WIDTH - Constants.WINDOW_WIDTH;
        assertTrue(camera.getX() <= maxCamX);
    }

    @Test
    @DisplayName("Camera at exact left boundary is 0")
    void exactLeftBoundary() {
        camera.update(0);
        assertEquals(0, camera.getX());
    }

    @Test
    @DisplayName("Camera at exact right boundary is clamped")
    void exactRightBoundary() {
        camera.update(LEVEL_WIDTH);
        int maxCamX = LEVEL_WIDTH - Constants.WINDOW_WIDTH;
        assertEquals(maxCamX, camera.getX());
    }

    @Test
    @DisplayName("Camera follows player movement")
    void followsMovement() {
        camera.update(600);
        int pos1 = camera.getX();
        camera.update(700);
        int pos2 = camera.getX();
        assertTrue(pos2 > pos1, "Camera should move right when player moves right");
    }

    @Test
    @DisplayName("Camera works with narrow level (level width <= window width)")
    void narrowLevel() {
        Camera narrowCam = new Camera(Constants.WINDOW_WIDTH);
        narrowCam.update(100);
        assertEquals(0, narrowCam.getX());
    }
}
