package com.sihenzhang.simplebbq.recipe;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class SkeweringRecipe implements Recipe<SkeweringInput> {
    private final Ingredient ingredient;
    private final int count;
    private final ItemStack result;

    public SkeweringRecipe(Ingredient ingredient, int count, ItemStack result) {
        Preconditions.checkArgument(count >= 1 && count <= 64, "Count must be between 1 and 64");
        this.ingredient = ingredient;
        this.count = count;
        this.result = result;
    }
    
    @Override
    public boolean matches(SkeweringInput skeweringInput, Level level) {
        var stack = skeweringInput.getItem(0);
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    @Override
    public ItemStack assemble(SkeweringInput skeweringInput, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getToastSymbol() {
        return SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ITEM.getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SimpleBBQRegistry.SKEWERING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return SimpleBBQRegistry.SKEWERING_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<SkeweringRecipe> {

        public static final MapCodec<SkeweringRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(SkeweringRecipe::getIngredient),
                Codec.INT.fieldOf("count").forGetter(SkeweringRecipe::getCount),
                ItemStack.CODEC.fieldOf("result").forGetter(SkeweringRecipe::getResult)
        ).apply(instance, SkeweringRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SkeweringRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC,SkeweringRecipe::getIngredient,
                        ByteBufCodecs.INT, SkeweringRecipe::getCount,
                        ItemStack.STREAM_CODEC, SkeweringRecipe::getResult,
                        SkeweringRecipe::new
                );
        @Override
        public MapCodec<SkeweringRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SkeweringRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
