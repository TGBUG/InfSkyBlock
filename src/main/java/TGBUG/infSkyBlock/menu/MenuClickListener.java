package TGBUG.infSkyBlock.menu;

import TGBUG.infSkyBlock.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;

/**
 * 监听玩家点击菜单并执行配置动作。
 */
public class MenuClickListener implements Listener {

    private final ConfigManager cfm;

    public MenuClickListener(ConfigManager cfm) {
        this.cfm = cfm;
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Inventory inv = event.getClickedInventory();
        if (inv == null || event.getSlotType() != InventoryType.SlotType.CONTAINER) return;

        // 判断是否为自定义菜单
        String title = ChatColor.stripColor(event.getView().getTitle());
        Object mainMenuObj = cfm.getConfig("menus.yml", "main");
        if (!(mainMenuObj instanceof Map<?, ?> mainMenu)) return;

        String configTitle = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',
                Objects.toString(mainMenu.get("title"), "空岛菜单")));

        // 若标题匹配，认为是我们的菜单
        if (!title.equals(configTitle)) return;

        event.setCancelled(true); // 防止玩家拿走物品
        int slot = event.getRawSlot();

        // 从配置中读取该槽位定义
        Object itemsObj = mainMenu.get("items");
        if (!(itemsObj instanceof Map<?, ?> itemsMap)) return;
        Object itemObj = itemsMap.get(String.valueOf(slot));
        if (!(itemObj instanceof Map<?, ?> itemData)) return;

        // 处理占位符点击
        if (itemData.containsKey("placeholder")) {
            String placeholder = Objects.toString(itemData.get("placeholder"));
            PlaceholderMenuHandler.handlePlaceholderClick(player, placeholder);
            return;
        }

        // 处理命令点击
        if (itemData.containsKey("command")) {
            String cmd = Objects.toString(itemData.get("command")).trim();
            if (cmd.equalsIgnoreCase("menu close")) {
                player.closeInventory();
                return;
            }

            if (!cmd.isEmpty()) {
                // 支持执行玩家命令
                player.closeInventory();
                Bukkit.getScheduler().runTaskLater( // 异步执行以防止冲突
                        Bukkit.getPluginManager().getPlugin("YourPluginName"),
                        () -> player.performCommand(cmd),
                        1L
                );
            }
        }
    }

    /**
     * 可选：监听关闭菜单事件（预留扩展）
     */
    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        // 将来可在此处理菜单清理逻辑，例如刷新缓存、释放资源等
    }
}
