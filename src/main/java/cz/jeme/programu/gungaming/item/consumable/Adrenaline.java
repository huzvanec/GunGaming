package cz.jeme.programu.gungaming.item.consumable;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Adrenaline extends Consumable {
    @Override
    protected final void onConsume(@NotNull PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        for (PotionEffect effect : getEffects()) {
            PotionEffect playerEffect = player.getPotionEffect(effect.getType());
            int duration;
            if (playerEffect != null) {
                 duration = effect.getDuration() + playerEffect.getDuration();
            } else {
                duration = effect.getDuration();
            }
            player.addPotionEffect(effect.withDuration(duration));
        }
    }

    abstract @NotNull List<PotionEffect> getEffects();
}
