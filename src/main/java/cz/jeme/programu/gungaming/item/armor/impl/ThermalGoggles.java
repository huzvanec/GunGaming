package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.armor.Helmet;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Packets;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ThermalGoggles extends Helmet {
    protected ThermalGoggles() {
        new Updater();
        item.editMeta(Damageable.class, meta -> meta.setMaxDamage(10));
    }

    @Override
    protected double provideArmor() {
        return 0;
    }

    @Override
    protected double provideToughness() {
        return 0;
    }

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return List.of();
    }

    @Override
    protected @NotNull String provideDescription() {
        return "see players through walls";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.CHAINMAIL_HELMET;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "thermal_goggles";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Thermal Goggles");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    private static final class Updater extends BukkitRunnable {
        private static final @NotNull EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID;
        private static final int FLAG_GLOWING;

        static {
            try {
                final Field sharedFlagsField = Entity.class.getDeclaredField("DATA_SHARED_FLAGS_ID");
                sharedFlagsField.setAccessible(true);
                @SuppressWarnings("unchecked") final EntityDataAccessor<Byte> tempSharedFlags = (EntityDataAccessor<Byte>) sharedFlagsField.get(null);
                DATA_SHARED_FLAGS_ID = tempSharedFlags;
                final Field glowingFlagField = Entity.class.getDeclaredField("FLAG_GLOWING");
                glowingFlagField.setAccessible(true);
                FLAG_GLOWING = glowingFlagField.getInt(null);
            } catch (final IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Could not read flag data from Entity!", e);
            }
        }

        public Updater() {
            runTaskTimer(GunGaming.plugin(), 0L, 40L);
        }

        @Override
        public void run() {
            final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (final Player player : players) {
                final ItemStack helmet = player.getInventory().getHelmet();
                final boolean hasGoggles = CustomItem.is(helmet, ThermalGoggles.class);
                if (hasGoggles) player.addPotionEffect(new PotionEffect(
                        PotionEffectType.BLINDNESS,
                        60,
                        0,
                        false,
                        false,
                        false
                ));
                for (final Player enemy : players) {
                    if (!enemy.isValid()) continue;
                    if (enemy.getGameMode() == GameMode.SPECTATOR) continue;
                    final ServerPlayer enemyServer = ((CraftPlayer) enemy).getHandle();
                    Packets.send(player, glowPacket(enemyServer, hasGoggles || enemyServer.isCurrentlyGlowing()));
                }
            }
        }

        private static @NotNull ClientboundSetEntityDataPacket glowPacket(final @NotNull Entity entity, final boolean glow) {
            final SynchedEntityData data = entity.getEntityData();
            final byte sharedFlags = data.get(DATA_SHARED_FLAGS_ID);
            // copied from Entity#setSharedFlag
            final byte newSharedFlags = glow
                    ? (byte) (sharedFlags | 1 << FLAG_GLOWING) //  write 1
                    : (byte) (sharedFlags & ~(1 << FLAG_GLOWING)); // & invert mask = write 0
            List<SynchedEntityData.DataValue<?>> packed = data.packDirty();
            if (packed == null) packed = new ArrayList<>(); // omg protocol dementia
            packed.add(SynchedEntityData.DataValue.create(DATA_SHARED_FLAGS_ID, newSharedFlags));
            return new ClientboundSetEntityDataPacket(
                    entity.getId(),
                    packed
            );
        }
    }
}
