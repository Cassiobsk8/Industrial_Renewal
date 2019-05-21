package cassiokf.industrialrenewal.tileentity.railroad.railloader;

import cassiokf.industrialrenewal.Registry.ModBlocks;
import cassiokf.industrialrenewal.blocks.rails.BlockRailFacing;
import cassiokf.industrialrenewal.tileentity.railroad.cargoloader.BlockCargoLoader;
import cassiokf.industrialrenewal.tileentity.railroad.fluidloader.BlockFluidLoader;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockLoaderRail extends BlockRailFacing {

    static final PropertyBool PASS = PropertyBool.create("pass");

    private int Tick = 0;

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
        TileEntityLoaderRail te = (TileEntityLoaderRail) world.getTileEntity(pos);
        boolean dontHaveCorrectInv = GetCartInvType(world, pos, cart) == 0;
        boolean canPass = te.GetPass();

        te.SetCart(cart);

        if (dontHaveCorrectInv || canPass) {
            propelMinecart(world, pos, state, cart);
            if (canPass) {
                //te.ChangePass(false);
            }
        } else {
            cart.posX = pos.getX() + 0.5D;
            cart.posZ = pos.getZ() + 0.5D;
            cart.motionX = 0.0D;
            cart.motionZ = 0.0D;
            //world.scheduleUpdate(new BlockPos(pos), this, tickRate(world));
        }
    }

    private int GetCartInvType(World world, BlockPos pos, EntityMinecart cart) {
        Block downBlock = world.getBlockState(pos.down()).getBlock();
        Block upBlock = world.getBlockState(pos.up(2)).getBlock();
        boolean itemCapability = cart.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        boolean fluidCapability = cart.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        int type = 0;
        if (itemCapability && (downBlock instanceof BlockCargoLoader || upBlock instanceof BlockCargoLoader)) {
            type = 1;
        } else if (fluidCapability && (downBlock instanceof BlockFluidLoader || upBlock instanceof BlockFluidLoader)) {
            type = 2;
        }
        return type;
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

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        String str = I18n.format("tile.industrialrenewal.loader_rail.info") + " " + ModBlocks.cargoLoader.getLocalizedName();
        tooltip.add(str);
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