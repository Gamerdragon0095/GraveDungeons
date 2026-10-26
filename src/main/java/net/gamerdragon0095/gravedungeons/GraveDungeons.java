package net.gamerdragon0095.gravedungeons;

import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.gamerdragon0095.gravedungeons.block.ModBlocks;
import net.gamerdragon0095.gravedungeons.block.custom.decoratedPots.ModPatterns;
import net.gamerdragon0095.gravedungeons.block.entity.ModBlockEntities;
//import net.gamerdragon0095.gravedungeons.gui.ModMenus;
import net.gamerdragon0095.gravedungeons.item.ModDataComponentTypes;
import net.gamerdragon0095.gravedungeons.item.ModItems;
//import net.gamerdragon0095.gravedungeons.world.features.StructureFeature;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.Map;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GraveDungeons.MODID)
public class GraveDungeons {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "gravedungeons";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public GraveDungeons(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::registerNetworking);


        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModDataComponentTypes.DATA_COMPONENT_TYPES.register(modEventBus);
        ModPatterns.register(modEventBus);
        //StructureFeature.REGISTRY.register(modEventBus);
        //ModMenus.REGISTRY.register(modEventBus);
        //modEventBus.addListener(this::commonSetup);
    }

    /*private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                // 1. Find the private static map inside DecoratedPotPatterns that maps items to their patterns
                // In 26.1.2, this map tracks which pattern resource key belongs to which sherd item ID string
                Field itemToPatternField = DecoratedPotPatterns.class.getDeclaredField("ITEM_TO_POT_TEXTURE");
                itemToPatternField.setAccessible(true);

                // 2. Safely grab the live map instance
                @SuppressWarnings("unchecked")
                Map<Item, ResourceKey<DecoratedPotPattern>> map = (Map<Item, ResourceKey<DecoratedPotPattern>>) itemToPatternField.get(null);

                // 3. Inject your custom item and its pattern key directly into the map
                map.put(
                        ModItems.IMMORTAL_POTTERY_SHERD.get(),
                        ModPatterns.IMMORTAL_POTTERY_PATTERN.getKey()
                );

            } catch (Exception e) {
                // Logs any mapping layout failures to your console during startup
                LogManager.getLogger().error("Failed to inject custom pottery sherd mapping via reflection", e);
            }
        });
    }*/

    private static boolean networkingRegistered = false;
    private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> MESSAGES = new HashMap<>();

    private record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
    }


    public static <T extends CustomPacketPayload> void addNetworkMessage(CustomPacketPayload.Type<T> id, StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
        if (networkingRegistered)
            throw new IllegalStateException("Cannot register new network messages after networking has been registered");
        MESSAGES.put(id, new NetworkMessage<>(reader, handler));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID);
        MESSAGES.forEach((id, networkMessage) -> registrar.playBidirectional(id, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler(), ((NetworkMessage) networkMessage).handler()));
        networkingRegistered = true;
    }

    private static final Queue<IntObjectPair<Runnable>> workToBeScheduled = new ConcurrentLinkedQueue<>();
    private static final PriorityQueue<TickTask> workQueue = new PriorityQueue<>(Comparator.comparingInt(TickTask::getTick));

    public static void queueServerWork(int delay, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workToBeScheduled.add(new IntObjectImmutablePair<>(delay, action));
    }

    @SubscribeEvent
    public void tick(ServerTickEvent.Post event) {
        int currentTick = event.getServer().getTickCount();
        IntObjectPair<Runnable> work;
        while ((work = workToBeScheduled.poll()) != null) {
            workQueue.add(new TickTask(currentTick + work.leftInt(), work.right()));
        }
        while (!workQueue.isEmpty() && currentTick >= workQueue.peek().getTick()) {
            workQueue.poll().run();
        }
    }

    private static Object minecraft;
    private static MethodHandle playerHandle;

    @Nullable
    public static Player clientPlayer() {
        if (FMLEnvironment.getDist().isClient()) {
            try {
                if (minecraft == null || playerHandle == null) {
                    Class<?> minecraftClass = Class.forName("net.minecraft.client.Minecraft");
                    minecraft = MethodHandles.publicLookup().findStatic(minecraftClass, "getInstance", MethodType.methodType(minecraftClass)).invoke();
                    playerHandle = MethodHandles.publicLookup().findGetter(minecraftClass, "player", Class.forName("net.minecraft.client.player.LocalPlayer"));
                }
                return (Player) playerHandle.invoke(minecraft);
            } catch (Throwable e) {
                LOGGER.error("Failed to get client player", e);
                return null;
            }
        } else {
            return null;
        }
    }



}
