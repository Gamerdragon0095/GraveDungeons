package net.gamerdragon0095.gravedungeons.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
//import net.gamerdragon0095.gravedungeons.block.custom.EchoCrystal;
import net.gamerdragon0095.gravedungeons.item.ModDataComponentTypes;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.*;
//import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
import net.minecraft.client.renderer.item.properties.numeric.CustomModelDataProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
//import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplates;
//import net.minecraft.client.data.models.model.TextureMapping;
//import net.minecraft.client.data.models.model.TextureSlot;
//import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, GraveDungeons.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        Identifier baseId = ModItems.SOUL_TUNER.getId(); // Gets 'gravedungeons:your_custom_item'

        Identifier empty = ModelTemplates.FLAT_ITEM.create(
                Identifier.fromNamespaceAndPath(baseId.getNamespace(), "item/" + baseId.getPath() + "_empty"),
                new TextureMapping().put(TextureSlot.LAYER0, new Material(Identifier.fromNamespaceAndPath(GraveDungeons.MODID, "item/" + baseId.getPath()))),
                itemModels.modelOutput
        );

        Identifier echo = ModelTemplates.FLAT_ITEM.create(
                Identifier.fromNamespaceAndPath(baseId.getNamespace(), "item/" + baseId.getPath() + "_echo"),
                new TextureMapping().put(TextureSlot.LAYER0, new Material(Identifier.fromNamespaceAndPath(GraveDungeons.MODID, "item/" + baseId.getPath() + "_echo"))),
                itemModels.modelOutput
        );

        Identifier amethyst = ModelTemplates.FLAT_ITEM.create(
                Identifier.fromNamespaceAndPath(baseId.getNamespace(), "item/" + baseId.getPath() + "_amethyst"),
                new TextureMapping().put(TextureSlot.LAYER0, new Material(Identifier.fromNamespaceAndPath(GraveDungeons.MODID, "item/" + baseId.getPath() + "_amethyst"))),
                itemModels.modelOutput
        );

        Identifier iron = ModelTemplates.FLAT_ITEM.create(
                Identifier.fromNamespaceAndPath(baseId.getNamespace(), "item/" + baseId.getPath() + "_iron"),
                new TextureMapping().put(TextureSlot.LAYER0, new Material(Identifier.fromNamespaceAndPath(GraveDungeons.MODID, "item/" + baseId.getPath() + "_iron"))),
                itemModels.modelOutput
        );

        Identifier copper = ModelTemplates.FLAT_ITEM.create(
                Identifier.fromNamespaceAndPath(baseId.getNamespace(), "item/" + baseId.getPath() + "_copper"),
                new TextureMapping().put(TextureSlot.LAYER0, new Material(Identifier.fromNamespaceAndPath(GraveDungeons.MODID, "item/" + baseId.getPath() + "_copper"))),
                itemModels.modelOutput
        );

        itemModels.itemModelOutput.accept(
                ModItems.SOUL_TUNER.get(),
                new SelectItemModel.Unbaked(
                        Optional.empty(),
                        // Replace the anonymous inner class with a native data-driven key reference
                        new SelectItemModel.UnbakedSwitch(
                        SoulTunerModeProperty.INSTANCE,
                        List.of(
                                new SelectItemModel.SwitchCase(List.of(1), new CuboidItemModelWrapper.Unbaked(echo, Optional.empty(), List.of())),
                                new SelectItemModel.SwitchCase(List.of(2), new CuboidItemModelWrapper.Unbaked(amethyst, Optional.empty(), List.of())),
                                new SelectItemModel.SwitchCase(List.of(3), new CuboidItemModelWrapper.Unbaked(iron, Optional.empty(), List.of())),
                                new SelectItemModel.SwitchCase(List.of(4), new CuboidItemModelWrapper.Unbaked(copper, Optional.empty(), List.of()))
                        )
                ),
                        Optional.of(new CuboidItemModelWrapper.Unbaked(empty, Optional.empty(), List.of()))
                )
        );

        // 2. Declare the custom range dispatch logic wrapper cleanly using vanilla definitions
        // This automatically saves out to assets/gravedungeons/items/your_custom_item.json

        //Items
        //itemModels.generateFlatItem(ModItems.SOUL_TUNER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        //itemModels.declareCustomModelItem(ModItems.SOUL_TUNER.get());

        itemModels.generateFlatItem(ModItems.AWAKENED_AMETHYST_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.AWAKENED_ECHO_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.AWAKENED_ECHO_FRAGMENT.get(), ModelTemplates.FLAT_ITEM);

        //Blocks
        //blockModels.createAmethystCluster(ModBlocks.ECHO_CRYSTAL.get());
        blockModels.createTrivialCube(ModBlocks.SOUL_BLOCK_SENSOR.get());
        blockModels.createTrivialCube(ModBlocks.SOUL_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.INFESTED_EARTH.get());
        blockModels.createTrivialCube(ModBlocks.INFESTED_STONE.get());
        blockModels.woodProvider(ModBlocks.INFESTED_BONE.get()).logWithHorizontal(ModBlocks.INFESTED_BONE.get());

        //blockModels.createTrivialBlock(ModBlocks.RESONATOR_BLOCK.get(), (TexturedModel.Provider) ModelTemplates.CUBE_COLUMN);
        blockModels.createNonTemplateModelBlock(ModBlocks.RESONATOR_BLOCK.get());

        //BlockItems
        blockModels.registerSimpleFlatItemModel(ModBlocks.ECHO_CRYSTAL.get());
    }

    public static class SoulTunerModeProperty implements SelectItemModelProperty<Integer> {
        public static final SoulTunerModeProperty INSTANCE = new SoulTunerModeProperty();

        @Override
        public @Nullable Integer get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
            return itemStack.getOrDefault(ModDataComponentTypes.SOUL_TUNER_MODE.get(), 0);
        }

        @Override
        public Codec<Integer> valueCodec() {
            return Codec.INT;
        }

        @Override
        public SelectItemModelProperty.Type<? extends SelectItemModelProperty<Integer>, Integer> type() {
            // Use the static factory method to return a valid registration wrapper instead of null
            return SelectItemModelProperty.Type.create(
                    com.mojang.serialization.MapCodec.unit(INSTANCE),
                    Codec.INT
            );
        }
    }
}
