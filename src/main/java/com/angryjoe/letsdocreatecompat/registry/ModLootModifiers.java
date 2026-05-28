package com.angryjoe.letsdocreatecompat.registry;

import com.angryjoe.letsdocreatecompat.LetsDoCreateCompat;
import com.angryjoe.letsdocreatecompat.loot.VineryGrapeHarvesterLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, LetsDoCreateCompat.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<VineryGrapeHarvesterLootModifier>>
            VINERY_GRAPE_HARVESTER = LOOT_MODIFIERS.register(
                    "vinery_grape_harvester",
                    () -> VineryGrapeHarvesterLootModifier.CODEC
            );

    private ModLootModifiers() {
    }

    public static void register(IEventBus modBus) {
        LOOT_MODIFIERS.register(modBus);
    }
}
