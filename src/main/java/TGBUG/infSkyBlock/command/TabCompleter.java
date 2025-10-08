package TGBUG.infSkyBlock.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private final Plugin plugin;

    public TabCompleter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {




        return List.of();
    }
}
