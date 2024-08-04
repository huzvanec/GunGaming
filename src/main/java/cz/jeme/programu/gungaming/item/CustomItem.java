package cz.jeme.programu.gungaming.item;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class CustomItem extends CustomElement {
    private final @NotNull Set<String> tags = new HashSet<>();

    protected final @NotNull Rarity rarity = provideRarity();
    protected final @NotNull Component name = rarity.color().append(provideName());
    protected final @NotNull String description = Components.strip(provideDescription());
    protected final @NotNull Material material = provideMaterial();
    protected final @NotNull ItemStack item = ItemStack.of(material);
    protected final @Nullable Integer customModelData = provideCustomModelData();
    protected final int minAmount = provideMinAmount();
    protected final int maxAmount = provideMaxAmount();

    protected CustomItem() {
        addTags("item");
        item.editMeta(meta -> {
            meta.displayName(Components.of("<!i>").append(name));
            meta.setCustomModelData(customModelData);
            KEY_DATA.write(meta, key.asString());
        });
    }

    @ApiStatus.Internal
    public void init() {
        item.editMeta(meta -> meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        ));
        // ran after initialization
        updateItem(item);
    }

    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return List.of();
    }

    private static final @NotNull String LORE_PREFIX = "<!i><white>";

    public final void updateItem(final @NotNull ItemStack item) {
        if (!CustomItem.is(item, getClass()))
            throw new IllegalArgumentException("The provided item is not this CustomItem!");
        final List<Component> lore = new ArrayList<>();
        final String rarityName = Components.latinString(rarity.key().value());
        lore.add(Components.of(LORE_PREFIX + "<b>").append(rarity.color().append(Components.of(rarityName))));
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

    protected final void addTags(final String @NotNull ... addTags) {
        tags.addAll(Arrays.asList(addTags));
    }

    // providers

    protected abstract @NotNull Component provideName();

    protected abstract @NotNull String provideDescription();

    protected abstract @NotNull Material provideMaterial();

    protected @Nullable Integer provideCustomModelData() {
        return null;
    }

    protected abstract @NotNull Rarity provideRarity();

    protected abstract int provideMinAmount();

    protected abstract int provideMaxAmount();

    // getters

    public final @NotNull Component name() {
        return name;
    }

    public final @NotNull Set<String> tags() {
        return new HashSet<>(tags);
    }

    public final @NotNull Material material() {
        return material;
    }

    public final @NotNull String description() {
        return description;
    }

    public final @NotNull ItemStack item() {
        return item.clone();
    }

    public final boolean hasCustomModelData() {
        return customModelData != null;
    }

    public final int customModelData() {
        if (customModelData == null)
            throw new IllegalStateException("This custom item has no customModelData!");
        return customModelData;
    }

    public final @NotNull Rarity rarity() {
        return rarity;
    }

    public final int minAmount() {
        return minAmount;
    }

    public final int maxAmount() {
        return maxAmount;
    }

    protected void onLeftClick(final @NotNull PlayerInteractEvent event) {
    }

    protected void onLeftClickAir(final @NotNull PlayerInteractEvent event) {
    }

    protected void onLeftClickBlock(final @NotNull PlayerInteractEvent event) {
    }

    protected void onRightClick(final @NotNull PlayerInteractEvent event) {
    }

    protected void onRightClickAir(final @NotNull PlayerInteractEvent event) {
    }

    protected void onRightClickBlock(final @NotNull PlayerInteractEvent event) {
    }

    protected void onUse(final @NotNull PlayerInteractEvent event) {
    }

    // sound

    protected final @NotNull Key heldSoundKey = GunGaming.namespaced("item." + key.value() + ".held");
    protected final @NotNull Sound heldSound = Sound.sound(heldSoundKey, Sound.Source.PLAYER, 1.9F, 1);

    public @NotNull Sound heldSound(final @NotNull ItemStack item) {
        return heldSound;
    }

    // static accessors

    public static @NotNull CustomItem of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, CustomItem.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, CustomItem.class);
    }

    public static <T extends CustomItem> @NotNull T of(final @NotNull ItemStack item, final @NotNull Class<T> itemClass) {
        return CustomElement.of(CustomElement.KEY_DATA.require(item), itemClass);
    }

    public static @NotNull CustomItem of(final @NotNull ItemStack item) {
        return CustomItem.of(item, CustomItem.class);
    }


    public static boolean is(final @Nullable ItemStack item) {
        if (item == null) return false;
        return CustomElement.KEY_DATA.read(item)
                .map(CustomItem::is)
                .orElse(false);
    }

    public static boolean is(final @Nullable ItemStack item, final @NotNull Class<? extends CustomItem> itemClass) {
        if (item == null) return false;
        return CustomElement.KEY_DATA.read(item)
                .map(keyStr -> CustomElement.is(keyStr, itemClass))
                .orElse(false);
    }

    @Override
    public @NotNull String toString() {
        return Components.strip(name);
    }
}