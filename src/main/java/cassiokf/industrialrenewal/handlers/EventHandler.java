package cassiokf.industrialrenewal.handlers;

import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.entity.LocomotiveBase;
import cassiokf.industrialrenewal.item.ItemCartLinkable;
import cassiokf.industrialrenewal.recipes.LatheRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MODID)
public class EventHandler {
    public static final String id = "indr_" + IRConfig.Generation.deepVeinID.get();
    public static final String deepVeinKeyItem = id + "_id";
    public static final String deepVeinKeyQuantity = id + "_q";

    @SubscribeEvent
    public static void onMinecartUpdate(EntityEvent.CanUpdate event) {
        if (event.getCanUpdate())
        {
            Entity e = event.getEntity();
            if (e instanceof MinecartEntity)
            {
                MinecartEntity cart = (MinecartEntity) e;
                CouplingHandler.onMinecartTick(cart);
                if (cart instanceof LocomotiveBase) ((LocomotiveBase) cart).onLocomotiveUpdate();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteractWithMineCarts(MinecartInteractEvent event)
    {
        if (event.getItem().getItem() instanceof ItemCartLinkable)
            event.setCanceled(true);
        else return;
        PlayerEntity thePlayer = event.getPlayer();
        if (thePlayer.getEntityWorld().isRemote || !event.getHand().equals(EnumHand.MAIN_HAND)) return;

        ItemCartLinkable.onPlayerUseLinkableItemOnCart(thePlayer, event.getMinecart());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> ev)
    {
        LatheRecipe.populateLatheRecipes();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyboardEvent(GuiScreenEvent.KeyboardKeyEvent event)
    {
        GuiScreen screen = event.getGui();
        if (screen instanceof GUIStorageChest)
        {
            ((GUIStorageChest) screen).onKeyboardEvent(event);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderGui(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        new GUIWire(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkDataEvent.Load event)
    {
        if (event.getWorld().isRemote() || event.getWorld().getDimension().isSurfaceWorld())
            return;
        CompoundNBT data = event.getData();
        ItemStack stack;
        if (data.contains(deepVeinKeyItem) && data.contains(deepVeinKeyQuantity))
        {
            int id = data.getInt(deepVeinKeyItem);
            int count = data.getInt(deepVeinKeyQuantity);
            if (count == 0) count = 1;
            stack = new ItemStack(Item.getItemById(id));
            stack.setCount(count);
        } else
        {
            stack = OreGeneration.generateNewVein(event.getWorld());
            event.getChunk().markDirt();
        }
        if (!OreGeneration.CHUNKS_VEIN.containsKey(event.getChunk().getPos()))
            OreGeneration.CHUNKS_VEIN.put(event.getChunk().getPos(), stack);
    }

    @SubscribeEvent
    public static void onChunkSave(ChunkDataEvent.Save event)
    {
        if (event.getWorld().isRemote() || event.getWorld().getDimension().isSurfaceWorld())
            return;
        ItemStack stack = OreGeneration.CHUNKS_VEIN.get(event.getChunk().getPos());
        if (stack == null)
        {
            stack = OreGeneration.generateNewVein(event.getWorld());
            OreGeneration.CHUNKS_VEIN.put(event.getChunk().getPos(), stack);
        }

        CompoundNBT data = event.getData();
        data. putInt(deepVeinKeyItem, Item.getIdFromItem(stack.getItem()));
        data. putInt(deepVeinKeyQuantity, stack.getCount() <= 0 ? 1 : stack.getCount());

        if (!event.getChunk().isLoaded())
        {
            OreGeneration.CHUNKS_VEIN.remove(event.getChunk().getPos());
        }
    }
}
