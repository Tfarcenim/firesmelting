package tfar.firesmelting.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.firesmelting.Smelted;
import tfar.firesmelting.recipe.FireSmeltingRecipe;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity implements Smelted {

	@Shadow public abstract ItemStack getItem();

	public void setCooked() {
		this.cooked = true;
	}

	public boolean cooked = false;

	@Inject(method = "attackEntityFrom",at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/item/ItemEntity;markVelocityChanged()V"),cancellable = true)
	private void smelt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
		if (cooked && source.isFireDamage()) {
			cir.setReturnValue(false);
			return;
		}
		if (source.isFireDamage()) {
			ItemStack stack = getItem();
			int size = stack.getCount();//todo, add custom recipe type

			world.getRecipeManager().getRecipe(FireSmeltingRecipe.FIRE_SMELTING, new Inventory(stack), world).ifPresent(smeltingRecipe -> {
				ItemStack result = smeltingRecipe.getCraftingResult(null);
				ItemEntity cooked = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), result);
				((Smelted) cooked).setCooked();
				if (size > 1) {
					ItemEntity toCook = new ItemEntity(world, getPosX(), getPosY(), getPosZ(),
									new ItemStack(stack.getItem(), stack.getCount() - 1));
					world.addEntity(toCook);
				}
				world.addEntity(cooked);
				this.remove();
				cir.setReturnValue(true);
			});

			world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), world).ifPresent(smeltingRecipe -> {
				ItemStack result = smeltingRecipe.getCraftingResult(null);
				ItemEntity cooked = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), result);
				((Smelted) cooked).setCooked();
				if (size > 1) {
					ItemEntity toCook = new ItemEntity(world, getPosX(), getPosY(), getPosZ(),
									new ItemStack(stack.getItem(), stack.getCount() - 1));
					world.addEntity(toCook);
				}
				world.addEntity(cooked);
				this.remove();
				cir.setReturnValue(true);
			});
		}
	}

	public ItemEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
}
