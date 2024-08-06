package cz.jeme.programu.gungaming.item.block;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BlockEventHandler {
    public static final @NotNull Data<Byte, Boolean> ITEM_MODIFIED_DATA = Data.ofBoolean(GunGaming.namespaced("item_modified"));

    private BlockEventHandler() {
        throw new AssertionError();
    }

    public static void onItemSpawn(final @NotNull ItemSpawnEvent event) {
        final ItemStack item = event.getEntity().getItemStack();
        if (CustomItem.is(item)) return;
        final Material material = item.getType();
        if (CustomBlock.BLOCK_REGISTRY.containsKey(material)) {
            // custom blocks
            event.getEntity().setItemStack(CustomBlock.BLOCK_REGISTRY.get(material).item());
            return;
        }
        if (ITEM_MODIFIED_DATA.read(item).orElse(false)) return;
        item.editMeta(meta -> {
            final Rarity rarity = getRarity(material);
            meta.itemName(rarity.color().append(Component.translatable(item)));
            final String rarityName = rarity.key().value();
            final String typeStr = material.isBlock() ? " block" : " item";
            meta.lore(List.of(
                    Components.of("<!i><b>")
                            .append(rarity.color()
                                    .append(
                                            Component.text(Components.latinString(rarityName + typeStr))
                                    )
                            )
            ));
            ITEM_MODIFIED_DATA.write(meta, true);
        });
    }

    // using Paper internal api to workaround issues with rarity
    // https://github.com/PaperMC/Paper/issues/11040
    // https://github.com/PaperMC/Paper/pull/11049
    @SuppressWarnings("UnstableApiUsage")
    private static @NotNull Rarity getRarity(final @NotNull Material material) {
        final ItemType type = material.asItemType();
        if (type == null) return Rarity.COMMON;
        final ItemRarity rarity = type.getItemRarity();
        if (rarity == null) return Rarity.COMMON;
        return switch (rarity) {
            case COMMON -> Rarity.COMMON;
            case UNCOMMON -> Rarity.UNCOMMON;
            case RARE -> Rarity.RARE;
            case EPIC -> Rarity.LEGENDARY;
        };
    }
}
