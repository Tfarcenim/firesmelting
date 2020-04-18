package tfar.firesmelting.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

public class FireSmeltingRecipe implements IRecipe<IInventory> {
  public static final String s = "firesmelting:fire_smelting";

  public static final IRecipeType<FireSmeltingRecipe> FIRE_SMELTING = IRecipeType.register(s);
  @ObjectHolder(s)
  public static IRecipeSerializer<FireSmeltingRecipe> TYPE;

  public final ResourceLocation id;
  public final Ingredient ingredient;
  public final ItemStack output;

  public FireSmeltingRecipe(ResourceLocation id,
                            Ingredient ingredient, ItemStack output) {
    this.id = id;
    this.ingredient = ingredient;
    this.output = output;
  }

  @Override
  public boolean matches(IInventory inventory, World world) {
    return ingredient.test(inventory.getStackInSlot(0));
  }

  @Override
  @Nonnull
  public ItemStack getCraftingResult(IInventory p_77572_1_) {
    return output.copy();
  }

  /**
   * Used to determine if this recipe can fit in a grid of the given width/height
   *
   * @param p_194133_1_
   * @param p_194133_2_
   */
  @Override
  public boolean canFit(int p_194133_1_, int p_194133_2_) {
    return true;
  }

  /**
   * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
   * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
   */
  @Override
  @Nonnull
  public ItemStack getRecipeOutput() {
    return output;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return TYPE;
  }

  @Override
  public IRecipeType<?> getType() {
    return FIRE_SMELTING;
  }

  public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FireSmeltingRecipe> {
    final IRecipeFactory factory;

    public Serializer(Serializer.IRecipeFactory p_i50146_1_) {
      this.factory = p_i50146_1_;
    }

    public FireSmeltingRecipe read(ResourceLocation recipeId, JsonObject json) {
      JsonElement jsonelement = (JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
      Ingredient ingredient = Ingredient.deserialize(jsonelement);
      //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
      if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
      ItemStack itemstack;
      if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
      else {
        String s1 = JSONUtils.getString(json, "result");
        ResourceLocation resourcelocation = new ResourceLocation(s1);
        itemstack = new ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + s1 + " does not exist")));
      }
      return this.factory.create(recipeId, ingredient, itemstack);
    }

    public FireSmeltingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
      Ingredient ingredient = Ingredient.read(buffer);
      ItemStack itemstack = buffer.readItemStack();
      return this.factory.create(recipeId, ingredient, itemstack);
    }

    public void write(PacketBuffer buffer, FireSmeltingRecipe recipe) {
      recipe.ingredient.write(buffer);
      buffer.writeItemStack(recipe.output);
    }

    public interface IRecipeFactory {
      FireSmeltingRecipe create(ResourceLocation id, Ingredient input, ItemStack output);
    }
  }
}
