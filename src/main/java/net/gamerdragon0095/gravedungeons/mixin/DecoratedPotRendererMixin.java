package net.gamerdragon0095.gravedungeons.mixin;

import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(DecoratedPotRenderer.class)
public class DecoratedPotRendererMixin {

    @Inject(method = "getSideSprite", at = @At("HEAD"), cancellable = true)
    private static void onGetSideSprite(Optional<Item> item, CallbackInfoReturnable<SpriteId> cir) {
        // Check if the side decoration item contains your custom sherd
        if (item.isPresent() && item.get() == ModItems.IMMORTAL_POTTERY_SHERD.get()) {
            // Manually point it to the decorated pot atlas sheet with your custom path identifier
            cir.setReturnValue(new SpriteId(
                    Sheets.DECORATED_POT_SHEET,
                    Identifier.fromNamespaceAndPath("gravedungeons", "entity/decorated_pot/immortal_pottery_pattern")
            ));
        }
    }
}
