package cassiokf.industrialrenewal.blocks.industrialfloor;

import cassiokf.industrialrenewal.blocks.pipes.BlockEnergyCable;
import cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockFloorCable extends BlockEnergyCable
{
    public static final PropertyBool WSOUTH = PropertyBool.create("w_south");
    public static final PropertyBool WNORTH = PropertyBool.create("w_north");
    public static final PropertyBool WEAST = PropertyBool.create("w_east");
    public static final PropertyBool WWEST = PropertyBool.create("w_west");
    public static final PropertyBool WUP = PropertyBool.create("w_up");
    public static final PropertyBool WDOWN = PropertyBool.create("w_down");

    public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockFloorCable(String name, CreativeTabs tab) {
        super(name, tab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    /*
        @Override
        protected BlockStateContainer createBlockState()
        {
            return new BlockStateContainer(this, SOUTH, NORTH, EAST, WEST, UP, DOWN, WSOUTH, WNORTH, WEAST, WWEST, WUP, WDOWN);
        }

        @SuppressWarnings("deprecation")
        @Override
        public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos)
        {
            state = state.withProperty(SOUTH, canConnectPipe(world, pos, EnumFacing.SOUTH)).withProperty(NORTH, canConnectPipe(world, pos, EnumFacing.NORTH))
                    .withProperty(EAST, canConnectPipe(world, pos, EnumFacing.EAST)).withProperty(WEST, canConnectPipe(world, pos, EnumFacing.WEST))
                    .withProperty(UP, canConnectPipe(world, pos, EnumFacing.UP)).withProperty(DOWN, canConnectPipe(world, pos, EnumFacing.DOWN))
                    .withProperty(WSOUTH, BlockIndustrialFloor.canConnectTo(world, pos, EnumFacing.SOUTH)).withProperty(WNORTH, BlockIndustrialFloor.canConnectTo(world, pos, EnumFacing.NORTH))
                    .withProperty(WEAST, BlockIndustrialFloor.canConnectTo(world, pos, EnumFacing.EAST)).withProperty(WWEST, BlockIndustrialFloor.canConnectTo(world, pos, EnumFacing.WEST))
                    .withProperty(WUP, BlockIndustrialFloor.canConnectTo(world, pos, EnumFacing.UP)).withProperty(WDOWN, BlockIndustrialFloor.canConnectTo(world, pos, EnumFacing.DOWN));
            return state;
        }
    */
    private boolean canConnectPipe(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        return canConnectToPipe(world, pos, facing) || canConnectToCapability(world, pos, facing);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
        return new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor)).getItem();
    }

    @Override
    public void onPlayerDestroy(World world, BlockPos pos, IBlockState state)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        ItemStack itemst = new ItemStack(net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.energyCable));
        EntityItem entity = new EntityItem(world, x, y, z, itemst);
        if (!world.isRemote) {
            world.spawnEntity(entity);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean p_185477_7_) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(worldIn, pos));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }

}