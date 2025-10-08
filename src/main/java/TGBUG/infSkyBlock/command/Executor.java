package TGBUG.infSkyBlock.command;

import TGBUG.infSkyBlock.data.IslandDataManager;
import TGBUG.infSkyBlock.islandsGenerator.OneBlockGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Executor implements CommandExecutor {
    private final Plugin plugin;
    private IslandDataManager isdm;

    public Executor(Plugin plugin, IslandDataManager isdm) {
        this.plugin = plugin;
        this.isdm = isdm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof ConsoleCommandSender) {
                plugin.getLogger().info("§b--------InfSkyBlock--------\n§2运行状态: §a正常\n§6当前版本: Test");
            } else {
                //TODO:打开菜单
            }
            return false;
        }

        switch (args[0]) {
            case "help":
                //TODO:帮助菜单
                return true;

            case "toisland":
                if (sender instanceof ConsoleCommandSender) {
                    plugin.getLogger().warning("只有玩家才能使用此指令!");
                } else {
                    if (isdm.getIslandCenter(sender.getName()) == null) {
                        isdm.addIsland(sender);
                        OneBlockGenerator.createSingleBlockIsland((Player) sender, isdm.getIslandCenter(sender.getName()));
                    } else {
                        ((Player) sender).teleport(isdm.getIslandCenter(sender.getName()).add(0, 1, 0));
                        sender.sendMessage("🌱 已传送到你的岛屿!");
                    }
                }

            default:
                return true;
        }
    }
}

