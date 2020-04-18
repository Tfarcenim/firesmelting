package tfar.firesmelting;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.firesmelting.recipe.FireSmeltingRecipe;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
    // Directly reference a log4j logger.

    public static final String MODID = "firesmelting";

    private static final Logger LOGGER = LogManager.getLogger();

    public ExampleMod() {

        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class,this::serializers);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void serializers(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        e.getRegistry().register(new FireSmeltingRecipe.Serializer(FireSmeltingRecipe::new).setRegistryName("fire_smelting"));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }
}
