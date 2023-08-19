package cz.jeme.programu.gungaming.util;

import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class Packets {

    private Packets() {
        // Static class cannot be initialized
    }

    public static void sendPacket(@NotNull Player player, @NotNull Packet<? extends PacketListener> packet) {
        sendPacket((CraftPlayer) player, packet);
    }

    public static void sendPacket(@NotNull CraftPlayer craftPlayer, @NotNull Packet<? extends PacketListener> packet) {
        sendPacket(craftPlayer.getHandle(), packet);
    }

    public static void sendPacket(@NotNull ServerPlayer serverPlayer, @NotNull Packet<? extends PacketListener> packet) {
        sendPacket(serverPlayer.connection, packet);
    }

    public static void sendPacket(@NotNull ServerGamePacketListenerImpl listener, @NotNull Packet<? extends PacketListener> packet) {
        listener.send(packet);
    }

    public static void sendPacketToAll(@NotNull Packet<? extends PacketListener> packet) {
        sendPacket(Bukkit.getOnlinePlayers(), packet);
    }

    public static void sendPacket(@NotNull Collection<? extends Player> players, @NotNull Packet<? extends PacketListener> packet) {
        players.forEach(player -> sendPacket(player, packet));
    }
}
