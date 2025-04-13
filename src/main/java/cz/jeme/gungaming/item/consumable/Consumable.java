package cz.jeme.gungaming.item.consumable;

import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable.Builder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.datacomponent.item.Consumable.consumable;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public abstract class Consumable extends CustomItem {

    protected Consumable() {
        addTags("consumable");

        final var builder = consumable()
                .sound(eatSoundKey);
        buildConsumable(builder);
        item.setData(DataComponentTypes.CONSUMABLE, builder.build());
    }

    protected void buildConsumable(final Builder builder) {
    }

    // consuming

    final void consume(final PlayerItemConsumeEvent event) {
        onConsume(event);
    }

    protected void onConsume(final PlayerItemConsumeEvent event) {
    }

    // sounds

    protected final Key eatSoundKey = GunGaming.key("item." + key.value() + ".eat");
    protected final Key burpSoundKey = GunGaming.key("item." + key.value() + ".burp");

    protected final Sound eatSound = Sound.sound(eatSoundKey, Sound.Source.PLAYER, 1, 1);
    protected final Sound burpSound = Sound.sound(burpSoundKey, Sound.Source.PLAYER, 1, 1);

    public Sound eatSound(final ItemStack item) {
        return eatSound;
    }

    public Sound burpSound(final ItemStack item) {
        return burpSound;
    }

    // override stuff

    @Override
    protected final Material provideMaterial() {
        return Material.POPPED_CHORUS_FRUIT;
    }

    @Override
    protected final String provideType() {
        return "consumable";
    }

    // static accessors

    public static Consumable of(final String keyStr) {
        return CustomElement.of(keyStr, Consumable.class);
    }

    public static Consumable of(final ItemStack item) {
        return CustomItem.of(item, Consumable.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, Consumable.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Consumable.class);
    }
}