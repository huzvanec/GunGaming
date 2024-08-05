package cz.jeme.programu.gungaming.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;

public final class Maps {
    private Maps() {
        throw new AssertionError();
    }

    public static byte direction(final float yaw) {
        if (-180 > yaw || yaw > 180)
            throw new IllegalArgumentException("Invalid yaw: " + yaw);
        return (byte) ((yaw / 22.5F + 16.5F) % 16F);
    }

    /**
     * Method copied from {@link net.minecraft.world.item.MapItem#update(Level, Entity, MapItemSavedData)} to make it static.
     *
     * @param world  world for the map to be drawn in.
     * @param entity entity holding (and rendering) the map.
     * @param state  the data of the map.
     */
    public static void update(final @NotNull Level world, final @NotNull Entity entity, final @NotNull MapItemSavedData state) {
        if (world.dimension() == state.dimension && entity instanceof Player) {
            final int i = 1 << state.scale;
            final int j = state.centerX;
            final int k = state.centerZ;
            final int l = Mth.floor(entity.getX() - (double) j) / i + 64;
            final int m = Mth.floor(entity.getZ() - (double) k) / i + 64;
            int n = 128 / i;
            if (world.dimensionType().hasCeiling()) {
                n /= 2;
            }

            final MapItemSavedData.HoldingPlayer holdingPlayer = state.getHoldingPlayer((Player) entity);
            holdingPlayer.step++;
            final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            final BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
            boolean bl = false;

            for (int o = l - n + 1; o < l + n; o++) {
                if ((o & 15) == (holdingPlayer.step & 15) || bl) {
                    bl = false;
                    double d = 0.0;

                    for (int p = m - n - 1; p < m + n; p++) {
                        if (o >= 0 && p >= -1 && o < 128 && p < 128) {
                            final int q = Mth.square(o - l) + Mth.square(p - m);
                            final boolean bl2 = q > (n - 2) * (n - 2);
                            final int r = (j / i + o - 64) * i;
                            final int s = (k / i + p - 64) * i;
                            final Multiset<MapColor> multiset = LinkedHashMultiset.create();
                            final LevelChunk levelChunk = world.getChunkIfLoaded(SectionPos.blockToSectionCoord(r), SectionPos.blockToSectionCoord(s)); // Paper - Maps shouldn't load chunks
                            if (levelChunk != null && !levelChunk.isEmpty()) { // Paper - Maps shouldn't load chunks
                                int t = 0;
                                double e = 0.0;
                                if (world.dimensionType().hasCeiling()) {
                                    int u = r + s * 231871;
                                    u = u * u * 31287121 + u * 11;
                                    if ((u >> 20 & 1) == 0) {
                                        multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(world, BlockPos.ZERO), 10);
                                    } else {
                                        multiset.add(Blocks.STONE.defaultBlockState().getMapColor(world, BlockPos.ZERO), 100);
                                    }

                                    e = 100.0;
                                } else {
                                    for (int v = 0; v < i; v++) {
                                        for (int w = 0; w < i; w++) {
                                            mutableBlockPos.set(r + v, 0, s + w);
                                            int x = levelChunk.getHeight(Heightmap.Types.WORLD_SURFACE, mutableBlockPos.getX(), mutableBlockPos.getZ()) + 1;
                                            BlockState blockState3;
                                            if (x <= world.getMinBuildHeight() + 1) {
                                                blockState3 = Blocks.BEDROCK.defaultBlockState();
                                            } else {
                                                do {
                                                    mutableBlockPos.setY(--x);
                                                    blockState3 = levelChunk.getBlockState(mutableBlockPos);
                                                } while (blockState3.getMapColor(world, mutableBlockPos) == MapColor.NONE && x > world.getMinBuildHeight());

                                                if (x > world.getMinBuildHeight() && !blockState3.getFluidState().isEmpty()) {
                                                    int y = x - 1;
                                                    mutableBlockPos2.set(mutableBlockPos);

                                                    BlockState blockState2;
                                                    do {
                                                        mutableBlockPos2.setY(y--);
                                                        blockState2 = levelChunk.getBlockState(mutableBlockPos2);
                                                        t++;
                                                    } while (y > world.getMinBuildHeight() && !blockState2.getFluidState().isEmpty());

                                                    blockState3 = getCorrectStateForFluidBlock(world, blockState3, mutableBlockPos);
                                                }
                                            }

                                            state.checkBanners(world, mutableBlockPos.getX(), mutableBlockPos.getZ());
                                            e += (double) x / (double) (i * i);
                                            multiset.add(blockState3.getMapColor(world, mutableBlockPos));
                                        }
                                    }
                                }

                                t /= i * i;
                                final MapColor mapColor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.NONE);
                                final MapColor.Brightness brightness;
                                if (mapColor == MapColor.WATER) {
                                    final double f = (double) t * 0.1 + (double) (o + p & 1) * 0.2;
                                    if (f < 0.5) {
                                        brightness = MapColor.Brightness.HIGH;
                                    } else if (f > 0.9) {
                                        brightness = MapColor.Brightness.LOW;
                                    } else {
                                        brightness = MapColor.Brightness.NORMAL;
                                    }
                                } else {
                                    final double g = (e - d) * 4.0 / (double) (i + 4) + ((double) (o + p & 1) - 0.5) * 0.4;
                                    if (g > 0.6) {
                                        brightness = MapColor.Brightness.HIGH;
                                    } else if (g < -0.6) {
                                        brightness = MapColor.Brightness.LOW;
                                    } else {
                                        brightness = MapColor.Brightness.NORMAL;
                                    }
                                }

                                d = e;
                                if (p >= 0 && q < n * n && (!bl2 || (o + p & 1) != 0)) {
                                    bl |= state.updateColor(o, p, mapColor.getPackedId(brightness));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Method copied from {@link net.minecraft.world.item.MapItem} to make it static.
     */
    private static @NotNull BlockState getCorrectStateForFluidBlock(final @NotNull Level world, final @NotNull BlockState state, final @NotNull BlockPos pos) {
        final FluidState fluidState = state.getFluidState();
        return !fluidState.isEmpty() && !state.isFaceSturdy(world, pos, Direction.UP) ? fluidState.createLegacyBlock() : state;
    }
}
