package cz.jeme.gungaming.loot.crate;

import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record CrateInfo(
        Crate crate,
        Inventory inventory
) {
    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof CrateInfo(final Crate thatCrate, final Inventory thatInventory))) return false;

        return crate.equals(thatCrate) && inventory.equals(thatInventory);
    }

    @Override
    public int hashCode() {
        int result = crate.hashCode();
        result = 31 * result + inventory.hashCode();
        return result;
    }
}
