package cz.jeme.programu.gungaming.item.consumable;

import cz.jeme.programu.gungaming.GunGaming;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

final class Eating extends BukkitRunnable {
    private int counter = 0;
    private final @NotNull Player player;
    private final @NotNull EquipmentSlot hand;
    private final @NotNull ItemStack item;
    private final @NotNull Sound sound;

    Eating(final @NotNull Player player,
           final @NotNull EquipmentSlot hand) {
        this.player = player;
        this.hand = hand;
        item = player.getInventory().getItem(hand);
        sound = Consumable.of(item).eatSound(item);
        runTaskTimer(GunGaming.plugin(), 9L, 4L);
    }

    @Override
    public void run() {
        if (counter >= 7 || !player.getInventory().getItem(hand).equals(item)) {
            cancel();
            return;
        }
        player.getWorld().playSound(sound, player);
        counter++;
    }
}
