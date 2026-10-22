package net.gamerdragon0095.gravedungeons.item;

import com.mojang.serialization.Codec;
import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(net.minecraft.core.registries.Registries.DATA_COMPONENT_TYPE, GraveDungeons.MODID);

    public static final Supplier<DataComponentType<BlockPos>> INPUT_POS = DATA_COMPONENT_TYPES.register(
            "input_pos", () -> DataComponentType.<BlockPos>builder()
                    .persistent(BlockPos.CODEC)
                    .build()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SOUL_TUNER_MODE =
            DATA_COMPONENT_TYPES.register("soul_tuner_mode", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT) // Codec for saving/loading to disk
                    .networkSynchronized(ByteBufCodecs.INT) // StreamCodec for syncing across the network
                    .build());
}
