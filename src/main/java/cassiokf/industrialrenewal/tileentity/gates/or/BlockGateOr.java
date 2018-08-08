package cassiokf.industrialrenewal.tileentity.gates.or;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


public class BlockGateOr extends BlockTileEntity<TileEntityGateOr> {

    protected static final AxisAlignedBB BLOCK_GATE_AND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool ACTIVE10 = PropertyBool.create("active10");
    public static final PropertyBool ACTIVE01 = PropertyBool.create("active01");

    public BlockGateOr(String name, CreativeTabs tab) {
        super(Material.CIRCUITS, name, tab);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
    }
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return side.getAxis() != EnumFacing.Axis.Y;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        boolean Vactive = blockState.getValue(ACTIVE);
        if (!Vactive)
        {
            return 0;
        }
        else {
            return blockState.getValue(FACING).getOpposite() == side ? this.getActiveSignal(blockAccess, pos, blockState) : 0;
        }
    }
    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        return 15;
    }
    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getWeakPower(blockAccess, pos, side);
    }
    protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (block == Blocks.REDSTONE_BLOCK) {
            return 15;
        }
        else {
            return block == Blocks.REDSTONE_WIRE ? ((Integer)iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : worldIn.getStrongPower(pos, side);
        }
    }
    public int tickRate()
    {
        return 200;
    }
    public void updateTick() {

    }
    private int getPowerLevelIn1(World world, BlockPos pos) {
        EnumFacing v1Face = world.getBlockState(pos).getValue(FACING);
        EnumFacing r1Face = pickFace(v1Face);
        BlockPos neighborPos = pos.offset(r1Face);
        int powerLevel1 = getPowerOnSide(world, neighborPos, r1Face);
        return powerLevel1;
    }
    private int getPowerLevelIn2(World world, BlockPos pos) {
        EnumFacing v2Face = world.getBlockState(pos).getValue(FACING);
        EnumFacing r2Face = pickFace(v2Face).getOpposite();
        BlockPos neighborPos = pos.offset(r2Face);
        int powerLevel2 = getPowerOnSide(world, neighborPos, r2Face);
        return powerLevel2;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos) {
        change(worldIn, pos, state);
    }
    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false).withProperty(ACTIVE10, false).withProperty(ACTIVE01, false));
        change(world, pos, state);
    }
    public void change(World worldIn, BlockPos pos, IBlockState state){
        int powerLevel1 = getPowerLevelIn1(worldIn, pos);
        int powerLevel2 = getPowerLevelIn2(worldIn, pos);
        final TileEntityGateOr tileEntity = getTileEntity(worldIn, pos);
        if (powerLevel1 > 0 && powerLevel2 <= 0) {
            tileEntity.exchange10(worldIn, pos, state);
        }
        if (powerLevel1 <= 0 && powerLevel2 > 0) {
            tileEntity.exchange01(worldIn, pos, state);
        }
        if (powerLevel1 > 0 && powerLevel2 > 0) {
            tileEntity.exchangeOut(worldIn, pos, state, true);
        }
        if (powerLevel1 <= 0 && powerLevel2 <= 0) {
            tileEntity.exchangeOut(worldIn, pos, state, false);
        }
    }
    private EnumFacing pickFace(EnumFacing face){
        if (face == EnumFacing.NORTH) {
            return EnumFacing.WEST;
        }
        if (face == EnumFacing.SOUTH) {
            return EnumFacing.EAST;
        }
        if (face == EnumFacing.EAST) {
            return EnumFacing.NORTH;
        }
        if (face == EnumFacing.WEST) {
            return EnumFacing.SOUTH;
        }
        return EnumFacing.NORTH;
    }
    protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        if(net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), java.util.EnumSet.of(enumfacing.getOpposite()), false).isCanceled())
            return;
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        final TileEntityGateOr tileEntity = getTileEntity(world, pos);
        if (entity.inventory.getCurrentItem().getItem() == ModItems.screwDrive) {
            tileEntity.dorotateBlock(world, pos, state);
            IBlockState nstate = world.getBlockState(pos);
            change(world, pos, nstate);
        }
        return true;
    }
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        world.scheduleUpdate(new BlockPos(i, j, k), this, this.tickRate(world));
        notifyNeighbors(world,pos,state);
    }
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BLOCK_GATE_AND_AABB;
    }
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE, ACTIVE10, ACTIVE01);
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);

        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facingbits = state.getValue(FACING).getIndex();
        return facingbits;
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
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

    @Override
    public Class<TileEntityGateOr> getTileEntityClass() {
        return TileEntityGateOr.class;
	}

    @Nullable
    @Override
    public TileEntityGateOr createTileEntity(World world, IBlockState state) {
        return new TileEntityGateOr();
    }
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}