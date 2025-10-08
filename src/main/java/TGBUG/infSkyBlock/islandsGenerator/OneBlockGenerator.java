package TGBUG.infSkyBlock.islandsGenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class OneBlockGenerator {
    /**
     * 在指定位置创建一个单方块岛屿并将玩家传送上去
     *
     * @param player  玩家
     * @param center  岛屿中心坐标
     */
    public static void createSingleBlockIsland(Player player, Location center) {
        if (player == null || center == null || center.getWorld() == null) {
            throw new IllegalArgumentException("Player 或 Center 位置无效！");
        }

        center.getBlock().setType(Material.GRASS_BLOCK);

        player.teleport(center.add(0, 1, 0));

        player.sendMessage("🌱 你的岛屿已创建! 好好生存吧!");
    }
}
