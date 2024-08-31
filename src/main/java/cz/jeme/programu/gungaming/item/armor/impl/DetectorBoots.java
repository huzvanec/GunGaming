package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.armor.Boots;
import cz.jeme.programu.gungaming.item.block.impl.Mine;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class DetectorBoots extends Boots {
    private static final double MINE_RANGE = Mine.MINE_CHECK_RADIUS;
    private static final double DETECTION_RANGE = MINE_RANGE + 15;
    private static final @NotNull DecimalFormat FORMATTER = new DecimalFormat("00.00");
    private static final @NotNull Sound WARNING_SOUND = Sound.sound(GunGaming.namespaced("item.detector_boots.warning"), Sound.Source.PLAYER, 1, 1);

    protected DetectorBoots() {
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
        return "warns you about mines nearby";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.CHAINMAIL_BOOTS;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "detector_boots";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Detector Boots");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    private static final class Updater extends BukkitRunnable {
        public Updater() {
            runTaskTimer(GunGaming.plugin(), 0L, 10L);
        }

        @Override
        public void run() {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!CustomItem.is(player.getInventory().getBoots(), DetectorBoots.class)) continue;
                final Location location = player.getLocation();
                final double distance = Mine.active().stream()
                                                .mapToDouble(mine -> mine.location().distance(location))
                                                .min()
                                                .orElse(MINE_RANGE + DETECTION_RANGE + 1) - MINE_RANGE;
                if (distance > DETECTION_RANGE) continue;
                player.playSound(WARNING_SOUND, player);
                final double phase = Math.min(1, distance / (DETECTION_RANGE + Mine.MINE_CHECK_RADIUS));
                player.sendActionBar(Components.of("<red>Mine <transition:#FF0000:#FFFF00:#00FF00:" + phase
                                                   + ">" + FORMATTER.format(Math.max(0, distance)) + "</transition> blocks away!"));
            }
        }
    }
}
