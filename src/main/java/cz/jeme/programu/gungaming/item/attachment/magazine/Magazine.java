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
    public static final ItemStack PLACE_HOLDER = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

    public Float magazinePercentage = null;

    static {
        ItemMeta scopeMeta = PLACE_HOLDER.getItemMeta();
        scopeMeta.displayName(Messages.from("<!italic>Magazine</!italic>"));
        scopeMeta.setCustomModelData(1);
        PLACE_HOLDER.setItemMeta(scopeMeta);
    }

    {
        id = 2;
        placeHolder = PLACE_HOLDER;
        nbt = Namespaces.GUN_MAGAZINE;
    }

    public Magazine() {
        setup();

        assert magazinePercentage != null : "Magazine enlarge percentage is null!";
    }

    public static void updateMagazine(ItemStack item) {
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
}
