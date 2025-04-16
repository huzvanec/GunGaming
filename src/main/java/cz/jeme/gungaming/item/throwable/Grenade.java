package cz.jeme.gungaming.item.throwable;

import cz.jeme.gungaming.GunGaming;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Grenade extends Throwable {
    protected Grenade() {
        addTags("grenade");
    }

    protected final Key throwSoundKey = GunGaming.key("item.grenade.throw");
    protected final Sound throwSound = Sound.sound(throwSoundKey, Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public Sound throwSound(final ItemStack item) {
        return throwSound;
    }

    protected final Sound heldSound = Sound.sound(GunGaming.key("item.grenade.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public Sound heldSound(final ItemStack item) {
        return heldSound;
    }
}