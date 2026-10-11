package net.gamerdragon0095.gravedungeons.block.entity;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.block.entity.custom.EchoCrystalEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, GraveDungeons.MODID);

    public static final Supplier<BlockEntityType<EchoCrystalEntity>> ECHO_CRYSTAL =
            BLOCK_ENTITIES.register("echo_crystal", () -> new BlockEntityType<>(
                    EchoCrystalEntity::new, ModBlocks.ECHO_CRYSTAL.get()));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
