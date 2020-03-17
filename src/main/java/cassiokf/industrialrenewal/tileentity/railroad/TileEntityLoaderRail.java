package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.railroad.BlockRailFacing;
import cassiokf.industrialrenewal.tileentity.abstractclass.TEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityLoaderRail extends TEBase
{

    public void onMinecartPass(EntityMinecart ecart)
    {
        IBlockState state = world.getBlockState(pos);
        if (GetCartInvType(world, pos, state, ecart) == 0)
        {
            BlockRailFacing.propelMinecart(state, ecart);
        } else
        {
            informLoader(state, ecart);
        }
    }

    private void informLoader(IBlockState state, EntityMinecart ecart)
    {
        EnumFacing facing = state.getValue(BlockRailFacing.FACING);
        TileEntity leftTE = world.getTileEntity(pos.offset(facing.rotateYCCW()));
        TileEntity rightTE = world.getTileEntity(pos.offset(facing.rotateY()));

        if (leftTE instanceof TileEntityBaseLoader || rightTE instanceof TileEntityBaseLoader)
        {
            TileEntityBaseLoader teLoader = leftTE instanceof TileEntityBaseLoader
                    ? (TileEntityBaseLoader) leftTE
                    : (TileEntityBaseLoader) rightTE;

            if (teLoader.onMinecartPass(ecart, this))
            {
                ecart.posX = pos.getX() + 0.5D;
                ecart.posZ = pos.getZ() + 0.5D;
                ecart.motionX = 0.0D;
                ecart.motionZ = 0.0D;
            } else
            {
                BlockRailFacing.propelMinecart(state, ecart);
            }
        } else
        {
            BlockRailFacing.propelMinecart(state, ecart);
        }
    }

    private int GetCartInvType(World world, BlockPos pos, IBlockState state, EntityMinecart cart)
    {
        EnumFacing facing = state.getValue(BlockRailFacing.FACING);
        TileEntity leftTE = world.getTileEntity(pos.offset(facing.rotateYCCW()));
        TileEntity rightTE = world.getTileEntity(pos.offset(facing.rotateY()));
        boolean itemCapability = cart.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        boolean fluidCapability = cart.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
        int type = 0;
        if (itemCapability && (leftTE instanceof TileEntityCargoLoader || rightTE instanceof TileEntityCargoLoader))
        {
            type = 1;
        } else if (fluidCapability && (leftTE instanceof TileEntityFluidLoader || rightTE instanceof TileEntityFluidLoader))
        {
            type = 2;
        }
        return type;
    }
}
