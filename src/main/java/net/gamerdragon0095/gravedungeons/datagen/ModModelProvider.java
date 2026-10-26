package net.gamerdragon0095.gravedungeons.datagen;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, GraveDungeons.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // --- Standard Items ---
        itemModels.generateFlatItem(ModItems.SOUL_TUNER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.AWAKENED_AMETHYST_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.AWAKENED_ECHO_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.AWAKENED_ECHO_FRAGMENT.get(), ModelTemplates.FLAT_ITEM);

        // --- Blocks ---
        blockModels.createTrivialCube(ModBlocks.SOUL_BLOCK_SENSOR.get());
        blockModels.createTrivialCube(ModBlocks.SOUL_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.INFESTED_EARTH.get());
        blockModels.createTrivialCube(ModBlocks.INFESTED_STONE.get());
        blockModels.woodProvider(ModBlocks.INFESTED_BONE.get()).logWithHorizontal(ModBlocks.INFESTED_BONE.get());
        blockModels.createNonTemplateModelBlock(ModBlocks.RESONATOR_BLOCK.get());

        // --- BlockItems ---
        blockModels.registerSimpleFlatItemModel(ModBlocks.ECHO_CRYSTAL.get());
    }
}
