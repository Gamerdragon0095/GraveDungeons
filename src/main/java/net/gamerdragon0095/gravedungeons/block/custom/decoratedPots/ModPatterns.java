package net.gamerdragon0095.gravedungeons.block.custom.decoratedPots;

//import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPatterns {
    // Create the registry for decorated pot patterns
    public static final DeferredRegister<DecoratedPotPattern> POT_PATTERNS =
            DeferredRegister.create(Registries.DECORATED_POT_PATTERN, "gravedungeons");

    // Register the pattern itself, pointing it to your asset ID string
    public static final DeferredHolder<DecoratedPotPattern, DecoratedPotPattern> IMMORTAL_POTTERY_PATTERN =
            POT_PATTERNS.register("immortal_pottery_pattern",
                    () -> new DecoratedPotPattern(Identifier.fromNamespaceAndPath("gravedungeons", "immortal_pottery_pattern")));

    public static void register(IEventBus eventBus) {
        POT_PATTERNS.register(eventBus);
    }
}
