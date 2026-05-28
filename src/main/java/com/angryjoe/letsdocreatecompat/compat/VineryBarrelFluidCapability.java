package com.angryjoe.letsdocreatecompat.compat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class VineryBarrelFluidCapability {
    private static final ResourceLocation FERMENTATION_BARREL =
            ResourceLocation.fromNamespaceAndPath("vinery", "fermentation_barrel");

    private VineryBarrelFluidCapability() {
    }

    @SuppressWarnings("unchecked")
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        BlockEntityType<?> type = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(FERMENTATION_BARREL);
        if (type == null) {
            return;
        }

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                (BlockEntityType<BlockEntity>) type,
                (blockEntity, side) -> new VineryFermentationBarrelFluidHandler(blockEntity)
        );
    }
}
