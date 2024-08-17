package src.game;

import java.awt.*;

public class Constants {
    public static final int FPS = 60;
    public static final double DT = 1 / (double) FPS;

    public static final double EPSILON = 0.0001;
    public static final double EPSILON_SQ = EPSILON * EPSILON;

    public static final String WINDOW_NAME = "window_name!";
    public static final int BASE_WIDTH = 500;
    public static final int BASE_HEIGHT = 300;
    public static final int RES_MUL = 2;
    public static final Dimension BASE_SIZE = new Dimension(BASE_WIDTH, BASE_HEIGHT);
    public static final Dimension SCALED_SIZE = new Dimension(BASE_WIDTH * RES_MUL, BASE_HEIGHT * RES_MUL);

    public static final Font DEFAULT_FONT = new Font(Font.DIALOG_INPUT, Font.PLAIN, 10 * RES_MUL);

    public static final Color BG_COL = new Color(0, 10, 5);
    public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
}
