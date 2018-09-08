package cassiokf.industrialrenewal.tileentity.railroad.railloader;

import cassiokf.industrialrenewal.blocks.rails.BlockRailFacing;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockLoaderRail extends BlockRailFacing {

    static final PropertyBool PASS = PropertyBool.create("pass");

    public BlockLoaderRail(String name, CreativeTabs tab) {
        super(name, tab);

        setSoundType(SoundType.METAL);
        setDefaultState(blockState.getBaseState()
                .withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH)
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(PASS, false));
    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        boolean canPass = state.getActualState(world, pos).getValue(PASS);
        boolean dontHaveInv = !cart.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (canPass || dontHaveInv || !(cart instanceof EntityMinecartChest)) {
            propelMinecart(world, pos, state, cart);
            if (canPass) {
                youShallNotPass(world, pos);
            }
        } else {
            cart.motionX = 0;
            cart.motionY = 0;
            cart.motionZ = 0;
            world.scheduleUpdate(new BlockPos(pos), this, tickRate(world));
        }
    }

    private void youShallNotPass(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.withProperty(PASS, false), 3);
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        boolean powered = (meta & 1) == 1;
        EnumRailDirection shape = EnumRailDirection.byMetadata((meta >> 1) & 1);
        EnumFacing facing = EnumFacing.getHorizontal(meta >> 2);

        return getDefaultState()
                .withProperty(SHAPE, shape)
                .withProperty(FACING, facing)
                .withProperty(PASS, powered);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumRailDirection shape = state.getValue(SHAPE);
        EnumFacing facing = state.getValue(FACING);
        boolean powered = state.getValue(PASS);
        int meta = powered ? 1 : 0;
        meta |= shape.getMetadata() << 1;
        meta |= facing.getHorizontalIndex() << 2;
        return meta;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PASS, SHAPE, FACING);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    public Class<TileEntityLoaderRail> getTileEntityClass() {
        return TileEntityLoaderRail.class;
    }

    @Nullable
    @Override
    public TileEntityLoaderRail createTileEntity(World world, IBlockState state) {
        return new TileEntityLoaderRail();
    }
}