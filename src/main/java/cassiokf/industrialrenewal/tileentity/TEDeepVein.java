package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.config.IRConfig;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.tileentity.abstracts.TEBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TEDeepVein extends TEBase
{
    private ItemStack stack;

    public TEDeepVein()
    {
        stack = generateOre();
    }

    private ItemStack generateOre()
    {
        int min = IRConfig.MainConfig.Generation.deepVeinMinOre;
        int oreQuantity = rand.nextInt(IRConfig.MainConfig.Generation.deepVeinMaxOre - min) + min;
        Item item = ModItems.deepVeinOres.get(rand.nextInt(ModItems.deepVeinOres.size() - 1));
        return new ItemStack(item, oreQuantity);
    }

    public int getOreQuantity()
    {
        return stack.getCount();
    }

    public ItemStack getOre(int fortune, boolean simulate)
    {
        if (stack.getCount() <= 0)
        {
            onDepleted();
            return ItemStack.EMPTY;
        }

        Block block = Block.getBlockFromItem(stack.getItem());
        int count = block.quantityDroppedWithBonus(fortune, world.rand);
        ItemStack newStack = new ItemStack(block.getItemDropped(block.getDefaultState(), world.rand, fortune), count);

        if (!simulate)
        {
            stack.shrink(1);
            if (stack.getCount() <= 0) onDepleted();
            markDirty();
        }
        return newStack;
    }

    private void onDepleted()
    {
        world.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
        compound.setInteger("count", stack.getCount());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        stack = new ItemStack(compound.getCompoundTag("stack"));
        stack.setCount(compound.getInteger("count"));
        super.readFromNBT(compound);
    }
}
