package TGBUG.infSkyBlock.menu;

import TGBUG.infSkyBlock.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 菜单管理器 - 统一控制菜单打开与解析逻辑
 */
public class MenuManager {

    private final ConfigManager cfm;
    private final Map<String, BaseMenu> menuInstances = new HashMap<>();

    public MenuManager(ConfigManager cfm) {
        this.cfm = cfm;
    }

    /**
     * 打开指定菜单
     */
    public void openMenu(String menuId, Player player) {
        BaseMenu menu = getOrCreate(menuId);
        if (menu == null) {
            player.sendMessage("§c菜单未找到: " + menuId);
            return;
        }

        // 打开前刷新占位符（可拓展为更新物品状态）
        menu.open(player);
    }

    /**
     * 从缓存中获取菜单实例，若不存在则构建
     */
    private BaseMenu getOrCreate(String id) {
        if (menuInstances.containsKey(id)) return menuInstances.get(id);

        Object obj = cfm.getConfig("menus.yml", id);
        if (obj == null) return null;

        // ✅ 此处可按ID选择不同子类
        BaseMenu menu;
        switch (id.toLowerCase()) {
            case "main" -> menu = new MainMenu(cfm);
            default -> menu = new GenericMenu(id, cfm);
        }

        menuInstances.put(id, menu);
        return menu;
    }

    /**
     * 刷新单个菜单实例（可在 /menu reload 时调用）
     */
    public void refresh(String id) {
        menuInstances.remove(id);
    }

    /**
     * 刷新全部菜单实例
     */
    public void refreshAll() {
        menuInstances.clear();
    }
}
