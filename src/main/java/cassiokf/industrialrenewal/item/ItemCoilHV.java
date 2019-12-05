package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCoilHV extends ItemBase
{
    private IConnectorHV firstConnection;
    private EnumFacing firstConnectionSide;
    private boolean isSecond = false;
    private BlockPos firstPos;

    public ItemCoilHV(String name, CreativeTabs tab)
    {
        super(name, tab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag)
    {
        list.add("Long distance energy transport");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            if (hand.equals(EnumHand.MAIN_HAND))
            {
                IConnectorHV te = (IConnectorHV) worldIn.getTileEntity(pos);
                if (te == null) return EnumActionResult.FAIL;
                if (te instanceof TileEntityTransformerHV) te = ((TileEntityTransformerHV) te).getMaster();
                if (!isSecond)
                {
                    if (te.canConnectToSide(pos, facing))
                    {
                        firstConnection = te;
                        firstConnectionSide = facing;
                        isSecond = true;
                        firstPos = pos;
                        Utils.sendChatMessage("Connection Start");
                        return EnumActionResult.SUCCESS;
                    }
                } else
                {
                    if (firstConnection != null && te != firstConnection && te.canConnectToSide(pos, facing))
                    {
                        isSecond = false;
                        te.connectSide(facing, firstConnection, firstConnectionSide);
                        Utils.sendChatMessage("Connected Distance: " + Utils.getDistancePointToPoint(firstPos, pos));
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
