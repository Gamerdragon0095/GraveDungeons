package net.gamerdragon0095.gravedungeons.tags;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> REGENERATABLE_SCULK = createTag("regeneratable_sculk");

        public static final TagKey<Block> TUNABLE_BLOCK = createTag("tunable_block");

        public static final TagKey<Block> TUNABLE_INPUT_BLOCK = createTag("tunable_input_block");


        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath(GraveDungeons.MODID, name));
        }
    }


    public static class Items {



        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(GraveDungeons.MODID, name));
        }
    }
}
