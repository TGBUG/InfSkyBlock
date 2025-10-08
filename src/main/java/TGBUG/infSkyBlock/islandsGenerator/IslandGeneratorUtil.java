package TGBUG.infSkyBlock.islandsGenerator;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;
import java.util.Random;

public class IslandGeneratorUtil {

    private static final int MAX_ATTEMPTS = 5000;  //最大尝试次数
    private static final Random random = new Random();

    /**
     * 生成一个可用的岛屿中心坐标，保证与已有岛屿保持足够间隔。
     *
     * @param world            世界对象
     * @param spacing          岛屿之间的最小间隔距离（单位：方块）
     * @param existingIslands  已有岛屿的 Map<UUID, Location>
     * @return 可用的岛屿中心位置
     */
    public static Location findAvailableIslandCenter(World world, int spacing, Map<String, Location> existingIslands) {
        if (world == null) throw new IllegalArgumentException("World cannot be null");
        if (spacing <= 0) throw new IllegalArgumentException("Spacing must be positive");

        int attempt = 0;
        int range = Math.max(spacing * (existingIslands.size() + 1), spacing * 2);

        while (attempt < MAX_ATTEMPTS) {
            attempt++;

            // 随机生成坐标（分布在 ±range 区域）
            int x = random.nextInt(range * 2) - range;
            int z = random.nextInt(range * 2) - range;

            // 对齐坐标到 spacing 的倍数
            x = Math.round((float) x / spacing) * spacing;
            z = Math.round((float) z / spacing) * spacing;

            // 跳过过近的中心点
            if (!isLocationValid(x, z, spacing, existingIslands)) continue;

            return new Location(world, x + 0.5, 64, z + 0.5); // 中心点对齐
        }

        throw new IllegalStateException("无法在规定次数内找到可用的岛屿中心位置！");
    }

    /**
     * 检查给定坐标是否与所有已有岛屿保持足够间隔。
     */
    private static boolean isLocationValid(int x, int z, int spacing, Map<String, Location> existingIslands) {
        for (Location loc : existingIslands.values()) {
            if (loc == null || loc.getWorld() == null) continue;
            double dx = loc.getX() - x;
            double dz = loc.getZ() - z;
            double distanceSq = dx * dx + dz * dz;
            if (distanceSq < (spacing * spacing)) {
                return false; // 太近
            }
        }
        return true;
    }
}
