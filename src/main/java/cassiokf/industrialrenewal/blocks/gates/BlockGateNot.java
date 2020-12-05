package cassiokf.industrialrenewal.blocks.gates;

import cassiokf.industrialrenewal.blocks.BlockBase;
import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGateNot extends BlockBase {

    protected static final AxisAlignedBB BLOCK_GATE_AND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockGateNot(String name, CreativeTabs tab) {
        super(Material.CIRCUITS, name, tab);
        setHardness(0.8f);
        setSoundType(SoundType.METAL);

    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        boolean Vactive = blockState.getValue(ACTIVE);
        if (!Vactive) {
            return 0;
        }
        else {
            return blockState.getValue(FACING).getOpposite() == side ? this.getActiveSignal(blockAccess, pos, blockState) : 0;
        }
    }

    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos,  BlockState state) {
        return 15;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
         BlockState  BlockState = worldIn.getBlockState(pos);
        Block block =  BlockState.getBlock();
        if (block == Blocks.REDSTONE_BLOCK) {
            return 15;
        }
        else {
            return block == Blocks.REDSTONE_WIRE ? (Integer)  BlockState.getValue(BlockRedstoneWire.POWER) : worldIn.getStrongPower(pos, side);
        }
    }

    private int getPowerLevelIn1(World world, BlockPos pos) {
        EnumFacing v1Face = world.getBlockState(pos).getValue(FACING).getOpposite();
        BlockPos neighborPos = pos.offset(v1Face);
        return getPowerOnSide(world, neighborPos, v1Face);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos) {
        change(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final  BlockState state, final EntityLivingBase placer, final ItemStack stack) {
        this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false));
        change(world, pos, state);
    }

    private void change(World worldIn, BlockPos pos,  BlockState state) {
        int powerLevel1 = getPowerLevelIn1(worldIn, pos);
        if (powerLevel1 > 0) {
            worldIn.setBlockState(pos, state.withProperty(ACTIVE, false));
        }
        if (powerLevel1 <= 0) {
            worldIn.setBlockState(pos, state.withProperty(ACTIVE, true));
        }
    }

    private boolean out(World worldIn, BlockPos pos) {
        int powerLevel1 = getPowerLevelIn1(worldIn, pos);
        return powerLevel1 <= 0;
    }

    protected void notifyNeighbors(World worldIn, BlockPos pos,  BlockState state) {
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        if(net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), java.util.EnumSet.of(enumfacing.getOpposite()), false).isCanceled())
            return;
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos,  BlockState state, PlayerEntity entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (entity.inventory.getCurrentItem().getItem() == ModItems.screwDrive) {
            rotateBlock(world, pos, state);
        }
        return true;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos,  BlockState state) {
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        world.scheduleUpdate(new BlockPos(i, j, k), this, this.tickRate(world));
        notifyNeighbors(world,pos,state);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    private void rotateBlock(World world, BlockPos pos,  BlockState state) {
        EnumFacing newFace = state.getValue(FACING).rotateY();
        world.setBlockState(pos, state.withProperty(FACING, newFace));
         BlockState newState = world.getBlockState(pos);
        change(world, pos, newState);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BLOCK_GATE_AND_AABB;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public  BlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7)).withProperty(ACTIVE, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getIndex();
        if (state.getValue(ACTIVE)) {
            i |= 8;
        }
        return i;
    }

    @SuppressWarnings("deprecation")
    @Override
    public  BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
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

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn,  BlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}