package tfar.firesmelting.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import tfar.firesmelting.ExampleMod;
import tfar.firesmelting.recipe.FireSmeltingRecipe;

public class FireSmeltingRecipeCategory implements IRecipeCategory<FireSmeltingRecipe> {

  private final IDrawable icon;
  private final IDrawable background;
  private final String localizedName;

  public FireSmeltingRecipeCategory(IGuiHelper guiHelper, String translationKey) {
    this.background = guiHelper.createDrawable(new ResourceLocation(ExampleMod.MODID, "textures/gui/jei/firesmelting.png"), 0, 0, 89, 63);
    this.icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.NETHERRACK));
    this.localizedName = I18n.format(translationKey);
  }

  @Override
  public ResourceLocation getUid() {
    return new ResourceLocation(ExampleMod.MODID, "fire_smelting");
  }

  @Override
  public Class<? extends FireSmeltingRecipe> getRecipeClass() {
    return FireSmeltingRecipe.class;
  }

  @Override
  public String getTitle() {
    return localizedName;
  }

  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setIngredients(FireSmeltingRecipe recipe, IIngredients iIngredients) {
    iIngredients.setInputIngredients(Lists.newArrayList(recipe.ingredient));
    ItemStack output = recipe.output;
      iIngredients.setOutput(VanillaTypes.ITEM, output);
  }

  @Override
  public void setRecipe(IRecipeLayout iRecipeLayout, FireSmeltingRecipe FireSmeltingRecipe, IIngredients iIngredients) {
    IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 0, 23);
    guiItemStacks.init(1, false, 71, 23);
    guiItemStacks.set(iIngredients);
  }
}
