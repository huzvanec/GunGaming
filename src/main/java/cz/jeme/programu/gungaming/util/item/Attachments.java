package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Namespaces;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Attachments {

    public static Map<String, Attachment> attachments = new HashMap<>();

    private Attachments() {
    }

    public static boolean isAttachment(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return Namespaces.ATTACHMENT.has(meta);
    }

    public static Attachment getAttachment(String name) {
        return attachments.get(name);
    }

    public static Attachment getAttachment(Component component) {
        return getAttachment(Messages.strip(component));
    }

    public static Attachment getAttachment(ItemStack item) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        return getAttachment(meta.displayName());
    }

    public static void register(Attachment attachment) {
        attachments.put(attachment.name, attachment);
    }

    public static void setUnmodifiable() {
        attachments = Collections.unmodifiableMap(attachments);
    }
}
