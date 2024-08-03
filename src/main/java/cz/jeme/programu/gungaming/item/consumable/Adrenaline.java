package cz.jeme.programu.gungaming.item.consumable;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class Adrenaline extends Consumable {
    protected final @NotNull Set<PotionEffect> effects = Collections.unmodifiableSet(provideEffects());

    protected Adrenaline() {
        addTags("adrenaline");
    }

    // providers

    protected abstract @NotNull Set<PotionEffect> provideEffects();

    // getters

    public final @NotNull Set<PotionEffect> effects() {
        return effects;
    }

    // adrenaline

    @Override
    protected void onConsume(final @NotNull PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        for (final PotionEffect effect : effects) {
            final PotionEffect current = player.getPotionEffect(effect.getType());
            final boolean same = current != null && current.getAmplifier() == effect.getAmplifier();
            final int duration = same
                    ? current.getDuration() + effect.getDuration()
                    : effect.getDuration();
            player.addPotionEffect(effect.withDuration(duration));
        }
    }
}
