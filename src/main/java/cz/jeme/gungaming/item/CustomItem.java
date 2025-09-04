package cz.jeme.gungaming.item;

import com.google.common.collect.HashMultimap;
import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.loot.crate.Crate;
import cz.jeme.gungaming.util.Components;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

@NullMarked
public abstract class CustomItem extends CustomElement {
    private final Set<String> tags = new HashSet<>();

    protected final String description = Components.strip(provideDescription());
    protected final Material material = provideMaterial();
    protected final ItemStack item = ItemStack.of(material);
    protected final int minAmount = provideMinAmount();
    protected final int maxAmount = provideMaxAmount();
    protected final String type = Components.strip(provideType());

    @SuppressWarnings("UnstableApiUsage")
    protected CustomItem() {
        addTags("item");
        item.editMeta(meta -> {
            meta.itemName(name);
            meta.setAttributeModifiers(HashMultimap.create());
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            KEY_DATA.write(meta, key.asString());
        });
        if (!provideUsesDefaultModel())
            item.setData(DataComponentTypes.ITEM_MODEL, key);
    }

    @ApiStatus.Internal
    public void init() {
        // ran after initialization
        updateItem(item);
    }

    protected List<String> update(final ItemStack item) {
        return List.of();
    }

    private static final String LORE_PREFIX = "<!i><white>";

    public final void updateItem(final ItemStack item) {
        if (!CustomItem.is(item, getClass()))
            throw new IllegalArgumentException("The provided item is not this CustomItem!");
        final List<Component> lore = new ArrayList<>();
        final String rarityName = rarity.key().value();
        lore.add(Components.of(LORE_PREFIX + "<b>").append(rarity.color().append(
                Component.text(Components.latinString(rarityName + " " + type))
        )));
        lore.add(Components.of(LORE_PREFIX + "<#CADCFF>" + Components.latinString(description)));
        final List<String> childLore = update(item);
        if (!childLore.isEmpty()) {
            lore.add(Component.empty());
            lore.addAll(update(item).stream()
                    .map(loreLine -> Components.of(LORE_PREFIX + loreLine))
                    .toList()
            );
        }
        item.editMeta(meta -> meta.lore(lore));
    }

    protected final void addTags(final String... addTags) {
        tags.addAll(Arrays.asList(addTags));
    }

    @ApiStatus.Internal
    public void generated(final ItemStack item, final Crate crate) {
    }

    // providers

    protected boolean provideUsesDefaultModel() {
        return false;
    }

    protected abstract String provideDescription();

    protected abstract Material provideMaterial();

    protected abstract int provideMinAmount();

    protected abstract int provideMaxAmount();

    protected String provideType() {
        return "item";
    }

    // getters

    public final Set<String> tags() {
        return new HashSet<>(tags);
    }

    public final Material material() {
        return material;
    }

    public final String description() {
        return description;
    }

    public final ItemStack createStack() {
        return item.clone();
    }

    public final int minAmount() {
        return minAmount;
    }

    public final int maxAmount() {
        return maxAmount;
    }

    public final String type() {
        return type;
    }

    protected void onLeftClick(final PlayerInteractEvent event) {
    }

    protected void onLeftClickAir(final PlayerInteractEvent event) {
    }

    protected void onLeftClickBlock(final PlayerInteractEvent event) {
    }

    protected void onRightClick(final PlayerInteractEvent event) {
    }

    protected void onRightClickAir(final PlayerInteractEvent event) {
    }

    protected void onRightClickBlock(final PlayerInteractEvent event) {
    }

    protected void onUse(final PlayerInteractEvent event) {
    }

    // sound

    protected final Key heldSoundKey = GunGaming.key("item." + key.value() + ".held");
    protected final Sound heldSound = Sound.sound(heldSoundKey, Sound.Source.PLAYER, 1.9F, 1);

    public Sound heldSound(final ItemStack item) {
        return heldSound;
    }

    // static accessors

    public static CustomItem of(final String keyStr) {
        return CustomElement.of(keyStr, CustomItem.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, CustomItem.class);
    }

    public static <T extends CustomItem> T of(final ItemStack item, final Class<T> itemClass) {
        return CustomElement.of(CustomElement.KEY_DATA.require(item), itemClass);
    }

    public static CustomItem of(final ItemStack item) {
        return CustomItem.of(item, CustomItem.class);
    }


    public static boolean is(final @Nullable ItemStack item) {
        if (item == null) return false;
        return CustomElement.KEY_DATA.read(item)
                .map(CustomItem::is)
                .orElse(false);
    }

    public static boolean is(final @Nullable ItemStack item, final Class<? extends CustomItem> itemClass) {
        if (item == null) return false;
        return CustomElement.KEY_DATA.read(item)
                .map(keyStr -> CustomElement.is(keyStr, itemClass))
                .orElse(false);
    }

    @Override
    public String toString() {
        return Components.strip(name);
    }
}