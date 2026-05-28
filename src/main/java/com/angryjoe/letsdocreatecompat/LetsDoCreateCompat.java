package com.angryjoe.letsdocreatecompat;

import com.angryjoe.letsdocreatecompat.compat.VineryBarrelFluidCapability;
import com.angryjoe.letsdocreatecompat.registry.ModFluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(LetsDoCreateCompat.MOD_ID)
public final class LetsDoCreateCompat {
    public static final String MOD_ID = "lets_do_create_compat";

    public LetsDoCreateCompat(IEventBus modBus) {
        ModFluids.register(modBus);
        modBus.addListener(VineryBarrelFluidCapability::registerCapabilities);
    }
}
