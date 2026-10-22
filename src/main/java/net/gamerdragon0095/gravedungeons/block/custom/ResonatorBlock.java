package net.gamerdragon0095.gravedungeons.block.custom;

import com.mojang.serialization.MapCodec;
import net.gamerdragon0095.dragon_lib.data.entity.GetCurrentHandWithItem;
import net.gamerdragon0095.gravedungeons.action.block.entity.EchoCrystalAction;
import net.gamerdragon0095.gravedungeons.action.block.entity.ResonatorBlockAction;
import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.gamerdragon0095.gravedungeons.block.entity.custom.EchoCrystalEntity;
import net.gamerdragon0095.gravedungeons.block.entity.custom.ResonatorBlockEntity;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class ResonatorBlock extends BaseEntityBlock {

    public ResonatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResonatorBlockEntity(blockPos, blockState);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState blockstate, LivingEntity entity, ItemStack itemstack) {
        super.setPlacedBy(world, pos, blockstate, entity, itemstack);
        ResonatorBlockAction.placeResonatorBlock(world, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        //if (level.isClientSide())
        //   return super.useWithoutItem(state, level, pos, player, hitResult);

        //ItemStack itemUsing = GetCurrentHandWithItem.mainHandPriority(player, ModItems.SOUL_TUNER.get());

        if (player.getMainHandItem().isEmpty()) {
            //EchoCrystalAction.placeEchoCrystal(level, pos);
            //ResonatorBlockAction.despinsOneItemFromTable(level, pos, "gravedungeons:genaric/grave_dungeon_small", 0.5f, Direction.UP);
            //return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
        if (entity.isSteppingCarefully() && entity instanceof ServerPlayer player) {

            if (level instanceof ServerLevel _level) {
                //if (player.gameMode().isCreative()) {
                    //ItemStack stack = player.getMainHandItem();

                    //if (stack.getItem() == ModItems.SOUL_TUNER.get()) {
                        //ResonatorBlockAction.highlightEffectedBlocksForPlayersWithResonator(_level, stack, player, pos);
                    //}
                //}
            }


            //entity.hurt(level.damageSources().hotFloor(), 1.0F);
        }

        super.stepOn(level, pos, onState, entity);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Optional: If you only want your code to run on the Server side (common for processing/logic)
        //if (level.isClientSide()) {
        //    return null;
        //}

        // Match your BlockEntityType with the registry entry to return the matching ticker
        return createTickerHelper(type, ModBlockEntities.RESONATOR_BLOCK.get(), ResonatorBlockEntity::tick);
        //return null;
    }
}
