package com.sihenzhang.simplebbq.recipe;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SeasoningRecipe implements Recipe<RecipeInput> {
    private final LoadingCache<SeasoningRecipe, List<ItemStack>> cachedResultItems;

    private final Ingredient ingredient;
    private final Ingredient seasoning;
    private final String name;

    public SeasoningRecipe(Ingredient ingredient, Ingredient seasoning, String name) {
        this.ingredient = ingredient;
        this.seasoning = seasoning;
        this.name = name;

        this.cachedResultItems = CacheBuilder.newBuilder().maximumSize(25).build(new CacheLoader<>() {
            @Override
            public List<ItemStack> load(SeasoningRecipe key) {
                return Arrays.stream(key.getIngredient().getItems()).map(stack -> {
                    var copiedStack = stack.copy();
                    CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                    CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
                    var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
                    seasoningList.add(StringTag.valueOf(key.getName().toLowerCase(Locale.ROOT)));
                    // Sort the seasoning list so that item can be stacked even if the seasoning order is not the same
                    seasoningList.sort(Comparator.comparing(Tag::getAsString));
                    seasoningTag.put("SeasoningList", seasoningList);
                    compoundTag.put("Seasoning", seasoningTag);
                    copiedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
                    return copiedStack;
                }).toList();
            }
        });
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {

        var inputStack = recipeInput.getItem(0);
        var seasoningStack = recipeInput.getItem(1);
        if (!ingredient.test(inputStack) || !seasoning.test(seasoningStack)) {
            return false;
        }
        CompoundTag compoundTag = inputStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
        if (seasoningTag != null && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            for (var i = 0; i < seasoningList.size(); i++) {
                if (seasoningList.getString(i).equalsIgnoreCase(name)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        var result = recipeInput.getItem(0).copy();
        CompoundTag compoundTag = result.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
        var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
        seasoningList.add(StringTag.valueOf(name.toLowerCase(Locale.ROOT)));
        // Sort the seasoning list so that item can be stacked even if the seasoning order is not the same
        seasoningList.sort(Comparator.comparing(Tag::getAsString));
        seasoningTag.put("SeasoningList", seasoningList);
        compoundTag.put("Seasoning", seasoningTag);
        result.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
        return result;
    }
    

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Ingredient getSeasoning() {
        return seasoning;
    }

    public LoadingCache<SeasoningRecipe, List<ItemStack>> getCachedResultItems() {
        return cachedResultItems;
    }

    public String getName() {
        return name;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> {
            list.add(ingredient);
            list.add(seasoning);
        });
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SimpleBBQRegistry.SEASONING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SimpleBBQRegistry.SEASONING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<SeasoningRecipe> {

        public static final MapCodec<SeasoningRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(SeasoningRecipe::getIngredient),
                Ingredient.CODEC.fieldOf("seasoning").forGetter(SeasoningRecipe::getSeasoning),
                Codec.STRING.fieldOf("name").forGetter(SeasoningRecipe::getName)
        ).apply(instance, SeasoningRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SeasoningRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC,SeasoningRecipe::getIngredient,
                        Ingredient.CONTENTS_STREAM_CODEC,SeasoningRecipe::getSeasoning,
                        ByteBufCodecs.STRING_UTF8,SeasoningRecipe::getName,
                        SeasoningRecipe::new
                );
        
        @Override
        public MapCodec<SeasoningRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SeasoningRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
