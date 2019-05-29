package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.blocks.BlockFireExtinguisher;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

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
        if (player.isSneaking())
        {
            if (worldIn.isAirBlock(posOffset) || worldIn.getBlockState(posOffset).getBlock().isReplaceable(worldIn, posOffset))
            {
                playSound(worldIn, pos, "block.metal.place");
                worldIn.setBlockState(posOffset, ModBlocks.fireExtinguisher.getDefaultState().withProperty(BlockFireExtinguisher.FACING, player.getHorizontalFacing()).withProperty(BlockFireExtinguisher.ONWALL, facing != EnumFacing.UP));
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        } else if (IRConfig.fireExtinguisheronNether || player.dimension != DimensionType.NETHER.getId())
        {
            ArrayList<BlockPos> list = new ArrayList<>();
            add9x9pos(list, pos);
            for (BlockPos bpos : list)
            {
                worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, bpos.offset(facing).getX() + 0.5D, bpos.offset(facing).getY() + 0.5D, bpos.offset(facing).getZ() + 0.5D, 0, 1, 0);
                for (EnumFacing faces : EnumFacing.values())
                {
                    IBlockState state = worldIn.getBlockState(bpos);
                    if (worldIn.extinguishFire(player, bpos, faces))
                    {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, bpos.getX() + 0.5D, bpos.getY() + 0.5D, bpos.getZ() + 0.5D, 0, 1, 0);
                        playSound(worldIn, pos, "block.fire.extinguish");
                    }
                    if (state.getMaterial() == Material.LAVA && state.getValue(BlockFluidBase.LEVEL) == 0)
                    {
                        worldIn.setBlockState(bpos, Blocks.OBSIDIAN.getDefaultState());
                    }
                    if (state.getMaterial() == Material.LAVA && state.getValue(BlockFluidBase.LEVEL) > 0)
                    {
                        worldIn.setBlockState(bpos, Blocks.COBBLESTONE.getDefaultState());
                    }
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    private void playSound(World world, BlockPos pos, String resourceLocation) {
        world.playSound(null, pos, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation((resourceLocation)))), SoundCategory.BLOCKS, 1.0F, 1.0F);
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
