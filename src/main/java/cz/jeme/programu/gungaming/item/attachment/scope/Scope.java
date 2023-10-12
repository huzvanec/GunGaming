package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.util.Message;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class Scope extends Attachment {

    public @NotNull Double scope;

    public Scope() {
        setup();

        assert scope != null : "Scope level is null!";

        ItemMeta scopeMeta = placeHolder.getItemMeta();
        scopeMeta.displayName(Message.from("<!italic><gray>Scope</gray></!italic>"));
        scopeMeta.setCustomModelData(2);
        placeHolder.setItemMeta(scopeMeta);
    }

    @Override
    protected final @NotNull Material getMaterial() {
        return Material.CREEPER_BANNER_PATTERN;
    }

    @Override
    public final int getSlotId() {
        return 2;
    }

    @Override
    public final @NotNull Namespace getNbt() {
        return Namespace.GUN_SCOPE;
    }

    @Override
    protected final @NotNull Class<? extends Attachment> getGroupClass() {
        return Scope.class;
    }
}
