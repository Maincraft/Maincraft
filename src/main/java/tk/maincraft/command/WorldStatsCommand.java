package tk.maincraft.command;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.cache.CacheStats;

import tk.maincraft.MainServer;
import tk.maincraft.world.MainWorld;

public class WorldStatsCommand extends MaincraftCommand {
    public WorldStatsCommand(MainServer server) {
        super("worldstats", "Displays stats about a world.", "/<command> [WORLD]",
                "maincraft.worldstats", server);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String commandLabel, String[] args) {
        MainWorld world;
        if (args.length == 0) {
            if (sender instanceof Player) {
                world = (MainWorld) ((Player) sender).getWorld();
            }
            else {
                sender.sendMessage(ChatColor.RED + "You have to specify a world from console!");
                return true;
            }
        }
        else if (args.length == 1) {
            World sWorld = server.getWorld(args[0]);
            if (sWorld == null) {
                sender.sendMessage(ChatColor.RED + "That world doesn't exist!");
            }
            world = (MainWorld) sWorld;
        }
        else {
            return false;
        }
        if (world == null)
            return false;
        
        CacheStats stats = world.getCacheStats();
        sender.sendMessage("Average time spent to create one chunk: " + (stats.averageLoadPenalty() / 1000000) + "ms");
        sender.sendMessage("Total time spent on creating chunks: " + (stats.totalLoadTime() / 1000000) + "ms");
        sender.sendMessage("Number of Chunk-Gens: " + stats.loadCount());
        sender.sendMessage("Number of automatic unloads: " + stats.evictionCount());
        sender.sendMessage("Number of Chunk-Accesses: " + stats.requestCount());
        sender.sendMessage("Cache miss-rate: " + stats.missRate());
        sender.sendMessage("Cache hit-rate: " + stats.hitRate());
        sender.sendMessage("Number of currently loaded chunks: " + world.getNumberOfLoadedChunks());
        //sender.sendMessage("");
        return true;
    }

}
