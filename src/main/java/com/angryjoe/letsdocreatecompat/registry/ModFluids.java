package com.angryjoe.letsdocreatecompat.registry;

import com.angryjoe.letsdocreatecompat.LetsDoCreateCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, LetsDoCreateCompat.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, LetsDoCreateCompat.MOD_ID);

    public static final List<JuiceFluid> JUICES = new ArrayList<>();

    public static final JuiceFluid APPLE_JUICE = registerJuice("apple_juice", "apple", 0xFFE3C35A);
    public static final JuiceFluid RED_GRAPEJUICE = registerJuice("red_grapejuice", "red_general", 0xFF7A1432);
    public static final JuiceFluid WHITE_GRAPEJUICE = registerJuice("white_grapejuice", "white_general", 0xFFE1D78A);
    public static final JuiceFluid RED_SAVANNA_GRAPEJUICE = registerJuice("red_savanna_grapejuice", "red_savanna", 0xFF9C2A2A);
    public static final JuiceFluid WHITE_SAVANNA_GRAPEJUICE = registerJuice("white_savanna_grapejuice", "white_savanna", 0xFFE8D674);
    public static final JuiceFluid RED_TAIGA_GRAPEJUICE = registerJuice("red_taiga_grapejuice", "red_taiga", 0xFF74306A);
    public static final JuiceFluid WHITE_TAIGA_GRAPEJUICE = registerJuice("white_taiga_grapejuice", "white_taiga", 0xFFC8D994);
    public static final JuiceFluid RED_JUNGLE_GRAPEJUICE = registerJuice("red_jungle_grapejuice", "red_jungle", 0xFFB5354A);
    public static final JuiceFluid WHITE_JUNGLE_GRAPEJUICE = registerJuice("white_jungle_grapejuice", "white_jungle", 0xFFD9E67A);

    private ModFluids() {
    }

    public static void register(IEventBus modBus) {
        FLUID_TYPES.register(modBus);
        FLUIDS.register(modBus);
    }

    public static Optional<JuiceFluid> byFluid(Fluid fluid) {
        return JUICES.stream()
                .filter(juice -> juice.source().get() == fluid || juice.flowing().get() == fluid)
                .findFirst();
    }

    public static Optional<JuiceFluid> byJuiceType(String juiceType) {
        return JUICES.stream()
                .filter(juice -> juice.juiceType().equals(juiceType))
                .findFirst();
    }

    private static JuiceFluid registerJuice(String name, String juiceType, int tint) {
        DeferredHolder<FluidType, FluidType> fluidType = FLUID_TYPES.register(name,
                () -> new FluidType(FluidType.Properties.create()
                        .descriptionId("fluid." + LetsDoCreateCompat.MOD_ID + "." + name)
                        .density(1000)
                        .viscosity(1000)));

        AtomicReference<Supplier<? extends Fluid>> sourceRef = new AtomicReference<>();
        AtomicReference<Supplier<? extends Fluid>> flowingRef = new AtomicReference<>();

        DeferredHolder<Fluid, FlowingFluid> source = FLUIDS.register(name,
                () -> new BaseFlowingFluid.Source(properties(fluidType, sourceRef.get(), flowingRef.get())));
        DeferredHolder<Fluid, FlowingFluid> flowing = FLUIDS.register("flowing_" + name,
                () -> new BaseFlowingFluid.Flowing(properties(fluidType, sourceRef.get(), flowingRef.get())));

        sourceRef.set(source);
        flowingRef.set(flowing);

        JuiceFluid juice = new JuiceFluid(name, juiceType, tint, fluidType, source, flowing);
        JUICES.add(juice);
        return juice;
    }

    private static BaseFlowingFluid.Properties properties(
            Supplier<? extends FluidType> fluidType,
            Supplier<? extends Fluid> source,
            Supplier<? extends Fluid> flowing
    ) {
        return new BaseFlowingFluid.Properties(fluidType, source, flowing)
                .slopeFindDistance(2)
                .levelDecreasePerBlock(2)
                .tickRate(20);
    }

    public record JuiceFluid(
            String name,
            String juiceType,
            int tint,
            DeferredHolder<FluidType, FluidType> fluidType,
            DeferredHolder<Fluid, FlowingFluid> source,
            DeferredHolder<Fluid, FlowingFluid> flowing
    ) {
    }
}
