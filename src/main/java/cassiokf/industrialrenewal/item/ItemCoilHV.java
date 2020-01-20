package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import cassiokf.industrialrenewal.tileentity.TileEntityWireBase;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
    private BlockPos firstConnectionPos;
    private boolean isSecond = false;

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
                TileEntity te = worldIn.getTileEntity(pos);
                if (te == null)
                {
                    cleanConnection(player);
                    return EnumActionResult.PASS;
                }
                ItemStack itemstack = player.getHeldItem(hand);
                if (te instanceof IConnectorHV)
                {
                    IConnectorHV teT = (IConnectorHV) te;
                    if (teT instanceof TileEntityTransformerHV) teT = ((TileEntityTransformerHV) teT).getMaster();
                    if (!isSecond)
                    {
                        if (teT.canConnect(pos))
                        {
                            firstConnectionPos = teT.getConnectorPos();
                            isSecond = true;
                            Utils.sendChatMessage(player, "Connection Start");
                            return EnumActionResult.SUCCESS;
                        } else
                        {
                            Utils.sendChatMessage(player, "Connection already in use");
                        }
                    } else
                    {
                        int distance = (int) Utils.getDistancePointToPoint(firstConnectionPos, pos);
                        if (teT.getConnectorPos() != firstConnectionPos && teT.canConnect(pos) && distance > 0 && distance <= IRConfig.MainConfig.Main.maxHVWireLength)
                        {
                            isSecond = false;
                            connectFirst(worldIn, teT.getConnectorPos());
                            teT.connect(firstConnectionPos);
                            Utils.sendChatMessage(player, "Connected Distance: " + distance);
                            itemstack.shrink(1);
                            return EnumActionResult.SUCCESS;
                        } else
                        {
                            if (distance > 64)
                                Utils.sendChatMessage(player, "Far away from each other, Distence: " + distance);
                            cleanConnection(player);
                            return EnumActionResult.FAIL;
                        }
                    }
                } else if (te instanceof TileEntityWireBase)
                {
                    TileEntityWireBase teT = (TileEntityWireBase) te;
                    if (!isSecond)
                    {
                        if (teT.canConnect())
                        {
                            firstConnectionPos = teT.getPos();
                            isSecond = true;
                            Utils.sendChatMessage(player, "Connection Start");
                            return EnumActionResult.SUCCESS;
                        } else
                        {
                            Utils.sendChatMessage(player, "Connection already in use");
                        }
                    } else
                    {
                        int distance = (int) Utils.getDistancePointToPoint(firstConnectionPos, pos);
                        if (teT.getPos() != firstConnectionPos && teT.canConnect() && distance > 0 && distance <= 64)
                        {
                            isSecond = false;
                            connectFirst(worldIn, teT.getPos());
                            teT.setConnection(firstConnectionPos);
                            Utils.sendChatMessage(player, "Connected Distance: " + distance);
                            itemstack.shrink(1);
                            return EnumActionResult.SUCCESS;
                        } else
                        {
                            if (distance > 64)
                                Utils.sendChatMessage(player, "Far away from each other, Distence: " + distance);
                            cleanConnection(player);
                            return EnumActionResult.FAIL;
                        }
                    }
                } else if (isSecond)
                {
                    cleanConnection(player);
                }
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    private void cleanConnection(EntityPlayer player)
    {
        isSecond = false;
        Utils.sendChatMessage(player, "Can not connect");
    }

    private void connectFirst(World world, BlockPos endPos)
    {
        TileEntity te = world.getTileEntity(firstConnectionPos);
        if (te instanceof IConnectorHV)
        {
            ((IConnectorHV) te).connect(endPos);
        } else if (te instanceof TileEntityWireBase)
        {
            ((TileEntityWireBase) te).setConnection(endPos);
        }
    }
}
