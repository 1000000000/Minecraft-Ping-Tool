package aor.PingTool;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
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

	public static void replaceBlock(){
		Location pingLocation = replacedBlocks.remove(0);
		if(!replacedBlocks.contains(pingLocation)){
			pingLocation.getBlock().getState().update(false); //forces existing block to become the block represented by blockState
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
				pingBlock(plugin.getServer().getOnlinePlayers(), targetBlock, Material.WOOL, ((Dye)itemInHand).getColor().getWoolData(),  20L);
			}
		}
	}
	
	public void pingBlock(final Player[] hallucinators, final Location blockLocation, final Material material, final byte data, long fakeBlockTimeSpan) {
		fakeBlock(hallucinators, blockLocation, material, data);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				replaceBlock();
			}
		}, fakeBlockTimeSpan);
		if(!replacedBlocks.contains(blockLocation)){
			replacedBlocks.add(blockLocation);//store the block location
		}
		else{
			int replacedBlockIndex = replacedBlocks.indexOf(blockLocation);
			replacedBlocks.add(replacedBlocks.get(replacedBlockIndex));//store the block
		}
	}
	
	/**
	 * This causes a block that does not necessarily exist at
	 * blockLocation to seem to exist there by players in the
	 * array of players given until a block update occurs.
	 * 
	 * This differs from pingBlock() by not forcing an update
	 * after a certain amount of time has passed so a faked
	 * block can remain indefinitely
	 */
	public void fakeBlock(Player[] hallucinators, Location blockLocation, Material material, byte data) {
		for(Player p : hallucinators) {
			if(!p.isOnline()) {
				plugin.getLogger().fine("[PingPlugin] Trying to hallucinate to Player " + p + " who is not online");
				continue;
			}
			p.sendBlockChange(blockLocation, material, data); // Turn it to the given block
		}
	}
}
