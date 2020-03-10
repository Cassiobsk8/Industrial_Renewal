package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.BlockBasicContainer;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.tileentity.railroad.TileEntityCargoLoader;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlockCargoLoader extends BlockBasicContainer<TileEntityCargoLoader>
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool MASTER = PropertyBool.create("master");

    public BlockCargoLoader(String name, CreativeTabs tab) {
        super(name, tab, Material.IRON);
    }

    public static BlockPos getMasterPos(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        for (int y = -2; y < 3; y++)
        {
            BlockPos newPos = pos.up(y);
            BlockPos newPosFront = pos.offset(facing).up(y);
            BlockPos newPosBack = pos.offset(facing.getOpposite()).up(y);
            if (world.getBlockState(newPos).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPos).getValue(MASTER)) return newPos;
            }
            if (world.getBlockState(newPosFront).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPosFront).getValue(MASTER)) return newPosFront;
            }
            if (world.getBlockState(newPosBack).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPosBack).getValue(MASTER)) return newPosBack;
            }
        }
        return null;
    }

    private void OpenGUI(World world, BlockPos pos, EntityPlayer player)
    {
        player.openGui(IndustrialRenewal.instance, GUIHandler.CARGOLOADER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && hand.equals(EnumHand.MAIN_HAND))
        {
            if (state.getValue(MASTER))
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityCargoLoader)
                {
                    OpenGUI(worldIn, pos, playerIn);
                }
            } else
            {
                BlockPos masterPos = getMasterPos(worldIn, pos, state.getValue(FACING));
                if (masterPos != null)
                {
                    TileEntity tileentity = worldIn.getTileEntity(masterPos);
                    if (tileentity instanceof TileEntityCargoLoader)
                    {
                        OpenGUI(worldIn, masterPos, playerIn);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing facing = state.getValue(FACING);
        if (state.getValue(MASTER))
        {
            for (BlockPos pos1 : getBlocks(pos, facing))
            {
                worldIn.setBlockToAir(pos1);
            }
        } else
        {
            BlockPos masterPos = getMasterPos(worldIn, pos, facing);
            if (masterPos != null)
            {
                worldIn.setBlockToAir(masterPos);
                for (BlockPos pos1 : getBlocks(masterPos, facing))
                {
                    if (pos1 != pos) worldIn.setBlockToAir(pos1);
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    private Set<BlockPos> getBlocks(BlockPos posMaster, EnumFacing facing)
    {
        Set<BlockPos> positions = new HashSet<>();
        positions.add(posMaster.down());
        positions.add(posMaster.down().offset(facing));
        positions.add(posMaster.up());
        positions.add(posMaster.up().offset(facing));
        return positions;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, MASTER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(MASTER, false);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.up(), state.withProperty(MASTER, true));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(MASTER))
        {
            EnumFacing facing = state.getValue(FACING);
            worldIn.setBlockState(pos.up(), state.withProperty(MASTER, false));
            worldIn.setBlockState(pos.up().offset(facing), state.withProperty(MASTER, false));
            worldIn.setBlockState(pos.down(), state.withProperty(MASTER, false));
            worldIn.setBlockState(pos.down().offset(facing), state.withProperty(MASTER, false));
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        EntityPlayer player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        if (player == null) return false;
        if (!isReplaceable(worldIn, pos)) return false;
        if (!isReplaceable(worldIn, pos.up())) return false;
        if (!isReplaceable(worldIn, pos.up(2))) return false;
        if (!isReplaceable(worldIn, pos.up(2).offset(player.getHorizontalFacing()))) return false;
        return isReplaceable(worldIn, pos.offset(player.getHorizontalFacing()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta)
    {
        int directionIndex = meta;
        if (meta > 3) directionIndex -= 4;
        boolean index = true;
        if (meta > 3) index = false;
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(directionIndex)).withProperty(MASTER, index);
    }

    @Override
    public int getMetaFromState(final IBlockState state)
    {
        int i = state.getValue(FACING).getHorizontalIndex();
        if (!state.getValue(MASTER)) i += 4;
        return i;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        if (face == EnumFacing.UP) return BlockFaceShape.SOLID;
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return false;
    }

    public Class<TileEntityCargoLoader> getTileEntityClass() {
        return TileEntityCargoLoader.class;
    }

    @Nullable
    @Override
    public TileEntityCargoLoader createTileEntity(World world, IBlockState state) {
        return new TileEntityCargoLoader();
    }
}
