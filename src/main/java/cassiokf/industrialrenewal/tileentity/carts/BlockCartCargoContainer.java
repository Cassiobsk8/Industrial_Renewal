package cassiokf.industrialrenewal.tileentity.carts;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCartCargoContainer extends BlockTileEntity<TileEntityCartCargoContainer> {


    public BlockCartCargoContainer(String name) {
        super(Material.IRON, name);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
        setCreativeTab(IndustrialRenewal.creativeTab);

    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public Class<TileEntityCartCargoContainer> getTileEntityClass() {
        return TileEntityCartCargoContainer.class;
    }

    @Nullable
    @Override
    public TileEntityCartCargoContainer createTileEntity(World world, IBlockState state) {
        return new TileEntityCartCargoContainer();
    }
}
