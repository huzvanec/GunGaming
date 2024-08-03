package cz.jeme.programu.gungaming.item.impl;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import cz.jeme.programu.gungaming.util.Components;
import cz.jeme.programu.gungaming.util.Maps;
import cz.jeme.programu.gungaming.util.Packets;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Radar extends CustomItem implements SingleLoot {

    private static final @NotNull MapId MAP_ID = new MapId(0);

    @SuppressWarnings("deprecation")
    protected Radar() {
        item.editMeta(MapMeta.class, meta -> {
            meta.setMaxStackSize(64);
            meta.setMapId(0);
            final MapView map = meta.getMapView() == null
                    ? Bukkit.createMap(Bukkit.getWorlds().getFirst())
                    : meta.getMapView();
            meta.setMapView(map);
            map.getRenderers().forEach(map::removeRenderer);
            map.addRenderer(new Renderer());
        });
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Radar");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Scans the surroundings for players and air drops";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.FILLED_MAP;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "radar";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    private static final class Renderer extends MapRenderer {
        @Override
        public void render(@NotNull final MapView map, @NotNull final MapCanvas canvas, @NotNull final Player player) {
            final MapCursorCollection cursors = canvas.getCursors();
            for (int i = 0; i < cursors.size(); i++)
                cursors.removeCursor(cursors.getCursor(i));

            // add player cursor
            cursors.addCursor(new MapCursor(
                    (byte) 0,
                    (byte) 0,
                    Maps.direction(player.getYaw()),
                    MapCursor.Type.BLUE_MARKER,
                    true
            ));

            final List<? extends Player> players = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
                    .toList();

            final Location location = player.getLocation();
            for (final Player mapPlayer : players) {
                if (!mapPlayer.isValid()) continue;
                final Location mapLocation = mapPlayer.getLocation();
                final int xDiff = (mapLocation.getBlockX() - location.getBlockX()) * 2;
                final int zDiff = (mapLocation.getBlockZ() - location.getBlockZ()) * 2;
                if (Math.abs(xDiff) >= 128 || Math.abs(zDiff) >= 128) continue;
                final MapCursor cursor = new MapCursor(
                        (byte) xDiff,
                        (byte) zDiff,
                        Maps.direction(mapPlayer.getYaw()),
                        MapCursor.Type.RED_MARKER,
                        true
                );
                cursor.caption(Components.of("<red>" + mapPlayer.getName()));
                cursors.addCursor(cursor);
            }
            final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            final Level level = serverPlayer.level();
            final MapItemSavedData data = MapItemSavedData.createFresh(
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
            Packets.send(player, new ClientboundMapItemDataPacket(
                    MAP_ID,
                    (byte) 0,
                    true,
                    Collections.emptyList(),
                    new MapItemSavedData.MapPatch(0, 0, 128, 128, data.colors)
            ));
        }
    }
}
