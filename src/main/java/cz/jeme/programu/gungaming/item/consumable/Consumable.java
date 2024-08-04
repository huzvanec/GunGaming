package cz.jeme.programu.gungaming.item.consumable;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Consumable extends CustomItem {

    protected Consumable() {
        addTags("consumable");
    }

    // consuming

    @Override
    protected void onUse(final @NotNull PlayerInteractEvent event) {
        startConsume(event);
    }

    private void startConsume(final @NotNull PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        final EquipmentSlot hand = event.getHand();
        assert item != null;
        assert hand != null;
        EatManager.INSTANCE.startEating(event.getPlayer(), hand);
    }

    final void consume(final @NotNull PlayerItemConsumeEvent event) {
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final ItemStack item = player.getInventory().getItem(event.getHand());
        if (player.getGameMode() != GameMode.CREATIVE)
            item.setAmount(item.getAmount() - 1);

        onConsume(event);
    }

    protected void onConsume(final @NotNull PlayerItemConsumeEvent event) {
    }

    // sounds

    protected final @NotNull Key eatSoundKey = GunGaming.namespaced("item." + key.value() + ".eat");
    protected final @NotNull Key burpSoundKey = GunGaming.namespaced("item." + key.value() + ".burp");

    protected final @NotNull Sound eatSound = Sound.sound(eatSoundKey, Sound.Source.PLAYER, 1, 1);
    protected final @NotNull Sound burpSound = Sound.sound(burpSoundKey, Sound.Source.PLAYER, 1, 1);

    public @NotNull Sound eatSound(final @NotNull ItemStack item) {
        return eatSound;
    }

    public @NotNull Sound burpSound(final @NotNull ItemStack item) {
        return burpSound;
    }

    // override stuff

    @Override
    protected final @NotNull Material provideMaterial() {
        return Material.CHORUS_FRUIT;
    }

    @Override
    protected final @NotNull String provideType() {
        return "consumable";
    }

    // static accessors

    public static @NotNull Consumable of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Consumable.class);
    }

    public static @NotNull Consumable of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Consumable.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Consumable.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Consumable.class);
    }
}