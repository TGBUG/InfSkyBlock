package TGBUG.infSkyBlock;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class VoidWorldGenerator extends org.bukkit.generator.ChunkGenerator {
    private final int spawnX;
    private final int spawnY;
    private final int spawnZ;

    public VoidWorldGenerator(int spawnX, int spawnY, int spawnZ) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);
        return chunkData;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, spawnX, spawnY, spawnZ);
    }
}