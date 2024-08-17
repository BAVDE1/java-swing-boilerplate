package src.utility;

import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    public static double nanoToSecond(double nanoSecs) {
        return nanoSecs / 1_000_000_000.0;
    }

    public static double millisToSecond(double milliseconds) {
        return milliseconds / 1_000.0;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        double factor = Math.pow(10, places);
        value = value * factor;
        return (double) Math.round(value) / factor;
    }
}
