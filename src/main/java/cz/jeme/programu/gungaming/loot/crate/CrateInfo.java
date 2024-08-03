package cz.jeme.programu.gungaming.loot.crate;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CrateInfo(
        @NotNull Crate crate,
        @NotNull Inventory inventory
) {
    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof final CrateInfo crateInfo)) return false;

        return crate.equals(crateInfo.crate) && inventory.equals(crateInfo.inventory);
    }

    @Override
    public int hashCode() {
        int result = crate.hashCode();
        result = 31 * result + inventory.hashCode();
        return result;
    }
}
