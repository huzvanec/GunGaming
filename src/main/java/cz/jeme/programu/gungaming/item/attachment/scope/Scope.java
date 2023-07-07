package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.util.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Scope extends Attachment {

    public Double scope = null;

    public Scope() {
        setup();

        assert scope != null : "Scope level is null!";

        ItemMeta scopeMeta = placeHolder.getItemMeta();
        scopeMeta.displayName(Messages.from("<!italic><gray>Scope</gray></!italic>"));
        scopeMeta.setCustomModelData(2);
        placeHolder.setItemMeta(scopeMeta);
    }

    @Override
    protected Material getMaterial() {
        return Material.STONE_AXE;
    }

    @Override
    public int getSlotId() {
        return 2;
    }

    @Override
    public Namespaces getNbt() {
        return Namespaces.GUN_SCOPE;
    }
    @Override
    protected Class<? extends Attachment> getGroupClass() {
        return Scope.class;
    }
}
