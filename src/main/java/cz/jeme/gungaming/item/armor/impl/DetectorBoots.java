package cz.jeme.gungaming.item.armor.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.armor.Boots;
import cz.jeme.gungaming.item.block.impl.Mine;
import cz.jeme.gungaming.loot.Rarity;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;

import java.text.DecimalFormat;
import java.util.List;

@NullMarked
public class DetectorBoots extends Boots {
    private static final double MINE_RANGE = Mine.ENTITY_CHECK_RADIUS;
    private static final double DETECTION_RANGE = MINE_RANGE + 15;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00.00");
    private static final Sound WARNING_SOUND = Sound.sound(GunGaming.key("item.detector_boots.warning"), Sound.Source.PLAYER, 1, 1);

    protected DetectorBoots() {
        new Updater();
    }

    @Override
    protected Key provideArmorKey() {
        return GunGaming.key("detector");
    }

    @Override
    protected int provideDurability() {
        return 25;
    }

    @Override
    protected List<String> update(final ItemStack item) {
        return List.of();
    }

    @Override
    protected String provideDescription() {
        return "warns you about mines nearby";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "detector_boots";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text("Detector Boots");
    }

    private static final class Updater extends BukkitRunnable {
        public Updater() {
            runTaskTimer(GunGaming.instance(), 0L, 10L);
        }

        @Override
        public void run() {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!CustomItem.is(player.getInventory().getBoots(), DetectorBoots.class)) continue;
                final Location location = player.getLocation();
                final double distance = Mine.activeMines().stream()
                                                .mapToDouble(mine -> mine.location().distance(location))
                                                .min()
                                                .orElse(MINE_RANGE + DETECTION_RANGE + 1) - MINE_RANGE;
                if (distance > DETECTION_RANGE) continue;
                player.playSound(WARNING_SOUND, player);
                final double phase = Math.min(1, distance / (DETECTION_RANGE + Mine.ENTITY_CHECK_RADIUS));
                player.sendActionBar(Components.of("<red>Mine <transition:#FF0000:#FFFF00:#00FF00:" + phase
                                                   + ">" + FORMATTER.format(Math.max(0, distance)) + "</transition> blocks away!"));
            }
        }
    }
}
