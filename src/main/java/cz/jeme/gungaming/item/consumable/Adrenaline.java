package cz.jeme.gungaming.item.consumable;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.Set;

@NullMarked
public abstract class Adrenaline extends Consumable {
    protected final Set<PotionEffect> effects = Collections.unmodifiableSet(provideEffects());

    protected Adrenaline() {
        addTags("adrenaline");
    }

    // providers

    protected abstract Set<PotionEffect> provideEffects();

    // getters

    public final Set<PotionEffect> effects() {
        return effects;
    }

    // adrenaline

    @Override
    protected void onConsume(final PlayerItemConsumeEvent event) {

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
