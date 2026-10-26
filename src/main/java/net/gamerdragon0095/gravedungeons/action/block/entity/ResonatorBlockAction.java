package net.gamerdragon0095.gravedungeons.action.block.entity;

import com.mojang.math.Transformation;
import net.gamerdragon0095.gravedungeons.action.block.GenericBlockAction;
import net.gamerdragon0095.gravedungeons.action.lootTables.GenericLootTableAction;
import net.gamerdragon0095.gravedungeons.block.custom.ResonatorBlock;
import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.gamerdragon0095.gravedungeons.data.block.entity.RelativeCompoundTagPos;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.gamerdragon0095.gravedungeons.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResonatorBlockAction {
    public static void placeResonatorBlock(LevelAccessor world, BlockPos pos) {
        if (!world.isClientSide()) {
            BlockState blockState = world.getBlockState(pos);
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity != null) {
                CompoundTag input1 = new CompoundTag();

                CompoundTag output = new CompoundTag();

                CompoundTag inputPos = new CompoundTag();

                String lootTable = "gravedungeons:genaric/grave_dungeon_small";


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

                //Set default output value
                if (output.isEmpty()) {
                    output.putBoolean("IsPowered", false);
                    output.putInt("DespinsLootTimer", 30);
                }

                blockEntity.getPersistentData().put("Input", input1);

                blockEntity.getPersistentData().put("Output", output);

                blockEntity.getPersistentData().putBoolean("IsLocked", false);

                blockEntity.getPersistentData().putString("LootTable", lootTable);

                blockEntity.getPersistentData().putFloat("EXPSpawnChance", 50);

                if (world instanceof Level _level) {
                    _level.sendBlockUpdated(pos, blockState, blockState, 3);
                }
            }
        }
    }

    public static void despinsOneItemFromTable(LevelAccessor world, BlockPos pos, String loot, float velocity, Direction direction, float expChance) {
        if (world instanceof ServerLevel serverLevel) {

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            // 2. Define the resource key for the target loot table
            if (world instanceof Level level) {
                ResourceKey<LootTable> lootTableKey = GenericLootTableAction.getLootTableKeyFromStringOr(
                        loot,
                        level,
                        "gravedungeons:genaric/grave_dungeon_small"
                );

                LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableKey);

                // 4. Build the LootParams context (e.g., origin position and parameters)
                LootParams lootParams = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                        .create(LootContextParamSets.CHEST);

                // 5. Generate and retrieve the list of ItemStack objects
                List<ItemStack> items = lootTable.getRandomItems(lootParams);

                for (ItemStack stack : items) {
                    // Do something with the ItemStack
                    if (world instanceof ServerLevel _level) {
                        ItemEntity entityToSpawn = new ItemEntity(_level, (x + 0.5), (y + 1), (z + 0.5), stack);
                        entityToSpawn.setPickUpDelay(10);
                        entityToSpawn.setDeltaMovement(direction.getStepX() * velocity, direction.getStepY() * velocity, direction.getStepZ() * velocity);

                        int expValue = Mth.nextInt(RandomSource.create(), 1, 10);
                        ExperienceOrb expToSpawn = new ExperienceOrb(_level, (x + 0.5), (y + 1), (z + 0.5), expValue);
                        expToSpawn.setDeltaMovement(direction.getStepX() * velocity, direction.getStepY() * velocity, direction.getStepZ() * velocity);

                        float expChancePercentage = (expChance == 0) ? 0.0f : (expChance / 100);

                        if (Math.random() < (expChancePercentage)) {
                            _level.addFreshEntity(expToSpawn);
                        } else {
                            _level.addFreshEntity(entityToSpawn);
                        }
                    }
                    break;
                }
            }

            // 3. Retrieve the LootTable from the server's reloadable registries

        }
    }

    public static void tickDespinsLootTimer(LevelAccessor world, BlockEntity blockEntity, BlockState blockState) {
        if (blockEntity == null) {
            return;
        }

        CompoundTag output = blockEntity.getPersistentData().getCompoundOrEmpty("Output");

        if (!output.isEmpty()) {
            //System.out.println("test:0" + output);
            int countDownVal = output.getIntOr("DespinsLootTimer", 0);
            boolean isPowered = output.getBooleanOr("IsPowered", false);

            if (isPowered && !blockState.getValue(ResonatorBlock.IS_POWERED)) {
                BlockState currentState = world.getBlockState(blockEntity.getBlockPos());
                BlockState newState = currentState.setValue(ResonatorBlock.IS_POWERED, true);

                // 2. Pass flag 2 (Block.UPDATE_CLIENTS) to prevent full destruction loops
                world.setBlock(blockEntity.getBlockPos(), newState, Block.UPDATE_CLIENTS);

                blockEntity.setChanged();
            } else if (blockState.getValue(ResonatorBlock.IS_POWERED)) {
                if ((countDownVal >= 1)) {
                    countDownVal = countDownVal - 1;

                    //System.out.println("test:2" + countDownVal);

                    output.putInt("DespinsLootTimer", countDownVal);
                    output.putBoolean("IsPowered", true);

                    //System.out.println("test:3" + output);

                    blockEntity.getPersistentData().put("Output", output);

                    String lootTable = blockEntity.getPersistentData().getStringOr("LootTable", "");
                    float expChance = blockEntity.getPersistentData().getFloatOr("EXPSpawnChance", 50);

                    //Component customNameComponent = itemStack.get(DataComponents.CUSTOM_NAME);
                    //String lootTable = customNameComponent.getString();

                    //Float expChance = 0f;

                    ResonatorBlockAction.despinsOneItemFromTable(world, blockEntity.getBlockPos(), lootTable, 0.5f, Direction.UP, expChance);
                } else if ((countDownVal == 0)) {
                    world.levelEvent(2001, blockEntity.getBlockPos(), Block.getId(world.getBlockState(blockEntity.getBlockPos())));
                    if (world instanceof ServerLevel level) {
                        level.setBlock(blockEntity.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    public static void fetchInputTags(LevelAccessor world, BlockEntity blockEntity, BlockPos pos) {
        if (blockEntity != null) {
            if (blockEntity.getType() == ModBlockEntities.RESONATOR_BLOCK.get()) {

                CompoundTag inputPos = new CompoundTag();

                //Set default inputPos values
                if (inputPos.isEmpty()) {
                    inputPos.putFloat("X", 0);
                    inputPos.putFloat("Y", 0);
                    inputPos.putFloat("Z", 0);
                }

                //Get CompoundTags
                CompoundTag inputTag1 = blockEntity.getPersistentData().getCompoundOrEmpty("Input");

                //Get BlockPosses
                BlockPos inputTagPos1 = RelativeCompoundTagPos.getOrReturnPos(inputTag1.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos(), blockEntity.getBlockPos());
                BlockPos inputTagPosOffset1 = RelativeCompoundTagPos.getOffsetOrReturnPos(inputTag1.getCompoundOrEmpty("InputPos"), blockEntity.getBlockPos());

                //Get BlockEntities
                BlockEntity inputTagBlock1 = world.getBlockEntity(inputTagPos1);

                //Set default values of outputs
                boolean input1 = false;

                int X = blockEntity.getBlockPos().getX();
                int Y = blockEntity.getBlockPos().getY();
                int Z = blockEntity.getBlockPos().getZ();

                int input1X = inputTagPos1.getX();
                int input1Y = inputTagPos1.getY();
                int input1Z = inputTagPos1.getZ();

                if (inputTagBlock1 != null) {
                    if ((X != input1X) || (Y != input1Y) || (Z != input1Z)) {
                        input1 = inputTagBlock1.getPersistentData().getCompoundOrEmpty("Output").getBooleanOr("IsPowered", false);
                    }
                } else {

                    CompoundTag rotatedCompoundTagPos = RelativeCompoundTagPos.getRotedPosOffsetOrReturnEmpty(world, inputTag1.getCompoundOrEmpty("InputPos"), pos);

                    if (!rotatedCompoundTagPos.isEmpty()) {
                        inputTag1.put("InputPos", rotatedCompoundTagPos);
                        inputTag1.putBoolean("IsPowered", false);
                        blockEntity.getPersistentData().put("Input", inputTag1);
                        if (world instanceof Level _level) {
                            _level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                        }
                        return;
                    }

                    inputTag1.put("InputPos", inputPos);
                    blockEntity.getPersistentData().put("Input", inputTag1);
                    if (world instanceof Level _level) {
                        _level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    }
                    System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:ResonatorBlockAction.fetchInputTags] Error: The BlockEntity at: '" + inputTagPos1 + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
                    return;
                }

                //Update CompoundTag data
                inputTag1.putBoolean("IsPowered", input1);

                //Update BlockEntity data
                blockEntity.getPersistentData().put("Input", inputTag1);

                if (world instanceof Level _level) {
                    _level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:ResonatorBlockAction.fetchInputTags] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:resonator_block' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:ResonatorBlockAction.fetchInputTags] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
        }
    }

    public static void setOutput(Level level, BlockPos pos) {
        //System.out.println("is ticking");

        CompoundTag output = new CompoundTag();

        if (level.getBlockEntity(pos) == null) {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:ResonatorBlockAction.setOutput] Error: The BlockEntity at: '" + pos + "' is null, please insure the BlockEntity is of the correct type and is not null before running this function.");
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        //BlockPos pos = blockEntity.getBlockPos();

        BlockState blockState = level.getBlockState(pos);

        CompoundTag input = blockEntity.getPersistentData().getCompoundOrEmpty("Input");

        CompoundTag baseOutput = blockEntity.getPersistentData().getCompoundOrEmpty("Output");

        //if (!level.isClientSide()) {

        int despinsLootTimer = baseOutput.getIntOr("DespinsLootTimer", 0);

        if ((output.isEmpty())) {
            output.putBoolean("IsPowered", input.getBooleanOr("IsPowered", false));
            output.putInt("DespinsLootTimer", despinsLootTimer);
        }


        blockEntity.getPersistentData().put("Output", output);

        level.sendBlockUpdated(pos, blockState, blockState, 3);
        //}

        //Set default output value

    }

    public static void highlightEffectedBlocksForPlayersWithResonator(ServerLevel world, ItemStack soulTuner, Entity entity, BlockPos pos) {
        if (soulTuner.getItem() != ModItems.SOUL_TUNER.get()) {
            return;
        }

        BundleContents currentContents = soulTuner.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        if (currentContents.items().isEmpty()) {
            //System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:SoulTunerAction.useAction] Error: The BundleContents of item: '" + soulTuner + "' is empty or null, please insure the BundleContents of the item is not empty or null before running this function.");
            return;
        }

        List<BlockPos> effectedBlocksPos = GenericBlockAction.getPosOfBlocksInRadiusOfTag(world, pos, 10, ModTags.Blocks.REGENERATABLE_SCULK);

        if (entity instanceof Player player) {
            //System.out.println("test9:" + currentContents.items());

            List<ItemStack> contentsList = new ArrayList<>();
            currentContents.items().forEach(template -> contentsList.add(template.create()));

            ItemStack awakenedShard = contentsList.get(0);

            //if (awakenedShard.get(ModDataComponentTypes.INPUT_POS) != null) {
                //System.out.println("test11:" + soulTuner);

                //BlockPos inputPos = awakenedShard.get(ModDataComponentTypes.INPUT_POS);

            for (BlockPos pos_ : effectedBlocksPos) {

                BlockState state = world.getBlockState(pos_);

                // 3. Check if it matches the target block
                // Do something when found, e.g., spawn particles or log
                int x = pos_.getX();
                int y = pos_.getY();
                int z = pos_.getZ();

                if (world instanceof ServerLevel _level) {
                    //_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                    //"execute as " + player.getStringUUID() + " positioned " + x + " " + y + " " + z + " run particle minecraft:trial_spawner_detection_ominous ~ ~ ~ 0.3 0.3 0.3 0 1 force @s");

                    // 2. Define the exact block state (e.g., an Oak Fence Post)
                    //BlockState state =  _level.getBlockState(pos_);

                    // 3. Set the block state to show
                    //blockDisplay.setBlockState(state);

                    Vector3f translation = new Vector3f(-0.005f, -0.005f, -0.005f);
                    Quaternionf leftRotation = new Quaternionf();
                    Vector3f scale = new Vector3f(1.01f, 1.01f, 1.01f);
                    Quaternionf rightRotation = new Quaternionf();
                    Transformation customTransform = new Transformation(translation, leftRotation, scale, rightRotation);

                    if (player instanceof ServerPlayer _player) {
                        //GeneralEntityAction.spawnPrivateBlockDisplay(_level, _player, pos_, state, customTransform, true);
                    }

                    //System.out.println("CommandOutput:" + "execute as " + player.getStringUUID() + " positioned " + x + " " + y + " " + z + " run particle minecraft:crit ~ ~0.1 ~ 0.15 0.5 0.15 0.1 3 force @s");
                }
            }

            //} else {
                //return;
            //}
        }
    }

    public static CompoundTag getPosOfInput(BlockEntity blockEntity) {
        CompoundTag output = new CompoundTag();
        if (blockEntity != null) {
            if (blockEntity.getType() == ModBlockEntities.RESONATOR_BLOCK.get()) {
                CompoundTag inputTag = blockEntity.getPersistentData().getCompoundOrEmpty("Input");

                if (!inputTag.isEmpty()) {
                    output = inputTag.getCompoundOrEmpty("InputPos");
                }
            } else {
                System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:ResonatorBlockAction.getPosOfInput] Error: The BlockEntity at: '" + blockEntity.getBlockPos() + "' and of type: '" + blockEntity.getType() + " is not a 'gravedungeons:resonator_block' and therefore does not posses the right data for this function to work with it, please insure the BlockEntity is of the correct type before running this function.");
            }
        } else {
            System.out.println("[" + (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)) + "] [GraveDungeons:ResonatorBlockAction.getPosOfInput] Error: The BlockEntity provided is null, please insure the BlockEntity is of the correct type and is not null before calling this function.");
        }

        return output;
    }
}
