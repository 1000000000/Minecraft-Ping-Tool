package aor.PingTool;

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
	public static final boolean ALLOW_PING_MIDAIR = true;
	//public static List<BlockState> replacedBlocksState=new ArrayList<BlockState>();

	public PingToolPlayerListener(PingTool instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		MaterialData itemInHand = player.getItemInHand().getData();
		// Right clicking air or a block event:
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && itemInHand.getItemType() == Material.INK_SACK) // If they left clicked with dye.
		{
			Location targetBlock = player.getTargetBlock(null, 1000).getLocation(); // Select the target block.
			//player.sendMessage("Pinging " + targetBlock);
			if (targetBlock.getBlock().getType() == Material.AIR) {
				targetBlock = player.getTargetBlock(null, 10).getLocation();
			}
			if (targetBlock.getBlock().getType() != Material.AIR || ALLOW_PING_MIDAIR) // No pinging midair! (unless you can ping midair)
			{
				event.setCancelled(true);
				plugin.pingBlock(plugin.getServer().getOnlinePlayers(), targetBlock, Material.WOOL, ((Dye)itemInHand).getColor().getWoolData(),  20L);
			}
		}
	}
}
