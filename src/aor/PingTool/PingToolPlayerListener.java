package aor.PingTool;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.*;
import java.util.*;
import org.bukkit.scheduler.*;

public class PingToolPlayerListener extends PlayerListener {	
	public static PingTool plugin;
	public static List<Block> replacedBlocks=new ArrayList<Block>();
	public static List<Material> replacedBlocksMaterial=new ArrayList<Material>();
	public PingToolPlayerListener(PingTool instance) {
		plugin = instance;
	}
	public static void replaceBlock(){
		//put the block back in world
		replacedBlocks.get(0).setType(replacedBlocksMaterial.get(0));
		replacedBlocks.remove(0);
		replacedBlocksMaterial.remove(0);
	}
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		// Left clicking air or a block event:
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.WOOL) // If they left clicked with wool.
		{
			Block targetBlock = player.getTargetBlock(null, 1000); // Select the target block.
			if (targetBlock.getType() != Material.AIR) // No pinging midair!
			{
				replacedBlocks.add(targetBlock);//store the block
				replacedBlocksMaterial.add(targetBlock.getType());
				targetBlock.setType(Material.WOOL); // Turn it to wool!
				scheduleAsyncDelayedTask(plugin,ResetBlock.class,8);
			}
		}
	}
}