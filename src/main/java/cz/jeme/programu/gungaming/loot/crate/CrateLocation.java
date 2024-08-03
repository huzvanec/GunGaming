package cz.jeme.programu.gungaming.loot.crate;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record CrateLocation(
        int x,
        int y,
        int z,
        @NotNull UUID worldUid
) {
    public CrateLocation(final @NotNull Block block) {
        this(
                block.getX(),
                block.getY(),
                block.getZ(),
                block.getWorld().getUID()
        );
    }

    public CrateLocation(final @NotNull Location location) {
        this(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                location.getWorld().getUID()
        );
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof final CrateLocation that)) return false;

        return x == that.x && y == that.y && z == that.z && worldUid.equals(that.worldUid);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + worldUid.hashCode();
        return result;
    }
}