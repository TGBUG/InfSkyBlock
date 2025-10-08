package TGBUG.infSkyBlock.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * 所有菜单的基础类
 */
public abstract class BaseMenu {
    protected final String id;

    public BaseMenu(String id) {
        this.id = id;
    }

    /** 获取菜单唯一ID */
    public String getId() {
        return id;
    }

    /** 构建菜单（由子类实现） */
    public abstract Inventory build(Player player);

    /** 打开菜单 */
    public void open(Player player) {
        Inventory inv = build(player);
        player.openInventory(inv);
    }
}
