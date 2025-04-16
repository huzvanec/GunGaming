package cz.jeme.gungaming.item.tracker;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.loot.SingleLoot;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;


@NullMarked
public abstract class PlayerTracker extends CustomItem implements SingleLoot {
    protected final Key inactiveKey = provideInactiveKey();

    @SuppressWarnings("UnstableApiUsage")
    protected PlayerTracker() {
        addTags("tracker");

        item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);
        item.setData(DataComponentTypes.ITEM_MODEL, inactiveKey);

        if (!TrackerRunnable.running()) new TrackerRunnable();
    }

    protected abstract boolean validate(final Player player, final Player trackPlayer);

    @Override
    protected final Material provideMaterial() {
        return Material.POPPED_CHORUS_FRUIT;
    }

    protected Key provideInactiveKey() {
        return Key.key(
                key.namespace(),
                key.value() + "_inactive"
        );
    }

    public final Key inactiveKey() {
        return inactiveKey;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    protected final Sound heldSound = Sound.sound(GunGaming.key("item.tracker.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public Sound heldSound(final ItemStack item) {
        return heldSound;
    }
}
