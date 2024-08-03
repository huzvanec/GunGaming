package cz.jeme.programu.gungaming.item.block;

import cz.jeme.programu.gungaming.item.CustomItem;

public abstract class CustomBlock extends CustomItem {
    protected CustomBlock() {
        if (!material.isBlock())
            throw new IllegalArgumentException("A CustomBlock must have a block material!");
        addTags("block");
    }
}
