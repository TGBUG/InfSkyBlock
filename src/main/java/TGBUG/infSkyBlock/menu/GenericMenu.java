package TGBUG.infSkyBlock.menu;

import TGBUG.infSkyBlock.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenericMenu extends BaseMenu {

    private final ConfigManager cfm;

    public GenericMenu(String id, ConfigManager cfm) {
        super(id);
        this.cfm = cfm;
    }

    @Override
    public Inventory build(Player player) {
        Object obj = cfm.getConfig("menus.yml", id);
        if (!(obj instanceof Map<?, ?> menu)) {
            return Bukkit.createInventory(null, 9, "§c菜单加载失败: " + id);
        }

        String title = ChatColor.translateAlternateColorCodes('&', Objects.toString(menu.get("title"), "&7未命名菜单"));
        // 读取菜单大小
        Object sizeObj = menu.get("size");
        int size = 54;
        if (sizeObj instanceof Number n) size = n.intValue();
        Inventory inv = Bukkit.createInventory(null, size, title);

        Object itemsObj = menu.get("items");
        if (itemsObj instanceof Map<?, ?> itemsMap) {
            for (Map.Entry<?, ?> entry : itemsMap.entrySet()) {
                int slot;
                try {
                    slot = Integer.parseInt(entry.getKey().toString());
                } catch (NumberFormatException e) {
                    continue;
                }

                if (!(entry.getValue() instanceof Map<?, ?> itemData)) continue;

                // 🔹 占位符槽位
                if (itemData.containsKey("placeholder")) {
                    String placeholder = Objects.toString(itemData.get("placeholder"));
                    PlaceholderMenuHandler.handle(inv, slot, placeholder, player);
                    continue;
                }

                // 读取物品类型
                Object matObj = itemData.get("material");
                String matName = (matObj != null) ? matObj.toString() : "STONE";
                Material material = Material.matchMaterial(matName);
                if (material == null) material = Material.STONE;
                if (material == null) material = Material.STONE;

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();

                if (itemData.containsKey("name"))
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.toString(itemData.get("name"))));

                if (itemData.containsKey("lore")) {
                    Object loreObj = itemData.get("lore");
                    if (loreObj instanceof List<?> list) {
                        List<String> lore = list.stream()
                                .map(line -> ChatColor.translateAlternateColorCodes('&', Objects.toString(line)))
                                .toList();
                        meta.setLore(lore);
                    }
                }

                if (Boolean.TRUE.equals(itemData.get("enchanted"))) {
                    meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                item.setItemMeta(meta);
                inv.setItem(slot, item);
            }
        }

        // 播放打开音效
        if (menu.containsKey("open-sound")) {
            try {
                Sound sound = Sound.valueOf(Objects.toString(menu.get("open-sound")));
                player.playSound(player.getLocation(), sound, 1f, 1f);
            } catch (Exception ignored) {}
        }

        return inv;
    }
}
