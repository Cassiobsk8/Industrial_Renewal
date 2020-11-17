package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.LocomotiveBase;
import cassiokf.industrialrenewal.item.ItemCartLinkable;
import cassiokf.industrialrenewal.recipes.LatheRecipe;
import cassiokf.industrialrenewal.world.generation.OreGeneration;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOW;

@Mod.EventBusSubscriber(modid = References.MODID)
public class EventHandler
{
    public static final String deepVeinKeyItem = "indr_dv_id";
    public static final String deepVeinKeyQuantity = "indr_dv_q";

    @SubscribeEvent
    public static void onMinecartUpdate(MinecartUpdateEvent event)
    {
        EntityMinecart cart = event.getMinecart();
        CouplingHandler.onMinecartTick(cart);
        if (cart instanceof LocomotiveBase) ((LocomotiveBase) cart).onLocomotiveUpdate();
    }

    @SubscribeEvent
    public static void onPlayerInteractWithMineCarts(MinecartInteractEvent event)
    {
        if (event.getItem().getItem() instanceof ItemCartLinkable)
            event.setCanceled(true);
        else return;
        EntityPlayer thePlayer = event.getPlayer();
        if (thePlayer.getEntityWorld().isRemote || !event.getHand().equals(EnumHand.MAIN_HAND)) return;

        ItemCartLinkable.onPlayerUseLinkableItemOnCart(thePlayer, event.getMinecart());
    }

    @SubscribeEvent(priority = LOW)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> ev)
    {
        LatheRecipe.populateLatheRecipes();
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkDataEvent.Load event)
    {
        if (event.getWorld().isRemote || event.getWorld().provider.getDimension() != 0)
            return;
        NBTTagCompound data = event.getData();
        ItemStack stack;
        if (data.hasKey(deepVeinKeyItem) && data.hasKey(deepVeinKeyQuantity))
        {
            int id = data.getInteger(deepVeinKeyItem);
            int count = data.getInteger(deepVeinKeyQuantity);
            if (count == 0) count = 1;
            stack = new ItemStack(Item.getItemById(id));
            stack.setCount(count);
        } else
        {
            stack = OreGeneration.generateNewVein(event.getWorld());
        }
        if (!OreGeneration.CHUNKS_VEIN.containsKey(event.getChunk().getPos()))
            OreGeneration.CHUNKS_VEIN.put(event.getChunk().getPos(), stack);
    }

    @SubscribeEvent
    public static void onChunkSave(ChunkDataEvent.Save event)
    {
        if (event.getWorld().isRemote || event.getWorld().provider.getDimension() != 0)
            return;
        ItemStack stack = OreGeneration.CHUNKS_VEIN.get(event.getChunk().getPos());
        if (stack == null)
        {
            stack = OreGeneration.generateNewVein(event.getWorld());
            OreGeneration.CHUNKS_VEIN.put(event.getChunk().getPos(), stack);
        }

        NBTTagCompound data = event.getData();
        data.setInteger(deepVeinKeyItem, Item.getIdFromItem(stack.getItem()));
        data.setInteger(deepVeinKeyQuantity, stack.getCount() <= 0 ? 1 : stack.getCount());

        if (!event.getChunk().isLoaded())
        {
            OreGeneration.CHUNKS_VEIN.remove(event.getChunk().getPos());
        }
    }
}
