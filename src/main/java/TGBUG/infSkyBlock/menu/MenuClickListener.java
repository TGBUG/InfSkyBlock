package TGBUG.infSkyBlock.menu;

import TGBUG.infSkyBlock.ConfigManager;
import TGBUG.infSkyBlock.InfSkyBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;
import java.util.Objects;

public class MenuClickListener implements Listener {

    private final InfSkyBlock main;

    public MenuClickListener(InfSkyBlock main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (e.getClickedInventory() == null) return;

        String menuId = MenuManager.getCurrentMenu(player);
        if (menuId == null) return; // 玩家当前没打开菜单

        e.setCancelled(true);

        Object menuObj = main.getConfigManager().getConfig("menus.yml", menuId);
        if (!(menuObj instanceof Map<?, ?> menu)) return;

        Object itemsObj = menu.get("items");
        if (!(itemsObj instanceof Map<?, ?> itemsMap)) return;

        int slot = e.getSlot();
        Object itemObj = itemsMap.get(slot);
        if (!(itemObj instanceof Map<?, ?> itemData)) return;

        // 如果有 placeholder，则交由占位符解释器处理点击
        if (itemData.containsKey("placeholder")) {
            String placeholder = Objects.toString(itemData.get("placeholder"));
            PlaceholderMenuHandler.handleClick(player, placeholder);
            return;
        }

        // 如果有 command 配置
        if (itemData.containsKey("command")) {
            Object cmdObj = itemData.get("command");
            if (cmdObj instanceof String str) {
                if (str.equalsIgnoreCase("menu close")) {
                    player.closeInventory();
                    return;
                }
                player.performCommand(str.replace("%player%", player.getName()));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player player) {
            MenuManager.clearCurrentMenu(player);
        }
    }
}
