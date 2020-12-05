package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockBase extends Block
{
    public BlockBase(Block.Properties properties) {
        super(properties);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
    {
        if (this.hasTileEntity(state))
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TEBase) ((TEBase) te).onBlockBreak();
        }
        super.breakBlock(worldIn, pos, state);
    }
}