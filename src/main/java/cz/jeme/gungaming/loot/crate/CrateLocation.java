package cz.jeme.gungaming.loot.crate;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@NullMarked
public record CrateLocation(
        int x,
        int y,
        int z,
        UUID worldUid
) {
    public CrateLocation(final Block block) {
        this(
                block.getX(),
                block.getY(),
                block.getZ(),
                block.getWorld().getUID()
        );
    }

    public CrateLocation(final Location location) {
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
        if (!(o instanceof CrateLocation(final int thatX, final int thatY, final int thatZ, final UUID thatWorldUuid)))
            return false;

        return x == thatX && y == thatY && z == thatZ && worldUid.equals(thatWorldUuid);
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