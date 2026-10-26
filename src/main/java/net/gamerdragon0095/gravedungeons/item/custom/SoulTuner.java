package net.gamerdragon0095.gravedungeons.item.custom;


import net.gamerdragon0095.gravedungeons.action.item.SoulTunerAction;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.apache.commons.lang3.math.Fraction;

import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SoulTuner extends BundleItem {
    private static final int MAX_CUSTOM_CAPACITY = 64;
    //private static final int MIN_CUSTOM_CAPACITY = 16;

    public SoulTuner(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> output, TooltipFlag tooltipFlag) {
        // Target string we want to filter out
        String targetInfoString = Component.translatable("item.minecraft.bundle.info").getString();

        // Pass an intercepted consumer to super to filter out the bundle text dynamically
        super.appendHoverText(stack, context, display, component -> {
            if (!component.getString().equals(targetInfoString)) {
                output.accept(component);
            }
        }, tooltipFlag);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack bundle) {
        BundleContents contents = bundle.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null || contents.items().isEmpty()) {
            return Optional.empty();
        }

        // Return a custom component instead of the vanilla BundleTooltip
        return Optional.of(new SoulTunerTooltip(contents));
    }

    public record SoulTunerTooltip(BundleContents contents) implements TooltipComponent {
        // Leaving this custom record block allows client side mixins / rendering systems
        // to read your layout separately from vanilla's hardcoded progress bar code.
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        //BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return false;
    }

    // Handled when you click AN ITEM with THIS BUNDLE
    @Override
    public boolean overrideStackedOnOther(ItemStack bundleStack, Slot slot, ClickAction action, Player player) {
        System.out.println("I clicked on item with this bundle! slotItem: '" + slot.getItem() + "' bundleItem: '" + bundleStack + "' clickAction: '" + action + "'");
        if (!slot.allowModification(player))
            return false;

        if (action == ClickAction.PRIMARY) {
            if (slot.getItem().isEmpty())
                return false;

            int amountInserted = tryInsertIntoCustomBundle(bundleStack, slot.getItem());
            if (amountInserted > 0) {
                slot.getItem().shrink(amountInserted);

                List<String> modelData = new ArrayList<>();

                ItemStack contents = SoulTunerAction.getContentsOrEmpty(bundleStack);

                if (!contents.isEmpty()) {
                    if (contents.getItem() == ModItems.AWAKENED_ECHO_SHARD.get()) {
                        modelData.add("echo");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.AWAKENED_AMETHYST_SHARD.get()) {
                        modelData.add("amethyst");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.INFESTED_IRON_NUGET.get()) {
                        modelData.add("iron");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.INFESTED_COPPER_NUGET.get()) {
                        modelData.add("copper");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.AWAKENED_LOOT_GEM.get()) {
                        modelData.add("loot");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    //bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(modelData, List.of(), List.of(), List.of()));
                } else if (contents.isEmpty() && modelData.isEmpty()) {
                    //bundleStack.remove(DataComponents.CUSTOM_MODEL_DATA);
                }

                bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
            }
        } else {
            if (slot.getItem().isEmpty()) {
                // Take an item out if holding nothing and right-clicking the bundle
                removeTopItem(bundleStack, slot, player);

                List<String> modelData = new ArrayList<>();

                ItemStack contents = SoulTunerAction.getContentsOrEmpty(bundleStack);

                if (!contents.isEmpty()) {
                    if (contents.getItem() == ModItems.AWAKENED_ECHO_SHARD.get()) {
                        modelData.add("echo");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.AWAKENED_AMETHYST_SHARD.get()) {
                        modelData.add("amethyst");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.INFESTED_IRON_NUGET.get()) {
                        modelData.add("iron");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.INFESTED_COPPER_NUGET.get()) {
                        modelData.add("copper");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.AWAKENED_LOOT_GEM.get()) {
                        modelData.add("loot");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    //bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(modelData, List.of(), List.of(), List.of()));
                } else if (contents.isEmpty() && modelData.isEmpty()) {
                    //bundleStack.remove(DataComponents.CUSTOM_MODEL_DATA);
                }

                bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
            } else {
                // Try to put the held item inside
                return false;
            }
        }


        return true;


    }

    // Handled when you click THIS BUNDLE with AN ITEM
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack bundleStack, ItemStack otherStack, Slot slot, ClickAction action, Player player, SlotAccess slotAccess) {
        //System.out.println("I clicked on bundle with this item! otherStack: '" + otherStack + "', slot: '" + slot.getItem() + "', bundleStack: '" + bundleStack + "', slotAccess: '" + slotAccess + "'");
        if (!slot.allowModification(player))
            return false;

        if (action == ClickAction.PRIMARY) {
            int amountInserted = tryInsertIntoCustomBundle(bundleStack, otherStack);

            if (!otherStack.isEmpty()) {
                insertItem(bundleStack, otherStack, slot, player);
                otherStack.shrink(amountInserted);

                List<String> modelData = new ArrayList<>();

                ItemStack contents = SoulTunerAction.getContentsOrEmpty(bundleStack);

                if (!contents.isEmpty()) {
                    if (contents.getItem() == ModItems.AWAKENED_ECHO_SHARD.get()) {
                        modelData.add("echo");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.AWAKENED_AMETHYST_SHARD.get()) {
                        modelData.add("amethyst");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.INFESTED_IRON_NUGET.get()) {
                        modelData.add("iron");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.INFESTED_COPPER_NUGET.get()) {
                        modelData.add("copper");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.AWAKENED_LOOT_GEM.get()) {
                        modelData.add("loot");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    //bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(modelData, List.of(), List.of(), List.of()));
                } else if (contents.isEmpty() && modelData.isEmpty()) {
                    //bundleStack.remove(DataComponents.CUSTOM_MODEL_DATA);
                }

                bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
            } else {
                return false;
            }
        } else {
            if (otherStack.isEmpty()) {
                removeTopItemBySlotAccess(bundleStack, slotAccess, player);

                List<String> modelData = new ArrayList<>();

                ItemStack contents = SoulTunerAction.getContentsOrEmpty(bundleStack);

                if (!contents.isEmpty()) {
                    if (contents.getItem() == ModItems.AWAKENED_ECHO_SHARD.get()) {
                        modelData.add("echo");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.AWAKENED_AMETHYST_SHARD.get()) {
                        modelData.add("amethyst");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.INFESTED_IRON_NUGET.get()) {
                        modelData.add("iron");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.INFESTED_COPPER_NUGET.get()) {
                        modelData.add("copper");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    if (contents.getItem() == ModItems.AWAKENED_LOOT_GEM.get()) {
                        modelData.add("loot");
                        bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
                    }

                    //bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(modelData, List.of(), List.of(), List.of()));
                } else if (contents.isEmpty() && modelData.isEmpty()) {
                    //bundleStack.remove(DataComponents.CUSTOM_MODEL_DATA);
                }

                bundleStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), modelData, List.of()));
            } else {
                // Try to put the held item inside
                return false;
            }
        }

        return true;



    }

    private void broadcastChangesOnContainerMenu(Player player) {
        AbstractContainerMenu containerMenu = player.containerMenu;
        if (containerMenu != null) {
            containerMenu.slotsChanged(player.getInventory());
        }

    }

    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertFailSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
    }

    private static void playDropContentsSound(Level level, Entity entity) {
        level.playSound((Entity)null, entity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void insertItem(ItemStack bundleStack, ItemStack itemToInsert, Slot slot, Player player) {
        int inserted = tryInsertIntoCustomBundle(bundleStack, itemToInsert);
        if (inserted > 0) {
            slot.safeTake(itemToInsert.getCount(), inserted, player);
        }
    }

    private int tryInsertIntoCustomBundle(ItemStack bundleStack, ItemStack insertStack) {
        if (insertStack.isEmpty() || insertStack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem
                && blockItem.getBlock() instanceof net.minecraft.world.level.block.ShulkerBoxBlock) {
            return 0;
        }

        if ((insertStack.getItem() != ModItems.AWAKENED_ECHO_SHARD.get())
                && (insertStack.getItem() != ModItems.AWAKENED_AMETHYST_SHARD.get())
                && (insertStack.getItem() != ModItems.INFESTED_IRON_NUGET.get())
                && (insertStack.getItem() != ModItems.INFESTED_COPPER_NUGET.get())
                && (insertStack.getItem() != ModItems.AWAKENED_LOOT_GEM.get())
        ) {
            return 0;
        }

        // Optional security: also block items that have internal container data components
        if (insertStack.has(net.minecraft.core.component.DataComponents.CONTAINER)) {
            return 0;
        }

        BundleContents currentContents = bundleStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        int currentWeight = getBundleWeight(currentContents);
        int itemWeight = getWeight(insertStack);

        /*if (MIN_CUSTOM_CAPACITY > MAX_CUSTOM_CAPACITY) {
            return 0;
        }*/

        if (currentWeight + itemWeight > MAX_CUSTOM_CAPACITY) {
            return 0;
        }

        /*if (!(currentWeight + itemWeight >= MIN_CUSTOM_CAPACITY)) {
            return 0;
        }*/

        // Reconstruct the bundle inventory list to append the new item
        List<ItemStack> newItems = new ArrayList<>();
        currentContents.items().forEach(stack -> newItems.add(stack.create()));

        // Find if item already exists to merge stacks, or add as new
        boolean merged = false;
        for (ItemStack existing : newItems) {
            if (ItemStack.isSameItemSameComponents(existing, insertStack)) {
                int transferable = Math.min(insertStack.getCount(), existing.getMaxStackSize() - existing.getCount());
                if (transferable > 0) {
                    existing.grow(transferable);
                    merged = true;
                    break;
                }
            }
        }

        if (!merged) {
            newItems.add(0, insertStack.copyWithCount(insertStack.getCount()));
        }

        // Apply updated list back into Data Components
        List<ItemStackTemplate> templateItems = new ArrayList<>();
        for (ItemStack stack : newItems) {
            if (!stack.isEmpty()) {
                templateItems.add(ItemStackTemplate.fromNonEmptyStack(stack));
            }
        }

        bundleStack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(templateItems));
        return insertStack.getCount();
    }

    private void removeTopItem(ItemStack bundleStack, Slot slot, Player player) {
        BundleContents currentContents = bundleStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        if (currentContents.items().isEmpty())
            return;

        List<ItemStack> newItems = new ArrayList<>();
        currentContents.items().forEach(stack -> newItems.add(stack.create()));

        ItemStack removed = newItems.remove(0); // Pop LIFO top item
        slot.setByPlayer(removed);

        List<ItemStackTemplate> templateItems = new ArrayList<>();
        for (ItemStack stack : newItems) {
            if (!stack.isEmpty()) {
                templateItems.add(ItemStackTemplate.fromNonEmptyStack(stack));
            }
        }

        bundleStack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(templateItems));
    }

    private void removeTopItemBySlotAccess(ItemStack bundleStack, SlotAccess slot, Player player) {
        BundleContents currentContents = bundleStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        if (currentContents.items().isEmpty())
            return;

        List<ItemStack> newItems = new ArrayList<>();
        currentContents.items().forEach(stack -> newItems.add(stack.create()));

        ItemStack removed = newItems.remove(0); // Pop LIFO top item
        slot.set(removed);

        List<ItemStackTemplate> templateItems = new ArrayList<>();
        for (ItemStack stack : newItems) {
            if (!stack.isEmpty()) {
                templateItems.add(ItemStackTemplate.fromNonEmptyStack(stack));
            }
        }

        bundleStack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(templateItems));
    }

    // Helper math mimicking vanilla's weight rules relative to your new MAX
    private int getBundleWeight(BundleContents contents) {
        int total = 0;
        for (ItemStackTemplate stack : contents.items()) {
            total += getWeight(stack.create());
        }
        return total;
    }

    private int getWeight(ItemStack stack) {
        // Unstackable items take a full 64 slots weight. 16-stackable items take 4.
        return (64 / stack.getMaxStackSize()) * stack.getCount();
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemstack) {
        return ItemUseAnimation.BUNDLE;
    }

    @Override
    public void hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        super.hurtEnemy(itemstack, entity, sourceentity);
        //SoulTunerAction.setItem(entity, itemstack);
    }

    @Override
    public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity, InteractionHand hand) {
        boolean retval = super.onEntitySwing(itemstack, entity, hand);
        //SoulTunerAction.setItem(entity, itemstack);
        return retval;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack soulTuner = context.getItemInHand();

        if (!level.isClientSide()) {
            //if (level.getBlockState(pos).getBlock() == ModBlocks.ECHO_CRYSTAL.get()) {
                SoulTunerAction.useAction(soulTuner, pos, player, level);
            //}
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        if (livingEntity instanceof Player player) {
            int useDuration = this.getUseDuration(itemStack, livingEntity);
            boolean isFirstTick = ticksRemaining == useDuration;
            if (isFirstTick || ticksRemaining < useDuration - 10 && ticksRemaining % 2 == 0) {
                //this.dropContent(level, player, itemStack);
            }
        }

    }

    @Override
    public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        super.inventoryTick(itemstack, world, entity, equipmentSlot);
        if ((equipmentSlot == EquipmentSlot.MAINHAND) || (equipmentSlot == EquipmentSlot.OFFHAND)) {
            BundleContents currentContents = itemstack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
            if (!currentContents.isEmpty()) {
                SoulTunerAction.highlightInputPosBlock(world, itemstack, entity);
            }
        }
    }

    @Override
    public ItemResource computeDefaultResource(Function<Item, ItemResource> resourceConstructor) {
        return super.computeDefaultResource(resourceConstructor);
    }


}
