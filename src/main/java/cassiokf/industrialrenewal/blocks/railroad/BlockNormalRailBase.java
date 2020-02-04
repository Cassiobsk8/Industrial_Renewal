package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import cassiokf.industrialrenewal.enums.enumproperty.EnumSnowRail;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockNormalRailBase extends BlockRail {

    public static final IProperty<EnumSnowRail> SNOW = PropertyEnum.create("snow", EnumSnowRail.class);

    protected String name;

    public BlockNormalRailBase(String name, CreativeTabs tab) {
        this.name = name;
        setRegistryName(References.MODID, name);
        setTranslationKey(References.MODID + "." + name);
        setHardness(0.4f);
        //setSoundType(SoundType.METAL);
        setCreativeTab(tab);
        this.setDefaultState(blockState.getBaseState().withProperty(SNOW, EnumSnowRail.FALSE));
    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
        if (world.getBlockState(pos).getActualState(world, pos).getValue(SNOW) == EnumSnowRail.LAYER2 && isCartRunning(cart)) {
            spawnSnowParticle(world, cart.posX, cart.posY, cart.posZ);
            if (cart instanceof EntitySteamLocomotive && !world.isRemote)
            {
                boolean plow = ((EntitySteamLocomotive) cart).hasPlowItem;
                if (plow) {
                    plowSnow(world, pos);
                }
            }
        }
    }

    private void spawnSnowParticle(World world, double x, double y, double z) {
        float f = (float) MathHelper.ceil(1.0F);
        double d0 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
        int i = (int) (150.0D * d0);
        ((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y, z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(Blocks.SNOW.getDefaultState()));
    }

    private void plowSnow(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        BlockPos neighbor1;
        BlockPos neighbor2;
        switch (state.getValue(SHAPE)) {
            default:
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            case NORTH_SOUTH:
                neighbor1 = pos.offset(EnumFacing.WEST);
                neighbor2 = pos.offset(EnumFacing.EAST);
                break;
            case ASCENDING_EAST:
            case ASCENDING_WEST:
            case EAST_WEST:
                neighbor1 = pos.offset(EnumFacing.NORTH);
                neighbor2 = pos.offset(EnumFacing.SOUTH);
                break;
            case SOUTH_EAST:
                neighbor1 = pos.offset(EnumFacing.NORTH);
                neighbor2 = pos.offset(EnumFacing.WEST);
                break;
            case NORTH_EAST:
                neighbor1 = pos.offset(EnumFacing.WEST);
                neighbor2 = pos.offset(EnumFacing.SOUTH);
                break;
            case NORTH_WEST:
                neighbor1 = pos.offset(EnumFacing.EAST);
                neighbor2 = pos.offset(EnumFacing.SOUTH);
                break;
            case SOUTH_WEST:
                neighbor1 = pos.offset(EnumFacing.NORTH);
                neighbor2 = pos.offset(EnumFacing.EAST);
                break;
        }
        IBlockState state1 = world.getBlockState(neighbor1);
        IBlockState state2 = world.getBlockState(neighbor2);
        Block n1 = state1.getBlock();
        Block n2 = state2.getBlock();
        if (n1 instanceof BlockSnow && state1.getValue(BlockSnow.LAYERS) > 1) {
            spawnSnowParticle(world, neighbor1.getX(), neighbor1.getY(), neighbor1.getZ());
            world.setBlockState(neighbor1, state1.withProperty(BlockSnow.LAYERS, 1));
        }
        if (n2 instanceof BlockSnow && state2.getValue(BlockSnow.LAYERS) > 1) {
            spawnSnowParticle(world, neighbor2.getX(), neighbor2.getY(), neighbor2.getZ());
            world.setBlockState(neighbor2, state2.withProperty(BlockSnow.LAYERS, 1));
        }
    }

    private EnumSnowRail checkSnow(IBlockAccess world, BlockPos pos, IBlockState state) {
        BlockPos neighbor1;
        BlockPos neighbor2;
        switch (state.getValue(SHAPE)) {
            default:
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            case NORTH_SOUTH:
                neighbor1 = pos.offset(EnumFacing.WEST);
                neighbor2 = pos.offset(EnumFacing.EAST);
                break;
            case ASCENDING_EAST:
            case ASCENDING_WEST:
            case EAST_WEST:
                neighbor1 = pos.offset(EnumFacing.NORTH);
                neighbor2 = pos.offset(EnumFacing.SOUTH);
                break;
            case SOUTH_EAST:
                neighbor1 = pos.offset(EnumFacing.NORTH);
                neighbor2 = pos.offset(EnumFacing.WEST);
                break;
            case NORTH_EAST:
                neighbor1 = pos.offset(EnumFacing.WEST);
                neighbor2 = pos.offset(EnumFacing.SOUTH);
                break;
            case NORTH_WEST:
                neighbor1 = pos.offset(EnumFacing.EAST);
                neighbor2 = pos.offset(EnumFacing.SOUTH);
                break;
            case SOUTH_WEST:
                neighbor1 = pos.offset(EnumFacing.NORTH);
                neighbor2 = pos.offset(EnumFacing.EAST);
                break;
        }
        IBlockState state1 = world.getBlockState(neighbor1);
        IBlockState state2 = world.getBlockState(neighbor2);
        Block n1 = state1.getBlock();
        Block n2 = state2.getBlock();
        if (n1 instanceof BlockSnow || n2 instanceof BlockSnow) {
            if ((n1 instanceof BlockSnow && state1.getValue(BlockSnow.LAYERS) >= 2) || (n2 instanceof BlockSnow && state2.getValue(BlockSnow.LAYERS) >= 2)) {
                return EnumSnowRail.LAYER2;
            }
            if ((n1 instanceof BlockSnow && state1.getValue(BlockSnow.LAYERS) == 1) || (n2 instanceof BlockSnow && state2.getValue(BlockSnow.LAYERS) == 1)) {
                return EnumSnowRail.LAYER1;
            }
            return EnumSnowRail.FALSE;
        }
        return EnumSnowRail.FALSE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        return state.withProperty(SNOW, checkSnow(world, pos, state));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SHAPE, SNOW);
    }

    private boolean isCartRunning(EntityMinecart cart) {
        Double speed = Math.max(Math.abs(cart.motionX), Math.abs(cart.motionZ));
        return speed > 0.01D;
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

    public void registerItemModel(Item itemBlock) {
        IndustrialRenewal.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }

    @Override
    public float getRailMaxSpeed(World world, net.minecraft.entity.item.EntityMinecart cart, BlockPos pos) {
        if (world.getBlockState(pos).getActualState(world, pos).getValue(SNOW) == EnumSnowRail.LAYER2) {
            return 0.2f;
        }
        return 0.4f;
    }
}
