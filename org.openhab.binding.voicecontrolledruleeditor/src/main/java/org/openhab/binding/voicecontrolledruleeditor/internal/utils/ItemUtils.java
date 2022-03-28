package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;

public class ItemUtils {
    private static ItemRegistry itemRegistry;

    public static void Prepare(ItemRegistry itemRegistry) {
        if (ItemUtils.itemRegistry == null)
            ItemUtils.itemRegistry = itemRegistry;
    }

    public static Item getItemFromLabel(String label) {
        var itemsList = itemRegistry.getAll();
        Item item = itemsList.stream()
                .filter(i -> StringUtils.withoutSpaces(i.getLabel()).equalsIgnoreCase(StringUtils.withoutSpaces(label)))
                .findFirst().orElse(null);

        return item;
    }

    public static Item getItemFromName(String name) {
        var itemsList = itemRegistry.getAll();
        Item item = itemsList.stream()
                .filter(i -> StringUtils.withoutSpaces(i.getName()).equalsIgnoreCase(StringUtils.withoutSpaces(name)))
                .findFirst().orElse(null);

        return item;
    }

    public static Item getItemFromNameOrLabel(String input) {
        var item = getItemFromName(input);
        if (item != null)
            return item;

        item = getItemFromLabel(input);
        if (item != null)
            return item;

        return null;
    }

    // 315salzaz to be used with reader
    public static String[] getAllLabels() {
        var itemsList = itemRegistry.getAll();
        return itemsList.stream().map(i -> i.getLabel()).toArray(String[]::new);
    }

    // 315salzaz to be used with reader
    public static String[] getAllNames() {
        var itemsList = itemRegistry.getAll();
        return itemsList.stream().map(i -> i.getName()).toArray(String[]::new);
    }
}
