package com.sihenzhang.simplebbq.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GrillCookingRecipe extends AbstractCookingRecipe {

    public GrillCookingRecipe(String pGroup, Ingredient pIngredient, ItemStack pResult, int pCookingTime) {
        super(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE.get(), pGroup, CookingBookCategory.MISC, pIngredient, pResult, 0.0F, pCookingTime);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getToastSymbol() {
        return SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SimpleBBQRegistry.GRILL_COOKING_RECIPE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<GrillCookingRecipe> {

        public static final MapCodec<GrillCookingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("group").forGetter(GrillCookingRecipe::getGroup),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(GrillCookingRecipe::getIngredient),
            ItemStack.CODEC.fieldOf("result").forGetter(GrillCookingRecipe::getResult),
            Codec.INT.fieldOf("cookingtime").forGetter(GrillCookingRecipe::getCookingTime)
        ).apply(instance, GrillCookingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, GrillCookingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8,GrillCookingRecipe::getGroup,
                        Ingredient.CONTENTS_STREAM_CODEC,GrillCookingRecipe::getIngredient,
                        ItemStack.STREAM_CODEC,GrillCookingRecipe::getResult,
                        ByteBufCodecs.INT,GrillCookingRecipe::getCookingTime,
                        GrillCookingRecipe::new
                );

        @Override
        public MapCodec<GrillCookingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GrillCookingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
