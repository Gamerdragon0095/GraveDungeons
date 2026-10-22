package net.gamerdragon0095.gravedungeons.action.block.entity;

import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Calendar;

public class SoulBlockSensorAction {
    public static void placeSoulBlockSensor(LevelAccessor world, BlockPos pos) {
        if (!world.isClientSide()) {
            BlockState blockState = world.getBlockState(pos);
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity != null) {
                CompoundTag output = new CompoundTag();


                //Set default output value
                if (output.isEmpty()) {
                    output.putBoolean("IsPowered", false);
                }

                blockEntity.getPersistentData().put("Output", output);

                blockEntity.getPersistentData().putBoolean("IsLocked", false);

                if (world instanceof Level _level) {
                    _level.sendBlockUpdated(pos, blockState, blockState, 3);
                }
            }
        }
    }

    public static Boolean detectSoulBlock(LevelAccessor world, BlockPos pos) {
        int X = pos.getX();
        int Y = pos.getY();
        int Z = pos.getZ();

        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            BlockState adjacentState = world.getBlockState(adjacentPos);

            if (adjacentState.is(ModBlocks.SOUL_BLOCK.get())) {
                // Do something when a matching adjacent block is found
                return true;
            }
        }

        //System.out.println("is ticking 2");
        return false;
    }

    public static void setOutput(Level level, BlockPos pos) {
        //System.out.println("is ticking");

        CompoundTag output = new CompoundTag();

        BlockState blockState = level.getBlockState(pos);

        //Set default output value
        if (output.isEmpty()) {
            output.putBoolean("IsPowered", SoulBlockSensorAction.detectSoulBlock(level, pos));
        }
        level.getBlockEntity(pos).getPersistentData().put("Output", output);
        level.sendBlockUpdated(pos, blockState, blockState, 3);
    }
}
