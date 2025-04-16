package cz.jeme.gungaming.item.armor.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.armor.Helmet;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class StealthHelmet extends Helmet {
    @Override
    protected Key provideArmorKey() {
        return GunGaming.key("stealth");
    }

    @Override
    protected int provideDurability() {
        return 25;
    }

    @Override
    protected List<String> update(final ItemStack item) {
        return List.of();
    }

    @Override
    protected String provideDescription() {
        return "makes you untrackable and unable to track";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "stealth_helmet";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text("Stealth Helmet");
    }

    public static boolean hasEquipped(final Player player) {
        return CustomItem.is(player.getInventory().getHelmet(), StealthHelmet.class);
    }
}