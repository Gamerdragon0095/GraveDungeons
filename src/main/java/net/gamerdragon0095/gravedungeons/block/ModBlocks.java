package net.gamerdragon0095.gravedungeons.block;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.action.block.entity.EchoCrystalAction;
import net.gamerdragon0095.gravedungeons.block.custom.EchoCrystal;
import net.gamerdragon0095.gravedungeons.block.custom.ResonatorBlock;
import net.gamerdragon0095.gravedungeons.block.custom.SoulBlock;
import net.gamerdragon0095.gravedungeons.block.custom.SoulBlockSensor;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GraveDungeons.MODID);

    public static final DeferredBlock<Block> ECHO_CRYSTAL = registerBlock("echo_crystal",
            properties -> new EchoCrystal(6f, 12f, properties
                    .strength(2f)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .lightLevel(state -> (int) EchoCrystalAction.echoCrystalLightLevel(state))
            ));

    public static final DeferredBlock<Block> SOUL_BLOCK_SENSOR = registerBlock("soul_block_sensor",
            properties -> new SoulBlockSensor(properties
                    .strength(3f)
                    .sound(SoundType.SCULK_CATALYST)
            ));

    public static final DeferredBlock<Block> SOUL_BLOCK = registerBlock("soul_block",
            properties -> new SoulBlock(properties
                    .strength(2f)
                    .sound(SoundType.SCULK)
            ));

    public static final DeferredBlock<Block> RESONATOR_BLOCK = registerBlock("resonator_block",
            properties -> new ResonatorBlock(properties
                    .strength(2f)
                    .sound(SoundType.SCULK)
                    .requiresCorrectToolForDrops()
            ));

    public static final DeferredBlock<Block> INFESTED_EARTH = registerBlock("infested_earth",
            properties -> new Block(properties
                    .strength(2f)
                    .sound(SoundType.PACKED_MUD)
            ));

    public static final DeferredBlock<Block> INFESTED_STONE = registerBlock("infested_stone",
            properties -> new Block(properties
                    .strength(2f)
                    .sound(SoundType.DRIPSTONE_BLOCK)
                    .requiresCorrectToolForDrops()
            ));

    public static final DeferredBlock<Block> INFESTED_BONE = registerBlock("infested_bone",
            properties -> new RotatedPillarBlock(properties
                    .strength(2f)
                    .sound(SoundType.BONE_BLOCK)
            ));




    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
