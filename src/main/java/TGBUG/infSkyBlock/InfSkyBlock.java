package TGBUG.infSkyBlock;

import TGBUG.infSkyBlock.command.Executor;
import TGBUG.infSkyBlock.command.TabCompleter;
import TGBUG.infSkyBlock.data.IslandDataManager;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class InfSkyBlock extends JavaPlugin {
    private ConfigManager c;
    private IslandDataManager isdm;

    @Override
    public void onEnable() {
        initConfigManager();

        initIslands(c);

        initCommands(isdm);

        getLogger().info("InfSkyBlock Test version is enabled");
    }

    @Override
    public void onDisable() {
        if (isdm!= null) {
            isdm.saveIslands();
        }

        getLogger().info("InfSkyBlock Test version is disabled");
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidWorldGenerator(
                (int)c.getConfig("config.yml", "SpawnX"),
                (int)c.getConfig("config.yml", "SpawnY"),
                (int)c.getConfig("config.yml", "SpawnZ")
        );
    }

    private void initConfigManager() {
        c = new ConfigManager(this);
        try {
            c.loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initCommands(IslandDataManager isdm) {
        getCommand("infskyblock").setExecutor(new Executor(this, isdm));
        getCommand("infskyblock").setTabCompleter(new TabCompleter(this));
    }

    private void initIslands(ConfigManager c) {
        isdm = new IslandDataManager(c);
    }
}
