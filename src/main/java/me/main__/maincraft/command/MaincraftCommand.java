package me.main__.maincraft.command;

import me.main__.maincraft.MainServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public abstract class MaincraftCommand extends Command {
    protected final MainServer server;

    public MaincraftCommand(String name, String description, String usageMessage, String permission, MainServer server) {
        super(name);
        this.setDescription(description);
        this.setUsage(usageMessage);
        this.setPermission(permission);
        this.server = server;
    }

    public final MainServer getServer() {
        return server;
    }

    @Override
    public final boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }

        boolean result = false;
        try {
            result = executeCommand(sender, commandLabel, args);
        } catch (Throwable t) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel
                    + "'!", t);
        }

        if (!result && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }

        return result;
    }

    public abstract boolean executeCommand(CommandSender sender, String commandLabel, String[] args);
}
