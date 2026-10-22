package net.gamerdragon0095.gravedungeons.block.entity.custom;
import net.gamerdragon0095.gravedungeons.action.block.entity.ResonatorBlockAction;
import net.gamerdragon0095.gravedungeons.action.block.entity.SoulBlockSensorAction;
import net.gamerdragon0095.gravedungeons.block.custom.SoulBlockSensor;
import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;


public class ResonatorBlockEntity extends BlockEntity {

    public ResonatorBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.RESONATOR_BLOCK.get(), worldPosition, blockState);
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

    // NeoForge tickers MUST be static methods
    public static void tick(Level level, BlockPos pos, BlockState blockState, ResonatorBlockEntity blockEntity) {
        // This runs on BOTH client and server 20 times a second

        if (!level.isClientSide()) {
            // Everything in here runs strictly on the Server side (good for data storage)
            //System.out.println("TEST1");

            //Player player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 64.0, false);

            ResonatorBlockAction.tickDespinsLootTimer(level, blockEntity);
            ResonatorBlockAction.fetchInputTags(level, blockEntity);

            ResonatorBlockAction.setOutput(level, pos);
        }
    }
}
