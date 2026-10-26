package net.gamerdragon0095.gravedungeons.action.item;

import com.mojang.math.Transformation;
//import net.gamerdragon0095.dragon_lib.data.storage.RenderingEntityManager;
import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.action.block.GenericBlockAction;
import net.gamerdragon0095.gravedungeons.action.entity.GeneralEntityAction;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.gamerdragon0095.gravedungeons.data.block.entity.CompoundTagPos;
import net.gamerdragon0095.gravedungeons.data.block.entity.RelativeCompoundTagPos;
import net.gamerdragon0095.gravedungeons.item.ModDataComponentTypes;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class SoulTunerAction {
    public static void useAction(ItemStack soulTuner, BlockPos pos, Player player, Level level) {

        if (level.getBlockEntity(pos) != null) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            //System.out.println("test7:" + blockEntity);

            if ((blockEntity.getType() != ModBlockEntities.ECHO_CRYSTAL.get()) && (blockEntity.getType() != ModBlockEntities.SOUL_BLOCK_SENSOR.get()) && (blockEntity.getType() != ModBlockEntities.RESONATOR_BLOCK.get())) {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useAction] Error: The BlockEntity: '" + blockEntity + "' at: '" + pos + "' is not an excepted blockEntity type, please insure the BlockEntity is of the correct type and is not null before running this function.");
                return;
            }

            //System.out.println("test8:" + blockEntity.getType());

            BundleContents currentContents = soulTuner.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

            if (currentContents.items().isEmpty()) {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useAction] Error: The BundleContents of item: '" + soulTuner + "' is empty or null, please insure the BundleContents of the item is not empty or null before running this function.");
                return;
            }

            //System.out.println("test9:" + currentContents.items());

            List<ItemStack> contentsList = new ArrayList<>();
            currentContents.items().forEach(template -> contentsList.add(template.create()));

            ItemStack awakenedShard = contentsList.get(0);

            //System.out.println("test10:" + awakenedShard);

            if ((awakenedShard.get(ModDataComponentTypes.INPUT_POS) == null)) {
                if (awakenedShard.getItem() != ModItems.INFESTED_IRON_NUGET.get()
                        && awakenedShard.getItem() != ModItems.INFESTED_COPPER_NUGET.get()
                        && awakenedShard.getItem() != ModItems.AWAKENED_LOOT_GEM.get()
                ) {
                    awakenedShard.set(ModDataComponentTypes.INPUT_POS, pos);
                } else {
                    if (awakenedShard.getItem() == ModItems.INFESTED_IRON_NUGET.get()) {
                        useWithIronNuget(awakenedShard, blockEntity, player, pos);

                    } else if (awakenedShard.getItem() == ModItems.INFESTED_COPPER_NUGET.get()) {
                        useWithCopperNuget(awakenedShard, blockEntity, player, pos);

                    } else if (awakenedShard.getItem() == ModItems.AWAKENED_LOOT_GEM.get()) {
                        useWithLootGem(level, awakenedShard, blockEntity, player, pos);

                    }
                }
                //System.out.println("test11:" + soulTuner);
            } else {
                BlockPos inputItemPos = awakenedShard.get(ModDataComponentTypes.INPUT_POS);
                //System.out.println("test12:" + inputItemPos);
                if ((inputItemPos.getX() != pos.getX()) || (inputItemPos.getY() != pos.getY()) || (inputItemPos.getZ() != pos.getZ())) {
                    //System.out.println("test19:" + pos);
                    if (awakenedShard.getItem() == ModItems.AWAKENED_ECHO_SHARD.get()) {

                        useWithEchoShard(awakenedShard, blockEntity, player, pos);

                        //System.out.println("test14:" + soulTuner);

                    } else if (awakenedShard.getItem() == ModItems.AWAKENED_AMETHYST_SHARD.get()) {

                        useWithAmethystShard(awakenedShard, blockEntity, player, pos);

                        //System.out.println("test18:" + soulTuner);

                    }
                } else {
                    awakenedShard.remove(ModDataComponentTypes.INPUT_POS.get());

                    //System.out.println("test5:" + awakenedShard);
                }
            }

            List<ItemStackTemplate> updatedContents = new ArrayList<>();
            for (ItemStack stack : contentsList) {
                if (!stack.isEmpty()) {
                    updatedContents.add(ItemStackTemplate.fromNonEmptyStack(stack));
                }
            }

            soulTuner.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(updatedContents));

            //System.out.println("test6:" + soulTuner);
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useAction] Error: The BlockEntity at: '" + pos + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
        }
    }

    public static void useWithEchoShard(ItemStack awakenedShard, BlockEntity blockEntity, Player player, BlockPos pos) {
        BlockPos inputItemPos = awakenedShard.get(ModDataComponentTypes.INPUT_POS);
        //System.out.println("test12:" + inputItemPos);
        if ((inputItemPos.getX() != pos.getX()) || (inputItemPos.getY() != pos.getY()) || (inputItemPos.getZ() != pos.getZ())) {
            //System.out.println("test19:" + pos);
            if (awakenedShard.get(ModDataComponentTypes.INPUT_POS) == null) {
                awakenedShard.set(ModDataComponentTypes.INPUT_POS, pos);
                //System.out.println("test11:" + soulTuner);
            } else {
                //BlockPos inputItemPos = awakenedShard.get(ModDataComponentTypes.INPUT_POS);

                if (awakenedShard.getItem() == ModItems.AWAKENED_ECHO_SHARD.get()) {
                    CompoundTag inputTag0 = blockEntity.getPersistentData().getCompoundOrEmpty("Input");

                    CompoundTag inputTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Input1");
                    CompoundTag inputTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Input2");

                    CompoundTag output = blockEntity.getPersistentData().getCompoundOrEmpty("Output");

                    if (output.isEmpty()) {
                        System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithEchoShard] Error: The BlockEntity: '" + blockEntity + "' at: '" + pos + "' does not have an 'Output' data type associated with it, please insure the BlockEntity is of the correct type and is not null before running this function.");
                        return;
                    }

                    //System.out.println("test13:" + soulTuner);

                    if (player != null) {
                        if (!player.isCrouching()) {
                            //System.out.println("test1:" + inputTag1);

                            if (!inputTag0.isEmpty()) {
                                inputTag0.put("InputPos", RelativeCompoundTagPos.createTag(pos, inputItemPos));
                                blockEntity.getPersistentData().put("Input", inputTag0);
                            } else {
                                inputTag1.put("InputPos", RelativeCompoundTagPos.createTag(pos, inputItemPos));
                                blockEntity.getPersistentData().put("Input1", inputTag1);
                            }
                        } else {
                            inputTag2.put("InputPos", RelativeCompoundTagPos.createTag(pos, inputItemPos));

                            //System.out.println("test2:" + inputTag2);

                            if (!inputTag0.isEmpty()) {
                                inputTag0.put("InputPos", RelativeCompoundTagPos.createTag(pos, inputItemPos));
                                blockEntity.getPersistentData().put("Input", inputTag0);
                            } else {
                                blockEntity.getPersistentData().put("Input2", inputTag2);
                            }
                        }
                    } else {
                        System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithEchoShard] Error: The provided player is null, please insure the provided player entity is not null before running this function.");
                        return;
                    }

                    //System.out.println("test14:" + soulTuner);
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithEchoShard] Error: The provided ItemStack 'awakenedShard' is not a 'gravedungeons:awakened_echo_shard' item, please insure the provided awakenedShard item is of the correct type and not null before running this function.");
                }

            }
        }
    }

    public static void useWithAmethystShard(ItemStack awakenedShard, BlockEntity blockEntity, Player player, BlockPos pos) {
        BlockPos inputItemPos = awakenedShard.get(ModDataComponentTypes.INPUT_POS);
        //System.out.println("test12:" + inputItemPos);
        if ((inputItemPos.getX() != pos.getX()) || (inputItemPos.getY() != pos.getY()) || (inputItemPos.getZ() != pos.getZ())) {
            //System.out.println("test19:" + pos);
            if (awakenedShard.get(ModDataComponentTypes.INPUT_POS) == null) {
                awakenedShard.set(ModDataComponentTypes.INPUT_POS, pos);
                //System.out.println("test11:" + soulTuner);
            } else {
                //BlockPos inputItemPos = awakenedShard.get(ModDataComponentTypes.INPUT_POS);

                if (awakenedShard.getItem() == ModItems.AWAKENED_AMETHYST_SHARD.get()) {
                    CompoundTag logicTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic1");
                    CompoundTag logicTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic2");

                    CompoundTag output = blockEntity.getPersistentData().getCompoundOrEmpty("Output");

                    if (output.isEmpty()) {
                        System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithAmethystShard] Error: The BlockEntity: '" + blockEntity + "' at: '" + pos + "' does not have an 'Output' data type associated with it, please insure the BlockEntity is of the correct type and is not null before running this function.");
                        return;
                    }

                    //System.out.println("test13:" + soulTuner);

                    if (player != null) {
                        if (!player.isCrouching()) {
                            //System.out.println("test1:" + inputTag1);
                            logicTag1.put("InputPos", RelativeCompoundTagPos.createTag(pos, inputItemPos));

                            blockEntity.getPersistentData().put("Logic1", logicTag1);
                        } else {
                            logicTag2.put("InputPos", RelativeCompoundTagPos.createTag(pos, inputItemPos));

                            //System.out.println("test2:" + inputTag2);
                            blockEntity.getPersistentData().put("Logic2", logicTag2);
                        }
                    } else {
                        System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithAmethystShard] Error: The provided player is null, please insure the provided player entity is not null before running this function.");
                        return;
                    }

                    //System.out.println("test14:" + soulTuner);
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithAmethystShard] Error: The provided ItemStack 'awakenedShard' is not a 'gravedungeons:awakened_amethyst_shard' item, please insure the provided awakenedShard item is of the correct type and not null before running this function.");
                }

            }
        }
    }

    public static void useWithIronNuget(ItemStack infestedNuget, BlockEntity blockEntity, Player player, BlockPos pos) {
        //BlockPos inputItemPos = infestedNuget.get(ModDataComponentTypes.INPUT_POS);
        //System.out.println("test12:" + inputItemPos);
        //if ((inputItemPos.getX() != pos.getX()) || (inputItemPos.getY() != pos.getY()) || (inputItemPos.getZ() != pos.getZ())) {
            //System.out.println("test19:" + pos);
            //if (infestedNuget.get(ModDataComponentTypes.INPUT_POS) == null) {
                //infestedNuget.set(ModDataComponentTypes.INPUT_POS, pos);
                //System.out.println("test11:" + soulTuner);
            //} else {
                //BlockPos inputItemPos = infestedNuget.get(ModDataComponentTypes.INPUT_POS);

            if (infestedNuget.getItem() == ModItems.INFESTED_IRON_NUGET.get()) {
                CompoundTag logicTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Input1");
                CompoundTag logicTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Input2");

                CompoundTag output = blockEntity.getPersistentData().getCompoundOrEmpty("Output");

                if (output.isEmpty()) {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithIronNuget] Error: The BlockEntity: '" + blockEntity + "' at: '" + pos + "' does not have an 'Output' data type associated with it, please insure the BlockEntity is of the correct type and is not null before running this function.");
                    return;
                }

                    //System.out.println("test13:" + soulTuner);

                if (player != null) {
                    if (!player.isCrouching()) {
                        //System.out.println("test1:" + inputTag1);
                        logicTag1.put("InputPos", RelativeCompoundTagPos.createTag(pos, pos));

                        blockEntity.getPersistentData().put("Input1", logicTag1);
                    } else {
                        logicTag2.put("InputPos", RelativeCompoundTagPos.createTag(pos, pos));

                        //System.out.println("test2:" + inputTag2);
                        blockEntity.getPersistentData().put("Input2", logicTag2);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithIronNuget] Error: The provided player is null, please insure the provided player entity is not null before running this function.");
                    //return;
                }

                    //System.out.println("test14:" + soulTuner);
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithIronNuget] Error: The provided ItemStack 'awakenedShard' is not a 'gravedungeons:infested_iron_nuget' item, please insure the provided infestedNuget item is of the correct type and not null before running this function.");
            }

       // }
    }

    public static void useWithCopperNuget(ItemStack infestedNuget, BlockEntity blockEntity, Player player, BlockPos pos) {
        //BlockPos inputItemPos = infestedNuget.get(ModDataComponentTypes.INPUT_POS);
        //System.out.println("test12:" + inputItemPos);
        //if ((inputItemPos.getX() != pos.getX()) || (inputItemPos.getY() != pos.getY()) || (inputItemPos.getZ() != pos.getZ())) {
            //System.out.println("test19:" + pos);
            //if (infestedNuget.get(ModDataComponentTypes.INPUT_POS) == null) {
                //infestedNuget.set(ModDataComponentTypes.INPUT_POS, pos);
                //System.out.println("test11:" + soulTuner);
            //} else {
                //BlockPos inputItemPos = infestedNuget.get(ModDataComponentTypes.INPUT_POS);

            if (infestedNuget.getItem() == ModItems.INFESTED_COPPER_NUGET.get()) {
                CompoundTag logicTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic1");
                CompoundTag logicTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic2");

                CompoundTag output = blockEntity.getPersistentData().getCompoundOrEmpty("Output");

                if (output.isEmpty()) {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithCopperNuget] Error: The BlockEntity: '" + blockEntity + "' at: '" + pos + "' does not have an 'Output' data type associated with it, please insure the BlockEntity is of the correct type and is not null before running this function.");
                    return;
                }

                    //System.out.println("test13:" + soulTuner);

                if (player != null) {
                    if (!player.isCrouching()) {
                        //System.out.println("test1:" + inputTag1);
                        logicTag1.put("InputPos", RelativeCompoundTagPos.createTag(pos, pos));

                        blockEntity.getPersistentData().put("Logic1", logicTag1);
                    } else {
                        logicTag2.put("InputPos", RelativeCompoundTagPos.createTag(pos, pos));

                        //System.out.println("test2:" + inputTag2);
                        blockEntity.getPersistentData().put("Logic2", logicTag2);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithCopperNuget] Error: The provided player is null, please insure the provided player entity is not null before running this function.");
                    //return;
                }

                    //System.out.println("test14:" + soulTuner);
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithCopperNuget] Error: The provided ItemStack 'awakenedShard' is not a 'gravedungeons:infested_copper_nuget' item, please insure the provided infestedNuget item is of the correct type and not null before running this function.");
            }

        //}
    }

    public static void useWithLootGem(Level level ,ItemStack gem, BlockEntity blockEntity, Player player, BlockPos pos) {
        //BlockPos inputItemPos = infestedNuget.get(ModDataComponentTypes.INPUT_POS);
        //System.out.println("test12:" + inputItemPos);
        //if ((inputItemPos.getX() != pos.getX()) || (inputItemPos.getY() != pos.getY()) || (inputItemPos.getZ() != pos.getZ())) {
            //System.out.println("test19:" + pos);
            //if (infestedNuget.get(ModDataComponentTypes.INPUT_POS) == null) {
                //infestedNuget.set(ModDataComponentTypes.INPUT_POS, pos);
                //System.out.println("test11:" + soulTuner);
            //} else {
                //BlockPos inputItemPos = infestedNuget.get(ModDataComponentTypes.INPUT_POS);

            if (gem.getItem() == ModItems.AWAKENED_LOOT_GEM.get()) {
                //CompoundTag logicTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic1");
                //CompoundTag logicTag2 = blockEntity.getPersistentData().getCompoundOrEmpty("Logic2");

                CompoundTag output = blockEntity.getPersistentData().getCompoundOrEmpty("Output");

                //String output = blockEntity.getPersistentData().getStringOr("LootTable", "");

                Component customNameComponent = gem.get(DataComponents.CUSTOM_NAME);
                String customName = customNameComponent.getString();

                String lootTable = customName.substring((customName.indexOf("\"", 0) + 1), customName.indexOf("\"", customName.indexOf("\"", 1)));

                System.out.println("test1:" + customName.indexOf("\"", 0));

                String expChanceString = customName.substring((customName.indexOf("[", 0) + 1), customName.indexOf("]", customName.indexOf("[", 0)));
                float expChance = (float) parseDouble(expChanceString);


                if (output.isEmpty()) {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithLootGem] Error: The BlockEntity: '" + blockEntity + "' at: '" + pos + "' does not have an 'Output' data type associated with it, please insure the BlockEntity is of the correct type and is not null before running this function.");
                    return;
                }

                if (level.getBlockState(blockEntity.getBlockPos()).getBlock() != ModBlocks.RESONATOR_BLOCK.get()) {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithLootGem] Error: The Block: '" + level.getBlockState(blockEntity.getBlockPos()) + "' at: '" + pos + "' is not a 'gravedungeons:resonator_block' and does not posses the correct data type associated with it, please insure the Block is of the correct type and is not null before running this function.");
                    return;
                }

                    //System.out.println("test13:" + soulTuner);

                if (player != null) {
                    if (!player.isCrouching()) {
                        //System.out.println("test1:" + inputTag1);
                        //logicTag1.put("InputPos", RelativeCompoundTagPos.createTag(pos, pos));

                        blockEntity.getPersistentData().putString("LootTable", lootTable);
                    } else {
                        //logicTag2.put("InputPos", RelativeCompoundTagPos.createTag(pos, pos));

                        //System.out.println("test2:" + inputTag2);
                        //blockEntity.getPersistentData().putString("LootTable", lootTable);
                        blockEntity.getPersistentData().putFloat("EXPSpawnChance", expChance);
                    }
                } else {
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithLootGem] Error: The provided player is null, please insure the provided player entity is not null before running this function.");
                    //return;
                }

                    //System.out.println("test14:" + soulTuner);
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useWithLootGem] Error: The provided ItemStack 'gem' is not a 'gravedungeons:awakened_loot_gem' item, please insure the provided infestedNuget item is of the correct type and not null before running this function.");
            }

        //}
    }

    public static void highlightInputPosBlock(LevelAccessor world, ItemStack soulTuner, Entity entity) {
        if (soulTuner.getItem() != ModItems.SOUL_TUNER.get()) {
            return;
        }

        BundleContents currentContents = soulTuner.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        if (currentContents.items().isEmpty()) {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useAction] Error: The BundleContents of item: '" + soulTuner + "' is empty or null, please insure the BundleContents of the item is not empty or null before running this function.");
            return;
        }

        if (entity instanceof Player player) {
            //System.out.println("test9:" + currentContents.items());

            List<ItemStack> contentsList = new ArrayList<>();
            currentContents.items().forEach(template -> contentsList.add(template.create()));

            ItemStack awakenedShard = contentsList.get(0);

            if (awakenedShard.get(ModDataComponentTypes.INPUT_POS) != null) {
                //System.out.println("test11:" + soulTuner);

                BlockPos inputPos = awakenedShard.get(ModDataComponentTypes.INPUT_POS);

                int x = inputPos.getX();
                int y = inputPos.getY();
                int z = inputPos.getZ();

                if (world instanceof ServerLevel _level) {
                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                            "execute as " + player.getStringUUID() + " positioned " + x + " " + y + " " + z + " run particle minecraft:trial_spawner_detection_ominous ~ ~ ~ 0.3 0.3 0.3 0 1 force @s");

                        // 2. Define the exact block state (e.g., an Oak Fence Post)
                        BlockState state =  _level.getBlockState(inputPos);

                        // 3. Set the block state to show
                        //blockDisplay.setBlockState(state);

                        Vector3f translation = new Vector3f(-0.005f, -0.005f, -0.005f);
                        Quaternionf leftRotation = new Quaternionf();
                        Vector3f scale = new Vector3f(1.01f, 1.01f, 1.01f);
                        Quaternionf rightRotation = new Quaternionf();
                        Transformation customTransform = new Transformation(translation, leftRotation, scale, rightRotation);

                        if (player instanceof ServerPlayer _player) {
                            GeneralEntityAction.spawnPrivateBlockDisplay(_level, _player, inputPos, state, customTransform, ChatFormatting.DARK_AQUA, 0);

                        }

                    //System.out.println("CommandOutput:" + "execute as " + player.getStringUUID() + " positioned " + x + " " + y + " " + z + " run particle minecraft:crit ~ ~0.1 ~ 0.15 0.5 0.15 0.1 3 force @s");
                }
            } else {
                return;
            }
        }
    }

    public static ItemStack getContentsOrEmpty(ItemStack soulTuner) {
        //ItemStack output = new ItemStack.EMPTY.get();

        BundleContents currentContents = soulTuner.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        if (currentContents.items().isEmpty()) {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useAction] Error: The BundleContents of item: '" + soulTuner + "' is empty or null, please insure the BundleContents of the item is not empty or null before running this function.");
            return ItemStack.EMPTY;
        }

        //System.out.println("test9:" + currentContents.items());

        List<ItemStack> contentsList = new ArrayList<>();
        currentContents.items().forEach(template -> contentsList.add(template.create()));

        ItemStack awakenedShard = contentsList.get(0);

        return awakenedShard;
    }

    private static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

}
