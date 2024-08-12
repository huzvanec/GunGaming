package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.GunGaming;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class Grenade extends Throwable {
    protected Grenade() {
        addTags("grenade");
    }

    protected final @NotNull Key throwSoundKey = GunGaming.namespaced("item.grenade.throw");
    protected final @NotNull Sound throwSound = Sound.sound(throwSoundKey, Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public @NotNull Sound throwSound(final @NotNull ItemStack item) {
        return throwSound;
    }

    protected final @NotNull Sound heldSound = Sound.sound(GunGaming.namespaced("item.grenade.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public @NotNull Sound heldSound(final @NotNull ItemStack item) {
        return heldSound;
    }
}