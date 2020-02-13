package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.enums.enumproperty.EnumSnowRail;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BlockNormalRailBase extends RailBlock
{

    public static final IProperty<EnumSnowRail> SNOW = EnumProperty.create("snow", EnumSnowRail.class);


    public BlockNormalRailBase(Block.Properties properties)
    {
        super(properties);
        this.setDefaultState(getDefaultState().with(SNOW, EnumSnowRail.FALSE));
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart)
    {
        if (world.getBlockState(pos).get(SNOW) == EnumSnowRail.LAYER2 && isCartRunning(cart))
        {
            spawnSnowParticle(world, cart.getPosX(), cart.getPosY(), cart.getPosZ());
            //TODO ENTITY LOCOMOTIVE
            //if (cart instanceof EntitySteamLocomotive && !world.isRemote)
            //{
            //    boolean plow = ((EntitySteamLocomotive) cart).hasPlowItem;
            //    if (plow) {
            //        plowSnow(world, pos);
            //    }
            //}
        }
    }

    private void spawnSnowParticle(World world, double x, double y, double z)
    {
        float f = (float) MathHelper.ceil(1.0F);
        double d0 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
        int i = (int) (150.0D * d0);
        ((ServerWorld) world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, getDefaultState()), x, y, z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
    }

    private void plowSnow(World world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        BlockPos neighbor1;
        BlockPos neighbor2;
        switch (state.get(SHAPE))
        {
            default:
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            case NORTH_SOUTH:
                neighbor1 = pos.offset(Direction.WEST);
                neighbor2 = pos.offset(Direction.EAST);
                break;
            case ASCENDING_EAST:
            case ASCENDING_WEST:
            case EAST_WEST:
                neighbor1 = pos.offset(Direction.NORTH);
                neighbor2 = pos.offset(Direction.SOUTH);
                break;
            case SOUTH_EAST:
                neighbor1 = pos.offset(Direction.NORTH);
                neighbor2 = pos.offset(Direction.WEST);
                break;
            case NORTH_EAST:
                neighbor1 = pos.offset(Direction.WEST);
                neighbor2 = pos.offset(Direction.SOUTH);
                break;
            case NORTH_WEST:
                neighbor1 = pos.offset(Direction.EAST);
                neighbor2 = pos.offset(Direction.SOUTH);
                break;
            case SOUTH_WEST:
                neighbor1 = pos.offset(Direction.NORTH);
                neighbor2 = pos.offset(Direction.EAST);
                break;
        }
        BlockState state1 = world.getBlockState(neighbor1);
        BlockState state2 = world.getBlockState(neighbor2);
        Block n1 = state1.getBlock();
        Block n2 = state2.getBlock();
        if (n1 instanceof SnowBlock && state1.get(SnowBlock.LAYERS) > 1)
        {
            spawnSnowParticle(world, neighbor1.getX(), neighbor1.getY(), neighbor1.getZ());
            world.setBlockState(neighbor1, state1.with(SnowBlock.LAYERS, 1));
        }
        if (n2 instanceof SnowBlock && state2.get(SnowBlock.LAYERS) > 1)
        {
            spawnSnowParticle(world, neighbor2.getX(), neighbor2.getY(), neighbor2.getZ());
            world.setBlockState(neighbor2, state2.with(SnowBlock.LAYERS, 1));
        }
    }

    private EnumSnowRail checkSnow(IBlockReader world, BlockPos pos, BlockState state)
    {
        BlockPos neighbor1;
        BlockPos neighbor2;
        switch (state.get(SHAPE))
        {
            default:
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            case NORTH_SOUTH:
                neighbor1 = pos.offset(Direction.WEST);
                neighbor2 = pos.offset(Direction.EAST);
                break;
            case ASCENDING_EAST:
            case ASCENDING_WEST:
            case EAST_WEST:
                neighbor1 = pos.offset(Direction.NORTH);
                neighbor2 = pos.offset(Direction.SOUTH);
                break;
            case SOUTH_EAST:
                neighbor1 = pos.offset(Direction.NORTH);
                neighbor2 = pos.offset(Direction.WEST);
                break;
            case NORTH_EAST:
                neighbor1 = pos.offset(Direction.WEST);
                neighbor2 = pos.offset(Direction.SOUTH);
                break;
            case NORTH_WEST:
                neighbor1 = pos.offset(Direction.EAST);
                neighbor2 = pos.offset(Direction.SOUTH);
                break;
            case SOUTH_WEST:
                neighbor1 = pos.offset(Direction.NORTH);
                neighbor2 = pos.offset(Direction.EAST);
                break;
        }
        BlockState state1 = world.getBlockState(neighbor1);
        BlockState state2 = world.getBlockState(neighbor2);
        Block n1 = state1.getBlock();
        Block n2 = state2.getBlock();
        if (n1 instanceof SnowBlock || n2 instanceof SnowBlock)
        {
            if ((n1 instanceof SnowBlock && state1.get(SnowBlock.LAYERS) >= 2) || (n2 instanceof SnowBlock && state2.get(SnowBlock.LAYERS) >= 2))
            {
                return EnumSnowRail.LAYER2;
            }
            if ((n1 instanceof SnowBlock && state1.get(SnowBlock.LAYERS) == 1) || (n2 instanceof SnowBlock && state2.get(SnowBlock.LAYERS) == 1))
            {
                return EnumSnowRail.LAYER1;
            }
            return EnumSnowRail.FALSE;
        }
        return EnumSnowRail.FALSE;
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return stateIn.with(SNOW, checkSnow(worldIn, currentPos, stateIn));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(SHAPE, SNOW);
    }

    private boolean isCartRunning(AbstractMinecartEntity cart)
    {
        Double speed = Math.max(Math.abs(cart.getMotion().x), Math.abs(cart.getMotion().z));
        return speed > 0.01D;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public float getRailMaxSpeed(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart)
    {
        if (world.getBlockState(pos).get(SNOW) == EnumSnowRail.LAYER2)
        {
            return 0.2f;
        }
        return 0.4f;
    }
}
