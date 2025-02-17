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
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SimpleBBQRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SimpleBBQ.MOD_ID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SimpleBBQ.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, SimpleBBQ.MOD_ID);
    //public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, SimpleBBQ.MOD_ID);
    //public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, SimpleBBQ.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, SimpleBBQRecipeType<GrillCookingRecipe>> GRILL_COOKING_RECIPE_TYPE = RECIPE_TYPES.register("grill_cooking", () -> new SimpleBBQRecipeType<>("grill_cooking"));
    public static final DeferredHolder<RecipeSerializer<?>, GrillCookingRecipe.Serializer> GRILL_COOKING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("grill_cooking", GrillCookingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, SimpleBBQRecipeType<SeasoningRecipe>> SEASONING_RECIPE_TYPE = RECIPE_TYPES.register("seasoning", () -> new SimpleBBQRecipeType<>("seasoning"));
    public static final DeferredHolder<RecipeSerializer<?>, SeasoningRecipe.Serializer> SEASONING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("seasoning", SeasoningRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, SimpleBBQRecipeType<SkeweringRecipe>> SKEWERING_RECIPE_TYPE = RECIPE_TYPES.register("skewering", () -> new SimpleBBQRecipeType<>("skewering"));
    public static final DeferredHolder<RecipeSerializer<?>, SkeweringRecipe.Serializer> SKEWERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("skewering", SkeweringRecipe.Serializer::new);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CAMPFIRE_SMOKE_UNDER_GRILL = PARTICLE_TYPES.register("campfire_smoke_under_grill", () -> new SimpleParticleType(true));

    public static final DeferredHolder<Block, GrillBlock> GRILL_BLOCK = BLOCKS.register("grill", () -> new GrillBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(GrillBlock.LIT) ? 15 : 0).dynamicShape().noOcclusion()));
    public static final DeferredHolder<Item, GrillItem> GRILL_BLOCK_ITEM = ITEMS.register("grill", GrillItem::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrillBlockEntity>> GRILL_BLOCK_ENTITY = BLOCK_ENTITIES.register("grill", () -> BlockEntityType.Builder.of(GrillBlockEntity::new, GRILL_BLOCK.get()).build(null));
    public static final DeferredHolder<Block, SkeweringTableBlock> SKEWERING_TABLE_BLOCK = BLOCKS.register("skewering_table", () -> new SkeweringTableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredHolder<Item, BlockItem> SKEWERING_TABLE_BLOCK_ITEM = ITEMS.register("skewering_table", () -> new BlockItem(SKEWERING_TABLE_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SkeweringTableBlockEntity>> SKEWERING_TABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("skewering_table", () -> BlockEntityType.Builder.of(SkeweringTableBlockEntity::new, SKEWERING_TABLE_BLOCK.get()).build(null));


    public static final DeferredItem<Item> CHILI_POWDER = ITEMS.register("chili_powder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CUMIN = ITEMS.register("cumin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SALT_AND_PEPPER = ITEMS.register("salt_and_pepper", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BEEF_SKEWER = ITEMS.register("beef_skewer", () -> SkewerItem.builder(Items.BEEF).build());
    public static final DeferredItem<Item> COOKED_BEEF_SKEWER = ITEMS.register("cooked_beef_skewer", () -> SkewerItem.builder(Items.COOKED_BEEF).build());
    public static final DeferredItem<Item> CHICKEN_SKEWER = ITEMS.register("chicken_skewer", () -> SkewerItem.builder(Items.CHICKEN).build());
    public static final DeferredItem<Item> COOKED_CHICKEN_SKEWER = ITEMS.register("cooked_chicken_skewer", () -> SkewerItem.builder(Items.COOKED_CHICKEN, 1).build());
    public static final DeferredItem<Item> MUTTON_SKEWER = ITEMS.register("mutton_skewer", () -> SkewerItem.builder(Items.MUTTON).build());
    public static final DeferredItem<Item> COOKED_MUTTON_SKEWER = ITEMS.register("cooked_mutton_skewer", () -> SkewerItem.builder(Items.COOKED_MUTTON, 0.1F).build());
    public static final DeferredItem<Item> PORK_SKEWER = ITEMS.register("pork_skewer", () -> SkewerItem.builder(Items.PORKCHOP).build());
    public static final DeferredItem<Item> COOKED_PORK_SKEWER = ITEMS.register("cooked_pork_skewer", () -> SkewerItem.builder(Items.COOKED_PORKCHOP).build());
    public static final DeferredItem<Item> RABBIT_SKEWER = ITEMS.register("rabbit_skewer", () -> SkewerItem.builder(Items.RABBIT).build());
    public static final DeferredItem<Item> COOKED_RABBIT_SKEWER = ITEMS.register("cooked_rabbit_skewer", () -> SkewerItem.builder(Items.COOKED_RABBIT, 1).build());
    public static final DeferredItem<Item> COD_SKEWER = ITEMS.register("cod_skewer", () -> SkewerItem.builder(Items.COD).duration(24).build());
    public static final DeferredItem<Item> COOKED_COD_SKEWER = ITEMS.register("cooked_cod_skewer", () -> SkewerItem.builder(Items.COOKED_COD, 0.1F).duration(24).build());
    public static final DeferredItem<Item> SALMON_SKEWER = ITEMS.register("salmon_skewer", () -> SkewerItem.builder(Items.SALMON).build());
    public static final DeferredItem<Item> COOKED_SALMON_SKEWER = ITEMS.register("cooked_salmon_skewer", () -> SkewerItem.builder(Items.COOKED_SALMON, 0.1F).build());
    public static final DeferredItem<Item> BREAD_SLICE_SKEWER = ITEMS.register("bread_slice_skewer", () -> SkewerItem.builder(Items.BREAD).build());
    public static final DeferredItem<Item> TOAST_SKEWER = ITEMS.register("toast_skewer", () -> SkewerItem.builder(Items.BREAD, 0.3F).build());
    public static final DeferredItem<Item> MUSHROOM_SKEWER = ITEMS.register("mushroom_skewer", () -> SkewerItem.builder(Items.BROWN_MUSHROOM).nutrition(2).saturationMod(0.2F).duration(20).build());
    public static final DeferredItem<Item> ROASTED_MUSHROOM_SKEWER = ITEMS.register("roasted_mushroom_skewer", () -> SkewerItem.builder(Items.BROWN_MUSHROOM).nutrition(5).saturationMod(0.7F).duration(20).build());
    public static final DeferredItem<Item> POTATO_SKEWER = ITEMS.register("potato_skewer", () -> SkewerItem.builder(Items.POTATO).build());
    public static final DeferredItem<Item> BAKED_POTATO_SKEWER = ITEMS.register("baked_potato_skewer", () -> SkewerItem.builder(Items.BAKED_POTATO, 1).build());
}
