package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.blocks.BlockFireExtinguisher;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.BlocksRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.Objects;

public class ItemFireExtinguisher extends ItemBase {


    public ItemFireExtinguisher(Properties properties)
    {
        super(properties.maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        PlayerEntity player = context.getPlayer();
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        ItemStack itemstack = player.getHeldItem(context.getHand());
        BlockPos posOffset = pos.offset(context.getFace());
        if (player.isSneaking())
        {
            if (worldIn.isAirBlock(posOffset) || worldIn.getBlockState(posOffset).getBlock().isReplaceable(worldIn, posOffset))
            {
                playSound(worldIn, pos, "block.metal.place");
                worldIn.setBlockState(posOffset, BlocksRegistration.FIREEXTINGUISHER.get().getDefaultState().with(BlockFireExtinguisher.FACING, player.getHorizontalFacing()).with(BlockFireExtinguisher.ONWALL, context.getFace() != Direction.UP));
                itemstack.shrink(1);
                return ActionResultType.SUCCESS;
            }
        } else if (IRConfig.Main.fireExtinguisherOnNether.get() || player.dimension != DimensionType.THE_NETHER)
        {
            ArrayList<BlockPos> list = new ArrayList<>();
            add9x9pos(list, pos);
            for (BlockPos bpos : list)
            {
                worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, bpos.offset(facing).getX() + 0.5D, bpos.offset(facing).getY() + 0.5D, bpos.offset(facing).getZ() + 0.5D, 0, 1, 0);
                for (Direction faces : Direction.values())
                {
                     BlockState state = worldIn.getBlockState(bpos);
                    if (worldIn.extinguishFire(player, bpos, faces))
                    {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, bpos.getX() + 0.5D, bpos.getY() + 0.5D, bpos.getZ() + 0.5D, 0, 1, 0);
                        playSound(worldIn, pos, "block.fire.extinguish");
                    }
                    if (state.getMaterial() == Material.LAVA && state.get(BlockFluidBase.LEVEL) == 0)
                    {
                        worldIn.setBlockState(bpos, Blocks.OBSIDIAN.getDefaultState());
                    }
                    if (state.getMaterial() == Material.LAVA && state.get(BlockFluidBase.LEVEL) > 0)
                    {
                        worldIn.setBlockState(bpos, Blocks.COBBLESTONE.getDefaultState());
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    private void playSound(World world, BlockPos pos, String resourceLocation) {
        world.playSound(null, pos, Objects.requireNonNull(SoundEvent.REGISTRY.getObject(new ResourceLocation((resourceLocation)))), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private void add9x9pos(ArrayList<BlockPos> list, BlockPos initialPos) {
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
