package cassiokf.industrialrenewal.init;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.fluids.BlockSteam;
import cassiokf.industrialrenewal.fluids.FluidSteam;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static cassiokf.industrialrenewal.References.MODID;

public class FluidsRegistration
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
    public static final RegistryObject<FlowingFluid> STEAM = FLUIDS.register("steam", FluidSteam.Source::new);
    public static final RegistryObject<FlowingFluid> STEAM_FLOWING = FLUIDS.register("flowing_steam", FluidSteam.Flowing::new);
    public static final RegistryObject<BlockSteam> STEAM_BLOCK = BLOCKS.register("steam_block", BlockSteam::new);
    public static final RegistryObject<Item> STEAM_BUCKET = ITEMS.register("steam_bucket", () ->
            new BucketItem(STEAM, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(References.GROUP_INDR)));

    public static void init()
    {
        FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
