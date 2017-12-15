package cassiokf.industrialrenewal.tileentity.gates.and;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.valve.TileEntityValvePipeLarge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class BlockGateAnd extends BlockTileEntity<TileEntityGateAnd> {

    protected static final AxisAlignedBB BLOCK_GATE_AND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool ACTIVE10 = PropertyBool.create("active10");

    public BlockGateAnd(String name) {
        super(Material.IRON, name);
        setHardness(0.8f);
        //setSoundType(SoundType.METAL);
        setCreativeTab(IndustrialRenewal.creativeTab);
        this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false).withProperty(ACTIVE10, false));
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
    //test
    private int getPowerLevelIn1(World world, BlockPos pos) {
        int maxPowerFound = 0;
        EnumFacing vFace = world.getBlockState(pos).getValue(FACING);
        //for (EnumFacing whichFace : EnumFacing.HORIZONTALS) {
        EnumFacing rFace = pickFace(vFace);
        BlockPos neighborPos = pos.offset(rFace);
        int powerLevel1 = world.getRedstonePower(neighborPos, rFace);
        System.out.println("neighborChange: " + powerLevel1 + " " + rFace);
            maxPowerFound = Math.max(powerLevel1, maxPowerFound);
        //}
        return maxPowerFound;
    }
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos neighborPos)
    {
        // calculate the power level from neighbours and store in our TileEntity for later use in isProvidingWeakPower()
        int powerLevel1 = getPowerLevelIn1(worldIn, pos);
        //TileEntity tileentity = worldIn.getTileEntity(pos);
        if (powerLevel1 > 0) {
            System.out.println("neighborChange: " + powerLevel1);
        }
        notifyNeighbors(worldIn,pos,state);

    }
    public EnumFacing pickFace(EnumFacing face){
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
    //test
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote){
            final TileEntityGateAnd tileEntity = getTileEntity(world, pos);
            boolean Vactive = world.getBlockState(pos).getValue(ACTIVE);
            tileEntity.exchangeOut(world, pos, state, !Vactive);
            notifyNeighbors(world,pos,state);
        }
        return true;
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
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        world.scheduleUpdate(new BlockPos(i, j, k), this, this.tickRate(world));
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
        return new BlockStateContainer(this, FACING, ACTIVE, ACTIVE10);
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront((meta > 8) ? meta - 8 : meta);
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
    public Class<TileEntityGateAnd> getTileEntityClass() {
        return TileEntityGateAnd.class;
	}

    @Nullable
    @Override
    public TileEntityGateAnd createTileEntity(World world, IBlockState state) {
        return new TileEntityGateAnd();
        }
}