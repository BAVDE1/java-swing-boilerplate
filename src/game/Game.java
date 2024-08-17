package src.game;

import src.Main;
import src.utility.MathUtils;
import src.utility.Vec2;
import src.window.CanvasSurface;
import src.window.Event;
import src.window.Screen;
import src.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class Game {
    public int frameCounter = 0;
    public double timeStarted = 0;  // in milliseconds
    public int secondsElapsed = 0;

    public boolean running = false;
    public boolean optimiseTimeStepper = true;  // recommended if fps < 200
    private final Window window = new Window(Constants.BG_COL);

    Screen finalScreen = new Screen(this, Constants.SCALED_SIZE);
    CanvasSurface mainCanvas = new CanvasSurface(Constants.BASE_SIZE, Constants.BG_COL);
    CanvasSurface uiCanvas = new CanvasSurface(Constants.SCALED_SIZE, Constants.TRANSPARENT);

    public Game() {
        window.init(Constants.WINDOW_NAME, Constants.SCALED_SIZE, finalScreen);
    }

    public void start() {
        if (!running) {
            running = true;
            window.open();

            Thread timeStepper = Main.newTimeStepper(Constants.DT, this);
            timeStarted = System.currentTimeMillis();
            timeStepper.start();
        }
    }

    private void windowEvent(int type, WindowEvent we) {
        if (type == Event.CLOSE_PRESSED) {
            running = false;
            window.shutDown();
        }
    }

    private void mouseEvent(int type, MouseEvent me) {}

    private void keyEvent(int type, KeyEvent ke) {
        if (type == Event.KEY_PRESSED) {
            int k = ke.getKeyCode();  // note use of key code (not char)
            if (k == KeyEvent.VK_ESCAPE) {
                window.closeWindowSafe();
            }
        }
    }

    private void events() {
        if (!window.eventQueue.isEmpty()) {
            for (Event<?> event : window.popAllEvents()) {
                switch (event.event) {
                    case WindowEvent e: windowEvent(event.type, e); break;
                    case MouseEvent e: mouseEvent(event.type, e); break;
                    case KeyEvent e: keyEvent(event.type, e); break;
                    default: throw new ClassFormatError(String.format("'%s' case not handled. add it to events switch.", event.event));
                }
            }
        }
    }

    private void update(double dt) {}

    /** Render before screen */
    private void renderCanvases() {
        mainCanvas.clear();
        uiCanvas.clear();

        // example render
        Vec2 mid = Vec2.fromDim(Constants.BASE_SIZE).mul(.5);
        double s = Math.sin(MathUtils.millisToSecond(System.currentTimeMillis())) * 20;

        mainCanvas.drawCircle(Color.BLUE, mid, 20);
        mainCanvas.drawLine(Color.WHITE,
                new Vec2(0, Constants.BASE_HEIGHT / 2. - s),
                new Vec2(Constants.BASE_WIDTH, Constants.BASE_HEIGHT / 2. + s)
        );
        mainCanvas.fillCircle(Color.BLUE, mid, 12);

        uiCanvas.drawText(Color.RED, new Vec2(), String.valueOf(MathUtils.round(s, 3)));
    }

    /** Called in finalScreen so proper graphics object is used */
    public void renderScreen(Graphics g) {
        finalScreen.blitScaled(g, mainCanvas);
        finalScreen.blit(g, uiCanvas);
    }

    /** returns time taken in seconds */
    public double mainLoop(double dt) {
        double tStart = System.nanoTime();
        frameCounter++;

        // update fps count every second (ik it could be updated every frame but whatever)
        int newSeconds = (int) Math.floor(MathUtils.millisToSecond(System.currentTimeMillis()) - MathUtils.millisToSecond(timeStarted));
        if (newSeconds != secondsElapsed) {
            String sFps = String.valueOf(MathUtils.round(frameCounter, 1));
            window.setTitle(String.format("%s (%s fps)", Constants.WINDOW_NAME, sFps));
            secondsElapsed = newSeconds;
            frameCounter = 0;  // reset so frame counter doesn't pass max int value
        }

        events();
        update(dt);
        renderCanvases();
        window.repaint();  // calls paintComponent on all surfaces added to window
        return MathUtils.nanoToSecond(System.nanoTime() - tStart);
    }
}
