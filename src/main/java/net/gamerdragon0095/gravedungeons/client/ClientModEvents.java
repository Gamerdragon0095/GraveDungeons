package net.gamerdragon0095.gravedungeons.client;

import net.gamerdragon0095.gravedungeons.item.ModDataComponentTypes;
import net.gamerdragon0095.gravedungeons.item.ModItems;
//import net.minecraft.client.renderer.item.ItemModelProperties;
//import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
//import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
//import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//import net.minecraft.resources.Identifier;

public class ClientModEvents {
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register the property link: (Item, Property ID, Property Getter)

        });
    }
}
