package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public class MolotovCocktail extends Throwable {
    @Override
    protected void setup() {
        name = "Molotov Cocktail";
        info = "spreads fire everywhere";
        customModelData = 4;
        rarity = Rarity.RARE;
        throwCooldown = 500;
        damage = 2d;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 4;
    }

    @Override
    public void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 5f, true, false);
    }
}
