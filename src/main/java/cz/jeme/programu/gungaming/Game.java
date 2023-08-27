package cz.jeme.programu.gungaming;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Game {
    public static  @Nullable Game game;

    public int size;
    public int centerX;
    public int centerZ;
    public @Nullable CommandSender sender;

    public Game(int size, int centerX, int centerZ, @NotNull CommandSender sender) {
        this.sender = sender;
        this.size = size;
        this.centerX = centerX;
        this.centerZ = centerZ;
    }
}
