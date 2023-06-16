package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.util.Messages;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;

public class ModifiersInfo extends ArrayList<Component> {
    public void addBuff(String buff) {
        add(Messages.from("<!italic><green>" + Messages.latin(buff) + "</green></!italic>"));
    }

    public void addNerf(String nerf) {
        add(Messages.from("<!italic><red>" + Messages.latin(nerf) + "</red></!italic>"));
    }
}