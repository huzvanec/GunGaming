package cz.jeme.programu.gungaming.util;

import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class Packets {

    private Packets() {
        // Static class cannot be initialized
    }

    public static void sendPacket(Player player, Packet<? extends PacketListener> packet) {
        sendPacket((CraftPlayer) player, packet);
    }

    public static void sendPacket(CraftPlayer craftPlayer, Packet<? extends PacketListener> packet) {
        sendPacket(craftPlayer.getHandle(), packet);
    }

    public static void sendPacket(ServerPlayer serverPlayer, Packet<? extends PacketListener> packet) {
        sendPacket(serverPlayer.connection, packet);
    }

    public static void sendPacket(ServerGamePacketListenerImpl listener, Packet<? extends PacketListener> packet) {
        listener.send(packet);
    }

    public static void sendPacketToAll(Packet<? extends PacketListener> packet) {
        sendPacket(Bukkit.getOnlinePlayers(), packet);
    }

    public static void sendPacket(Collection<? extends Player> players, Packet<? extends PacketListener> packet) {
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }
}
