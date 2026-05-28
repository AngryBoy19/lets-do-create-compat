package com.angryjoe.letsdocreatecompat.client;

import com.angryjoe.letsdocreatecompat.LetsDoCreateCompat;
import com.angryjoe.letsdocreatecompat.registry.ModFluids;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = LetsDoCreateCompat.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {
    private static final ResourceLocation WATER_STILL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");
    private static final ResourceLocation WATER_FLOW = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow");

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        for (ModFluids.JuiceFluid juice : ModFluids.JUICES) {
            event.registerFluidType(new TintedFluidExtensions(juice.tint()), juice.fluidType());
        }
    }

    private record TintedFluidExtensions(int tint) implements IClientFluidTypeExtensions {
        @Override
        public ResourceLocation getStillTexture() {
            return WATER_STILL;
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return WATER_FLOW;
        }

        @Override
        public int getTintColor() {
            return tint;
        }
    }
}
