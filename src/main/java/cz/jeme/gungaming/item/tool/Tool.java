package cz.jeme.gungaming.item.tool;

import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.loot.SingleLoot;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Tool.Builder;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import static io.papermc.paper.datacomponent.item.Tool.tool;

@NullMarked
public abstract class Tool extends CustomItem implements SingleLoot {
    protected final int durability = provideDurability();

    @SuppressWarnings("UnstableApiUsage")
    protected Tool() {
        addTags("tool");

        final var builder = tool();
        buildTool(builder);
        item.setData(DataComponentTypes.TOOL, builder.build());

        item.editMeta(meta -> meta.setMaxStackSize(1));

        item.setData(DataComponentTypes.DAMAGE, 0);
        if (durability <= 0) item.unsetData(DataComponentTypes.MAX_DAMAGE);
        else item.setData(DataComponentTypes.MAX_DAMAGE, durability);
    }

    protected int provideDurability() {
        return -1;
    }

    @SuppressWarnings("UnstableApiUsage")
    protected void buildTool(final Builder builder) {
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    @Override
    protected final String provideType() {
        return "tool";
    }

    @Override
    protected final Material provideMaterial() {
        return Material.POPPED_CHORUS_FRUIT;
    }
}
