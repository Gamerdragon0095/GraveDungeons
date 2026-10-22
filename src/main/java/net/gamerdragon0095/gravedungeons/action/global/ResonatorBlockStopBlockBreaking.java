package net.gamerdragon0095.gravedungeons.action.global;

import net.gamerdragon0095.gravedungeons.action.block.GenericBlockAction;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.List;

@EventBusSubscriber
public class ResonatorBlockStopBlockBreaking {
    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        //System.out.println("Test1");
        BlockPos pos = event.getPos();
        LevelAccessor level = event.getLevel();

        if (event.getPlayer().gameMode().isCreative()) {
            return;
        }

        if (event.getState().is(ModTags.Blocks.REGENERATABLE_SCULK)) {
            List<BlockPos> targetBlockPoses = GenericBlockAction.getPosOfBlocksInRadiusOfType(
                    level,
                    pos,
                    10,
                    ModBlocks.RESONATOR_BLOCK.get()
            );

            // 2. Iterate through all BlockPos in the box
            for (BlockPos pos_ : targetBlockPoses) {

                //BlockState state = level.getBlockState(pos_);

                // 3. Check if it matches the target block
                // Do something when found, e.g., spawn particles or log
                if (event instanceof ICancellableEvent _cancellable) {
                    _cancellable.setCanceled(true);
                    break;
                }
            }
        }
    }
}
