package net.gamerdragon0095.gravedungeons.action.block;

import com.mojang.math.Transformation;
import net.gamerdragon0095.gravedungeons.action.block.entity.ResonatorBlockAction;
import net.gamerdragon0095.gravedungeons.action.entity.GeneralEntityAction;
import net.gamerdragon0095.gravedungeons.data.block.entity.EchoCrystalData;
import net.gamerdragon0095.gravedungeons.data.block.entity.RelativeCompoundTagPos;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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

    public static void displayInputsAboveHotbar(LevelAccessor world, BlockEntity blockEntity, Player player, BlockPos pos) {
        if (blockEntity != null) {
            BlockPos logicPos1 = RelativeCompoundTagPos.getOffsetOrReturnPos(EchoCrystalData.getPosOfLogicInput(blockEntity, 1), BlockPos.containing(0, 0, 0));
            BlockPos logicPos2 = RelativeCompoundTagPos.getOffsetOrReturnPos(EchoCrystalData.getPosOfLogicInput(blockEntity, 2), BlockPos.containing(0, 0, 0));

            BlockPos inputPos1 = RelativeCompoundTagPos.getOffsetOrReturnPos(EchoCrystalData.getPosOfInput(blockEntity, 1), BlockPos.containing(0, 0, 0));
            BlockPos inputPos2 = RelativeCompoundTagPos.getOffsetOrReturnPos(EchoCrystalData.getPosOfInput(blockEntity, 2), BlockPos.containing(0, 0, 0));

            BlockPos inputPos0 = RelativeCompoundTagPos.getOffsetOrReturnPos(ResonatorBlockAction.getPosOfInput(blockEntity), BlockPos.containing(0, 0, 0));

            BlockPos logicBlockPos1 = RelativeCompoundTagPos.getOrReturnPos(EchoCrystalData.getPosOfLogicInput(blockEntity, 1), BlockPos.containing(0, 0, 0), pos);
            BlockPos logicBlockPos2 = RelativeCompoundTagPos.getOrReturnPos(EchoCrystalData.getPosOfLogicInput(blockEntity, 2), BlockPos.containing(0, 0, 0), pos);

            BlockPos inputBlockPos1 = RelativeCompoundTagPos.getOrReturnPos(EchoCrystalData.getPosOfInput(blockEntity, 1), BlockPos.containing(0, 0, 0), pos);
            BlockPos inputBlockPos2 = RelativeCompoundTagPos.getOrReturnPos(EchoCrystalData.getPosOfInput(blockEntity, 2), BlockPos.containing(0, 0, 0), pos);

            BlockPos inputBlockPos0 = RelativeCompoundTagPos.getOrReturnPos(ResonatorBlockAction.getPosOfInput(blockEntity), BlockPos.containing(0, 0, 0), pos);

            int logic1X = logicPos1.getX();
            int logic1Y = logicPos1.getY();
            int logic1Z = logicPos1.getZ();

            int logic2X = logicPos2.getX();
            int logic2Y = logicPos2.getY();
            int logic2Z = logicPos2.getZ();


            int input1X = inputPos1.getX();
            int input1Y = inputPos1.getY();
            int input1Z = inputPos1.getZ();

            int input2X = inputPos2.getX();
            int input2Y = inputPos2.getY();
            int input2Z = inputPos2.getZ();

            int input0X = inputPos0.getX();
            int input0Y = inputPos0.getY();
            int input0Z = inputPos0.getZ();

            List<String> output = new ArrayList<>();

            Vector3f translation = new Vector3f(-0.005f, -0.005f, -0.005f);
            Quaternionf leftRotation = new Quaternionf();
            Vector3f scale = new Vector3f(1.01f, 1.01f, 1.01f);
            Quaternionf rightRotation = new Quaternionf();
            Transformation customTransform = new Transformation(translation, leftRotation, scale, rightRotation);

            //Vector3f translation2 = new Vector3f(-0.25f, -0.25f, -0.25f);
            //Quaternionf leftRotation2 = new Quaternionf();
            //Vector3f scale2 = new Vector3f(1.5f, 1.5f, 1.5f);
            //Quaternionf rightRotation2 = new Quaternionf();
            //Transformation customTransform2 = new Transformation(translation2, leftRotation2, scale2, rightRotation2);

            if ((0 != logic1X) || (0 != logic1Y) || (0 != logic1Z)) {
                //output.add("Logic1: [" + logic1X + "," + logic1Y + "," + logic1Z + "]");

                if (!player.isCrouching()) {
                    if (world instanceof ServerLevel level) {
                        if (player instanceof ServerPlayer _player) {
                            GeneralEntityAction.spawnPrivateBlockDisplay(level, _player, logicBlockPos1, level.getBlockState(logicBlockPos1), customTransform, ChatFormatting.LIGHT_PURPLE, 100);
                        }
                    }
                }
            }

            if ((0 != logic2X) || (0 != logic2Y) || (0 != logic2Z)) {
                //output.add("Logic2: [" + logic2X + "," + logic2Y + "," + logic2Z + "]");

                if (player.isCrouching()) {
                    if (world instanceof ServerLevel level) {
                        if (player instanceof ServerPlayer _player) {
                            GeneralEntityAction.spawnPrivateBlockDisplay(level, _player, logicBlockPos2, level.getBlockState(logicBlockPos2), customTransform, ChatFormatting.DARK_PURPLE, 100);
                        }
                    }
                }
            }

            if ((0 != input1X) || (0 != input1Y) || (0 != input1Z)) {
                //output.add("Input1: [" + input1X + "," + input1Y + "," + input1Z + "]");

                if (!player.isCrouching()) {
                    if (world instanceof ServerLevel level) {
                        if (player instanceof ServerPlayer _player) {
                            GeneralEntityAction.spawnPrivateBlockDisplay(level, _player, inputBlockPos1, level.getBlockState(inputBlockPos1), customTransform, ChatFormatting.AQUA, 100);
                        }
                    }
                }
            }

            if ((0 != input2X) || (0 != input2Y) || (0 != input2Z)) {
                //output.add("Input2: [" + input2X + "," + input2Y + "," + input2Z + "]");

                if (player.isCrouching()) {
                    if (world instanceof ServerLevel level) {
                        if (player instanceof ServerPlayer _player) {
                            GeneralEntityAction.spawnPrivateBlockDisplay(level, _player, inputBlockPos2, level.getBlockState(inputBlockPos2), customTransform, ChatFormatting.DARK_BLUE, 100);
                        }
                    }
                }
            }

            if ((0 != input0X) || (0 != input0Y) || (0 != input0Z)) {
                //output.add("Input: [" + input0X + "," + input0Y + "," + input0Z + "]");

                //if (!player.isCrouching()) {
                    if (world instanceof ServerLevel level) {
                        if (player instanceof ServerPlayer _player) {
                            GeneralEntityAction.spawnPrivateBlockDisplay(level, _player, inputBlockPos0, level.getBlockState(inputBlockPos0), customTransform, ChatFormatting.AQUA, 100);
                        }
                    }
                //}
            }

            //player.sendOverlayMessage(Component.literal(("Active Connections: " + output)));
        }
    }
}
