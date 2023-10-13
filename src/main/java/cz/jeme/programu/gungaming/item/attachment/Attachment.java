package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.SingletonLoot;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.registry.Attachments;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


public abstract class Attachment extends CustomItem implements SingletonLoot {
    private final @NotNull Set<Component> modifiersInfo = new HashSet<>();
    protected final @NotNull ItemStack placeHolder = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);

    public Attachment() {
        getBuffs().forEach(
                b -> modifiersInfo.add(Message.from("<!italic><green>" + Message.latin(b) + "</green></!italic>"))
        );

        getNerfs().forEach(
                n -> modifiersInfo.add(Message.from("<!italic><red>" + Message.latin(n) + "</red></!italic>"))
        );

        Namespace.ATTACHMENT.set(item, getName());

        if (!Attachments.placeHolders.containsKey(getAttachmentType())) {
            Attachments.placeHolders.put(getAttachmentType(), placeHolder);
        }
    }

    @Override
    public final int getMinStackLoot() {
        return 1;
    }

    @Override
    public final int getMaxStackLoot() {
        return 1;
    }

    public @NotNull ItemStack getPlaceHolder(@NotNull Gun gun) {
        return placeHolder;
    }

    public final @NotNull Set<Component> getModifiersInfo() {
        return modifiersInfo;
    }

    protected abstract @NotNull Class<? extends Attachment> getAttachmentType();

    public abstract int getSlotId();

    public abstract @NotNull Namespace getNamespace();

    protected abstract @NotNull Set<String> getBuffs();

    protected abstract @NotNull Set<String> getNerfs();
}