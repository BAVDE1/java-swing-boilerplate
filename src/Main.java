package src;

import src.game.Game;
import src.utility.MathUtils;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    /**
     * Time stepper.
     * if game has optimised boolean toggled, thread sleeps for half of dt once stepped.
     */
    public static Thread newTimeStepper(double dt, Game game) {
        return new Thread() {
            final double halfDt = dt * 0.5;  // seconds

            double accumulator = 0;  // seconds
            double lastFrame = System.nanoTime();  // nanoseconds

            public void run() {
                while (game.running) {
                    double t = System.nanoTime();  // nanoseconds
                    accumulator += MathUtils.nanoToSecond(t - lastFrame);
                    accumulator = Math.min(1, accumulator);  // min of 1 fps (avoid spiral of doom)
                    lastFrame = t;

                    while (accumulator >= dt) {
                        accumulator -= dt;

                        try {
                            double loopTime = game.mainLoop(dt);  // seconds
                            if (game.optimiseTimeStepper && accumulator + loopTime < halfDt) {  // only sleep if there is enough time
                                Thread.sleep((long) Math.floor(halfDt * 1_000));  // give it a little break *-*
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Program closed while thread was asleep (between frames)");
                        }
                    }
                }
            }
        };
    }
}
