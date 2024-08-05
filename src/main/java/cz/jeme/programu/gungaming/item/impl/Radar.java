package cz.jeme.programu.gungaming.item.impl;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import cz.jeme.programu.gungaming.loot.crate.CrateLocation;
import cz.jeme.programu.gungaming.util.Components;
import cz.jeme.programu.gungaming.util.Maps;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

public class Radar extends CustomItem implements SingleLoot {
    public static final @NotNull Data<Byte, Boolean> RADAR_INITIALIZED_DATA = Data.ofBoolean(GunGaming.namespaced("radar_initialized"));

    protected Radar() {
        item.editMeta(meta -> meta.setMaxStackSize(64));

        Bukkit.getScheduler().runTaskTimer(
                GunGaming.plugin(),
                () -> {
                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        final PlayerInventory inventory = player.getInventory();
                        final ItemStack mainHand = inventory.getItemInMainHand();
                        final ItemStack offHand = inventory.getItemInOffHand();
                        updateRadar(player, mainHand);
                        updateRadar(player, offHand);
                    }
                },
                0L,
                20L
        );
    }

    private void updateRadar(final @NotNull Player player, final @NotNull ItemStack item) {
        if (!CustomItem.is(item, Radar.class)) return;
        if (RADAR_INITIALIZED_DATA.read(item).orElse(false)) return;
        item.editMeta(MapMeta.class, meta -> {
            RADAR_INITIALIZED_DATA.write(meta, true);
            final MapView map = Bukkit.createMap(player.getWorld());
            meta.setMapView(map);
            map.getRenderers().forEach(map::removeRenderer);
            map.addRenderer(Renderer.INSTANCE);
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
        public static final @NotNull Renderer INSTANCE = new Renderer();

        private Renderer() {
        }

        private static final int MAP_SIZE = 128; // do not change, it wont work, because Maps#update works only for a small radius around the player

        @Override
        public void render(final @NotNull MapView map, final @NotNull MapCanvas canvas, final @NotNull Player player) {
            renderCursors(map, canvas, player);
            renderWorld(map, canvas, player);
        }

        private static void renderCursors(final @NotNull MapView map, final @NotNull MapCanvas canvas, final @NotNull Player player) {
            final MapCursorCollection cursors = new MapCursorCollection();
            final Location playerLocation = player.getLocation();
            // add the main player cursor
            cursors.addCursor(new MapCursor(
                    (byte) 0, (byte) 0, // in the center of the map
                    Maps.direction(player.getYaw()), // cursor rotation
                    MapCursor.Type.PLAYER, // white marker
                    true
            ));

            // add enemy player cursors
            for (final Player enemy : Bukkit.getOnlinePlayers()) {
                if (enemy.getUniqueId().equals(player.getUniqueId())) continue; // it's me lol
                if (enemy.getGameMode() == GameMode.SPECTATOR) continue; // don't show spectators
                final Location enemyLocation = enemy.getLocation();
                final int mapX = (enemyLocation.getBlockX() - playerLocation.getBlockX()) * 2;
                final int mapY = (enemyLocation.getBlockZ() - playerLocation.getBlockZ()) * 2;
                if (Math.abs(mapX) >= MAP_SIZE || Math.abs(mapY) >= MAP_SIZE)
                    continue; // the enemy is out of this map's scope
                cursors.addCursor(new MapCursor(
                        (byte) mapX, (byte) mapY,
                        Maps.direction(enemy.getYaw()),
                        MapCursor.Type.RED_MARKER,
                        true,
                        Components.of("<red>" + enemy.getName())
                ));
            }

            // add air drop cursors
            for (final CrateLocation crateLocation : CrateGenerator.INSTANCE.airDrops()) {
                final int mapX = (crateLocation.x() - playerLocation.getBlockX()) * 2;
                final int mapY = (crateLocation.z() - playerLocation.getBlockZ()) * 2;
                if (Math.abs(mapX) >= MAP_SIZE || Math.abs(mapY) >= MAP_SIZE)
                    continue; // the air drop is out of this map's scope
                cursors.addCursor(new MapCursor(
                        (byte) mapX, (byte) mapY,
                        (byte) 0,
                        MapCursor.Type.BANNER_ORANGE,
                        true
                ));
            }

            // apply the cursors
            canvas.setCursors(cursors);
        }

        private static void renderWorld(final @NotNull MapView map, final @NotNull MapCanvas canvas, final @NotNull Player player) {
            final Location location = player.getLocation();
            // create a new map data that will get updated
            final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            final Level level = serverPlayer.level();
            final MapItemSavedData data = MapItemSavedData.createFresh(
                    0, 0, // overridden later
                    (byte) 0,
                    true,
                    false,
                    level.dimension()
            );
            // the center needs to be assigned this way to prevent rounding up to chunks
            data.centerX = location.getBlockX();
            data.centerZ = location.getBlockZ();
            // update the map data
            Maps.update(level, serverPlayer, data);
            // transform the map data to canvas pixels
            for (int y = 0; y < MAP_SIZE; y++)
                for (int x = 0; x < MAP_SIZE; x++)
                    //noinspection deprecation
                    canvas.setPixel(x, y, data.colors[y * 128 + x]);
        }
    }
}
