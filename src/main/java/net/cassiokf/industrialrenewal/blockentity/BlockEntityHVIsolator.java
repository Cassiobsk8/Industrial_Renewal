package net.cassiokf.industrialrenewal.blockentity;


import net.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BlockEntityHVIsolator extends BlockEntitySyncable {
    
    //    public HvNode node;
    public Set<BlockPos> neighbors;
    public Set<BlockPos> allNodes;
    
    public BlockEntityHVIsolator(BlockPos pos, BlockState state) {
        super(ModBlockEntity.ISOLATOR_TILE.get(), pos, state);
        neighbors = new HashSet<>();
        allNodes = new HashSet<>();
        allNodes.add(this.worldPosition);
    }
    
    public void remove() {
        if (level == null) return;
        if (!level.isClientSide) {
            Block.popResource(level, worldPosition, new ItemStack(ModItems.WIRE_COIL.get(), neighbors.size()));
            unlinkAll();
        }
    }
    
    public boolean link(BlockEntityHVIsolator neighbor) {
        if (neighbors.contains(neighbor.worldPosition) && neighbor.neighbors.contains(this.worldPosition)) {
            return false;
        }
        
        neighbors.add(neighbor.worldPosition);
        neighbor.neighbors.add(this.worldPosition);
        
        search();
        propagate();
        
        update();
        neighbor.update();
        return true;
    }
    
    public void unlinkAll() {
        if (level == null) return;
        for (BlockPos neighbor : neighbors) {
            //            Utils.debug("UNLINKING AT", neighbor);
            BlockEntity te = level.getBlockEntity(neighbor);
            if (te instanceof BlockEntityHVIsolator nodeTE) {
                nodeTE.neighbors.remove(this.worldPosition);
                nodeTE.search();
                nodeTE.propagate();
                nodeTE.update();
            }
        }
    }
    
    public void search() {
        if (level == null) return;
        allNodes.clear();
        allNodes.add(this.worldPosition);
        
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(this.worldPosition);
        //Iterator<BlockPos> i = queue.iterator();
        
        while (!queue.isEmpty()) {
            BlockPos node = queue.poll();
            //            Utils.debug("SEARCH", node);
            BlockEntity te = level.getBlockEntity(node);
            if (te != null) {
                BlockEntityHVIsolator nodeTE = (BlockEntityHVIsolator) te;
                for (BlockPos neighborNodes : nodeTE.neighbors) {
                    if (!allNodes.contains(neighborNodes)) {
                        allNodes.add(neighborNodes);
                        queue.add(neighborNodes);
                    }
                }
            }
        }
        this.update();
    }
    
    public void propagate() {
        if (level == null) return;
        Set<BlockPos> clone = new HashSet<BlockPos>(allNodes);
        for (BlockPos node : this.allNodes) {
            BlockEntity te = level.getBlockEntity(node);
            if (te != null) {
                BlockEntityHVIsolator nodeTE = (BlockEntityHVIsolator) te;
                nodeTE.allNodes = clone;
                nodeTE.update();
            }
        }
    }
    
    public void update() {
        this.sync();
    }
    
    public long[] saveNeighbors() {
        return neighbors.stream().map(BlockPos::asLong).mapToLong(i -> i).toArray();
    }
    
    public long[] saveAllNodes() {
        return allNodes.stream().map(BlockPos::asLong).mapToLong(i -> i).toArray();
    }
    
    public void loadNeighbors(long[] savedNeighbors) {
        if (neighbors != null) neighbors.clear();
        else neighbors = new HashSet<>();
        
        for (long blockPos : savedNeighbors) {
            neighbors.add(BlockPos.of(blockPos));
        }
    }
    
    public void loadAllNodes(long[] savedAllNodes) {
        if (allNodes != null) allNodes.clear();
        else allNodes = new HashSet<>();
        
        for (long blockPos : savedAllNodes) {
            allNodes.add(BlockPos.of(blockPos));
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putLongArray("neighbors", saveNeighbors());
        compoundTag.putLongArray("all_nodes", saveAllNodes());
        super.saveAdditional(compoundTag);
    }
    
    @Override
    public void load(CompoundTag compoundTag) {
        loadNeighbors(compoundTag.getLongArray("neighbors"));
        loadAllNodes(compoundTag.getLongArray("all_nodes"));
        super.load(compoundTag);
    }
    
    
    @Override
    public String toString() {
        return "TileEntityWireIsolator " + worldPosition;
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
