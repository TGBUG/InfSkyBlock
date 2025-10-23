package TGBUG.infSkyBlock.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class PlaceholderHandler {
    private final BiFunction<Player, String, ItemStack> displayFunction;
    private final BiConsumer<Player, String> clickFunction;

    public PlaceholderHandler(BiFunction<Player, String, ItemStack> displayFunction,
                              BiConsumer<Player, String> clickFunction) {
        this.displayFunction = displayFunction;
        this.clickFunction = clickFunction;
    }

    public ItemStack getDisplay(Player player, String placeholder) {
        return displayFunction.apply(player, placeholder);
    }

    public void onClick(Player player, String placeholder) {
        clickFunction.accept(player, placeholder);
    }
}
