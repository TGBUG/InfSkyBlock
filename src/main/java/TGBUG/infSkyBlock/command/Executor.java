package TGBUG.infSkyBlock.command;

import TGBUG.infSkyBlock.InfSkyBlock;
import TGBUG.infSkyBlock.islandsGenerator.IslandDataManager;
import TGBUG.infSkyBlock.islandsGenerator.OneBlockGenerator;
import TGBUG.infSkyBlock.menu.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Executor implements CommandExecutor {
    private final InfSkyBlock main;

    public Executor(InfSkyBlock main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MenuManager mm = main.getMenuManager();
        IslandDataManager isdm = main.getIslandDataManager();

        if (args.length == 0) {
            if (sender instanceof ConsoleCommandSender) {
                getLogger().info("§b--------InfSkyBlock--------\n§2运行状态: §a正常\n§6当前版本: Test");
            } else {
                mm.openMenu("main", (Player) sender);
            }
            return false;
        }

        switch (args[0]) {
            case "help":
                //TODO:帮助菜单
                return true;

            case "toisland":
                if (sender instanceof ConsoleCommandSender) {
                    getLogger().warning("只有玩家才能使用此指令!");
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

    private Logger getLogger() {
        return main.getLogger();
    }
}

