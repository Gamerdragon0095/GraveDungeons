package net.gamerdragon0095.gravedungeons.client;

import net.gamerdragon0095.gravedungeons.GraveDungeons;
import net.gamerdragon0095.gravedungeons.item.custom.SoulTuner;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@EventBusSubscriber(modid = GraveDungeons.MODID, value = Dist.CLIENT)
public class ClientTooltipHandler {

    /**
     * Natively hooks into the NeoForge Client Tooltip Factory registry.
     * This completely prevents any "Unknown TooltipComponent" or Mixin interface crashes.
     */
    @SubscribeEvent
    public static void onRegisterTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        // Direct type mapping: maps your custom record straight into the bundle grid UI handler
        event.register(SoulTuner.SoulTunerTooltip.class, customTooltip -> new ClientBundleTooltip(customTooltip.contents()));
    }
}
