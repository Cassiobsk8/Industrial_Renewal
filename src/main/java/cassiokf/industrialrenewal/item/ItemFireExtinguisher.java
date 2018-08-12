package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.blocks.ModBlocks;
import cassiokf.industrialrenewal.blocks.gates.BlockFireExtinguisher;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Objects;

public class ItemFireExtinguisher extends ItemBase {


    public ItemFireExtinguisher(String name, CreativeTabs tab) {
        super(name, tab);

        this.maxStackSize = 1;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        BlockPos posOffset = pos.offset(facing);
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        if (player.isSneaking()) {
            if (worldIn.isAirBlock(posOffset) || worldIn.getBlockState(posOffset).getBlock().isReplaceable(worldIn, posOffset)) {
                worldIn.playSound(null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.metal.place")))), SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(posOffset, ModBlocks.fireExtinguisher.getDefaultState().withProperty(BlockFireExtinguisher.FACING, player.getHorizontalFacing()).withProperty(BlockFireExtinguisher.ONWALL, facing != EnumFacing.UP));
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.FAIL;
        } else {
            ArrayList<BlockPos> list = new ArrayList<>();
            add9x9pos(list, pos);
            for (BlockPos bpos : list) {
                worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, bpos.offset(facing).getX() + 0.5D, bpos.offset(facing).getY() + 0.5D, bpos.offset(facing).getZ() + 0.5D, 0, 1, 0);
                for (EnumFacing faces : EnumFacing.values()) {
                    if (worldIn.extinguishFire(player, bpos, faces)) {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, bpos.getX() + 0.5D, bpos.getY() + 0.5D, bpos.getZ() + 0.5D, 0, 1, 0);
                        worldIn.playSound(null, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation(("block.fire.extinguish")))), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
            return EnumActionResult.SUCCESS;
        }
    }

    private void add9x9pos(ArrayList<BlockPos> list, BlockPos initialPos) {
        list.add(initialPos);
        list.add(initialPos.offset(EnumFacing.NORTH));
        list.add(initialPos.offset(EnumFacing.SOUTH));
        list.add(initialPos.offset(EnumFacing.EAST));
        list.add(initialPos.offset(EnumFacing.EAST).offset(EnumFacing.NORTH));
        list.add(initialPos.offset(EnumFacing.EAST).offset(EnumFacing.SOUTH));
        list.add(initialPos.offset(EnumFacing.WEST));
        list.add(initialPos.offset(EnumFacing.WEST).offset(EnumFacing.NORTH));
        list.add(initialPos.offset(EnumFacing.WEST).offset(EnumFacing.SOUTH));
    }
}
