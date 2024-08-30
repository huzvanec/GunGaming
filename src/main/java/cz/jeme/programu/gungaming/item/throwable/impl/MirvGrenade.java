package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.item.throwable.Grenade;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.item.throwable.ThrownHelper;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Lores;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MirvGrenade extends Grenade {
    public static final double HORIZONTAL_POWER = 0.4;
    public static final double VERTICAL_POWER = 0.4;

    @Override
    protected int provideThrowCooldown() {
        return 50;
    }

    @Override
    protected double provideMaxDamage() {
        return 25;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("MIRV Grenade");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Explodes into eight small grenades";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 2;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "mirv_grenade";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 3;
    }

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        final String damageStr = Lores.STATS_FORMATTER.format(maxDamage) + " + 8×"
                                 + Lores.STATS_FORMATTER.format(SmallGrenade.MAX_DAMAGE);
        return List.of(Lores.loreStat("Damage", damageStr));
    }

    @Override
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        final Location location = thrown.getLocation();
        location.createExplosion(thrown, 5F, false, true);
        final SmallGrenade smallGrenade = CustomElement.of(SmallGrenade.class);
        final ItemStack smallGrenadeItem = smallGrenade.item();
        for (int deg = 0; deg < 360; deg += 45) {
            final double rad = Math.toRadians(deg);
            final double x = Math.cos(rad) * HORIZONTAL_POWER;
            final double z = Math.sin(rad) * HORIZONTAL_POWER;
            Objects.requireNonNull(thrown.getShooter(), "Shooter is null!").launchProjectile(
                    Snowball.class,
                    new Vector(x, VERTICAL_POWER, z),
                    snowball -> {
                        ThrownHelper.THROWABLE_KEY_DATA.write(snowball, smallGrenade.key().asString());
                        ThrownHelper.MAX_DAMAGE_DATA.write(snowball, Throwable.MAX_DAMAGE_DATA.require(smallGrenadeItem));
                        snowball.setItem(smallGrenadeItem);
                        snowball.teleport(location);
                    }
            );
        }
    }
}
