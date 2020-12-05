package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.blocks.railroad.BlockRailFacing;
import cassiokf.industrialrenewal.entity.EntityTenderBase;
import cassiokf.industrialrenewal.entity.LocomotiveBase;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityLoaderRail extends TEBase
{

    public TileEntityLoaderRail(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public void onMinecartPass(MinecartEntity ecart)
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

    private void informLoader(BlockState state, MinecartEntity ecart)
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
                ecart.setMotion(0,0,0);
            } else
            {
                BlockRailFacing.propelMinecart(state, ecart);
            }
        } else
        {
            BlockRailFacing.propelMinecart(state, ecart);
        }
    }

    private int GetCartInvType(World world, BlockPos pos, BlockState state, MinecartEntity cart)
    {
        if (cart instanceof EntityTenderBase || cart instanceof LocomotiveBase) return 0;
        Direction facing = state.get(BlockRailFacing.FACING);
        TileEntity leftTE = world.getTileEntity(pos.offset(facing.rotateYCCW()));
        TileEntity rightTE = world.getTileEntity(pos.offset(facing.rotateY()));

        int type = 0;
        if (cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null) != null
                && (leftTE instanceof TileEntityCargoLoader || rightTE instanceof TileEntityCargoLoader))
        {
            type = 1;
        } else if (cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).orElse(null) != null
                && (leftTE instanceof TileEntityFluidLoader || rightTE instanceof TileEntityFluidLoader))
        {
            type = 2;
        }
        return type;
    }
}
