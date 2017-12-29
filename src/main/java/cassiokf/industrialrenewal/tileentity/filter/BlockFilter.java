package cassiokf.industrialrenewal.tileentity.filter;

import cassiokf.industrialrenewal.blocks.BlockTileEntity;
import cassiokf.industrialrenewal.blocks.ModBlocks;
import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static net.minecraft.init.Blocks.AIR;

//import static net.minecraft.block.BlockDoor.getFacing;


public class BlockFilter extends BlockTileEntity<TileEntityFilter> {

    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.1875D, 0D, 0.1875D, 0.8125D, 0.750D, 0.8125D);

    public BlockFilter(String name) {
        super(Material.IRON, name);
        setHardness(4f);
        setResistance(5f);
    }

    //Fluid
    @Nullable
    public static <T> T getCapability(@Nullable final ICapabilityProvider provider, final Capability<T> capability, @Nullable final EnumFacing facing) {
        return provider != null && provider.hasCapability(capability, facing) ? provider.getCapability(capability, facing) : null;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        world.setBlockToAir(pos.down());
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (entity.inventory.getCurrentItem().getItem() == ModItems.screwDrive) {
            world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:drill")), SoundCategory.BLOCKS, 1.0F, 1.0F);
            return rotateBlock(world, pos, side);
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()));
        //Tem uma forma melhor?
        world.setBlockState(pos.down(), ModBlocks.dummyFilter.getDefaultState(), 3);
        final IFluidHandler fluidHandler = getFluidHandler(world, pos);
        if (fluidHandler != null) {
            FluidUtil.tryEmptyContainer(stack, fluidHandler, Integer.MAX_VALUE, null, true);
        }
    }

    public EnumFacing getFacing(final IBlockAccess world, final BlockPos pos) {
        return world.getBlockState(pos).getValue(FACING);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock() == AIR;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(FACING, getFacing(worldIn, pos));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BLOCK_AABB;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
    public boolean rotateBlock(final World world, final BlockPos pos, final EnumFacing axis) {
        final EnumFacing facing = getFacing(world, pos);
        world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, facing.rotateY()));

        return true;
    }

    @Override
    public Class<TileEntityFilter> getTileEntityClass() {
        return TileEntityFilter.class;
    }

    @Nullable
    @Override
    public TileEntityFilter createTileEntity(World world, IBlockState state) {
        return new TileEntityFilter();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean hasTileEntity(final IBlockState state) {
        return true;
    }

    @Nullable
    private IFluidHandler getFluidHandler(final IBlockAccess world, final BlockPos pos) {
        return getCapability(getTileEntity(world, pos), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    }
}