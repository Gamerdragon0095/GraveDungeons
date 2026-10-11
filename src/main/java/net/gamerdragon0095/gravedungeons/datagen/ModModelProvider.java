package net.gamerdragon0095.gravedungeons.datagen;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, GraveDungeons.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        //Items
        itemModels.generateFlatItem(ModItems.SOUL_TUNER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        //itemModels.generateFlatItem(ModBlocks.ECHO_CRYSTAL.get().asItem(), ModelTemplates.FLAT_ITEM);

        //Blocks
        blockModels.createAmethystCluster(ModBlocks.ECHO_CRYSTAL.get());
        blockModels.registerSimpleFlatItemModel(ModBlocks.ECHO_CRYSTAL.get());
    }
}
