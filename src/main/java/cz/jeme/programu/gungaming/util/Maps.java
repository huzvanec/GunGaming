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
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class Maps {
    private Maps() {
        // Static class cannot be initialized
    }

    public static void update(Level world, Entity entity, MapItemSavedData state) {
        if (world.dimension() == state.dimension && entity instanceof Player) {
            int i = 1 << state.scale;
            int j = state.centerX;
            int k = state.centerZ;
            int l = Mth.floor(entity.getX() - (double) j) / i + 64;
            int i1 = Mth.floor(entity.getZ() - (double) k) / i + 64;
            int j1 = 128 / i;
            if (world.dimensionType().hasCeiling()) {
                j1 /= 2;
            }

            MapItemSavedData.HoldingPlayer worldmap_worldmaphumantracker = state.getHoldingPlayer((Player) entity);
            ++worldmap_worldmaphumantracker.step;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();
            boolean flag = false;

            for (int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
                if ((k1 & 15) == (worldmap_worldmaphumantracker.step & 15) || flag) {
                    flag = false;
                    double d0 = 0.0;

                    for (int l1 = i1 - j1 - 1; l1 < i1 + j1; ++l1) {
                        if (k1 >= 0 && l1 >= -1 && k1 < 128 && l1 < 128) {
                            int i2 = Mth.square(k1 - l) + Mth.square(l1 - i1);
                            boolean flag1 = i2 > (j1 - 2) * (j1 - 2);
                            int j2 = (j / i + k1 - 64) * i;
                            int k2 = (k / i + l1 - 64) * i;
                            Multiset<MapColor> multiset = LinkedHashMultiset.create();
                            LevelChunk chunk = world.getChunkIfLoaded(SectionPos.blockToSectionCoord(j2), SectionPos.blockToSectionCoord(k2));
                            if (chunk != null && !chunk.isEmpty()) {
                                int l2 = 0;
                                double d1 = 0.0;
                                int i3;
                                if (world.dimensionType().hasCeiling()) {
                                    i3 = j2 + k2 * 231871;
                                    i3 = i3 * i3 * 31287121 + i3 * 11;
                                    if ((i3 >> 20 & 1) == 0) {
                                        multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(world, BlockPos.ZERO), 10);
                                    } else {
                                        multiset.add(Blocks.STONE.defaultBlockState().getMapColor(world, BlockPos.ZERO), 100);
                                    }

                                    d1 = 100.0;
                                } else {
                                    for (i3 = 0; i3 < i; ++i3) {
                                        for (int j3 = 0; j3 < i; ++j3) {
                                            blockposition_mutableblockposition.set(j2 + i3, 0, k2 + j3);
                                            int k3 = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockposition_mutableblockposition.getX(), blockposition_mutableblockposition.getZ()) + 1;
                                            BlockState iblockdata;
                                            if (k3 <= world.getMinBuildHeight() + 1) {
                                                iblockdata = Blocks.BEDROCK.defaultBlockState();
                                            } else {
                                                do {
                                                    --k3;
                                                    blockposition_mutableblockposition.setY(k3);
                                                    iblockdata = chunk.getBlockState(blockposition_mutableblockposition);
                                                } while (iblockdata.getMapColor(world, blockposition_mutableblockposition) == MapColor.NONE && k3 > world.getMinBuildHeight());

                                                if (k3 > world.getMinBuildHeight() && !iblockdata.getFluidState().isEmpty()) {
                                                    int l3 = k3 - 1;
                                                    blockposition_mutableblockposition1.set(blockposition_mutableblockposition);

                                                    BlockState iblockdata1;
                                                    do {
                                                        blockposition_mutableblockposition1.setY(l3--);
                                                        iblockdata1 = chunk.getBlockState(blockposition_mutableblockposition1);
                                                        ++l2;
                                                    } while (l3 > world.getMinBuildHeight() && !iblockdata1.getFluidState().isEmpty());

                                                    iblockdata = getCorrectStateForFluidBlock(world, iblockdata, blockposition_mutableblockposition);
                                                }
                                            }

                                            state.checkBanners(world, blockposition_mutableblockposition.getX(), blockposition_mutableblockposition.getZ());
                                            d1 += (double) k3 / (double) (i * i);
                                            multiset.add(iblockdata.getMapColor(world, blockposition_mutableblockposition));
                                        }
                                    }
                                }

                                l2 /= i * i;
                                MapColor materialmapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.NONE);
                                double d2;
                                MapColor.Brightness materialmapcolor_a;
                                if (materialmapcolor == MapColor.WATER) {
                                    d2 = (double) l2 * 0.1 + (double) (k1 + l1 & 1) * 0.2;
                                    if (d2 < 0.5) {
                                        materialmapcolor_a = MapColor.Brightness.HIGH;
                                    } else if (d2 > 0.9) {
                                        materialmapcolor_a = MapColor.Brightness.LOW;
                                    } else {
                                        materialmapcolor_a = MapColor.Brightness.NORMAL;
                                    }
                                } else {
                                    d2 = (d1 - d0) * 4.0 / (double) (i + 4) + ((double) (k1 + l1 & 1) - 0.5) * 0.4;
                                    if (d2 > 0.6) {
                                        materialmapcolor_a = MapColor.Brightness.HIGH;
                                    } else if (d2 < -0.6) {
                                        materialmapcolor_a = MapColor.Brightness.LOW;
                                    } else {
                                        materialmapcolor_a = MapColor.Brightness.NORMAL;
                                    }
                                }

                                d0 = d1;
                                if (l1 >= 0 && i2 < j1 * j1 && (!flag1 || (k1 + l1 & 1) != 0)) {
                                    flag |= state.updateColor(k1, l1, materialmapcolor.getPackedId(materialmapcolor_a));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private static BlockState getCorrectStateForFluidBlock(Level world, BlockState state, BlockPos pos) {
        FluidState fluid = state.getFluidState();
        return !fluid.isEmpty() && !state.isFaceSturdy(world, pos, Direction.UP) ? fluid.createLegacyBlock() : state;
    }

    public static byte calcDirection(float yaw) {
        assert -180 <= yaw && yaw <= 180 : "Invalid yaw: " + yaw + "!"; // Range check
        return (byte) ((yaw / 22.5f + 16.5f) % 16f);
    }
    public static byte calcDirection(@NotNull Location location) {
        return calcDirection(location.getYaw());
    }

    public static byte calcDirection(@NotNull org.bukkit.entity.Entity entity) {
        return calcDirection(entity.getLocation());
    }
}
