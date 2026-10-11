package net.gamerdragon0095.gravedungeons.item;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.item.custom.SoulTuner;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GraveDungeons.MODID);

    public static final DeferredItem<Item> SOUL_TUNER = ITEMS.registerItem("soul_tuner",
            SoulTuner::new,
            properties -> properties.stacksTo(1)
                    .component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
