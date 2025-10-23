package TGBUG.infSkyBlock.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import java.util.*;
import java.util.function.*;

/**
 * 统一的占位符解释与点击行为系统
 */
public class PlaceholderMenuHandler {

    // 注册占位符 → 对应的显示与点击处理器
    private static final Map<String, PlaceholderHandler> handlers = new HashMap<>();

    /**
     * 注册一个新的占位符
     * @param placeholder 占位符名称（例如 %island_config_1%）
     * @param displayFunction 构建显示物品的方法
     * @param clickFunction 点击该占位符物品时的行为
     */
    public static void register(String placeholder,
                                BiFunction<Player, String, ItemStack> displayFunction,
                                BiConsumer<Player, String> clickFunction) {
        handlers.put(placeholder.toLowerCase(), new PlaceholderHandler(displayFunction, clickFunction));
    }

    /**
     * 处理物品填充
     */
    public static void handle(Inventory inv, int slot, String placeholder, Player player) {
        PlaceholderHandler handler = handlers.get(placeholder.toLowerCase());
        if (handler == null) {
            // 未注册 → 显示默认占位物品
            inv.setItem(slot, createDefaultPlaceholderItem(placeholder));
            return;
        }
        inv.setItem(slot, handler.getDisplay(player, placeholder));
    }

    /**
     * 处理点击事件
     */
    public static void handleClick(Player player, String placeholder) {
        PlaceholderHandler handler = handlers.get(placeholder.toLowerCase());
        if (handler != null) {
            handler.onClick(player, placeholder);
        }
    }

    /**
     * 注册默认示例占位符
     */
    public static void registerDefaults() {
        register("%example_placeholder%", (player, ph) -> {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "示例占位符");
            meta.setLore(List.of(ChatColor.GRAY + "示例"));
            item.setItemMeta(meta);
            return item;
        }, (player, ph) -> {
            player.performCommand("say 114514");
        });
    }

    private static ItemStack createDefaultPlaceholderItem(String placeholder) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "未知占位符: " + placeholder);
        item.setItemMeta(meta);
        return item;
    }
}
