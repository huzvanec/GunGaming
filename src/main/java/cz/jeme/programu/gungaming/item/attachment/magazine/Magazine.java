package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class Magazine extends Attachment {
    public Float magazinePercentage = null;

    public Magazine() {
        setup();
        assert magazinePercentage != null : "Magazine enlarge percentage is null!";
        ItemMeta scopeMeta = placeHolder.getItemMeta();
        scopeMeta.displayName(Message.from("<!italic><gray>Magazine</gray></!italic>"));
        scopeMeta.setCustomModelData(1);
        placeHolder.setItemMeta(scopeMeta);
    }

    @Override
    public int getSlotId() {
        return 1;
    }

    @Override
    public @NotNull Namespace getNbt() {
        return Namespace.GUN_MAGAZINE;
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.WOODEN_AXE;
    }

    public static void update(ItemStack item) {
        String magazineName = Namespace.GUN_MAGAZINE.get(item);
        assert magazineName != null : "Magazine name is null!";
        Gun gun = Guns.getGun(item);
        if (magazineName.isEmpty()) {
            Namespace.GUN_RELOAD_COOLDOWN.set(item, gun.reloadCooldown);
            return;
        }
        Magazine magazine = (Magazine) Attachments.getAttachment(magazineName);
        assert magazine != null : "Magazine is null!";
        if (!gun.ammoType.equals(TwelveGauge.class)) {
            float newReloadCooldown = gun.reloadCooldown * (magazine.magazinePercentage / 100f);
            Namespace.GUN_RELOAD_COOLDOWN.set(item, Math.round(newReloadCooldown));
        }
    }

    @Override
    protected @NotNull Class<? extends Attachment> getGroupClass() {
        return Magazine.class;
    }
}
