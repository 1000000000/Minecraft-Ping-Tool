package aor.PingTool;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;

public class PingToolPlayerListener implements Listener {	
	public static PingTool plugin;
	public static List<Location> replacedBlocks=new ArrayList<Location>();
	//public static List<BlockState> replacedBlocksState=new ArrayList<BlockState>();

	public PingToolPlayerListener(PingTool instance) {
		plugin = instance;
	}

	@Deprecated
	@SuppressWarnings("all")
	public static void replaceBlock(){
		//put the block back in world
		BlockState blockState = null;//replacedBlocksState.remove(0);
		if(!replacedBlocks.contains(replacedBlocks.remove(0))){
			blockState.update(true); //forces existing block to become the block represented by blockState
		}
	}
	
	public static void updateBlock() {
		Location blockLoc = replacedBlocks.remove(0);
		if(!replacedBlocks.contains(blockLoc)){
			blockLoc.getBlock().getState().update();
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		MaterialData itemInHand = player.getItemInHand().getData();
		// Left clicking air or a block event:
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && itemInHand.getItemType() == Material.INK_SACK) // If they right clicked with dye.
		{
			Location targetBlock = player.getTargetBlock(null, 1000).getLocation(); // Select the target block.
			if (targetBlock.getBlock().getType() != Material.AIR) // No pinging midair!
			{
				event.setCancelled(true);
				if(!replacedBlocks.contains(targetBlock)){
					replacedBlocks.add(targetBlock);//store the block location
					//replacedBlocksState.add(targetBlock.getState());
				}
				else{
					int replacedBlockIndex = replacedBlocks.indexOf(targetBlock);
					replacedBlocks.add(replacedBlocks.get(replacedBlockIndex));//store the block
					//replacedBlocksState.add(replacedBlocksState.get(replacedBlockIndex));
				}
				for(Player p : plugin.getServer().getOnlinePlayers()) {
					p.sendBlockChange(targetBlock, Material.WOOL, ((Dye)itemInHand).getColor().getWoolData()); // Turn it to the wool player was holding!
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							updateBlock();
						}
					}, 20L);
				}
			}
		}
	}
}