package net.gamerdragon0095.gravedungeons.action.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericBlockAction {
    public static List<BlockPos> getPosOfBlocksInRadiusOfType(LevelAccessor level, BlockPos pos, int radius, Block targetBlock) {
        List<BlockPos> output = new ArrayList<>();

        BlockPos minPos = pos.offset(-radius, -radius, -radius);
        BlockPos maxPos = pos.offset(radius, radius, radius);
        double radiusSq = radius * radius;

        for (BlockPos pos_ : BlockPos.betweenClosed(minPos, maxPos)) {
            if (pos.distSqr(pos_) <= radiusSq) {
                BlockState state = level.getBlockState(pos_);

                if (state.is(targetBlock)) {
                    output.add(pos_);
                }
            }
        }

        return output;
    }

    public static List<BlockPos> getPosOfBlocksInRadiusOfTag(LevelAccessor level, BlockPos pos, int radius, TagKey<Block> targetBlock) {
        List<BlockPos> output = new ArrayList<>();

        BlockPos minPos = pos.offset(-radius, -radius, -radius);
        BlockPos maxPos = pos.offset(radius, radius, radius);
        double radiusSq = radius * radius;

        for (BlockPos pos_ : BlockPos.betweenClosed(minPos, maxPos)) {
            if (pos.distSqr(pos_) <= radiusSq) {
                BlockState state = level.getBlockState(pos_);

                if (state.tags().toList().contains(targetBlock)) {
                    output.add(pos_);
                }
            }
        }

        return output;
    }
}
