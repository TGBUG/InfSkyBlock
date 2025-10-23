package TGBUG.infSkyBlock.command;

import TGBUG.infSkyBlock.InfSkyBlock;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private final InfSkyBlock main;

    public TabCompleter(InfSkyBlock main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        return List.of();
    }
}
