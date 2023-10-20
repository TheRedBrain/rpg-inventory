package com.github.theredbrain.bamcore.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public abstract class AbstractCraftingBenchRecipe implements Recipe<Inventory> {
    protected final DefaultedList<Ingredient> input;
    protected final ItemStack output;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final Identifier id;
    protected final String group;

    public AbstractCraftingBenchRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, Identifier id, String group, DefaultedList<Ingredient> input, ItemStack output) {
        this.type = type;
        this.serializer = serializer;
        this.id = id;
        this.group = group;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        DefaultedList<Ingredient> ingredients = input;

        for (Ingredient ingredient : ingredients) {

            for (int i = 0; i < inventory.size(); i++) {
                if (ingredient.test(inventory.getStack(i))) {
                    if (inventory.getStack(i).getCount() > 1) {
                        inventory.getStack(i).setCount(inventory.getStack(i).getCount() - 1);
                        ingredient = Ingredient.empty();
                    } else if (inventory.getStack(i).getCount() == 1) {
                        inventory.setStack(i, ItemStack.EMPTY);
                        ingredient = Ingredient.empty();
                    }
                    break;
                }
            }
            if (ingredient != null) {
                if(!ingredient.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.output.copy();
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    public static class Serializer<T extends AbstractCraftingBenchRecipe>
            implements RecipeSerializer<T> {
        final AbstractCraftingBenchRecipe.Serializer.RecipeFactory<T> recipeFactory;

        public Serializer(AbstractCraftingBenchRecipe.Serializer.RecipeFactory<T> recipeFactory) {
            this.recipeFactory = recipeFactory;
        }

        @Override
        public T read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> defaultedList = AbstractCraftingBenchRecipe.Serializer.getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for abstractCraftingBench recipe");
            }
            String string2 = JsonHelper.getString(jsonObject, "result");
            int i = JsonHelper.getInt(jsonObject, "count");
            ItemStack itemStack = new ItemStack(Registries.ITEM.get(new Identifier(string2)), i);
            return this.recipeFactory.create(identifier, string, defaultedList, itemStack);
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();
            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i), false);
                if (ingredient.isEmpty()) continue;
                defaultedList.add(ingredient);
            }
            return defaultedList;
        }

        @Override
        public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);
            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }
            ItemStack itemStack = packetByteBuf.readItemStack();
            return this.recipeFactory.create(identifier, string, defaultedList, itemStack);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, T abstractCraftingBenchRecipe) {
            packetByteBuf.writeString(((AbstractCraftingBenchRecipe)abstractCraftingBenchRecipe).group);
            packetByteBuf.writeVarInt(abstractCraftingBenchRecipe.input.size());
            for (Ingredient ingredient : abstractCraftingBenchRecipe.input) {
                ingredient.write(packetByteBuf);
            }
//            ((AbstractCraftingBenchRecipe)abstractCraftingBenchRecipe).input.write(packetByteBuf);
            packetByteBuf.writeItemStack(((AbstractCraftingBenchRecipe)abstractCraftingBenchRecipe).output);
        }

//        // TODO maybe needs to be reactivated
//        @Override
//        public /* synthetic */ Recipe read(Identifier id, PacketByteBuf buf) {
//            return this.read(id, buf);
//        }
//
//        @Override
//        public /* synthetic */ Recipe read(Identifier id, JsonObject json) {
//            return this.read(id, json);
//        }

        public static interface RecipeFactory<T extends AbstractCraftingBenchRecipe> {
            public T create(Identifier var1, String var2, DefaultedList<Ingredient> var3, ItemStack var4);
        }
    }
}
