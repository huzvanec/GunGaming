package cz.jeme.programu.gungaming.item.impl;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TestItem extends CustomItem {

    private TestItem() {
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "test_item";
    }

    @Override
    protected @NotNull Component provideName() {
        return Components.of("<rainbow><b><u><obf>#</obf> Test Item <obf>#");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "a very sticky stick used for testing";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.STICK;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNOBTAINABLE;
    }

    @Override
    protected int provideMinAmount() {
        return 0;
    }

    @Override
    protected int provideMaxAmount() {
        return 0;
    }

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return List.of(":)");
    }

    @Override
    protected void onLeftClick(final @NotNull PlayerInteractEvent event) {
        event.getPlayer().sendMessage(Components.of("<red>Left click!"));
    }
}
