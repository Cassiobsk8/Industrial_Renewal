package cassiokf.industrialrenewal.blocks;

import cassiokf.industrialrenewal.blocks.abstracts.BlockMultiBlockBase;
import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.item.ItemFireBox;
import cassiokf.industrialrenewal.item.ItemPowerScrewDrive;
import cassiokf.industrialrenewal.tileentity.TileEntitySteamBoiler;
import cassiokf.industrialrenewal.util.MachinesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSteamBoiler extends BlockMultiBlockBase<TileEntitySteamBoiler>
{
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);

    public BlockSteamBoiler()
    {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.requires")
                + ":"));
        tooltip.add(new StringTextComponent(" -" + I18n.format("info.industrialrenewal.firebox")));
        tooltip.add(new StringTextComponent(" -" + Blocks.WATER.getNameTextComponent().getFormattedText()
                + ": "
                + IRConfig.Main.steamBoilerWaterPerTick.get()
                + " mB/t"));
        tooltip.add(new StringTextComponent(I18n.format("info.industrialrenewal.produces")
                + " "
                + FluidInit.STEAM.getName()
                + ": "
                + (IRConfig.Main.steamBoilerWaterPerTick.get() * IRConfig.Main.steamBoilerConversionFactor.get())
                + " mB/t"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        if (te != null && te.isMaster() && te.getBoilerType() != 0 && te.boiler.isBurning() && rand.nextInt(12) == 0)
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, (2F + rand.nextFloat()) * IRConfig.Sounds.masterVolumeMult.get(), rand.nextFloat() * 0.7F + 0.3F);
        }
    }

    @Override
    public List<BlockPos> getMachineBlockPosList(BlockPos masterPos, Direction facing)
    {
        return MachinesUtils.getBlocksIn3x3x3Centered(masterPos);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        TileEntitySteamBoiler tile = (TileEntitySteamBoiler) worldIn.getTileEntity(pos);
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemFireBox || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemFireBox && tile.getBoilerType() == 0)
            {
                int type = ((ItemFireBox) heldItem.getItem()).type;
                tile.setType(type);
                if (!worldIn.isRemote && !player.isCreative()) heldItem.shrink(1);
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && tile.getBoilerType() != 0)
            {
                if (!worldIn.isRemote && !player.isCreative()) player.addItemStackToInventory(tile.getFireBoxStack());
                tile.setType(0);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MASTER, TYPE);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        TileEntity te = worldIn.getTileEntity(currentPos);
        if (te instanceof TileEntitySteamBoiler && stateIn.get(MASTER))
        {
            return stateIn.with(TYPE, ((TileEntitySteamBoiler) te).getBoilerType());
        }
        return stateIn.with(TYPE, 0);
    }

    @Nullable
    @Override
    public TileEntitySteamBoiler createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySteamBoiler();
    }
}
