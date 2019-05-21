package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.Registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockSignBase extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ONWALL = PropertyBool.create("onwall");

    public static final ArrayList<Block> signs = new ArrayList<>();

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1D, 0.875D);

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0D, 0.125D, 0.125D, 0.0625D, 0.875D, 0.875D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1D, 0.125D, 0.125D, 0.9375D, 0.875D, 0.875D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.9375D, 0.875D, 0.875D, 1D);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.125D, 0.125D, 0.0625D, 0.875D, 0.875D, 0D);

    public BlockSignBase(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);

        signs.add(this);

        setSoundType(SoundType.METAL);
        setHardness(0.8f);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ONWALL, false));
    }

    public void changeSign(World world, BlockPos pos) {
        Block oldBlock = world.getBlockState(pos).getBlock();
        IBlockState oldState = oldBlock.getActualState(world.getBlockState(pos), world, pos);
        int oldMeta = oldBlock.getMetaFromState(oldState);
        EnumFacing oldFacing = EnumFacing.getHorizontal(oldMeta);
        boolean oldOnWall = oldState.getValue(ONWALL);
        int nextInt = signs.indexOf(oldBlock) + 1;
        if (nextInt > signs.size() - 1) {
            nextInt = 0;
        }
        Block nextBlock = signs.get(nextInt);

        world.setBlockState(pos, nextBlock.getDefaultState().withProperty(FACING, oldFacing).withProperty(ONWALL, oldOnWall));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(FACING, state.getValue(FACING)).withProperty(ONWALL, state.getValue(ONWALL));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.signHV)).getItem();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.signHV));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(ONWALL, facing != EnumFacing.UP);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ONWALL);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(ONWALL, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();

        if (state.getValue(ONWALL)) {
            i |= 4;
        }
        return i;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getActualState(source, pos).getValue(ONWALL)) {
            switch (state.getActualState(source, pos).getValue(FACING)) {
                default:
                case NORTH:
                    return NORTH_BLOCK_AABB;
                case SOUTH:
                    return SOUTH_BLOCK_AABB;
                case EAST:
                    return EAST_BLOCK_AABB;
                case WEST:
                    return WEST_BLOCK_AABB;
            }

        } else {
            return BASE_AABB;
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tile.industrialrenewal.sign_base.info"));
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

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
