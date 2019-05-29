package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class BlockFireExtinguisher extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ONWALL = PropertyBool.create("onwall");

    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1D, 0.75D);

    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(0F, 0F, 0.25F, 0.5F, 1F, 0.75D);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(1F, 0F, 0.25F, 0.5F, 1F, 0.75D);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0F, 0.5F, 0.75D, 1F, 1);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.25F, 0F, 0.5F, 0.75D, 1F, 0);

    public BlockFireExtinguisher(String name, CreativeTabs tab) {
        super(Material.IRON, name, tab);
        setSoundType(SoundType.METAL);
        setHardness(0.8f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isBurning()) {
            player.extinguish();
            world.spawnParticle(EnumParticleTypes.WATER_SPLASH, player.getPosition().getX(), player.getPosition().getY() + 1F, player.getPosition().getZ(), 0, 1, 0);
            world.playSound(null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.fire.extinguish")))), SoundCategory.BLOCKS, 1.0F, 1.0F);
        } else if (player.isSneaking()) {
            if (player.inventory.addItemStackToInventory(new ItemStack(ModItems.fireExtinguisher, 1))) {
                world.setBlockToAir(pos);
                return true;
            }
            return false;
        }
        return false;
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ONWALL);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(ONWALL, Boolean.valueOf((meta & 4) > 0));
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
    public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
        return new ItemStack(ModItems.fireExtinguisher).getItem();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModItems.fireExtinguisher);
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
