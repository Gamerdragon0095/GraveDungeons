package net.gamerdragon0095.gravedungeons.item;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.item.custom.SoulTuner;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GraveDungeons.MODID);

    public static final DeferredItem<Item> SOUL_TUNER = ITEMS.registerItem("soul_tuner",
            SoulTuner::new,
            properties -> properties.stacksTo(1)
                    .component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));

    public static final DeferredItem<Item> AWAKENED_AMETHYST_SHARD = ITEMS.registerSimpleItem("awakened_amethyst_shard",
            properties -> properties.stacksTo(1)
                    );

    public static final DeferredItem<Item> AWAKENED_ECHO_SHARD = ITEMS.registerSimpleItem("awakened_echo_shard",
            properties -> properties.stacksTo(1)
                    );

    public static final DeferredItem<Item> AWAKENED_ECHO_FRAGMENT = ITEMS.registerSimpleItem("awakened_echo_fragment",
            properties -> properties.stacksTo(64)
                    );

    public static final DeferredItem<Item> INFESTED_IRON_NUGET = ITEMS.registerSimpleItem("infested_iron_nuget",
            properties -> properties.stacksTo(1)
                    );

    public static final DeferredItem<Item> INFESTED_COPPER_NUGET = ITEMS.registerSimpleItem("infested_copper_nuget",
            properties -> properties.stacksTo(1)
                    );

    public static final DeferredItem<Item> AWAKENED_LOOT_GEM = ITEMS.registerSimpleItem("awakened_loot_gem",
            properties -> properties.stacksTo(1)
                    );

    public static final ResourceKey<DecoratedPotPattern> IMMORTAL_SHERD_PATTERN = ResourceKey.create(
            Registries.DECORATED_POT_PATTERN,
            Identifier.fromNamespaceAndPath(GraveDungeons.MODID, "my_sherd_pattern")
    );

    public static final DeferredItem<Item> IMMORTAL_POTTERY_SHERD = ITEMS.registerSimpleItem("immortal_pottery_sherd",
                    properties -> properties.stacksTo(16)
            );
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
