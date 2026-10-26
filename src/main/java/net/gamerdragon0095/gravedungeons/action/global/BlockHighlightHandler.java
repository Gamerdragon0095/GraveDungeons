package net.gamerdragon0095.gravedungeons.action.global;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.action.block.entity.ResonatorBlockAction;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.data.block.entity.EchoCrystalData;
import net.gamerdragon0095.gravedungeons.data.block.entity.RelativeCompoundTagPos;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.ArrayList;
import java.util.List;
//import net.neoforged.neoforge.client.event.RenderHighlightEvent;

// Replace "yourmodid" with your actual mod ID. Ensure it runs only on the CLIENT side.
@EventBusSubscriber(modid = GraveDungeons.MODID, value = Dist.CLIENT)
public class BlockHighlightHandler {

    // Cache the last block position to prevent text spamming every single tick
    private static BlockPos lastHighlightedPos = null;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        // Ensure the player is in an active world and not in a menu screen
        if (mc.level == null || mc.player == null || mc.screen != null) {
            return;
        }

        // Grab whatever is currently under the player's crosshair target
        HitResult target = mc.hitResult;

        if (target != null && target.getType() == HitResult.Type.BLOCK && target instanceof BlockHitResult blockHit) {
            BlockPos currentPos = blockHit.getBlockPos();
            BlockState state = mc.level.getBlockState(currentPos);

            // Verify if it matches your custom block registry object
            if (state.is(ModBlocks.ECHO_CRYSTAL.get())
                    || state.is(ModBlocks.RESONATOR_BLOCK.get())
            ) {

                // Only fire the event logic if the player moved their focus to a brand new block position
                if (!currentPos.equals(lastHighlightedPos)) {
                    lastHighlightedPos = currentPos;

                    //String logicState = EchoCrystalData.getLogicMode(mc.level.getBlockEntity(currentPos));

                    BlockEntity blockEntity = mc.level.getBlockEntity(currentPos);



                }
            } else {
                // Reset cache if they look at a different type of block
                lastHighlightedPos = null;
            }
        } else {
            // Reset cache if they look at the sky, fluid, void, or an entity
            lastHighlightedPos = null;
        }
    }
}
