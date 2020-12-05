package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.world.generation.OreGeneration;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemRegenerationWand extends ItemBase
{
    public ItemRegenerationWand(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.format("info.industrialrenewal.prospectingpan.info") + " (Creative only)");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, EnumHand handIn)
    {
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                ChunkPos cPos = worldIn.getChunk(playerIn.getPosition()).getPos();
                int n = 0;
                for (int x = -16; x <= 16; x++) {
                    for (int z = -16; z <= 16; z++) {
                        ItemStack stack = OreGeneration.CHUNKS_VEIN.get(new ChunkPos(cPos.x + x, cPos.z + z));
                        if (stack != null && !stack.isEmpty()) n++;
                    }
                }
                Utils.sendChatMessage(playerIn, "There is " + n + " Deep Vein in a 16x16 chunks near you");

                int n2 = 0;
                for (ItemStack stack : OreGeneration.CHUNKS_VEIN.values()) {
                    if (stack != null && !stack.isEmpty()) n2++;
                }
                Utils.sendChatMessage(playerIn, "Total DeepVein Loaded: " + n2);
            } else {
                ItemStack stack = OreGeneration.getChunkVein(worldIn, playerIn.getPosition());
                String str = stack.getItem().getItemStackDisplayName(stack) + " " + stack.getCount();
                if (stack.isEmpty()) str = ItemProspectingPan.notFound;
                Utils.sendChatMessage(playerIn, str);
                return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
