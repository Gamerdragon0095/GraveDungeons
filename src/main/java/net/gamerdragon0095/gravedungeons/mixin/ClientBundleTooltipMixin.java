package net.gamerdragon0095.gravedungeons.mixin;

import net.minecraft.client.gui.Font;
//import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientBundleTooltip.class)
public class ClientBundleTooltipMixin {

    /**
     * Intercepts the progress bar method inside the Client Bundle UI class.
     * By calling ci.cancel(), it completely skips drawing the green/red weight bar graphics.
     */
    @Inject(method = "extractProgressbar", at = @At("HEAD"), cancellable = true)
    private static void hideProgressBar(int x, int y, Font font, GuiGraphicsExtractor graphics, Fraction weight, CallbackInfo ci) {
        ci.cancel();
    }
}
