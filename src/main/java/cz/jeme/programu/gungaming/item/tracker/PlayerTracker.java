package cz.jeme.programu.gungaming.item.tracker;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerTracker extends CustomItem implements SingleLoot {
    public static final @NotNull Data<Byte, Boolean> TRACKER_ACTIVE_DATA = Data.ofBoolean(GunGaming.namespaced("player_tracker"));

    protected final int inactiveCustomModelData = provideInactiveCustomModelData();
    protected final int activeCustomModelData = provideActiveCustomModelData();

    protected PlayerTracker() {
        addTags("tracker");
        TRACKER_ACTIVE_DATA.write(item, false);
        if (TrackerRunnable.running()) return;
        new TrackerRunnable();
    }

    protected abstract boolean validate(final @NotNull Player player, final @NotNull Player trackPlayer);

    protected abstract int provideInactiveCustomModelData();

    protected abstract int provideActiveCustomModelData();

    public final int activeCustomModelData() {
        return activeCustomModelData;
    }

    public final int inactiveCustomModelData() {
        return inactiveCustomModelData;
    }

    @Override
    protected final @NotNull Integer provideCustomModelData() {
        return provideInactiveCustomModelData();
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
}
