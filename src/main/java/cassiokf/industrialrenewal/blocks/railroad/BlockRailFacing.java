package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class BlockRailFacing extends AbstractRailBlock
{

    protected String name;

    public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, dir ->
            dir == EnumRailDirection.NORTH_SOUTH || dir == EnumRailDirection.EAST_WEST
    );

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockRailFacing(String name, CreativeTabs tab) {
        super(false);
        this.name = name;

        setRegistryName(References.MODID, name);
        setTranslationKey(References.MODID + "." + name);
        setCreativeTab(tab);
    }

    public static void propelMinecart(BlockState state, MinecartEntity minecart)
    {
        BlockRailBase.EnumRailDirection dir = state.getValue(BlockRailFacing.SHAPE);
        EnumFacing facing = state.getValue(FACING);
        if (dir == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (facing == EnumFacing.EAST) {
                minecart.motionX = 0.2d;
            } else {
                minecart.motionX = -0.2d;
            }
        } else if (dir == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (facing == EnumFacing.SOUTH) {
                minecart.motionZ = 0.2d;
            } else {
                minecart.motionZ = -0.2d;
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing facing = EnumFacing.fromAngle(placer.rotationYawHead);
        EnumRailDirection shape = facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? EnumRailDirection.NORTH_SOUTH : EnumRailDirection.EAST_WEST;
        return getDefaultState().withProperty(FACING, facing).withProperty(SHAPE, shape);
    }

    @Nonnull
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }

    @Nonnull
    @Override
    @Deprecated
    public abstract  BlockState getStateFromMeta(int meta);

    @Override
    public abstract int getMetaFromState(IBlockState state);


    @Nonnull
    @Override
    protected abstract BlockStateContainer createBlockState();

    @Override
    public boolean isFlexibleRail(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {
        return false;
    }

    public void registerItemModel(Item itemBlock) {
        IndustrialRenewal.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }
}
