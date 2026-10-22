package net.gamerdragon0095.gravedungeons.datagen;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.tags.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, GraveDungeons.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ECHO_CRYSTAL.get())
                .add(ModBlocks.SOUL_BLOCK_SENSOR.get())
                .add(ModBlocks.SOUL_BLOCK.get())
                .add(ModBlocks.INFESTED_STONE.get())
                .add(ModBlocks.INFESTED_BONE.get())
                .add(ModBlocks.RESONATOR_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.SOUL_BLOCK_SENSOR.get())
                .add(ModBlocks.SOUL_BLOCK.get());

        tag(ModTags.Blocks.REGENERATABLE_SCULK)
                .add(ModBlocks.ECHO_CRYSTAL.get())
                .add(ModBlocks.SOUL_BLOCK_SENSOR.get())
                .add(ModBlocks.SOUL_BLOCK.get())
                .add(ModBlocks.INFESTED_STONE.get())
                .add(ModBlocks.INFESTED_BONE.get());

        tag(ModTags.Blocks.TUNABLE_INPUT_BLOCK)
                .add(ModBlocks.ECHO_CRYSTAL.get())
                .add(ModBlocks.SOUL_BLOCK_SENSOR.get());

        tag(ModTags.Blocks.TUNABLE_BLOCK)
                .addTag(ModTags.Blocks.TUNABLE_INPUT_BLOCK)
                .add(ModBlocks.RESONATOR_BLOCK.get());
    }
}
