package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.util.Message;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class Scope extends Attachment {
    {
        assert getScopeMultiplier() >= 0D && getScopeMultiplier() <= 10D : "Scope multiplier is a number between 1.0 and 10.0!";

        ItemMeta scopeMeta = placeHolder.getItemMeta();
        scopeMeta.displayName(Message.from("<!italic><gray>Scope</gray></!italic>"));
        scopeMeta.setCustomModelData(2);
        placeHolder.setItemMeta(scopeMeta);
    }

    @Override
    public final @NotNull Material getMaterial() {
        return Material.CREEPER_BANNER_PATTERN;
    }

    @Override
    public final int getSlotId() {
        return 2;
    }

    @Override
    public final @NotNull Namespace getNamespace() {
        return Namespace.GUN_SCOPE;
    }

    public abstract double getScopeMultiplier();

    @Override
    protected final @NotNull Class<? extends Attachment> getAttachmentType() {
        return Scope.class;
    }
}