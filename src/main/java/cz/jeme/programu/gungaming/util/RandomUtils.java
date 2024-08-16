package cz.jeme.programu.gungaming.util;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomUtils {
    private RandomUtils() {
        throw new AssertionError();
    }

    public static int nextChanced(final double chance, final int iterations) {
        if (chance < 0 || chance > 1)
            throw new IllegalArgumentException("Chance must be between 0 and 1!");
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        int value = 0;
        for (int i = 0; i < iterations; i++) if (random.nextDouble() <= chance) value++;
        return value;
    }
}
