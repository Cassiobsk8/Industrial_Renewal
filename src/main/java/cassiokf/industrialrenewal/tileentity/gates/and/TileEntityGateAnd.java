package cassiokf.industrialrenewal.tileentity.gates.and;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static cassiokf.industrialrenewal.tileentity.gates.and.BlockGateAnd.ACTIVE;

public class TileEntityGateAnd extends TileEntity {

    public void exchangeOut(World world, BlockPos pos, IBlockState state , boolean active) {
        world.setBlockState(pos, state.withProperty(ACTIVE, active));
    }

}
