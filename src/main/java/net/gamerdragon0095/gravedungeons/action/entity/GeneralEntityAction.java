package net.gamerdragon0095.gravedungeons.action.entity;

import com.mojang.math.Transformation;
import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Comparator;

public class GeneralEntityAction {
    public static void applyGlowTagWithCustomColor(ServerLevel level, Entity entity, ChatFormatting color) {
        // 1. Enable the glowing effect on the display entity
        entity.setGlowingTag(true);

        // 2. Grab the world's scoreboard system
        Scoreboard scoreboard = level.getScoreboard();

        // 3. Define a unique name for your mod's highlight team (e.g., "gd_highlight_red")
        String teamName = "gravedungeons_glow_" + color.getName();

        // 4. Get the existing team or create a new one if it doesn't exist yet
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) {
            team = scoreboard.addPlayerTeam(teamName);
        }

        // 5. Assign your desired vanilla color to the team's outline properties
        team.setColor(color);

        // 6. Add the entity's UUID string to the team so it inherits the color
        scoreboard.addPlayerToTeam(entity.getStringUUID(), team);
    }

    public static void spawnPrivateBlockDisplay(ServerLevel level, ServerPlayer targetPlayer, BlockPos pos, BlockState blockState, Transformation customTransform, @Nullable ChatFormatting color, int ticks) {
        // 1. Create the entity instance locally on the server (Do NOT use level.addFreshEntity)
        Display.BlockDisplay blockDisplay = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, level);

        if (blockDisplay == null) {
            return;
        }

        if (color != null) {
            applyGlowTagWithCustomColor(level, blockDisplay, color);
            blockDisplay.setGlowingTag(true);
        }

        Brightness maxBrightness = new Brightness(15, 15);
        blockDisplay.setBrightnessOverride(maxBrightness);

        // 2. Configure all your properties exactly as before
        blockDisplay.setBlockState(blockState);

        //Vector3f translation = new Vector3f(-0.005f, -0.005f, -0.005f);
        //Transformation customTransform = new Transformation(translation, new Quaternionf(), new Vector3f(1.01f, 1.01f, 1.01f), new Quaternionf());
        blockDisplay.setTransformation(customTransform);

        blockDisplay.setPos(new Vec3(pos.getX() + 0.0, pos.getY() + 0.0, pos.getZ() + 0.0));

        // 3. Send the spawn packet ONLY to the targeted player
        targetPlayer.connection.send(new ClientboundAddEntityPacket(blockDisplay, 0, blockDisplay.blockPosition()));

        // 4. Send the metadata packet so the client player knows its blockstate, scale, and glow configurations
        targetPlayer.connection.send(new ClientboundSetEntityDataPacket(blockDisplay.getId(), blockDisplay.getEntityData().getNonDefaultValues()));

        GraveDungeons.queueServerWork(2 + ticks, () -> {
            //blockDisplay.remove(Entity.RemovalReason.DISCARDED);
            targetPlayer.connection.send(new ClientboundRemoveEntitiesPacket(blockDisplay.getId()));
            //targetPlayer.connection.send(new ClientboundRemoveEntitiesPacket(blockDisplay.getId()));
        });
    }

    public static void spawnBlockDisplay(ServerLevel level, BlockPos pos, BlockState blockState, Transformation customTransform, @Nullable ChatFormatting color, int ticks) {
        // 1. Create the entity instance locally on the server (Do NOT use level.addFreshEntity)
        Display.BlockDisplay blockDisplay = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, level);

        if (blockDisplay == null) {
            return;
        }

        if (color != null) {
            applyGlowTagWithCustomColor(level, blockDisplay, color);
            blockDisplay.setGlowingTag(true);
        }

        Brightness maxBrightness = new Brightness(15, 15);
        blockDisplay.setBrightnessOverride(maxBrightness);

        // 2. Configure all your properties exactly as before
        blockDisplay.setBlockState(blockState);

        //Vector3f translation = new Vector3f(-0.005f, -0.005f, -0.005f);
        //Transformation customTransform = new Transformation(translation, new Quaternionf(), new Vector3f(1.01f, 1.01f, 1.01f), new Quaternionf());
        blockDisplay.setTransformation(customTransform);

        blockDisplay.setPos(new Vec3(pos.getX() + 0.0, pos.getY() + 0.0, pos.getZ() + 0.0));

        // 3. Send the spawn packet ONLY to the targeted player
        //targetPlayer.connection.send(new ClientboundAddEntityPacket(blockDisplay, 0, blockDisplay.blockPosition()));

        // 4. Send the metadata packet so the client player knows its blockstate, scale, and glow configurations
        //targetPlayer.connection.send(new ClientboundSetEntityDataPacket(blockDisplay.getId(), blockDisplay.getEntityData().getNonDefaultValues()));

        level.addFreshEntity(blockDisplay);

        GraveDungeons.queueServerWork(2 + ticks, () -> {
            blockDisplay.remove(Entity.RemovalReason.DISCARDED);
            //targetPlayer.connection.send(new ClientboundRemoveEntitiesPacket(blockDisplay.getId()));
            //targetPlayer.connection.send(new ClientboundRemoveEntitiesPacket(blockDisplay.getId()));
        });
    }

    public static Player findNearestPlayerInWorldWithRange(LevelAccessor world, double _x, double _y, double _z, double range) {
        Entity entity = world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(_x, _y, _z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(_x, _y, _z))).findFirst().orElse(null);

        if (entity == null) {
            return null;
        }

        if (entity instanceof Player player) {
            return player;
        }

        return null;
    }
}
