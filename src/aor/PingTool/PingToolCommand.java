package aor.PingTool;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PingToolCommand implements CommandExecutor{
	private final PingTool plugin;
	
	public PingToolCommand(PingTool pt){
		plugin = pt;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String color = "white";
		Location pos;
		if (!(sender instanceof Player)) {
			if(args.length < 3) sender.sendMessage("/ping from the console must specify cordinates");
			return false;
		}
		if(args.length > 3) color = args[3];
		if(args.length > 2){
			try{
				World w = plugin.getServer().getWorlds().get(0);
				if(sender instanceof Player){
					w = ((Player)sender).getWorld();
				}
				int x = Integer.parseInt(args[0]);
				int y = Integer.parseInt(args[1]);
				int z = Integer.parseInt(args[2]);
				pos = new Location(w, x, y, z);
			}catch(NumberFormatException e){
				// cant parse cordinates
				return false;
			}
		}else{
			pos = ((Player)sender).getTargetBlock(null, 1000).getLocation();
		}
		// ping it
		return true;
	}
}
