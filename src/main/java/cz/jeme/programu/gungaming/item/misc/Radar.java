package cz.jeme.programu.gungaming.item.misc;

import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.SingletonLoot;
import cz.jeme.programu.gungaming.util.Maps;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.Packets;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Radar extends Misc implements SingletonLoot {
    @SuppressWarnings("deprecation")
    public Radar() {
        setup();

        MapMeta meta = (MapMeta) item.getItemMeta();
        meta.setMapId(0);
        MapView map = meta.getMapView();
        if (map == null) {
            map = Bukkit.createMap(Game.getWorld());
        }
        map.getRenderers().forEach(map::removeRenderer);
        map.addRenderer(new Renderer());
        item.setItemMeta(meta);
    }

    @Override
    protected void setup() {
        name = "Radar";
        info = "Scans the surroundings";
        rarity = Rarity.LEGENDARY;
        customModelData = 1;
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.FILLED_MAP;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 1;
    }

    private static class Renderer extends MapRenderer {
        @Override
        public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
            MapCursorCollection cursors = canvas.getCursors();
            Location location = player.getLocation();
            final int size = cursors.size();

            for (int i = 0; i < size; i++) {
                cursors.removeCursor(cursors.getCursor(0));
            }

            cursors.addCursor(
                    new MapCursor(
                            (byte) 0,
                            (byte) 0,
                            Maps.calcDirection(player),
                            MapCursor.Type.GREEN_POINTER,
                            true
                    )
            );

            List<? extends Player> players = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getUniqueId() != player.getUniqueId())
                    .toList();

            for (Player p : players) {
                if (!p.isValid()) continue;
                Location entityLocation = p.getLocation();
                int xDiff = (entityLocation.getBlockX() - location.getBlockX()) * 2;
                int zDiff = (entityLocation.getBlockZ() - location.getBlockZ()) * 2;
                if (Math.abs(xDiff) >= 128 || Math.abs(zDiff) >= 128) continue;
                MapCursor cursor = new MapCursor(
                        (byte) xDiff,
                        (byte) zDiff,
                        Maps.calcDirection(p),
                        MapCursor.Type.RED_POINTER,
                        true
                );
                cursor.caption(Message.from(p.getName()));
                cursors.addCursor(cursor);
            }
            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            Level level = serverPlayer.level();
            MapItemSavedData data = MapItemSavedData.createFresh(
                    location.getX(),
                    location.getZ(),
                    (byte) 0,
                    false,
                    false,
                    level.dimension()
            );
            data.centerX = location.getBlockX();
            data.centerZ = location.getBlockZ();
            Maps.update(level, serverPlayer, data);
            Packets.sendPacket(player, new ClientboundMapItemDataPacket(
                    0,
                    (byte) 0,
                    true,
                    Collections.emptyList(),
                    new MapItemSavedData.MapPatch(0, 0, 128, 128, data.colors)
            ));
        }
    }
}
