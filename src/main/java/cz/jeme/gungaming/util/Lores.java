package cz.jeme.gungaming.util;

import org.jspecify.annotations.NullMarked;

import java.text.DecimalFormat;

@NullMarked
public final class Lores {
    public static final DecimalFormat STATS_FORMATTER = new DecimalFormat("#0.##");

    private Lores() {
        throw new AssertionError();
    }

    public static String loreStat(final String key, final String value) {
        return loreStat(key, value, "");
    }

    public static String loreStat(final String key, final String value, final String modification) {
        return "<#77A5FF>" + Components.latinString(key) + ": <#CADCFF>" + modification + value;
    }
}
