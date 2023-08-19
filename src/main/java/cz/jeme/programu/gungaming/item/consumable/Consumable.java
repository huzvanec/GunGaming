package cz.jeme.programu.gungaming.item.consumable;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Sounds;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

public abstract class Consumable extends CustomItem {
    public @NotNull Sound sound;
    public Consumable() {
        setup();

        sound = Sounds.getSound("consumable.eat." + Sounds.formatName(this), 1f);
        Namespace.CONSUMABLE.set(item, name);
    }

    public final void consume(@NotNull PlayerItemConsumeEvent event) {
        onConsume(event);
    }

    protected void onConsume(@NotNull PlayerItemConsumeEvent event) {
    }
}
