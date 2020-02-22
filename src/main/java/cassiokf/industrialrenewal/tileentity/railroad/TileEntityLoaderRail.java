package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.railroad.BlockRailFacing;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import static cassiokf.industrialrenewal.init.TileRegistration.LOADERRAIL_TILE;

public class TileEntityLoaderRail extends TileEntity
{

    public TileEntityLoaderRail()
    {
        super(LOADERRAIL_TILE.get());
    }

    public void onMinecartPass(AbstractMinecartEntity ecart)
    {
        BlockState state = world.getBlockState(pos);
        if (GetCartInvType(world, pos, state, ecart) == 0)
        {
            BlockRailFacing.propelMinecart(state, ecart);
        } else
        {
            informLoader(state, ecart);
        }
    }

    private void informLoader(BlockState state, AbstractMinecartEntity ecart)
    {
        Direction facing = state.get(BlockRailFacing.FACING);
        TileEntity leftTE = world.getTileEntity(pos.offset(facing.rotateYCCW()));
        TileEntity rightTE = world.getTileEntity(pos.offset(facing.rotateY()));

        if (leftTE instanceof TileEntityBaseLoader || rightTE instanceof TileEntityBaseLoader)
        {
            TileEntityBaseLoader teLoader = leftTE instanceof TileEntityBaseLoader
                    ? (TileEntityBaseLoader) leftTE
                    : (TileEntityBaseLoader) rightTE;

            if (teLoader.onMinecartPass(ecart, this))
            {
                ecart.setPosition(pos.getX() + 0.5D, ecart.getPosY(), pos.getZ() + 0.5D);
                ecart.setVelocity(0D, 0D, 0D);
            } else
            {
                BlockRailFacing.propelMinecart(state, ecart);
            }
        } else
        {
            BlockRailFacing.propelMinecart(state, ecart);
        }
    }

    private int GetCartInvType(World world, BlockPos pos, BlockState state, AbstractMinecartEntity cart)
    {
        Direction facing = state.get(BlockRailFacing.FACING);
        TileEntity leftTE = world.getTileEntity(pos.offset(facing.rotateYCCW()));
        TileEntity rightTE = world.getTileEntity(pos.offset(facing.rotateY()));
        LazyOptional<IItemHandler> itemCapability = cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
        LazyOptional<IFluidHandler> fluidCapability = cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP);
        int type = 0;
        if (itemCapability.isPresent() && (leftTE instanceof TileEntityCargoLoader || rightTE instanceof TileEntityCargoLoader))
        {
            type = 1;
        } else if (fluidCapability.isPresent() && (leftTE instanceof TileEntityFluidLoader || rightTE instanceof TileEntityFluidLoader))
        {
            type = 2;
        }
        return type;
    }
}
