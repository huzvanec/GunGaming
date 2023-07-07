package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.item.Attachments;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public abstract class Attachment extends CustomItem implements SingleLoot {
    public final List<Component> modifiersInfo = new ArrayList<>();
    public final ItemStack placeHolder = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    public Attachment() {
        setup();

        addBuffs(getBuffs());
        addNerfs(getNerfs());

        Namespaces.ATTACHMENT.set(item, name);
        addPlaceHolder();
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 1;
    }

    private void addBuffs(String[] buffArray) {
        for (String buff : buffArray) {
            addModifier(buff);
        }
    }

    private void addNerfs(String[] nerfArray) {
        for (String nerf : nerfArray) {
            addNerf(nerf);
        }
    }

    private void addModifier(String buff) {
        modifiersInfo.add(Messages.from("<!italic><green>" + Messages.latin(buff) + "</green></!italic>"));
    }

    private void addNerf(String nerf) {
        modifiersInfo.add(Messages.from("<!italic><red>" + Messages.latin(nerf) + "</red></!italic>"));
    }

    private void addPlaceHolder() {
        if (!Attachments.placeHolders.containsKey(getGroupClass())) {
            Attachments.placeHolders.put(getGroupClass(), placeHolder);
        }
    }

    abstract protected Class<? extends Attachment> getGroupClass();
    abstract public int getSlotId();
    abstract public Namespaces getNbt();
    abstract protected String[] getBuffs();
    abstract protected String[] getNerfs();
}