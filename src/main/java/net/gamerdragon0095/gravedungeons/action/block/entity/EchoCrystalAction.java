package net.gamerdragon0095.gravedungeons.action.block.entity;

import net.gamerdragon0095.gravedungeons.action.entity.GeneralEntityAction;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.block.custom.EchoCrystal;
import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.gamerdragon0095.gravedungeons.data.block.entity.EchoCrystalData;
import  net.gamerdragon0095.gravedungeons.data.block.entity.CompoundTagPos;
import net.gamerdragon0095.gravedungeons.data.block.entity.RelativeCompoundTagPos;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.swing.text.StyledEditorKit;
import java.util.Calendar;
import java.util.Comparator;

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
                    inputPos.putFloat("X", 0);
                    inputPos.putFloat("Y", 0);
                    inputPos.putFloat("Z", 0);
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

                if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL.get()) {
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
            if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL.get()) {

                CompoundTag inputPos = new CompoundTag();

                //Set default inputPos values
                if (inputPos.isEmpty()) {
                    inputPos.putFloat("X", 0);
                    inputPos.putFloat("Y", 0);
                    inputPos.putFloat("Z", 0);
                }

                //Get CompoundTags
                CompoundTag logicTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic1");
                CompoundTag logicTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic2");

                CompoundTag inputTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Input1");
                CompoundTag inputTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Input2");


                //Get BlockPosses
                BlockPos logicTagPos1 = RelativeCompoundTagPos.getOrReturnPos(logicTag1.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());
                BlockPos logicTagPos2 = RelativeCompoundTagPos.getOrReturnPos(logicTag2.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());

                BlockPos inputTagPos1 = RelativeCompoundTagPos.getOrReturnPos(inputTag1.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());
                BlockPos inputTagPos2 = RelativeCompoundTagPos.getOrReturnPos(inputTag2.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());


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

                int X = blockEntity.getBlockPos().getX();
                int Y = blockEntity.getBlockPos().getY();
                int Z = blockEntity.getBlockPos().getZ();

                BlockPos pos = blockEntity.getBlockPos();

                int logic1X = logicTagPos1.getX();
                int logic1Y = logicTagPos1.getY();
                int logic1Z = logicTagPos1.getZ();

                int logic2X = logicTagPos2.getX();
                int logic2Y = logicTagPos2.getY();
                int logic2Z = logicTagPos2.getZ();



                int input1X = inputTagPos1.getX();
                int input1Y = inputTagPos1.getY();
                int input1Z = inputTagPos1.getZ();

                int input2X = inputTagPos2.getX();
                int input2Y = inputTagPos2.getY();
                int input2Z = inputTagPos2.getZ();

                //Set true value of outputs
                if (logicTagBlock1 != null) {
                    if ((X != logic1X) || (Y != logic1Y) || (Z != logic1Z)) {
                        logic1 = logicTagBlock1.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {
                    //logicTagPos1 = BlockPos.containing(X, Y, Z);

                    CompoundTag posCheckRotationL1 = RelativeCompoundTagPos.getRotedPosOffsetOrReturnEmpty(world, logicTag1.getCompoundOrEmpty("InputPos"), pos);

                    if (!posCheckRotationL1.isEmpty()) {
                        logicTag1.put("InputPos", posCheckRotationL1);
                    } else {
                        logicTag1.put("InputPos", inputPos);
                    }

                    blockEntity.getPersistentData().put("Logic1", logicTag1);
                    if (world instanceof Level _level) {
                        _level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    }
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + logicTagPos1 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                    return;
                }

                if (logicTagBlock2 != null) {
                    if ((X != logic2X) || (Y != logic2Y) || (Z != logic2Z)) {
                        logic2 = logicTagBlock2.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {
                    //logicTagPos2 = BlockPos.containing(X, Y, Z);

                    CompoundTag posCheckRotationL2 = RelativeCompoundTagPos.getRotedPosOffsetOrReturnEmpty(world, logicTag2.getCompoundOrEmpty("InputPos"), pos);

                    if (!posCheckRotationL2.isEmpty()) {
                        logicTag2.put("InputPos", posCheckRotationL2);
                    } else {
                        logicTag2.put("InputPos", inputPos);
                    }

                    blockEntity.getPersistentData().put("Logic2", logicTag2);
                    if (world instanceof Level _level) {
                        _level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    }
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + logicTagPos2 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                    return;
                }

                if (inputTagBlock1 != null) {
                    if ((X != input1X) || (Y != input1Y) || (Z != input1Z)) {
                        input1 = inputTagBlock1.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {
                    //inputTagPos1 = BlockPos.containing(X, Y, Z);

                    CompoundTag posCheckRotationI1 = RelativeCompoundTagPos.getRotedPosOffsetOrReturnEmpty(world, inputTag1.getCompoundOrEmpty("InputPos"), pos);

                    if (!posCheckRotationI1.isEmpty()) {
                        inputTag1.put("InputPos", posCheckRotationI1);
                    } else {
                        inputTag1.put("InputPos", inputPos);
                    }

                    blockEntity.getPersistentData().put("Input1", inputTag1);
                    if (world instanceof Level _level) {
                        _level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    }
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + inputTagPos1 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                    return;
                }

                if (inputTagBlock2 != null) {
                    if ((X != input2X) || (Y != input2Y) || (Z != input2Z)) {
                        input2 = inputTagBlock2.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {
                    //inputTagPos2 = BlockPos.containing(X, Y, Z);

                    CompoundTag posCheckRotationI2 = RelativeCompoundTagPos.getRotedPosOffsetOrReturnEmpty(world, inputTag2.getCompoundOrEmpty("InputPos"), pos);

                    if (!posCheckRotationI2.isEmpty()) {
                        inputTag2.put("InputPos", posCheckRotationI2);
                    } else {
                        inputTag2.put("InputPos", inputPos);
                    }

                    blockEntity.getPersistentData().put("Input2", inputTag2);
                    if (world instanceof Level _level) {
                        _level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    }
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalAction.fetchInputsAndLogicTags] Error: The BlockEntity at: '" + inputTagPos2 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                    return;
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

    public static void setOutput(Level level, BlockPos pos) {
        //System.out.println("is ticking");

        CompoundTag output = new CompoundTag();

        BlockState blockState = level.getBlockState(pos);

        //if (!level.isClientSide()) {

            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (output.isEmpty()) {
                output.putBoolean("IsPowered", doLogicCalculations(level, blockEntity));
            }
            if (!level.getBlockEntity(pos).getPersistentData().getCompoundOrEmpty("Output").isEmpty()) {
                CompoundTag baseOutput = level.getBlockEntity(pos).getPersistentData().getCompoundOrEmpty("Output");

                if ((!baseOutput.getBooleanOr("IsPowered", false)) && (doLogicCalculations(level, blockEntity))) {
                    level.gameEvent(null, GameEvent.RESONATE_10, pos);

                } else if ((baseOutput.getBooleanOr("IsPowered", false)) && (!doLogicCalculations(level, blockEntity))) {
                    level.gameEvent(null, GameEvent.RESONATE_9, pos);
                }
            }

            level.getBlockEntity(pos).getPersistentData().put("Output", output);

            level.sendBlockUpdated(pos, blockState, blockState, 3);
        //}

        //Set default output value

    }

    public static void setBlockStateBasedOnNBTData(LevelAccessor world, BlockEntity blockEntity, BlockPos pos) {
        if (blockEntity == null) {
            return;
        }

        if (world.getBlockState(pos).getBlock() != ModBlocks.ECHO_CRYSTAL.get()) {
            return;
        }

        Boolean logic1 = EchoCrystalData.getIsPoweredOfLogic(blockEntity, 1);
        Boolean logic2 = EchoCrystalData.getIsPoweredOfLogic(blockEntity, 2);

        Boolean input1 = EchoCrystalData.getIsPoweredOfInput(blockEntity, 1);
        Boolean input2 = EchoCrystalData.getIsPoweredOfInput(blockEntity, 2);

        int logicStateValue = 0;

        if (input1) {
            logicStateValue = logicStateValue + 1;
        }

        if (input2) {
            logicStateValue = logicStateValue + 2;
        }

        if (logic1) {
            logicStateValue = logicStateValue + 4;
        }

        if (logic2) {
            logicStateValue = logicStateValue + 8;
        }

        BlockState currentState = world.getBlockState(pos);
        BlockState newState = currentState.setValue(EchoCrystal.LOGIC_STATE, logicStateValue);

        // 1. Capture the Block Entity instance BEFORE changing the state
        //BlockEntity blockEntity = world.getBlockEntity(pos);

        // 2. Pass flag 2 (Block.UPDATE_CLIENTS) to prevent full destruction loops
        world.setBlock(pos, newState, Block.UPDATE_CLIENTS);

        // 3. Mark the captured block entity as changed so it serializes immediately
        //if (blockEntity != null) {
        blockEntity.setChanged();
        //}
    }

    public static void spawnOutPutParticles(LevelAccessor world, BlockPos pos, BlockEntity blockEntity) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        Boolean input3 = EchoCrystalData.getIsPoweredOfLogic(blockEntity, 1);
        Boolean input4 = EchoCrystalData.getIsPoweredOfLogic(blockEntity, 2);

        Boolean input1 = EchoCrystalData.getIsPoweredOfInput(blockEntity, 1);
        Boolean input2 = EchoCrystalData.getIsPoweredOfInput(blockEntity, 2);

        CompoundTag logicTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic1");
        CompoundTag logicTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic2");

        CompoundTag inputTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Input1");
        CompoundTag inputTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Input2");


        //Get BlockPosses
        BlockPos targetPos3 = RelativeCompoundTagPos.getOrReturnPos(logicTag1.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());
        BlockPos targetPos4 = RelativeCompoundTagPos.getOrReturnPos(logicTag2.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());

        BlockPos targetPos1 = RelativeCompoundTagPos.getOrReturnPos(inputTag1.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());
        BlockPos targetPos2 = RelativeCompoundTagPos.getOrReturnPos(inputTag2.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());

        int targetX1 = targetPos1.getX();
        int targetY1 = targetPos1.getY();
        int targetZ1 = targetPos1.getZ();

        int targetX2 = targetPos2.getX();
        int targetY2 = targetPos2.getY();
        int targetZ2 = targetPos2.getZ();

        int targetX3 = targetPos3.getX();
        int targetY3 = targetPos3.getY();
        int targetZ3 = targetPos3.getZ();

        int targetX4 = targetPos4.getX();
        int targetY4 = targetPos4.getY();
        int targetZ4 = targetPos4.getZ();

        if (world instanceof ServerLevel _level) {
            //echoCrystalLightLevel(world.getBlockState(pos));

            if (Math.random() < 0.05) {
                if (input1) {
                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                            "execute positioned " + targetX1 + " " + targetY1 + " " + targetZ1 + " run particle minecraft:vibration{arrival_in_ticks:30,destination:{type:block,pos:[" + x + "," + y + "," + z + "]}} ~ ~0.5 ~ 0.2 0.2 0.2 0.5 3 force @a");
                }

                if (input2) {
                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                            "execute positioned " + targetX2 + " " + targetY2 + " " + targetZ2 + " run particle minecraft:vibration{arrival_in_ticks:30,destination:{type:block,pos:[" + x + "," + y + "," + z + "]}} ~ ~0.5 ~ 0.2 0.2 0.2 0.5 3 force @a");
                }

                if (input3) {
                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                            "execute positioned " + targetX3 + " " + targetY3 + " " + targetZ3 + " run particle minecraft:vibration{arrival_in_ticks:30,destination:{type:block,pos:[" + x + "," + y + "," + z + "]}} ~ ~0.5 ~ 0.2 0.2 0.2 0.5 3 force @a");
                }

                if (input4) {
                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                            "execute positioned " + targetX4 + " " + targetY4 + " " + targetZ4 + " run particle minecraft:vibration{arrival_in_ticks:30,destination:{type:block,pos:[" + x + "," + y + "," + z + "]}} ~ ~0.5 ~ 0.2 0.2 0.2 0.5 3 force @a");
                }
            }

            if (doLogicCalculations(_level, blockEntity)) {
                _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                        "execute positioned " + x + " " + y + " " + z + " run particle minecraft:soul_fire_flame ~0.5 ~0.8 ~0.5 0.1 0.1 0.1 0 2 force @a");

                if (Math.random() < 0.07) {
                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                            "execute positioned " + x + " " + y + " " + z + " run particle minecraft:shriek{delay:1} ~0.5 ~ ~0.5 0 0 0 0 1 force @a");
                }
            }
        }
    }

    public static double echoCrystalLightLevel(BlockState blockState) {
        Boolean shouldOutput = false;
        int output = 0;

        int state = blockState.getValue(EchoCrystal.LOGIC_STATE);

        String binaryStateAndState = String.format("%4s", Integer.toBinaryString(state)).replace(' ', '0');

        String stateBinary = binaryStateAndState.substring((int) ((binaryStateAndState).length() / 2d), (binaryStateAndState).length());

        String modBinary = binaryStateAndState.substring(0, (int) ((binaryStateAndState).length() / 2d));

        String input1Binary = stateBinary.substring(1);
        String input2Binary = stateBinary.substring(0, 1);

        Boolean input1 = input1Binary.contains("1");
        Boolean input2 = input2Binary.contains("1");

        if (modBinary.equals("00")) {
            shouldOutput = (input1 || input2);
        } else if (modBinary.equals("01")) {
            shouldOutput = (!input1 && !input2);
        } else if (modBinary.equals("10")) {
            shouldOutput = (input1 && input2);
        } else if (modBinary.equals("11")) {
            shouldOutput = (input1 ^ input2);
        }

        if (shouldOutput) {
            output = 10;
        }


        System.out.println("test1: " + binaryStateAndState);
        System.out.println("test2: " + stateBinary);
        System.out.println("test3: " + modBinary);

        System.out.println("test4 input1: " + input1Binary);
        System.out.println("test5 input2: " + input2Binary);

        System.out.println("test6: " + shouldOutput);

        System.out.println("test7: " + input1);
        System.out.println("test8: " + input2);





        return output;
    }
}
