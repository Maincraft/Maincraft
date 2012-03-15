package tk.maincraft.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import tk.maincraft.MainServer;

public class ConfigCommand extends MaincraftCommand {
    public ConfigCommand(MainServer server) {
        super("config", "Configure Maincraft.", "/<command> <property> <value>",
                "maincraft.config", server);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executeCommand(CommandSender sender, String commandLabel, String[] args) {
        if (args.length != 2)
            return false;

        String property = args[0];
        String value = args[1];

        if (server.setConfigProperty(property, value)) {
            sender.sendMessage(String.format("%sSuccessfully set '%s' to '%s'!", ChatColor.GREEN,
                    property, value));
        }
        else {
            sender.sendMessage(String.format("%sFailed to set '%s' to '%s' :(", ChatColor.RED,
                    property, value));
        }

        return true;
    }
}
