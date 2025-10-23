package TGBUG.infSkyBlock;

import TGBUG.infSkyBlock.command.Executor;
import TGBUG.infSkyBlock.command.TabCompleter;
import TGBUG.infSkyBlock.islandsGenerator.IslandDataManager;
import TGBUG.infSkyBlock.menu.MenuClickListener;
import TGBUG.infSkyBlock.menu.MenuManager;
import TGBUG.infSkyBlock.menu.PlaceholderMenuHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Set;

public final class InfSkyBlock extends JavaPlugin {
    private ConfigManager c;
    private IslandDataManager isdm;
    private MenuManager mm;

    @Override
    public void onEnable() {
        initConfigManager();

        initMenus();

        initEventHandlers();

        initCommands();

        //just for test
        PlaceholderMenuHandler.registerDefaults();

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

    private void initCommands() {
        getCommand("infskyblock").setExecutor(new Executor(this));
        getCommand("infskyblock").setTabCompleter(new TabCompleter(this));
    }

    private void initIslands() {
        isdm = new IslandDataManager(this);
    }

    private void initEventHandlers() {
        Bukkit.getPluginManager().registerEvents(new MenuClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new WorldloaderListener(this), this);
    }

    private void initMenus() {
        mm = new MenuManager(this);
    }

    private class WorldloaderListener implements Listener {
        private final InfSkyBlock plugin;

        private WorldloaderListener(InfSkyBlock plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onWorldLoad(WorldLoadEvent e) {
            if (Bukkit.getWorld("world") != null & Bukkit.getWorld("world_nether") != null & Bukkit.getWorld("world_the_end") != null) {
                plugin.getLogger().info("所有岛屿世界已加载完毕，开始加载岛屿数据...");
                Bukkit.getScheduler().runTask(plugin, InfSkyBlock.this::initIslands);
                HandlerList.unregisterAll(this);
            }
        }
    }

    public MenuManager getMenuManager() {
        return mm;
    }

    public ConfigManager getConfigManager() {
        return c;
    }

    public IslandDataManager getIslandDataManager() {
        return isdm;
    }
}
