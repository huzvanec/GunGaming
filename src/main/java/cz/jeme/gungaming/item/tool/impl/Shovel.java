package cz.jeme.gungaming.item.tool.impl;

import cz.jeme.gungaming.item.tool.Tool;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.datacomponent.item.Tool.Builder;
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

import static io.papermc.paper.datacomponent.item.Tool.rule;

public class Shovel extends Tool {
    @Override
    protected @NotNull String provideDescription() {
        return "great for breaking dirt in the way";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected void buildTool(final Builder builder) {
        builder.addRule(rule(
                Registry.BLOCK.getTag(BlockTypeTagKeys.MINEABLE_SHOVEL),
                9F,
                TriState.TRUE
        ));
    }

    @Override
    protected int provideDurability() {
        return 50;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "shovel";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Shovel");
    }
}
