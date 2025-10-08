package TGBUG.infSkyBlock.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 用于在菜单中处理占位符槽位的动态物品生成。
 * 支持注册自定义占位符解析器。
 */
public class PlaceholderMenuHandler {

    /**
     * 注册的占位符解析器
     * key = 占位符名称（不含%号）
     * value = 解析函数 (player, placeholderString) -> ItemStack
     */
    private static final Map<String, BiFunction<Player, String, ItemStack>> resolvers = new HashMap<>();

    /**
     * 注册一个占位符解析器
     * @param key 占位符名（例如 "island_config_1"）
     * @param handler 解析逻辑
     */
    public static void register(String key, BiFunction<Player, String, ItemStack> handler) {
        resolvers.put(key.toLowerCase(), handler);
    }

    /**
     * 处理占位符并填充到菜单槽位中
     */
    public static void handle(Inventory inv, int slot, String placeholder, Player player) {
        if (placeholder == null || placeholder.isEmpty()) return;

        // 去掉占位符两侧的 "%"
        String cleanKey = placeholder.replace("%", "").toLowerCase();

        // 如果存在对应解析器则调用
        BiFunction<Player, String, ItemStack> resolver = resolvers.get(cleanKey);
        if (resolver != null) {
            ItemStack item = resolver.apply(player, cleanKey);
            if (item != null) {
                inv.setItem(slot, item);
                return;
            }
        }

        // 未注册的占位符 -> 显示一个红色玻璃提示
        inv.setItem(slot, createPlaceholderErrorItem(placeholder));
    }

    /**
     * 演示解析器
     */
    public static void registerDefaults() {
        register("example_placeholder", (player, key) -> {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Hello, " + player.getName() + "!");
            meta.setLore(java.util.List.of(ChatColor.GRAY + "这是一个示例占位符: " + key));
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
            return item;
        });
    }

    /**
     * 当占位符无对应解析器时显示的默认物品
     */
    private static ItemStack createPlaceholderErrorItem(String placeholder) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "无效占位符");
        meta.setLore(java.util.List.of(ChatColor.GRAY + placeholder));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 当玩家点击带占位符的物品时调用。
     * 可以在此触发自定义逻辑，如打开子菜单、切换状态等。
     */
    public static void handlePlaceholderClick(Player player, String placeholder) {
        String cleanKey = placeholder.replace("%", "").toLowerCase();

        // 根据注册的解析器名称触发行为（可选扩展）
        if (cleanKey.startsWith("island_config_")) {
            player.sendMessage(ChatColor.YELLOW + "[菜单系统] 你点击了岛屿配置项: " + cleanKey);
            // TODO: 触发岛屿配置逻辑
            return;
        }

        player.sendMessage(ChatColor.RED + "该占位符未定义点击行为: " + placeholder);
    }
}
