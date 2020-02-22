package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.tileentity.TileEntityTransformerHV;
import cassiokf.industrialrenewal.tileentity.TileEntityWireIsolator;
import cassiokf.industrialrenewal.util.Utils;
import cassiokf.industrialrenewal.util.interfaces.IConnectorHV;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ItemCoilHV extends ItemBase
{
    private BlockPos firstConnectionPos;
    private boolean isSecond = false;

    public ItemCoilHV(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        list.add(new StringTextComponent("Long distance energy transport"));
    }

        @Override
        public ActionResultType onItemUse(ItemUseContext context)
        {
            World worldIn = context.getWorld();
            if (!worldIn.isRemote)
            {
                if (context.getHand().equals(Hand.MAIN_HAND))
                {
                    BlockPos pos = context.getPos();
                    PlayerEntity player = context.getPlayer();

                    TileEntity te = worldIn.getTileEntity(pos);
                    if (te == null)
                    {
                        cleanConnection(player);
                        return ActionResultType.PASS;
                    }
                    ItemStack itemstack = context.getItem();
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
                            int distance = (int) Utils.getDistancePointToPoint(firstConnectionPos, pos);
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
                                if (distance > 64)
                                    Utils.sendChatMessage(player, "Far away from each other, Distence: " + distance);
                                cleanConnection(player);
                                return ActionResultType.FAIL;
                            }
                        }
                    } else if (te instanceof TileEntityWireIsolator)
                    {
                        TileEntityWireIsolator teT = (TileEntityWireIsolator) te;
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
                            int distance = (int) Utils.getDistancePointToPoint(firstConnectionPos, pos);
                            if (teT.getPos() != firstConnectionPos && teT.canConnect() && distance > 0 && distance <= 64)
                            {
                                isSecond = false;
                                connectFirst(worldIn, teT.getPos());
                                teT.setConnection(firstConnectionPos);
                                Utils.sendChatMessage(player, "Connected Distance: " + distance);
                                itemstack.shrink(1);
                                return ActionResultType.SUCCESS;
                            } else
                            {
                                if (distance > 64)
                                    Utils.sendChatMessage(player, "Far away from each other, Distence: " + distance);
                                cleanConnection(player);
                                return ActionResultType.FAIL;
                            }
                        }
                    } else if (isSecond)
                    {
                        cleanConnection(player);
                    }
                }
            }
            return super.onItemUse(context);
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
        } else if (te instanceof TileEntityWireIsolator)
        {
            ((TileEntityWireIsolator) te).setConnection(endPos);
        }
    }
}
