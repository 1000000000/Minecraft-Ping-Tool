package aor.PingTool; //Your package

import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/* Example Template
 * By Adamki11s
 * HUGE Plugin Tutorial
 */


public class PingTool extends JavaPlugin {
	
	public static final List<Byte> UNPINGABLE_BLOCKS = Arrays.asList(new Byte[]{
		0, //air
		8, //water source
		9, //water flow
		10, //lava source
		11, //lava flow
	});

    //ClassListeners
	private final PingToolPlayerListener playerListener = new PingToolPlayerListener(this);
    //ClassListeners

	public void onDisable() {
		getLogger().info("Disabled message here, shown in console on startup");
	}

	public void onEnable() {
		getLogger().info("Ping Tool .5 is enabled!");
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
	}
	
}