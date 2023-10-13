package cz.jeme.programu.gungaming.item.consumable.adrenaline;

import cz.jeme.programu.gungaming.item.consumable.Consumable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class Adrenaline extends Consumable {
    protected abstract @NotNull Set<PotionEffect> getEffects();

    @Override
    protected final void onConsume(@NotNull PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        for (PotionEffect effect : getEffects()) {
            PotionEffect playerEffect = player.getPotionEffect(effect.getType());
            int duration = playerEffect == null ? effect.getDuration() : effect.getDuration() + playerEffect.getDuration();
            player.addPotionEffect(effect.withDuration(duration));
        }
    }
}
