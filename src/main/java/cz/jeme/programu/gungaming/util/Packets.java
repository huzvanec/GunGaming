package cz.jeme.programu.gungaming.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Packets {
    private Packets() {
        throw new AssertionError();
    }

    public static void send(final @NotNull Player player, final @NotNull Packet<?> packet) {
        send((CraftPlayer) player, packet);
    }

    public static void send(final @NotNull CraftPlayer craftPlayer, final @NotNull Packet<?> packet) {
        send(craftPlayer.getHandle(), packet);
    }

    public static void send(final @NotNull ServerPlayer serverPlayer, final @NotNull Packet<?> packet) {
        send(serverPlayer.connection, packet);
    }

    public static void send(final @NotNull ServerGamePacketListenerImpl listener, final @NotNull Packet<?> packet) {
        listener.send(packet);
    }

    public static void send(final @NotNull Iterable<Player> players, final @NotNull Packet<?> packet) {
        for (final Player player : players)
            send(player, packet);
    }

    public static void sendAll(final @NotNull Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> send(player, packet));
    }
}
