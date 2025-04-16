package cz.jeme.gungaming.item.tool.impl;

import cz.jeme.gungaming.item.tool.Tool;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.datacomponent.item.Tool.Builder;
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.bukkit.Registry;
import org.jspecify.annotations.NullMarked;

import static io.papermc.paper.datacomponent.item.Tool.rule;

@NullMarked
public class DiamondShovel extends Tool {
    @Override
    protected String provideDescription() {
        return "great for breaking dirt in the way";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected void buildTool(final Builder builder) {
        builder.addRule(rule(
                Registry.BLOCK.getTag(BlockTypeTagKeys.MINEABLE_SHOVEL),
                8F,
                TriState.TRUE
        ));
    }

    @Override
    protected int provideDurability() {
        return 50;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "diamond_shovel";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected Component provideName() {
        return Component.text("Diamond Shovel");
    }
}
