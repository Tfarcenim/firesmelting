package tfar.firesmelting.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import tfar.firesmelting.ExampleMod;
import tfar.firesmelting.recipe.FireSmeltingRecipe;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

@JeiPlugin
public class JeiCompat implements IModPlugin {

  private static final ResourceLocation pluginid = new ResourceLocation(ExampleMod.MODID,ExampleMod.MODID);

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(Blocks.NETHERRACK), new ResourceLocation(ExampleMod.MODID,"centrifuge"));
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    IJeiHelpers jeiHelpers = registration.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
    registration.addRecipeCategories(
            new FireSmeltingRecipeCategory(guiHelper,"gui.ExampleMod.category.centrifuge"));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();
    Collection<FireSmeltingRecipe> recipes = getRecipes(recipeManager, FireSmeltingRecipe.FIRE_SMELTING);
    registration.addRecipes(recipes,new ResourceLocation(ExampleMod.MODID, "fire_smelting"));
  }

  private static <C extends IInventory, T extends IRecipe<C>> Collection<T> getRecipes(RecipeManager recipeManager, IRecipeType<T> recipeType) {
    Map<ResourceLocation, IRecipe<C>> recipesMap = recipeManager.getRecipes(recipeType);
    return (Collection<T>) recipesMap.values();
  }

  @Nonnull
  @Override
  public ResourceLocation getPluginUid() {
    return pluginid;
  }
}
