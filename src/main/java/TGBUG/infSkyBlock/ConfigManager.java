package TGBUG.infSkyBlock;

import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class ConfigManager {
    private final Plugin plugin;
    private Map<String, Map<String, Object>> configsMap = new HashMap<>();

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public Object getConfig(String file, String item) {
        return configsMap.get(file).get(item);
    }

    public File getConfigFile(String file) {
        return new File(plugin.getDataFolder(), file);
    }

    public void loadConfig() throws IOException {
        configsMap = new HashMap<>();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        File configFolder = new File(plugin.getDataFolder(), "configs");
        if (!configFolder.exists()) {
            configFolder.mkdir();
        }
        List<String> configList = new ArrayList<>();
        configList.add("config.yml");
        configList.add("menus.yml");
        for (String configFile : configList) {
            File f = new File(plugin.getDataFolder(), configFile);
            if (!f.exists()) {
                plugin.saveResource(configFile, false);
            }
            Yaml yaml = new Yaml();
            Map<String, Object> configMap = yaml.load(new FileInputStream(f));
            configsMap.put(configFile, configMap);
        }

        File f = new File(plugin.getDataFolder(), "islands.yml");
        if (!f.exists()) {
            plugin.saveResource("islands.yml", false);
        }
    }
}
