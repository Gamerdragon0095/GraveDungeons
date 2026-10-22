package net.gamerdragon0095.gravedungeons.block.entity;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.block.custom.SoulBlockSensor;
import net.gamerdragon0095.gravedungeons.block.entity.custom.EchoCrystalEntity;
import net.gamerdragon0095.gravedungeons.block.entity.custom.ResonatorBlockEntity;
import net.gamerdragon0095.gravedungeons.block.entity.custom.SoulBlockSensorEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, GraveDungeons.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EchoCrystalEntity>> ECHO_CRYSTAL =
            BLOCK_ENTITIES.register("echo_crystal",
            () -> new BlockEntityType<>(
                    EchoCrystalEntity::new,
                    ModBlocks.ECHO_CRYSTAL.get())
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulBlockSensorEntity>> SOUL_BLOCK_SENSOR =
            BLOCK_ENTITIES.register("soul_block_sensor",
                    () -> new BlockEntityType<>(
                            SoulBlockSensorEntity::new,
                            ModBlocks.SOUL_BLOCK_SENSOR.get()
                    )
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResonatorBlockEntity>> RESONATOR_BLOCK =
            BLOCK_ENTITIES.register("resonator_block",
                    () -> new BlockEntityType<>(
                            ResonatorBlockEntity::new,
                            ModBlocks.RESONATOR_BLOCK.get()
                    )
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
