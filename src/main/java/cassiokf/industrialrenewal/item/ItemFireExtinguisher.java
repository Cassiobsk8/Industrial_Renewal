package cassiokf.industrialrenewal.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;

public class ItemFireExtinguisher extends ItemBase
{


    public ItemFireExtinguisher(Item.Properties properties)
    {
        super(properties.maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        PlayerEntity player = context.getPlayer();
        ItemStack itemstack = context.getItem();
        BlockPos pos = context.getPos();
        BlockPos posOffset = pos.offset(context.getFace());
        World worldIn = context.getWorld();
        if (player.isCrouching())
        {
            if (worldIn.isAirBlock(posOffset) || worldIn.getBlockState(posOffset).getBlock().isReplaceable(worldIn.getBlockState(posOffset), new BlockItemUseContext(context)))
            {
                playSound(worldIn, pos, SoundEvents.BLOCK_METAL_PLACE);
                //worldIn.setBlockState(posOffset, ModBlocks.fireExtinguisher.getDefaultState().withProperty(BlockFireExtinguisher.FACING, player.getHorizontalFacing()).withProperty(BlockFireExtinguisher.ONWALL, facing != EnumFacing.UP));
                itemstack.shrink(1);
                return ActionResultType.SUCCESS;
            }
        } else if (true/*IRConfig.MainConfig.Main.fireExtinguisherOnNether*/ || player.dimension != DimensionType.THE_NETHER)
        {
            ArrayList<BlockPos> list = new ArrayList<>();
            add9x9pos(list, pos);
            for (BlockPos bpos : list)
            {
                worldIn.addParticle(ParticleTypes.SPLASH, bpos.offset(context.getFace()).getX() + 0.5D, bpos.offset(context.getFace()).getY() + 0.5D, bpos.offset(context.getFace()).getZ() + 0.5D, 0, 1, 0);
                for (Direction faces : Direction.values())
                {
                    BlockState state = worldIn.getBlockState(bpos);
                    if (worldIn.extinguishFire(player, bpos, faces))
                    {
                        worldIn.addParticle(ParticleTypes.LARGE_SMOKE, bpos.getX() + 0.5D, bpos.getY() + 1D, bpos.getZ() + 0.5D, 0, 0.1D, 0);
                        playSound(worldIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                    }
                    if (state.getMaterial() == Material.LAVA && state.get(FlowingFluidBlock.LEVEL) == 0)
                    {
                        worldIn.addParticle(ParticleTypes.LARGE_SMOKE, bpos.getX() + 0.5D, bpos.getY() + 1D, bpos.getZ() + 0.5D, 0, 0.1D, 0);
                        playSound(worldIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                        worldIn.setBlockState(bpos, Blocks.OBSIDIAN.getDefaultState());
                    }
                    if (state.getMaterial() == Material.LAVA && state.get(FlowingFluidBlock.LEVEL) > 0)
                    {
                        worldIn.addParticle(ParticleTypes.LARGE_SMOKE, bpos.getX() + 0.5D, bpos.getY() + 1D, bpos.getZ() + 0.5D, 0, 0.1D, 0);
                        playSound(worldIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                        worldIn.setBlockState(bpos, Blocks.COBBLESTONE.getDefaultState());
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    private void playSound(World world, BlockPos pos, SoundEvent soundEvent)
    {
        world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private void add9x9pos(ArrayList<BlockPos> list, BlockPos initialPos)
    {
        list.add(initialPos);
        list.add(initialPos.offset(Direction.NORTH));
        list.add(initialPos.offset(Direction.SOUTH));
        list.add(initialPos.offset(Direction.EAST));
        list.add(initialPos.offset(Direction.EAST).offset(Direction.NORTH));
        list.add(initialPos.offset(Direction.EAST).offset(Direction.SOUTH));
        list.add(initialPos.offset(Direction.WEST));
        list.add(initialPos.offset(Direction.WEST).offset(Direction.NORTH));
        list.add(initialPos.offset(Direction.WEST).offset(Direction.SOUTH));
    }
}
