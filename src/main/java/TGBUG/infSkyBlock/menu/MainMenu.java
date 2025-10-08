package TGBUG.infSkyBlock.menu;


import TGBUG.infSkyBlock.ConfigManager;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;

public class MainMenu extends GenericMenu {
    public MainMenu(ConfigManager cfm) {
        super("main", cfm);
    }

    @Override
    public Inventory build(Player player) {
        // 可在此为主菜单加入独特逻辑（如检测岛屿状态等）
        return super.build(player);
    }
}
