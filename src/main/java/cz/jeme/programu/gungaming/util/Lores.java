package cz.jeme.programu.gungaming.util;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public final class Lores {
    public static final @NotNull DecimalFormat STATS_FORMATTER = new DecimalFormat("#0.##");

    private Lores() {
        throw new AssertionError();
    }

    public static @NotNull String loreStat(final @NotNull String key, final @NotNull String value) {
        return loreStat(key, value, "");
    }

    public static @NotNull String loreStat(final @NotNull String key, final @NotNull String value, final @NotNull String modification) {
        return "<#77A5FF>" + Components.latinString(key) + ": <#CADCFF>" + modification + value;
    }
}
