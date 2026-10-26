package net.gamerdragon0095.gravedungeons.data.block.entity;

import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Calendar;

public class EchoCrystalData {
    public static String getLogicMode(BlockEntity blockEntity) {
        String output = "";

        if (blockEntity != null) {
            if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL.get()) {
                CompoundTag logicTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic1");
                CompoundTag logicTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic2");

                Boolean logic1 = logicTag1.getBooleanOr("IsPowered", false);
                Boolean logic2 = logicTag2.getBooleanOr("IsPowered", false);

                if (logic1 || logic2) {
                    if (logic1 && logic2) {
                        output = "xor";
                    } else if (logic1 && !logic2) {
                        output = "not";
                    } else {
                        output = "and";
                    }
                } else {
                    output = "or";
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getLogicMode] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:echo_crystal' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getLogicMode] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
        }

        return output;
    }

    public static Boolean getIsPoweredOfInput(BlockEntity blockEntity, int id) {
        Boolean output = false;

        if (!(id > 2) && (id > 0)) {
            if (blockEntity != null) {
                if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL.get()) {
                    CompoundTag inputTag = blockEntity.getPersistentData().getCompoundOrEmpty("Input" + id);

                    if (!inputTag.isEmpty()) {
                        output = inputTag.getBooleanOr("IsPowered", false);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getIsPoweredOfInput] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:echo_crystal' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getIsPoweredOfInput] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before calling this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getIsPoweredOfInput] Error: The provided 'id' value is outside the excepted range, please insure the value of 'id' is an int of either 1 or 2 before calling this function.");
        }
        return output;
    }

    public static Boolean getIsPoweredOfLogic(BlockEntity blockEntity, int id) {
        Boolean output = false;

        if (!(id > 2) && (id > 0)) {
            if (blockEntity != null) {
                if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL.get()) {
                    CompoundTag inputTag = blockEntity.getPersistentData().getCompoundOrEmpty("Logic" + id);

                    if (!inputTag.isEmpty()) {
                        output = inputTag.getBooleanOr("IsPowered", false);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getIsPoweredOfInput] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:echo_crystal' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getIsPoweredOfInput] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before calling this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getIsPoweredOfInput] Error: The provided 'id' value is outside the excepted range, please insure the value of 'id' is an int of either 1 or 2 before calling this function.");
        }
        return output;
    }

    public static CompoundTag getPosOfInput(BlockEntity blockEntity, int id) {
        CompoundTag output = new CompoundTag();

        if (!(id > 2) && (id > 0)) {
            if (blockEntity != null) {
                if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL.get()) {
                    CompoundTag inputTag = blockEntity.getPersistentData().getCompoundOrEmpty("Input" + id);

                    if (!inputTag.isEmpty()) {
                        output = inputTag.getCompoundOrEmpty("InputPos");
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getPosOfInput] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:echo_crystal' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getPosOfInput] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before calling this function.");
            }
        } else {
            if (id == 0) {
                CompoundTag inputTag = blockEntity.getPersistentData().getCompoundOrEmpty("Input");

                if (!inputTag.isEmpty()) {
                    output = inputTag.getCompoundOrEmpty("InputPos");
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getPosOfInput] Error: The input of '" + id + "' dose not exist inside the targeted BlockEntity at '" + blockEntity.getBlockPos() + "' of type '" + blockEntity.getType() + "', please insure the provided 'blockEntity' is of a type that possesses the input tag of the provided 'id' before calling this function.");
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getPosOfInput] Error: The provided 'id' value is outside the excepted range, please insure the value of 'id' is an int in between 0 - 2 before calling this function.");
            }
        }
        return output;
    }

    public static CompoundTag getPosOfLogicInput(BlockEntity blockEntity, int id) {
        CompoundTag output = new CompoundTag();

        if (!(id > 2) && (id > 0)) {
            if (blockEntity != null) {
                if (blockEntity.getType() == ModBlockEntities.ECHO_CRYSTAL.get()) {
                    CompoundTag logicTag = blockEntity.getPersistentData().getCompoundOrEmpty("Logic" + id);

                    if (!logicTag.isEmpty()) {
                        output = logicTag.getCompoundOrEmpty("InputPos");
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getPosOfLogicInput] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:echo_crystal' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getPosOfLogicInput] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before calling this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:EchoCrystalData.getPosOfLogicInput] Error: The provided 'id' value is outside the excepted range, please insure the value of 'id' is an int of either 1 or 2 before calling this function.");
        }
        return output;
    }
}
