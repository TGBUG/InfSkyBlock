package TGBUG.infSkyBlock.menu;

import TGBUG.infSkyBlock.ConfigManager;
import TGBUG.infSkyBlock.InfSkyBlock;
import org.bukkit.entity.Player;
import java.util.*;

public class MenuManager {
    private final InfSkyBlock main;
    private final Map<String, BaseMenu> menuInstances = new HashMap<>();

    private static final Map<String, String> playerMenuMap = new HashMap<>();

    public MenuManager(InfSkyBlock main) {
        this.main = main;
    }

    /** 记录玩家当前打开的菜单 */
    public static void setCurrentMenu(Player player, String id) {
        playerMenuMap.put(player.getName(), id);
    }

    /** 获取玩家当前打开的菜单ID */
    public static String getCurrentMenu(Player player) {
        return playerMenuMap.get(player.getName());
    }

    /** 清除玩家菜单记录 */
    public static void clearCurrentMenu(Player player) {
        playerMenuMap.remove(player.getName());
    }

    /** 打开菜单 */
    public void openMenu(String menuId, Player player) {
        BaseMenu menu = getOrCreate(menuId);
        if (menu == null) {
            player.sendMessage("§c菜单未找到: " + menuId);
            return;
        }
        menu.open(player);
    }

    private BaseMenu getOrCreate(String id) {
        ConfigManager cfm = main.getConfigManager();

        if (menuInstances.containsKey(id)) return menuInstances.get(id);

        Object obj = cfm.getConfig("menus.yml", id);
        if (obj == null) return null;

        BaseMenu menu;
        switch (id.toLowerCase()) {
            case "main" -> menu = new MainMenu(cfm);
            default -> menu = new GenericMenu(id, cfm);
        }

        menuInstances.put(id, menu);
        return menu;
    }

    public void refresh(String id) { menuInstances.remove(id); }
    public void refreshAll() { menuInstances.clear(); }
}
