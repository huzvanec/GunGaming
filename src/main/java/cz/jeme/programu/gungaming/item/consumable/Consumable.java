package cz.jeme.programu.gungaming.item.consumable;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

public abstract class Consumable extends CustomItem {
    public Consumable() {
        Namespace.CONSUMABLE.set(item, getName());
    }

    public final void consume(@NotNull PlayerItemConsumeEvent event) {
        onConsume(event);
    }

    protected void onConsume(@NotNull PlayerItemConsumeEvent event) {
    }
}
