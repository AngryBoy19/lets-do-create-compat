package com.angryjoe.letsdocreatecompat.compat;

import com.angryjoe.letsdocreatecompat.registry.ModFluids;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.lang.reflect.Method;
import java.util.Optional;

public final class VineryFermentationBarrelFluidHandler implements IFluidHandler {
    private static final int MILLIBUCKETS_PER_JUICE_BOTTLE = 250;
    private static final int FALLBACK_MAX_FLUID_LEVEL = 100;
    private static final int FALLBACK_MAX_FLUID_INCREASE = 25;

    private final BlockEntity barrel;

    public VineryFermentationBarrelFluidHandler(BlockEntity barrel) {
        this.barrel = barrel;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        if (tank != 0) {
            return FluidStack.EMPTY;
        }

        int fluidLevel = getFluidLevel();
        if (fluidLevel <= 0) {
            return FluidStack.EMPTY;
        }

        return ModFluids.byJuiceType(getJuiceType())
                .map(juice -> new FluidStack(juice.source().get(), toMillibuckets(fluidLevel)))
                .orElse(FluidStack.EMPTY);
    }

    @Override
    public int getTankCapacity(int tank) {
        return tank == 0 ? toMillibuckets(getMaxFluidLevel()) : 0;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return tank == 0 && !stack.isEmpty() && ModFluids.byFluid(stack.getFluid()).isPresent();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return 0;
        }

        Optional<ModFluids.JuiceFluid> requestedJuice = ModFluids.byFluid(resource.getFluid());
        if (requestedJuice.isEmpty()) {
            return 0;
        }

        int currentLevel = getFluidLevel();
        String currentJuiceType = getJuiceType();
        String requestedJuiceType = requestedJuice.get().juiceType();
        if (currentLevel > 0 && !currentJuiceType.equals(requestedJuiceType)) {
            return 0;
        }

        int maxLevel = getMaxFluidLevel();
        int availableUnits = Math.max(0, maxLevel - currentLevel);
        int requestedUnits = toVineryUnits(resource.getAmount());
        int acceptedUnits = Math.min(availableUnits, requestedUnits);
        if (acceptedUnits <= 0) {
            return 0;
        }

        if (action.execute()) {
            setJuiceType(requestedJuiceType);
            setFluidLevel(currentLevel + acceptedUnits);
            sync();
        }

        return toMillibuckets(acceptedUnits);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return FluidStack.EMPTY;
        }

        Fluid currentFluid = getFluidInTank(0).getFluid();
        if (currentFluid != resource.getFluid()) {
            return FluidStack.EMPTY;
        }

        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack current = getFluidInTank(0);
        if (current.isEmpty() || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        int drainedAmount = Math.min(current.getAmount(), maxDrain);
        int drainedUnits = toVineryUnits(drainedAmount);
        if (drainedUnits <= 0) {
            return FluidStack.EMPTY;
        }

        FluidStack drained = new FluidStack(current.getFluid(), toMillibuckets(drainedUnits));
        if (action.execute()) {
            int newLevel = Math.max(0, getFluidLevel() - drainedUnits);
            setFluidLevel(newLevel);
            if (newLevel == 0) {
                setJuiceType("");
            }
            sync();
        }

        return drained;
    }

    private int getFluidLevel() {
        return invokeInt("getFluidLevel", 0);
    }

    private void setFluidLevel(int value) {
        invokeVoid("setFluidLevel", int.class, value);
    }

    private String getJuiceType() {
        Object result = invoke("getJuiceType");
        return result instanceof String value ? value : "";
    }

    private void setJuiceType(String value) {
        invokeVoid("setJuiceType", String.class, value);
    }

    private int getMaxFluidLevel() {
        return invokePlatformHelperInt("getMaxFluidLevel", FALLBACK_MAX_FLUID_LEVEL);
    }

    private int getMaxFluidIncrease() {
        return Math.max(1, invokePlatformHelperInt("getMaxFluidIncrease", FALLBACK_MAX_FLUID_INCREASE));
    }

    private int invokePlatformHelperInt(String methodName, int fallback) {
        try {
            Class<?> helper = Class.forName("net.satisfy.vinery.platform.PlatformHelper");
            Method method = helper.getMethod(methodName);
            Object result = method.invoke(null);
            return result instanceof Integer value ? value : fallback;
        } catch (ReflectiveOperationException ignored) {
            return fallback;
        }
    }

    private int toMillibuckets(int vineryUnits) {
        if (vineryUnits <= 0) {
            return 0;
        }
        return Math.max(1, vineryUnits * MILLIBUCKETS_PER_JUICE_BOTTLE / getMaxFluidIncrease());
    }

    private int toVineryUnits(int millibuckets) {
        return millibuckets * getMaxFluidIncrease() / MILLIBUCKETS_PER_JUICE_BOTTLE;
    }

    private int invokeInt(String methodName, int fallback) {
        Object result = invoke(methodName);
        return result instanceof Integer value ? value : fallback;
    }

    private Object invoke(String methodName) {
        try {
            Method method = barrel.getClass().getMethod(methodName);
            return method.invoke(barrel);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    private void invokeVoid(String methodName, Class<?> parameterType, Object value) {
        try {
            Method method = barrel.getClass().getMethod(methodName, parameterType);
            method.invoke(barrel, value);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private void sync() {
        barrel.setChanged();
        Level level = barrel.getLevel();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(barrel.getBlockPos(), barrel.getBlockState(), barrel.getBlockState(), 3);
        }
    }
}
