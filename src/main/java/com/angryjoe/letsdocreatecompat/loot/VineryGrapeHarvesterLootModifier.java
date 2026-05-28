package com.angryjoe.letsdocreatecompat.loot;

import com.angryjoe.letsdocreatecompat.registry.ModLootModifiers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Map;
import java.util.Set;

public final class VineryGrapeHarvesterLootModifier extends LootModifier {
    public static final MapCodec<VineryGrapeHarvesterLootModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance -> codecStart(instance)
                    .apply(instance, VineryGrapeHarvesterLootModifier::new));

    private static final Set<ResourceLocation> GRAPE_BUSHES = Set.of(
            vinery("red_grape_bush"),
            vinery("white_grape_bush"),
            vinery("savanna_grape_bush_red"),
            vinery("savanna_grape_bush_white"),
            vinery("taiga_grape_bush_red"),
            vinery("taiga_grape_bush_white"),
            vinery("jungle_grape_bush_red"),
            vinery("jungle_grape_bush_white")
    );

    private static final Map<ResourceLocation, ResourceLocation> GRAPES_BY_SEED = Map.ofEntries(
            Map.entry(vinery("red_grape_seeds"), vinery("red_grape")),
            Map.entry(vinery("white_grape_seeds"), vinery("white_grape")),
            Map.entry(vinery("savanna_grape_seeds_red"), vinery("savanna_grapes_red")),
            Map.entry(vinery("savanna_grape_seeds_white"), vinery("savanna_grapes_white")),
            Map.entry(vinery("taiga_grape_seeds_red"), vinery("taiga_grapes_red")),
            Map.entry(vinery("taiga_grape_seeds_white"), vinery("taiga_grapes_white")),
            Map.entry(vinery("jungle_grape_seeds_red"), vinery("jungle_grapes_red")),
            Map.entry(vinery("jungle_grape_seeds_white"), vinery("jungle_grapes_white"))
    );

    public VineryGrapeHarvesterLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!isAutomatedVineryGrapeHarvest(context)) {
            return generatedLoot;
        }

        boolean changed = false;
        ObjectArrayList<ItemStack> replacedLoot = new ObjectArrayList<>(generatedLoot.size());
        for (ItemStack stack : generatedLoot) {
            ItemStack replacement = replaceSeedWithGrape(stack);
            replacedLoot.add(replacement);
            changed |= replacement != stack;
        }

        return changed ? replacedLoot : generatedLoot;
    }

    private static boolean isAutomatedVineryGrapeHarvest(LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null) {
            return false;
        }

        // Create's harvester has no player in the loot context; normal player breaking should stay unchanged.
        if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            return false;
        }

        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return GRAPE_BUSHES.contains(blockId);
    }

    private static ItemStack replaceSeedWithGrape(ItemStack stack) {
        ResourceLocation seedId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        ResourceLocation grapeId = GRAPES_BY_SEED.get(seedId);
        if (grapeId == null) {
            return stack;
        }

        return BuiltInRegistries.ITEM.getOptional(grapeId)
                .map(grape -> new ItemStack(grape, stack.getCount()))
                .orElse(stack);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return ModLootModifiers.VINERY_GRAPE_HARVESTER.get();
    }

    private static ResourceLocation vinery(String path) {
        return ResourceLocation.fromNamespaceAndPath("vinery", path);
    }
}
