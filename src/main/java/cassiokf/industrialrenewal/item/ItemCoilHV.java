package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntityHVConnectorBase;
import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ItemCoilHV extends ItemBase
{
    private BlockPos firstConnectionPos;
    private boolean isSecond = false;

    public ItemCoilHV(Properties properties)
    {
        super(properties);
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        list.add(new StringTextComponent("Long distance energy transport"));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        PlayerEntity player = context.getPlayer();
        World worldIn = context.getWorld();
        if (worldIn.isRemote) player.swingArm(context.getHand());
        else
        {
            if (context.getHand().equals(Hand.MAIN_HAND))
            {
                BlockPos pos = context.getPos();
                TileEntity te = worldIn.getTileEntity(pos);
                if (te == null)
                {
                    cleanConnection(player);
                    return ActionResultType.PASS;
                }
                ItemStack itemstack = player.getHeldItem(context.getHand());
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
                            return ActionResultType.SUCCESS;
                        } else
                        {
                            Utils.sendChatMessage(player, "Connection already in use");
                        }
                    } else
                    {
                        int distance = Utils.getDistancePointToPoint(firstConnectionPos, pos);
                        if (teT.getConnectorPos() != firstConnectionPos && teT.canConnect(pos) && distance > 0 && distance <= IRConfig.Main.maxHVWireLength.get())
                        {
                            isSecond = false;
                            connectFirst(worldIn, teT.getConnectorPos());
                            teT.connect(firstConnectionPos);
                            Utils.sendChatMessage(player, "Connected Distance: " + distance);
                            itemstack.shrink(1);
                            return ActionResultType.SUCCESS;
                        } else
                        {
                            if (distance > IRConfig.Main.maxHVWireLength.get())
                                Utils.sendChatMessage(player, "Far away from each other, Distence: " + distance);
                            cleanConnection(player);
                            return ActionResultType.FAIL;
                        }
                    }
                } else if (te instanceof TileEntityHVConnectorBase)
                {
                    TileEntityHVConnectorBase teT = (TileEntityHVConnectorBase) te;
                    if (!isSecond)
                    {
                        if (teT.canConnect())
                        {
                            firstConnectionPos = teT.getPos();
                            isSecond = true;
                            Utils.sendChatMessage(player, "Connection Start");
                            return ActionResultType.SUCCESS;
                        } else
                        {
                            Utils.sendChatMessage(player, "Connection already in use");
                        }
                    } else
                    {
                        int distance = Utils.getDistancePointToPoint(firstConnectionPos, pos);
                        if (teT.getPos() != firstConnectionPos && teT.canConnect() && distance > 0 && distance <= IRConfig.Main.maxHVWireLength.get())
                        {
                            isSecond = false;
                            connectFirst(worldIn, teT.getPos());
                            teT.setConnection(firstConnectionPos);
                            Utils.sendChatMessage(player, "Connected Distance: " + distance);
                            itemstack.shrink(1);
                            return ActionResultType.SUCCESS;
                        } else
                        {
                            if (distance > IRConfig.Main.maxHVWireLength.get())
                                Utils.sendChatMessage(player, "Far away from each other, Distence: " + distance);
                            cleanConnection(player);
                            return ActionResultType.FAIL;
                        }
                    }
                }
                else if (isSecond)
                {
                    cleanConnection(player);
                }
            }
        }
        return super.onItemUse(context);
    }

    public String getDistanceText(PlayerEntity player)
    {
        if (!isSecond) return "";
        int distance = Utils.getDistancePointToPoint(firstConnectionPos, player.getPosition());
        String text = "Current Wire Distance: " + distance;
        return (distance > IRConfig.Main.maxHVWireLength.get() ? TextFormatting.RED : TextFormatting.GREEN) + text;
    }

    private void cleanConnection(PlayerEntity player)
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
        } else if (te instanceof TileEntityHVConnectorBase)
        {
            ((TileEntityHVConnectorBase) te).setConnection(endPos);
        }
    }
}
