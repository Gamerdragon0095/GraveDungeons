package net.gamerdragon0095.gravedungeons.action.lootTables;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.Identifier;

public class GenericLootTableAction {
    public static ResourceKey<LootTable> getLootTableKeyFromStringOr(String lootTable, Level level, String fallback) {
        // 1. Fallback default value
        ResourceKey<LootTable> defaultKey = ResourceKey.create(
                Registries.LOOT_TABLE,
                Identifier.parse(fallback)
        );

        //if (itemStack == null || !itemStack.has(DataComponents.CUSTOM_NAME)) {
            //return defaultKey;
        //}

        //Component customNameComponent = itemStack.get(DataComponents.CUSTOM_NAME);
        //if (customNameComponent == null) return defaultKey;

        //String lootTableString = customNameComponent.getString();

        // 2. Safely parse the string. tryParse returns null if the string contains illegal characters.
        Identifier location = Identifier.tryParse(lootTable);
        if (location == null) {
            return defaultKey;
        }

        // 3. Create the target resource key
        ResourceKey<LootTable> targetKey = ResourceKey.create(Registries.LOOT_TABLE, location);

        // 4. Verify that this loot table actually exists inside the server's datapack registries
        boolean exists = level.getServer()
                .reloadableRegistries()
                .getLootTable(targetKey) != LootTable.EMPTY;

        return exists ? targetKey : defaultKey;
    }
}
