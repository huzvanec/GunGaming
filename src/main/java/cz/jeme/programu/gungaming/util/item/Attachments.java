package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.util.Lores;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Attachments {

    public static @NotNull Map<String, Attachment> attachments = new HashMap<>();
    public static @NotNull Map<Class<? extends Attachment>, ItemStack> placeHolders = new HashMap<>();

    private Attachments() {
        // Static class cannot be initialized
    }

    public static boolean isAttachment(@Nullable ItemStack item) {
        return Namespace.ATTACHMENT.has(item);
    }

    public static @Nullable Attachment getAttachment(@NotNull String name) {
        return attachments.get(name);
    }

    public static @NotNull Attachment getAttachment(@NotNull ItemStack item) {
        String name = Namespace.ATTACHMENT.get(item);
        assert name != null : "This item is not an Attachment!";
        Attachment attachment = getAttachment(name);
        assert attachment != null : "This item has an Attachment tag, that doesn't represent any registered Attachment!";
        return attachment;
    }

    public static void register(@NotNull Attachment attachment) {
        attachments.put(attachment.name, attachment);
        Lores.update(attachment.item);
    }

    public static void registered() {
        attachments = Collections.unmodifiableMap(attachments);
    }
}
