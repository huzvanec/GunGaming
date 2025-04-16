package cz.jeme.gungaming.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Packets {
    private Packets() {
        throw new AssertionError();
    }

    public static void send(final Player player, final Packet<?> packet) {
        send((CraftPlayer) player, packet);
    }

    public static void send(final CraftPlayer craftPlayer, final Packet<?> packet) {
        send(craftPlayer.getHandle(), packet);
    }

    public static void send(final ServerPlayer serverPlayer, final Packet<?> packet) {
        send(serverPlayer.connection, packet);
    }

    public static void send(final ServerGamePacketListenerImpl listener, final Packet<?> packet) {
        listener.send(packet);
    }

    public static void send(final Iterable<Player> players, final Packet<?> packet) {
        for (final Player player : players)
            send(player, packet);
    }

    public static void sendAll(final Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> send(player, packet));
    }
}
