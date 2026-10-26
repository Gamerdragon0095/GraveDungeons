package net.gamerdragon0095.gravedungeons.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
//import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotRenderer.class)
public class DecoratedPotRendererMixin {

    @Shadow @Final private SpriteGetter sprites;
    @Shadow @Final private ModelPart frontSide;
    @Shadow @Final private ModelPart backSide;
    @Shadow @Final private ModelPart leftSide;
    @Shadow @Final private ModelPart rightSide;

    @Inject(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/world/level/block/entity/PotDecorations;I)V",
            at = @At("TAIL")
    )
    private void onDrawCustomSides(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, PotDecorations decorations, int outlineColor, CallbackInfo ci) {
        // Safe reference to your texture asset path identifier inside the decorated pot atlas grid
        SpriteId customPatternId = new SpriteId(
                Sheets.DECORATED_POT_SHEET,
                Identifier.fromNamespaceAndPath("gravedungeons", "entity/decorated_pot/immortal_pottery_pattern")
        );

        // 1. Check Front Face
        if (decorations.front().isPresent() && decorations.front().get() == ModItems.IMMORTAL_POTTERY_SHERD.get()) {
            submitNodeCollector.submitModelPart(this.frontSide, poseStack, customPatternId.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(customPatternId), false, false, -1, null, outlineColor);
        }
        // 2. Check Back Face
        if (decorations.back().isPresent() && decorations.back().get() == ModItems.IMMORTAL_POTTERY_SHERD.get()) {
            submitNodeCollector.submitModelPart(this.backSide, poseStack, customPatternId.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(customPatternId), false, false, -1, null, outlineColor);
        }
        // 3. Check Left Face
        if (decorations.left().isPresent() && decorations.left().get() == ModItems.IMMORTAL_POTTERY_SHERD.get()) {
            submitNodeCollector.submitModelPart(this.leftSide, poseStack, customPatternId.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(customPatternId), false, false, -1, null, outlineColor);
        }
        // 4. Check Right Face
        if (decorations.right().isPresent() && decorations.right().get() == ModItems.IMMORTAL_POTTERY_SHERD.get()) {
            submitNodeCollector.submitModelPart(this.rightSide, poseStack, customPatternId.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(customPatternId), false, false, -1, null, outlineColor);
        }
    }
}
