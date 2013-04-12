package aor.PingTool;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
			return true;
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
				sender.sendMessage("Ping Coordinates must be integers");
				return true;
			}
		}else{
			pos = ((Player)sender).getTargetBlock(null, 1000).getLocation();
		}
		if(args.length == 1) {
			color = args[0];
		}
		byte woolData = 0;
		try {
			woolData = DyeColor.valueOf(color.toUpperCase()).getWoolData();
		} catch(IllegalArgumentException e) {
			sender.sendMessage("The color \"" + color + "\" does not exist");
			return true;
		}
		plugin.pingBlock(plugin.getServer().getOnlinePlayers(), pos, Material.WOOL, woolData,  20L);
		return true;
	}
}
