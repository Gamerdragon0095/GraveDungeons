package net.gamerdragon0095.gravedungeons.block.custom;

import net.gamerdragon0095.gravedungeons.action.block.SoulBlockActions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SoulBlock extends Block {
    //public static final MapCodec<SoulBlock> CODEC = simpleCodec(SoulBlock::new);

    public SoulBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        //if (level.isClientSide())
        //   return super.useWithoutItem(state, level, pos, player, hitResult);

        //ItemStack itemUsing = GetCurrentHandWithItem.mainHandPriority(player, ModItems.SOUL_TUNER.get());

        if (player.getMainHandItem() == ItemStack.EMPTY) {
            //EchoCrystalAction.placeEchoCrystal(level, pos);

            SoulBlockActions.pushBlock(level, pos.getX(), pos.getY(), pos.getZ(), hitResult.getDirection(), player);

            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
