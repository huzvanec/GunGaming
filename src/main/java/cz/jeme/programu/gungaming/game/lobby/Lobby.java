package cz.jeme.programu.gungaming.game.lobby;

import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.util.Components;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.minecraft.core.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Lobby {
    private static @Nullable Lobby instance;
    private final @NotNull Audience audience;
    private final @NotNull World world;
    private final @NotNull Location spawn;

    @SuppressWarnings("UnstableApiUsage")
    public Lobby(final @NotNull CommandSourceStack source) {
        audience = source.getSender();
        if (enabled()) {
            audience.sendMessage(Components.prefix("<red>Lobby is already enabled!"));
            throw new IllegalStateException("There can only be one instance of Lobby!");
        }
        if (Game.running()) {
            audience.sendMessage(Components.prefix("<red>You can't enable lobby while a game is running!"));
            throw new IllegalStateException("Cannot enable lobby while a game is running!");
        }
        // init success, no issues found
        instance = this;
        world = source.getLocation().getWorld();
        final BlockPos spawnPos = GameConfig.LOBBY_SPAWN.get()
                .getBlockPos(((net.minecraft.commands.CommandSourceStack) source));

        final int x = spawnPos.getX();
        final int z = spawnPos.getZ();
        final int y = world.getHighestBlockYAt(x, z) + 1;
        spawn = new Location(world, x + .5, y, z + .5);
        Bukkit.getOnlinePlayers().forEach(this::playerSetup);
        Game.worldSetup(world);
    }

    public void playerSetup(final @NotNull Player player) {
        Game.playerSetup(player);
        player.teleport(spawn);
        player.setGameMode(GameMode.ADVENTURE);
    }

    public void disable() {
        instance = null;
    }

    public static synchronized @NotNull Lobby instance() {
        return Objects.requireNonNull(instance, "Lobby is not enabled!");
    }

    public static boolean enabled() {
        return instance != null;
    }

    public @NotNull Location spawn() {
        return spawn;
    }
}
