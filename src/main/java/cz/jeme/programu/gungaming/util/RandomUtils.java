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

/*
private static void testNextChanced() {
        final int tests = 1_000_000;
        final double chance = .9;
        final int iterations = 100;
        final int[] results = new int[iterations + 1];
        Arrays.fill(results, 0);
        for (int t = 0; t < tests; t++) {
            final int value = RandomUtils.nextChanced(chance, iterations);
            results[value]++;
        }
        final int[] ascii = new int[results.length];
        for (int i = 0; i < results.length; i++) {
            ascii[i] = results[i] / (tests / iterations);
        }
        final DecimalFormat formatter = new DecimalFormat("0".repeat(((int) Math.log10(iterations) + 1)));
        for (int i = 0; i < results.length; i++) {
            System.out.println(formatter.format(i) + ": " + ">".repeat(ascii[i]));
        }
        for (int i = 0; i < results.length; i++) {
            System.out.println(formatter.format(i) + ": " + results[i]);
        }
    }
*/
}