package cz.jeme.programu.gungaming.loot.crate;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.LootGenerator;
import cz.jeme.programu.gungaming.loot.crate.impl.AirDrop;
import cz.jeme.programu.gungaming.util.Components;
import cz.jeme.programu.gungaming.util.Packets;
import net.kyori.adventure.audience.Audience;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum CrateGenerator {
    INSTANCE;


    private final @NotNull Map<CrateLocation, CrateInfo> inventories = new HashMap<>();
    private final @NotNull Set<CrateLocation> airDrops = new HashSet<>();
    volatile @Nullable Generation generation = null;

    private boolean checkLock(final @Nullable Audience audience) {
        if (generation == null) return false;
        if (audience != null)
            audience.sendMessage(Components.prefix("<red>You cannot run crate commands during generation!"));
        return true;
    }

    public boolean generate(final @NotNull Audience audience,
                            final @NotNull Location location,
                            final int x1,
                            final int z1,
                            final int x2,
                            final int z2,
                            final int bps) {
        if (checkLock(audience)) return false;
        audience.sendMessage(Components.prefix("<green>Generating crates..."));
        generation = new Generation(audience, location, x1, z1, x2, z2, bps);
        return true;
    }

    void addInventory(final @NotNull Block block, final @NotNull Crate crate, final @NotNull Inventory inventory) {
        final CrateLocation location = new CrateLocation(block);
        inventories.put(location, new CrateInfo(crate, inventory));
        if (crate instanceof AirDrop)
            airDrops.add(location);
    }

    void click(final @NotNull PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        assert block != null;
        final CrateLocation location = new CrateLocation(block);
        final CrateInfo info = inventories.get(location);
        if (info == null) return;
        event.setCancelled(true);
        final Player player = event.getPlayer();
        Packets.sendAll(new ClientboundAnimatePacket(
                ((CraftPlayer) player).getHandle(), ClientboundAnimatePacket.SWING_MAIN_HAND
        ));
        // needs to be scheduled, because the cancelling of this event prevents the opening of the inventory
        // 2 ticks are for syncing with the animation
        Bukkit.getScheduler().runTaskLater(
                GunGaming.plugin(),
                () -> player.openInventory(info.inventory()),
                2L
        );
    }

    public boolean removeCrates(final @Nullable Audience audience) {
        if (checkLock(audience)) return false;
        if (inventories.isEmpty()) {
            if (audience != null)
                audience.sendMessage(Components.prefix("<red>No crates to remove!"));
            return false;
        }
        for (final CrateLocation location : inventories.keySet()) {
            final World world = Bukkit.getWorld(location.worldUid());
            if (world == null)
                throw new RuntimeException("World \"" + location.worldUid() + "\" not found!");
            final Block block = world.getBlockAt(location.x(), location.y(), location.z());
            block.setType(Material.AIR);
        }
        inventories.clear();
        if (audience != null)
            audience.sendMessage(Components.prefix("<green>Crates removed successfully"));
        return true;
    }

    public boolean clearCrates(final @Nullable Audience audience) {
        if (checkLock(audience)) return false;
        if (inventories.isEmpty()) {
            if (audience != null)
                audience.sendMessage(Components.prefix("<red>No crates to clear!"));
            return false;
        }
        inventories.values().forEach(info -> info.inventory().clear());
        if (audience != null)
            audience.sendMessage(Components.prefix("<green>Crates cleared successfully"));
        return true;
    }

    public boolean refillCrates(final @Nullable Audience audience) {
        if (checkLock(audience)) return false;
        if (inventories.isEmpty()) {
            if (audience != null)
                audience.sendMessage(Components.prefix("<red>No crates to refill!"));
            return false;
        }
        clearCrates(null);
        for (final CrateInfo info : inventories.values()) {
            if (!info.crate().refill()) continue;
            final Inventory inventory = info.inventory();
            inventory.setContents(LootGenerator.INSTANCE.generate(info.crate(), inventory.getSize()));
        }
        if (audience != null)
            audience.sendMessage(Components.prefix("<green>Crates refilled successfully"));
        return true;
    }

    @ApiStatus.Internal
    public @NotNull Set<CrateLocation> airDrops() {
        return airDrops;
    }

    public boolean cancel(final @Nullable Audience audience) {
        if (generation == null) {
            if (audience != null)
                audience.sendMessage(Components.prefix("<red>No generation task to cancel!"));
            return false;
        }
        //noinspection DataFlowIssue
        generation.cancel();
        if (audience != null)
            audience.sendMessage(Components.prefix("<green>Generation task canceled successfully"));
        return true;
    }

    public boolean generating() {
        return generation != null;
    }

    public void generateCrate(final @NotNull Crate crate, final @NotNull Location location) {
        generateCrate(crate, location.getBlock());
    }

    public void generateCrate(final @NotNull Crate crate, final @NotNull Block block) {
        block.setType(crate.material());
        final Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, crate.strippedName());
        inventory.setContents(LootGenerator.INSTANCE.generate(crate, inventory.getSize()));
        addInventory(block, crate, inventory);
        crate.generated(block, inventory);
    }
}
