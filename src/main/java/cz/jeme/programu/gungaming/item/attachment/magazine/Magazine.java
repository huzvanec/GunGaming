package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Magazine extends Attachment {
    public Float magazinePercentage = null;

    public Magazine() {
        setup();
        assert magazinePercentage != null : "Magazine enlarge percentage is null!";
        ItemMeta scopeMeta = placeHolder.getItemMeta();
        scopeMeta.displayName(Messages.from("<!italic><gray>Magazine</gray></!italic>"));
        scopeMeta.setCustomModelData(1);
        placeHolder.setItemMeta(scopeMeta);
    }

    @Override
    public int getSlotId() {
        return 1;
    }

    @Override
    public Namespaces getNbt() {
        return Namespaces.GUN_MAGAZINE;
    }

    @Override
    protected Material getMaterial() {
        return Material.WOODEN_AXE;
    }

    public static void update(ItemStack item) {
        String magazineName = Namespaces.GUN_MAGAZINE.get(item);
        Gun gun = Guns.getGun(item);
        if (magazineName.equals("")) {
            Namespaces.GUN_RELOAD_COOLDOWN.set(item, gun.reloadCooldown);
            return;
        }
        Magazine magazine = (Magazine) Attachments.getAttachment(magazineName);
        float newReloadCooldown = gun.reloadCooldown * (magazine.magazinePercentage / 100f);
        Namespaces.GUN_RELOAD_COOLDOWN.set(item, Math.round(newReloadCooldown));
    }

    @Override
    protected Class<? extends Attachment> getGroupClass() {
        return Magazine.class;
    }
}
