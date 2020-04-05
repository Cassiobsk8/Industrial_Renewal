package cassiokf.industrialrenewal.blocks.railroad;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.IRSoundRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockRailGate extends BlockNormalRailBase
{

    public static final PropertyBool OPEN = PropertyBool.create("open");
    protected static final AxisAlignedBB CNORTH_AABB = new AxisAlignedBB(-0.25D, 0.0D, 0.375D, 1.25D, 2D, 0.625D);
    protected static final AxisAlignedBB CWEST_AABB = new AxisAlignedBB(0.375D, 0.0D, -0.25D, 0.625D, 2D, 1.25D);
    protected String name;

    public BlockRailGate(String name, CreativeTabs tab)
    {
        super(name, tab);
        setDefaultState(getDefaultState().withProperty(OPEN, false));
    }


    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return super.canCollideCheck(state, hitIfLiquid);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        EnumRailDirection direction = state.getValue(SHAPE);
        if (!state.getValue(OPEN))
        {
            switch (direction)
            {
                case NORTH_SOUTH:
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, CNORTH_AABB);
                    break;
                case EAST_WEST:
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, CWEST_AABB);
                    break;
            }
        }
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumRailDirection direction = state.getValue(SHAPE);
        switch (direction)
        {
            default:
            case NORTH_SOUTH:
                return CNORTH_AABB;
            case EAST_WEST:
                return CWEST_AABB;
        }
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getValue(OPEN);
    }

    @Override
    protected void updateState(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        boolean flag1 = state.getValue(OPEN);
        boolean flag2 = worldIn.isBlockPowered(pos);
        if (flag1 != flag2)
        {
            worldIn.setBlockState(pos, state.withProperty(OPEN, flag2), 3);
            //Sound
            Random r = new Random();
            float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
            if (flag2)
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_OPEN, SoundCategory.NEUTRAL, 1.0F * IRConfig.MainConfig.Sounds.masterVolumeMult, pitch);
            } else
            {
                worldIn.playSound(null, pos, IRSoundRegister.BLOCK_CATWALKGATE_CLOSE, SoundCategory.NEUTRAL, 1.0F * IRConfig.MainConfig.Sounds.masterVolumeMult, pitch);
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, OPEN, SHAPE, SNOW);
    }

    @Override
    public boolean canMakeSlopes(IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFlexibleRail(IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7)).withProperty(OPEN, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | (state.getValue(SHAPE)).getMetadata();

        if (state.getValue(OPEN))
        {
            i |= 8;
        }

        return i;
    }
}