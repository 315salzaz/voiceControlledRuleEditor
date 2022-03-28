package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;

public class ThingUtils {
    private static ThingRegistry thingRegistry;

    public static void Prepare(ThingRegistry thingRegistry) {
        if (ThingUtils.thingRegistry == null)
            ThingUtils.thingRegistry = thingRegistry;
    }

    public static Thing getThingFromLabel(String label) {
        var thingsList = thingRegistry.getAll();
        Thing thing = thingsList.stream()
                .filter(t -> StringUtils.withoutSpaces(t.getLabel()).equalsIgnoreCase(StringUtils.withoutSpaces(label)))
                .findFirst().orElse(null);

        return thing;
    }

    // 315salzaz to be used with reader
    public static String[] getAllLabels() {
        var thingsList = thingRegistry.getAll();
        return thingsList.stream().map(t -> t.getLabel()).toArray(String[]::new);
    }
}
