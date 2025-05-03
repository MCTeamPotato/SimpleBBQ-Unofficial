package com.sihenzhang.simplebbq;

import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.block.SkeweringTableBlock;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import com.sihenzhang.simplebbq.block.entity.SkeweringTableBlockEntity;
import com.sihenzhang.simplebbq.item.GrillItem;
import com.sihenzhang.simplebbq.item.SkewerItem;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import com.sihenzhang.simplebbq.recipe.SimpleBBQRecipeType;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import com.sihenzhang.simplebbq.util.RLUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SimpleBBQRegistry {
    public static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>();
    public static final Map<ResourceLocation, Block> BLOCKS = new LinkedHashMap<>();

    public static final SimpleBBQRecipeType<GrillCookingRecipe> GRILL_COOKING_RECIPE_TYPE = registerRecipeType("grill_cooking", new SimpleBBQRecipeType<>("grill_cooking"));
    public static final GrillCookingRecipe.Serializer GRILL_COOKING_RECIPE_SERIALIZER = registerRecipeSerializer("grill_cooking", new GrillCookingRecipe.Serializer());
    public static final SimpleBBQRecipeType<SeasoningRecipe> SEASONING_RECIPE_TYPE = registerRecipeType("seasoning", new SimpleBBQRecipeType<>("seasoning"));
    public static final SeasoningRecipe.Serializer SEASONING_RECIPE_SERIALIZER = registerRecipeSerializer("seasoning", new SeasoningRecipe.Serializer());
    public static final SimpleBBQRecipeType<SkeweringRecipe> SKEWERING_RECIPE_TYPE = registerRecipeType("skewering", new SimpleBBQRecipeType<>("skewering"));
    public static final SkeweringRecipe.Serializer SKEWERING_RECIPE_SERIALIZER = registerRecipeSerializer("skewering", new SkeweringRecipe.Serializer());

    public static final SimpleParticleType CAMPFIRE_SMOKE_UNDER_GRILL = registerParticleType("campfire_smoke_under_grill", new SimpleParticleType(true));

    public static final GrillBlock GRILL_BLOCK = registerBlock("grill", new GrillBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(GrillBlock.LIT) ? 15 : 0).dynamicShape().noOcclusion()));
    public static final GrillItem GRILL_BLOCK_ITEM = registerItem("grill", new GrillItem());
    public static final BlockEntityType<GrillBlockEntity> GRILL_BLOCK_ENTITY = registerBlockEntityType("grill", BlockEntityType.Builder.of(GrillBlockEntity::new, GRILL_BLOCK).build());
    public static final SkeweringTableBlock SKEWERING_TABLE_BLOCK = registerBlock("skewering_table", new SkeweringTableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
    public static final BlockItem SKEWERING_TABLE_BLOCK_ITEM = registerItem("skewering_table", new BlockItem(SKEWERING_TABLE_BLOCK, new Item.Properties()));
    public static final BlockEntityType<SkeweringTableBlockEntity> SKEWERING_TABLE_BLOCK_ENTITY = registerBlockEntityType("skewering_table", BlockEntityType.Builder.of(SkeweringTableBlockEntity::new, SKEWERING_TABLE_BLOCK).build());


    public static final Item CHILI_POWDER = registerItem("chili_powder", new Item(new Item.Properties()));
    public static final Item CUMIN = registerItem("cumin", new Item(new Item.Properties()));
    public static final Item SALT_AND_PEPPER = registerItem("salt_and_pepper", new Item(new Item.Properties()));

    public static final Item BEEF_SKEWER = registerItem("beef_skewer", SkewerItem.builder(Items.BEEF).build());
    public static final Item COOKED_BEEF_SKEWER = registerItem("cooked_beef_skewer", SkewerItem.builder(Items.COOKED_BEEF).build());
    public static final Item CHICKEN_SKEWER = registerItem("chicken_skewer", SkewerItem.builder(Items.CHICKEN).build());
    public static final Item COOKED_CHICKEN_SKEWER = registerItem("cooked_chicken_skewer", SkewerItem.builder(Items.COOKED_CHICKEN, 1).build());
    public static final Item MUTTON_SKEWER = registerItem("mutton_skewer", SkewerItem.builder(Items.MUTTON).build());
    public static final Item COOKED_MUTTON_SKEWER = registerItem("cooked_mutton_skewer", SkewerItem.builder(Items.COOKED_MUTTON, 0.1F).build());
    public static final Item PORK_SKEWER = registerItem("pork_skewer", SkewerItem.builder(Items.PORKCHOP).build());
    public static final Item COOKED_PORK_SKEWER = registerItem("cooked_pork_skewer", SkewerItem.builder(Items.COOKED_PORKCHOP).build());
    public static final Item RABBIT_SKEWER = registerItem("rabbit_skewer", SkewerItem.builder(Items.RABBIT).build());
    public static final Item COOKED_RABBIT_SKEWER = registerItem("cooked_rabbit_skewer", SkewerItem.builder(Items.COOKED_RABBIT, 1).build());
    public static final Item COD_SKEWER = registerItem("cod_skewer", SkewerItem.builder(Items.COD).duration(24).build());
    public static final Item COOKED_COD_SKEWER = registerItem("cooked_cod_skewer", SkewerItem.builder(Items.COOKED_COD, 0.1F).duration(24).build());
    public static final Item SALMON_SKEWER = registerItem("salmon_skewer", SkewerItem.builder(Items.SALMON).build());
    public static final Item COOKED_SALMON_SKEWER = registerItem("cooked_salmon_skewer", SkewerItem.builder(Items.COOKED_SALMON, 0.1F).build());
    public static final Item BREAD_SLICE_SKEWER = registerItem("bread_slice_skewer", SkewerItem.builder(Items.BREAD).build());
    public static final Item TOAST_SKEWER = registerItem("toast_skewer", SkewerItem.builder(Items.BREAD, 0.3F).build());
    public static final Item MUSHROOM_SKEWER = registerItem("mushroom_skewer", SkewerItem.builder(Items.BROWN_MUSHROOM).nutrition(2).saturationMod(0.2F).duration(20).build());
    public static final Item ROASTED_MUSHROOM_SKEWER = registerItem("roasted_mushroom_skewer", SkewerItem.builder(Items.BROWN_MUSHROOM).nutrition(5).saturationMod(0.7F).duration(20).build());
    public static final Item POTATO_SKEWER = registerItem("potato_skewer", SkewerItem.builder(Items.POTATO).build());
    public static final Item BAKED_POTATO_SKEWER = registerItem("baked_potato_skewer", SkewerItem.builder(Items.BAKED_POTATO, 1).build());

    private static <T extends Item> T registerItem(String id, T item) {
        ITEMS.put(RLUtils.createRL(id), item);
        return item;
    }

    private static <T extends Block> T registerBlock(String id, T block) {
        BLOCKS.put(RLUtils.createRL(id), block);
        return block;
    }

    private static <T extends BlockEntityType<?>> T registerBlockEntityType(String id, T blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, RLUtils.createRL(id), blockEntityType);
    }

    private static <T extends ParticleType<?>> T registerParticleType(String id, T particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, RLUtils.createRL(id), particleType);
    }

    private static <T extends RecipeSerializer<?>> T registerRecipeSerializer(String id, T recipeSerializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, RLUtils.createRL(id), recipeSerializer);
    }

    private static <T extends RecipeType<?>> T registerRecipeType(String id, T recipeType) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, RLUtils.createRL(id), recipeType);
    }

    public static void initialize() {
        ITEMS.forEach((id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
        BLOCKS.forEach((id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block));
    }
}
