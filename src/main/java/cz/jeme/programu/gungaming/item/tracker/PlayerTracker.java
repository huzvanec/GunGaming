package cz.jeme.programu.gungaming.item.tracker;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerTracker extends CustomItem implements SingleLoot {
    protected PlayerTracker() {
        addTags("tracker");
        if (TrackerRunnable.running()) return;
        new TrackerRunnable();
    }

    @Override
    protected final @NotNull Material provideMaterial() {
        return Material.COMPASS;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    protected final @NotNull Sound heldSound = Sound.sound(GunGaming.namespaced("item.tracker.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public @NotNull Sound heldSound(final @NotNull ItemStack item) {
        return heldSound;
    }

    protected abstract boolean validate(final @NotNull Player player, final @NotNull Player trackPlayer);
}
