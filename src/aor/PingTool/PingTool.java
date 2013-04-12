package aor.PingTool; //Your package



import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/* Example Template
 * By Adamki11s
 * HUGE Plugin Tutorial
 */


public class PingTool extends JavaPlugin {

    //ClassListeners
	private final PingToolPlayerListener playerListener = new PingToolPlayerListener(this);
    //ClassListeners

	public static List<Location> replacedBlocks=new ArrayList<Location>();

	public void onDisable() {
		getLogger().info("Disabled message here, shown in console on startup");
	}

	public void onEnable() {
		getLogger().info("Ping Tool .5 is enabled!");
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
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
	
	public void pingBlock(final Player[] hallucinators, final Location blockLocation, final Material material, final byte data, long fakeBlockTimeSpan) {
		fakeBlock(hallucinators, blockLocation, material, data);
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
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
				getLogger().fine("[PingPlugin] Trying to hallucinate to Player " + p + " who is not online");
				continue;
			}
			p.sendBlockChange(blockLocation, material, data); // Turn it to the given block
		}
	}
	
}