package net.gamerdragon0095.gravedungeons.block.entity.custom;

import net.gamerdragon0095.gravedungeons.action.block.entity.EchoCrystalAction;
import net.gamerdragon0095.gravedungeons.action.block.entity.SoulBlockSensorAction;
import net.gamerdragon0095.gravedungeons.block.custom.EchoCrystal;
import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import static net.gamerdragon0095.gravedungeons.action.block.entity.EchoCrystalAction.setBlockStateBasedOnNBTData;

public class EchoCrystalEntity extends BlockEntity {
    public EchoCrystalEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.ECHO_CRYSTAL.get(), worldPosition, blockState);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
    }

    //Sync block entity
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, EchoCrystalEntity blockEntity) {
        // This runs on BOTH client and server 20 times a second

        if (!level.isClientSide()) {
            // Everything in here runs strictly on the Server side (good for data storage)
            //System.out.println("The block entity is successfully ticking at: " + pos);

            EchoCrystalAction.fetchInputsAndLogicTags(level, blockEntity);

            EchoCrystalAction.setOutput(level, pos);

            EchoCrystalAction.spawnOutPutParticles(level, pos, blockEntity);

            //level.getBlockState(pos).cycle(EchoCrystal.LOGIC_STATE);

            setBlockStateBasedOnNBTData(level, blockEntity, pos);

            level.sendBlockUpdated(pos, blockState, blockState, 3);



            //SoulBlockSensorAction.setOutput(level, pos);

            /*CompoundTag output = new CompoundTag();

            //Set default output value
            if (output.isEmpty()) {
                output.putBoolean("IsPowered", SoulBlockSensorAction.detectSoulBlock(level, pos));
            }

            level.getBlockEntity(pos).getPersistentData().put("Output", output);
            level.sendBlockUpdated(pos, blockState, blockState, 3);*/
        }
    }
}
