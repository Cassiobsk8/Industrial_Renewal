package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.IRSoundHandler;
import cassiokf.industrialrenewal.config.IRConfig;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockElectricFence extends BlockBase {
    public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES).map(facing -> PropertyBool.create(facing.getName())).collect(Collectors.toList()));
    private static float NORTHZ1 = 0.4375f;
    private static float SOUTHZ2 = 0.5625f;
    private static float WESTX1 = 0.4375f;
    private static float EASTX2 = 0.5625f;
    private static float DOWNY1 = 0.0f;
    private static float UPY2 = 1.5f;
    private static float RUPY2 = 1.0f;

    public BlockElectricFence(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!(entityIn instanceof EntityLivingBase)) {
            return;
        }

        int mode = IRConfig.electricFenceMode;
        float damage = (float) IRConfig.electricFenceDamageAmount;

        if (mode == 0 && (entityIn instanceof EntityMob || entityIn instanceof EntityPlayer)) {
            float damageR = (entityIn instanceof EntityPlayer) ? 0f : damage;
            DoDamage(worldIn, pos, entityIn, damageR);
        } else if (mode == 1 && (entityIn instanceof EntityMob || entityIn instanceof EntityPlayer)) {
            DoDamage(worldIn, pos, entityIn, damage);
        } else if (mode == 2) {
            DoDamage(worldIn, pos, entityIn, 0f);
        } else if (mode == 3) {
            DoDamage(worldIn, pos, entityIn, damage);
        }

    }

    private void DoDamage(World world, BlockPos pos, Entity entityIn, float amount) {
        System.out.println(amount);
        entityIn.attackEntityFrom(DamageSource.LIGHTNING_BOLT, amount);
        ((EntityLivingBase) entityIn).knockBack(entityIn, 0.3f, pos.getX() - entityIn.posX, pos.getZ() - entityIn.posZ);
        Random r = new Random();
        float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
        world.playSound(null, pos, IRSoundHandler.EFECT_SHOCK, SoundCategory.BLOCKS, 0.6F, pitch);
    }

    @Override
    @Deprecated
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState actualState = state.getActualState(world, pos);
        if (isConnected(actualState, EnumFacing.UP)) {
            return 7;
        } else {
            return 0;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    protected boolean isValidConnection(final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
        final IBlockState neighbourState = world.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        if (neighbourDirection == EnumFacing.DOWN) {
            return false;
        }
        if (neighbourDirection == EnumFacing.UP) {
            int z = Math.abs(ownPos.getZ());
            int x = Math.abs(ownPos.getX());
            int w = x - z;
            return nb.isAir(neighbourState, world, neighbourPos) && (Math.abs(w) % 3 == 0);
        }
        return nb instanceof BlockElectricFence || nb instanceof BlockElectricGate || nb.isFullCube(neighbourState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.VALUES) {
            state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()),
                    isValidConnection(world, pos, facing));
        }
        return state;
    }

    public final boolean isConnected(final IBlockState state, final EnumFacing facing) {
        return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }
        if (isConnected(state, EnumFacing.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.NORTH)) {
            NORTHZ1 = 0.4375f;
        }
        if (isConnected(state, EnumFacing.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(state, EnumFacing.SOUTH)) {
            SOUTHZ2 = 0.5625f;
        }
        if (isConnected(state, EnumFacing.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(state, EnumFacing.WEST)) {
            WESTX1 = 0.4375f;
        }
        if (isConnected(state, EnumFacing.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(state, EnumFacing.EAST)) {
            EASTX2 = 0.5625f;
        }
        final AxisAlignedBB AA_BB = new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AA_BB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState actualState = state.getActualState(source, pos);

        if (isConnected(actualState, EnumFacing.NORTH)) {
            NORTHZ1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.NORTH)) {
            NORTHZ1 = 0.4375f;
        }
        if (isConnected(actualState, EnumFacing.SOUTH)) {
            SOUTHZ2 = 1.0f;
        } else if (!isConnected(actualState, EnumFacing.SOUTH)) {
            SOUTHZ2 = 0.5625f;
        }
        if (isConnected(actualState, EnumFacing.WEST)) {
            WESTX1 = 0.0f;
        } else if (!isConnected(actualState, EnumFacing.WEST)) {
            WESTX1 = 0.4375f;
        }
        if (isConnected(actualState, EnumFacing.EAST)) {
            EASTX2 = 1.0f;
        } else if (!isConnected(actualState, EnumFacing.EAST)) {
            EASTX2 = 0.5625f;
        }
        return new AxisAlignedBB(WESTX1, DOWNY1, NORTHZ1, EASTX2, RUPY2, SOUTHZ2);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
