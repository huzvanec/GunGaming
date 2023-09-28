package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.item.Attachments;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public abstract class Attachment extends CustomItem implements SingleLoot {
    public final @NotNull List<Component> modifiersInfo = new ArrayList<>();
    protected final @NotNull ItemStack placeHolder = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    public Attachment() {
        setup();

        addBuffs(getBuffs());
        addNerfs(getNerfs());

        Namespace.ATTACHMENT.set(item, name);
        addPlaceHolder();
    }

    @Override
    public final int getMinLoot() {
        return 1;
    }

    @Override
    public final int getMaxLoot() {
        return 1;
    }

    private void addBuffs(@NotNull String[] buffArray) {
        for (String buff : buffArray) {
            addModifier(buff);
        }
    }

    private void addNerfs(@NotNull String[] nerfArray) {
        for (String nerf : nerfArray) {
            addNerf(nerf);
        }
    }

    private void addModifier(@NotNull String buff) {
        modifiersInfo.add(Message.from("<!italic><green>" + Message.latin(buff) + "</green></!italic>"));
    }

    private void addNerf(@NotNull String nerf) {
        modifiersInfo.add(Message.from("<!italic><red>" + Message.latin(nerf) + "</red></!italic>"));
    }

    private void addPlaceHolder() {
        if (!Attachments.placeHolders.containsKey(getGroupClass())) {
            Attachments.placeHolders.put(getGroupClass(), placeHolder);
        }
    }

    public @NotNull ItemStack getPlaceHolder(@NotNull Gun gun) {
        return placeHolder;
    }

    abstract protected @NotNull Class<? extends Attachment> getGroupClass();
    abstract public int getSlotId();
    abstract public @NotNull Namespace getNbt();
    abstract protected @NotNull String[] getBuffs();
    abstract protected @NotNull String[] getNerfs();
}