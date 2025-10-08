package TGBUG.infSkyBlock;

import TGBUG.infSkyBlock.command.Executor;
import TGBUG.infSkyBlock.command.TabCompleter;
import TGBUG.infSkyBlock.islandsGenerator.IslandDataManager;
import TGBUG.infSkyBlock.menu.MenuClickListener;
import TGBUG.infSkyBlock.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class InfSkyBlock extends JavaPlugin {
    private ConfigManager c;
    private IslandDataManager isdm;
    private MenuManager mm;

    @Override
    public void onEnable() {
        initConfigManager();

        initIslands(c);

        initMenus(c);

        initCommands(isdm, mm);

        initEventHandlers(c);

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

    private void initCommands(IslandDataManager isdm, MenuManager mm) {
        getCommand("infskyblock").setExecutor(new Executor(this, isdm, mm));
        getCommand("infskyblock").setTabCompleter(new TabCompleter(this));
    }

    private void initIslands(ConfigManager c) {
        isdm = new IslandDataManager(c);
    }

    private void initEventHandlers(ConfigManager c) {
        Bukkit.getPluginManager().registerEvents(new MenuClickListener(c), this);
    }

    private void initMenus(ConfigManager c) {
        mm = new MenuManager(c);
    }
//
//    public MenuManager getMenuManager() {
//        return mm;
//    }
//
//    public ConfigManager getConfigManager() {
//        return c;
//    }
//
//    public IslandDataManager getIslandDataManager() {
//        return isdm;
//    }
}
