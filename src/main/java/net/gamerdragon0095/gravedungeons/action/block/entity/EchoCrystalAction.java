package net.gamerdragon0095.gravedungeons.action.block.entity;

import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.gamerdragon0095.gravedungeons.data.block.entity.EchoCrystalData;
import  net.gamerdragon0095.gravedungeons.data.block.entity.CompoundTagPos;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.swing.text.StyledEditorKit;
import java.util.Calendar;

public class EchoCrystalAction {
    public static void placeEchoCrystal(LevelAccessor world, BlockPos pos) {
        if (!world.isClientSide()) {
            BlockState blockState = world.getBlockState(pos);
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity != null) {
                CompoundTag input1 = new CompoundTag();
                CompoundTag input2 = new CompoundTag();

                CompoundTag logic1 = new CompoundTag();
                CompoundTag logic2 = new CompoundTag();

                CompoundTag output = new CompoundTag();

                CompoundTag inputPos = new CompoundTag();


                //Set default inputPos values
                if (inputPos.isEmpty()) {
                    inputPos.putFloat("X", pos.getX());
                    inputPos.putFloat("Y", pos.getY());
                    inputPos.putFloat("Z", pos.getZ());
                }


                //Set default input values
                if (input1.isEmpty()) {
                    input1.putBoolean("IsPowered", false);
                    input1.put("InputPos", inputPos);
                }

                if (input2.isEmpty()) {
                    input2.putBoolean("IsPowered", false);
                    input2.put("InputPos", inputPos);
                }


                //Set default logic values
                if (logic1.isEmpty()) {
                    logic1.putBoolean("IsPowered", false);
                    logic1.put("InputPos", inputPos);
                }

                if (logic2.isEmpty()) {
                    logic2.putBoolean("IsPowered", false);
                    logic2.put("InputPos", inputPos);
                }


                //Set default output value
                if (output.isEmpty()) {
                    output.putBoolean("IsPowered", false);
                }

                blockEntity.getPersistentData().put("Input1", input1);
                blockEntity.getPersistentData().put("Input2", input2);

                blockEntity.getPersistentData().put("Logic1", logic1);
                blockEntity.getPersistentData().put("Logic2", logic2);

                blockEntity.getPersistentData().put("Output", output);

                blockEntity.getPersistentData().putBoolean("IsLocked", false);

                if (world instanceof Level _level) {
                    _level.sendBlockUpdated(pos, blockState, blockState, 3);
                }
            }
        }
    }

    public static Boolean doLogicCalculations(LevelAccessor world, BlockEntity blockEntity) {
        boolean output = false;

        if (!world.isClientSide()) {
            if (blockEntity != null) {
                String logicState = EchoCrystalData.getLogicMode(blockEntity);

                boolean input1 = EchoCrystalData.getIsPoweredOfInput(blockEntity, 1);
                boolean input2 = EchoCrystalData.getIsPoweredOfInput(blockEntity, 2);

                if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL) {
                    if (logicState == "or") {
                        output = (input1 || input2);
                    } else if (logicState == "not") {
                        output = (!input1 && !input2);
                    } else if (logicState == "and") {
                        output = (input1 && input2);
                    } else if (logicState == "xor") {
                        output = (input1 ^ input2);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.doLogicCalculations] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:echo_crystal' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.doLogicCalculations] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
            }
        }

        return output;
    }

    public static void fetchInputsAndLogicTags(LevelAccessor world, BlockEntity blockEntity) {
        if (blockEntity != null) {
            if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL) {

                //Get CompoundTags
                CompoundTag logicTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic1");
                CompoundTag logicTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic2");

                CompoundTag inputTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Input1");
                CompoundTag inputTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Input2");


                //Get BlockPosses
                BlockPos logicTagPos1 = CompoundTagPos.getOrReturnPos(logicTag1.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos());
                BlockPos logicTagPos2 = CompoundTagPos.getOrReturnPos(logicTag2.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos());

                BlockPos inputTagPos1 = CompoundTagPos.getOrReturnPos(inputTag1.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos());
                BlockPos inputTagPos2 = CompoundTagPos.getOrReturnPos(inputTag2.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos());


                //Get BlockEntities
                BlockEntity logicTagBlock1 = world.getBlockEntity(logicTagPos1);
                BlockEntity logicTagBlock2 = world.getBlockEntity(logicTagPos2);

                BlockEntity inputTagBlock1 = world.getBlockEntity(inputTagPos1);
                BlockEntity inputTagBlock2 = world.getBlockEntity(inputTagPos2);


                //Set default values of outputs
                boolean logic1 = false;
                boolean logic2 = false;

                boolean input1 = false;
                boolean input2 = false;

                //Set true value of outputs
                if (logicTagBlock1 != null) {
                    if (blockEntity.getBlockPos() != logicTagPos1) {
                        logic1 = logicTagBlock1.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + logicTagPos1 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                }

                if (logicTagBlock2 != null) {
                    if (blockEntity.getBlockPos() != logicTagPos2) {
                        logic2 = logicTagBlock2.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + logicTagPos2 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                }

                if (inputTagBlock1 != null) {
                    if (blockEntity.getBlockPos() != inputTagPos1) {
                        input1 = inputTagBlock1.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + inputTagPos1 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                }

                if (inputTagBlock2 != null) {
                    if (blockEntity.getBlockPos() != inputTagPos2) {
                        input2 = inputTagBlock2.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + inputTagPos2 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                }


                //Update CompoundTag data
                logicTag1.putBoolean("IsPowered", logic1);
                logicTag2.putBoolean("IsPowered", logic2);

                inputTag1.putBoolean("IsPowered", input1);
                inputTag2.putBoolean("IsPowered", input2);

                //Update BlockEntity data
                blockEntity.getPersistentData().put("Logic1", logicTag1);
                blockEntity.getPersistentData().put("Logic2", logicTag2);

                blockEntity.getPersistentData().put("Input1", inputTag1);
                blockEntity.getPersistentData().put("Input2", inputTag2);

                if (world instanceof Level _level) {
                    _level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:echo_crystal' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
        }
    }
}
