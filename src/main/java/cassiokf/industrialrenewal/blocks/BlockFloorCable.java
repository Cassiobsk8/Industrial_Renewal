package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.item.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockFloorCable extends BlockEnergyCable {

    public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockFloorCable(String name) {
        super(name);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        if (entity.inventory.getCurrentItem().getItem() == ModItems.screwDrive) {
            world.playSound(null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:drill")), SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(new BlockPos(i, j, k), ModBlocks.blockIndFloor.getDefaultState(), 3);
            if (!entity.isCreative()) {
                entity.inventory.addItemStackToInventory(new ItemStack(net.minecraft.item.ItemBlock.getItemFromBlock(ModBlocks.energyCable), 1));
            }
            return true;
        }
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
        return new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.blockIndFloor)).getItem();
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
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
    public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean p_185477_7_) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(worldIn, pos));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

}