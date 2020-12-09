package cassiokf.industrialrenewal.blocks.machines;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.tileentity.machines.TELathe;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockLathe extends BlockMultiBlockBase<TELathe>
{
    public BlockLathe(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote) OpenGUI(worldIn, pos, player);
        return ActionResultType.SUCCESS;
    }

    private void OpenGUI(World world, BlockPos pos, PlayerEntity player)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TELathe)
        {
            TELathe masterTE = ((TELathe) te).getMaster();
            player.openGui(IndustrialRenewal.instance, GUIHandler.LATHE, world, masterTE.getPos().getX(), masterTE.getPos().getY(), masterTE.getPos().getZ());
        }
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing)
    {
        return MachinesUtils.getBlocksIn3x2x2Centered(masterPos, facing);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing()).with(MASTER, true);
    }

    @Override
    protected BlockPos getMasterPosBasedOnPlace(BlockPos pos, Direction facing)
    {
        return pos;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
    }

    @Nullable
    @Override
    public TELathe createTileEntity(BlockState state, IBlockReader world)
    {
        return new TELathe();
    }
}
