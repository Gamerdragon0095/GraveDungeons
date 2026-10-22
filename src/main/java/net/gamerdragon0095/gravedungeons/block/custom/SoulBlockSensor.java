package net.gamerdragon0095.gravedungeons.block.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.gamerdragon0095.gravedungeons.action.block.entity.EchoCrystalAction;
import net.gamerdragon0095.gravedungeons.action.block.entity.SoulBlockSensorAction;
import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.gamerdragon0095.gravedungeons.block.entity.custom.EchoCrystalEntity;
import net.gamerdragon0095.gravedungeons.block.entity.custom.SoulBlockSensorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class SoulBlockSensor extends BaseEntityBlock {

    public static final MapCodec<SoulBlockSensor> CODEC = simpleCodec(SoulBlockSensor::new);

    public SoulBlockSensor(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return (MapCodec<? extends BaseEntityBlock>) CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoulBlockSensorEntity(blockPos, blockState);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState blockstate, LivingEntity entity, ItemStack itemstack) {
        super.setPlacedBy(world, pos, blockstate, entity, itemstack);
        SoulBlockSensorAction.placeSoulBlockSensor(world, pos);
        //EchoCrystalAction.placeEchoCrystal(world, pos);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Optional: If you only want your code to run on the Server side (common for processing/logic)
        //if (level.isClientSide()) {
        //    return null;
        //}

        // Match your BlockEntityType with the registry entry to return the matching ticker
        return createTickerHelper(type, ModBlockEntities.SOUL_BLOCK_SENSOR.get(), SoulBlockSensorEntity::tick);
    }
}
