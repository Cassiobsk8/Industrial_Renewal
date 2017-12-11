package cassiokf.industrialrenewal.blocks.pipes;

import cassiokf.industrialrenewal.blocks.BlockBase;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockValvePipeLarge extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockValvePipeLarge(String name) {
        super(Material.IRON, name);
        setHardness(3f);
        setResistance(5f);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
        boolean Vactive = world.getBlockState(pos).getValue(ACTIVE);
        world.playSound((EntityPlayer) null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:valve")), SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!Vactive) {
            world.setBlockState(pos, state.withProperty(ACTIVE, true), 3);
        } else {
            world.setBlockState(pos, state.withProperty(ACTIVE, false), 3);
        }
        return true;
    }
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront((meta > 8) ? meta - 8 : meta);
        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facingbits = state.getValue(FACING).getIndex();
        return facingbits;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
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

}