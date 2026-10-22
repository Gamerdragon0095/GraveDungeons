package net.gamerdragon0095.gravedungeons.action.block;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class SoulBlockActions {

    public static void pushBlock(LevelAccessor world, double x, double y, double z, Direction direction, Entity entity) {
        if (direction == null || entity == null)
            return;
        double modeMaxValue = 0;
        BlockState BlockState = Blocks.AIR.defaultBlockState();
        if (entity.isShiftKeyDown() == false) {
            if (((world.getBlockState(BlockPos.containing(x + (direction.getOpposite()).getStepX(), y + (direction.getOpposite()).getStepY(), z + (direction.getOpposite()).getStepZ()))).getBlock() == Blocks.AIR)) {
                if (world instanceof ServerLevel _level)
                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                            ("summon falling_block ~0.5 ~0.01 ~0.5 {BlockState:{Name:\"gravedungeons:soul_block\"},NoGravity:1b,PersistenceRequired:1b,Motion:[" + "" + (direction.getOpposite()).getStepX() * 0.2 + "d," + (direction.getOpposite()).getStepY() * 0.2 + "d,"
                                    + (direction.getOpposite()).getStepZ() * 0.2 + "d]}"));
                GraveDungeons.queueServerWork(1, () -> {
                    world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                });
                if (world instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS, 0.3F, 0.005F);
                    } else {
                        _level.playLocalSound(x, y, z, SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS, (float) 0.3, (float) 0.005, false);
                    }
                }
                for (int index0 = 0; index0 < 5; index0++) {
                    if (world instanceof ServerLevel _level)
                        _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                                "execute at @e[type=minecraft:falling_block,limit=5,sort=nearest,nbt={BlockState:{Name:\"gravedungeons:soul_block\"},NoGravity:1b}] run particle minecraft:crit ~ ~0.1 ~ 0.15 0 0.15 0.1 3 force @a");
                }
                GraveDungeons.queueServerWork(6, () -> {
                    if (world instanceof ServerLevel _level)
                        _level.getServer().getCommands()
                                .performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3((x + (direction.getOpposite()).getStepX() + 0.5), y + (direction.getOpposite()).getStepY(), (z + (direction.getOpposite()).getStepZ() + 0.5)), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "",
                                                Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                                        "kill @e[type=minecraft:falling_block,limit=1,sort=nearest,nbt={BlockState:{Name:\"gravedungeons:soul_block\"},NoGravity:1b}]");
                    if (world instanceof ServerLevel _level)
                        _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                                "stopsound @a[distance=..10] player minecraft:block.grindstone.use");
                });
                GraveDungeons.queueServerWork(5, () -> {
                    world.setBlock(BlockPos.containing(x + (direction.getOpposite()).getStepX(), y + (direction.getOpposite()).getStepY(), z + (direction.getOpposite()).getStepZ()), ModBlocks.SOUL_BLOCK.get().defaultBlockState(), 3);
                });
            } else {
                if (world instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.DEEPSLATE_PLACE, SoundSource.PLAYERS, (float) 0.3, (float) 0.005);
                    } else {
                        _level.playLocalSound(x, y, z, SoundEvents.DEEPSLATE_PLACE, SoundSource.PLAYERS, (float) 0.3, (float) 0.005, false);
                    }
                }
            }
        } else {
            if (((world.getBlockState(BlockPos.containing(x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ()))).getBlock() == Blocks.AIR)) {
                if (world instanceof ServerLevel _level)
                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                            ("summon falling_block ~0.5 ~0.01 ~0.5 {BlockState:{Name:\"gravedungeons:soul_block\"},NoGravity:1b,PersistenceRequired:1b,Motion:[" + "" + direction.getStepX() * 0.2 + "d, " + direction.getStepY() * 0.2 + "d," + direction.getStepZ() * 0.2
                                    + "d]}"));
                GraveDungeons.queueServerWork(1, () -> {
                    world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                });
                if (world instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS, 0.3F, 0.005F);
                    } else {
                        _level.playLocalSound(x, y, z, SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS, (float) 0.3, (float) 0.005, false);
                    }
                }
                for (int index1 = 0; index1 < 5; index1++) {
                    if (world instanceof ServerLevel _level)
                        _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                                "execute at @e[type=minecraft:falling_block,limit=5,sort=nearest,nbt={BlockState:{Name:\"gravedungeons:soul_block\"},NoGravity:1b}] run particle minecraft:crit ~ ~0.1 ~ 0.15 0 0.15 0.1 3 force @a");
                }
                GraveDungeons.queueServerWork(6, () -> {
                    if (world instanceof ServerLevel _level)
                        _level.getServer().getCommands().performPrefixedCommand(
                                new CommandSourceStack(CommandSource.NULL, new Vec3((x + direction.getStepX() + 0.5), y + direction.getStepY(), (z + direction.getStepZ() + 0.5)), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null)
                                        .withSuppressedOutput(),
                                "kill @e[type=minecraft:falling_block,limit=1,sort=nearest,nbt={BlockState:{Name:\"gravedungeons:soul_block\"},NoGravity:1b}]");
                    if (world instanceof ServerLevel _level)
                        _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
                                "stopsound @a[distance=..10] player minecraft:block.grindstone.use");
                });
                GraveDungeons.queueServerWork(5, () -> {
                    world.setBlock(BlockPos.containing(x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ()), ModBlocks.SOUL_BLOCK.get().defaultBlockState(), 3);
                });
            } else {
                if (world instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.DEEPSLATE_PLACE, SoundSource.PLAYERS, (float) 0.3, (float) 0.005);
                    } else {
                        _level.playLocalSound(x, y, z, SoundEvents.DEEPSLATE_PLACE, SoundSource.PLAYERS, (float) 0.3, (float) 0.005, false);
                    }
                }
            }
        }
    }
}
