package cassiokf.industrialrenewal.blocks.redstone;

import cassiokf.industrialrenewal.blocks.abstracts.BlockTileEntity;
import cassiokf.industrialrenewal.tileentity.redstone.TileEntityTrafficLight;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTrafficLight extends BlockTileEntity<TileEntityTrafficLight>
{

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty ONWALL = BooleanProperty.create("onwall");
    public static final IntegerProperty SIGNAL = IntegerProperty.create("signal", 0, 2);

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.875D, 0.75D);

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.25F, 0.5F, 0.875F, 0.75D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125F, 0.25F, 0.5F, 0.875F, 0.75D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.125F, 0.5F, 0.75D, 0.875F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0.125F, 0.5F, 0.75D, 0.875F, 0);

    public BlockTrafficLight(String name, CreativeTabs tab)
    {
        super(Material.IRON, name, tab);

        setSoundType(SoundType.METAL);
        setHardness(0.8f);
        this.lightValue = 7;
        this.setDefaultState(this.blockState.getBaseState().with(FACING, Direction.NORTH).with(ONWALL, false).with(SIGNAL, 0));
    }

    private int getSignal(IBlockAccess world, BlockPos pos)
    {
        TileEntityTrafficLight te = (TileEntityTrafficLight) world.getTileEntity(pos);
        return te.active();
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getActualState(BlockState state, final IBlockAccess world, final BlockPos pos)
    {
        return state.with(SIGNAL, getSignal(world, pos));
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
    {
        if (side == Direction.UP)
        {
            return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up());
        }
        return super.canPlaceBlockAt(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getDefaultState().with(FACING, placer.getHorizontalFacing()).with(ONWALL, facing != Direction.UP);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, ONWALL, SIGNAL);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateFromMeta(int meta)
    {
        return getDefaultState().with(FACING, Direction.byHorizontalIndex(meta & 3)).with(ONWALL, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        int i = 0;
        i = i | state.get(FACING).getHorizontalIndex();

        if (state.get(ONWALL))
        {
            i |= 4;
        }
        return i;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(I18n.format("tile.industrialrenewal.traffic_light.des0"));
        tooltip.add(I18n.format("tile.industrialrenewal.traffic_light.des1"));
        tooltip.add(I18n.format("tile.industrialrenewal.traffic_light.des2"));
        tooltip.add(I18n.format("tile.industrialrenewal.traffic_light.des3"));
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
    {
        if (state.get(ONWALL))
        {
            switch (state.get(FACING))
            {
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

        }
        else
        {
            return BASE_AABB;
        }
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(BlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(BlockState state)
    {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public TileEntityTrafficLight createTileEntity(World world, BlockState state)
    {
        return new TileEntityTrafficLight();
    }
}
