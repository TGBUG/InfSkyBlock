package TGBUG.infSkyBlock.data;

import TGBUG.infSkyBlock.ConfigManager;
import TGBUG.infSkyBlock.islandsGenerator.IslandGeneratorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IslandDataManager {

    private final File file;
    private final FileConfiguration config;
    private final Map<String, Location> islandCenters = new HashMap<>();
    private final ConfigManager cfm;

    public IslandDataManager(ConfigManager cfm) {
        this.cfm = cfm;
        this.file = cfm.getConfigFile("islands.yml");

        this.config = YamlConfiguration.loadConfiguration(file);
        loadIslands();
    }

    /**
     * 从 islands.yml 加载岛屿中心数据到内存
     */
    public void loadIslands() {
        islandCenters.clear();

        if (!config.contains("islands")) return;

        for (String key : config.getConfigurationSection("islands").getKeys(false)) {
            try {
                String worldName = config.getString("islands." + key + ".world");
                double x = config.getDouble("islands." + key + ".x");
                double y = config.getDouble("islands." + key + ".y");
                double z = config.getDouble("islands." + key + ".z");

                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    Bukkit.getLogger().warning("跳过岛屿 " + key + "，世界 " + worldName + " 不存在！");
                    continue;
                }

                Location loc = new Location(world, x, y, z);
                islandCenters.put(key, loc);

            } catch (Exception e) {
                Bukkit.getLogger().warning("载入岛屿数据时出现错误: " + key);
                e.printStackTrace();
            }
        }

        Bukkit.getLogger().info("成功加载岛屿数据: " + islandCenters.size() + " 个岛屿");
    }

    /**
     * 保存内存中的岛屿中心数据到 islands.yml
     */
    public synchronized void saveIslands() {
        config.set("islands", null); // 清空旧数据

        for (Map.Entry<String, Location> entry : islandCenters.entrySet()) {
            String playerName = entry.getKey();
            Location loc = entry.getValue();

            String path = "islands." + playerName;
            config.set(path + ".world", loc.getWorld().getName());
            config.set(path + ".x", loc.getX());
            config.set(path + ".y", loc.getY());
            config.set(path + ".z", loc.getZ());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("保存 islands.yml 时出现错误！");
            e.printStackTrace();
        }
    }

    /**
     * 添加新的岛屿并自动保存
     */
    public void addIsland(CommandSender player) {
        islandCenters.put(
            player.getName(),
            IslandGeneratorUtil.findAvailableIslandCenter(
                ((Player)player).getWorld(),
                ((int)cfm.getConfig("config.yml", "IsLandProtectionRage")),
                getIslandCenters()
            )
        );
        saveIslands();
    }

    /**
     * 获取所有岛屿中心
     */
    public Map<String, Location> getIslandCenters() {
        return islandCenters;
    }

    /**
     * 获取指定玩家的岛屿中心
     */
    public Location getIslandCenter(String playerName) {
        return islandCenters.get(playerName);
    }

    /**
     * 删除某个岛屿数据
     */
    public void removeIsland(String playerName) {
        islandCenters.remove(playerName);
        saveIslands();
    }
}