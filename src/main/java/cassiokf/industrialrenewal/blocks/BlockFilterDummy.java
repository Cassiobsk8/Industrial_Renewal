package cassiokf.industrialrenewal.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFilterDummy extends BlockBase {

    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.1875D, 0.250D, 0.1875D, 0.8125D, 1.0D, 0.8125D);


    public BlockFilterDummy(String name) {
        super(Material.IRON, name);
        setHardness(4f);
        setResistance(5f);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        ItemStack itemst = new ItemStack(Item.getItemFromBlock(ModBlocks.filter));
        EntityItem entity = new EntityItem(world, x, y, z, itemst);
        world.setBlockToAir(pos.up());
        if (!world.isRemote) {
            world.spawnEntity(entity);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.filter));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BLOCK_AABB;
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