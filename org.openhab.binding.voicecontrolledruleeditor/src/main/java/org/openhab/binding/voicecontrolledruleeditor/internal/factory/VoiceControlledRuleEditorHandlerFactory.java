/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.voicecontrolledruleeditor.internal.factory;

import static org.openhab.binding.voicecontrolledruleeditor.internal.VoiceControlledRuleEditorBindingConstants.*;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.voicecontrolledruleeditor.internal.VoiceControlledRuleEditorHandler;
import org.openhab.core.audio.AudioManager;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.openhab.core.voice.VoiceManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link VoiceControlledRuleEditorHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Povilas Grusas - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.voicecontrolledruleeditor", service = ThingHandlerFactory.class)
public class VoiceControlledRuleEditorHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(THING_TYPE_SAMPLE);
    private final VoiceManager voiceManager;
    private final RuleRegistry ruleRegistry;
    private final ThingRegistry thingRegistry;
    private final ItemRegistry itemRegistry;
    private final AudioManager audioManager;

    @Activate
    public VoiceControlledRuleEditorHandlerFactory(@Reference VoiceManager voiceManager,
            @Reference RuleRegistry ruleRegistry, @Reference ThingRegistry thingRegistry,
            @Reference ItemRegistry itemRegistry, @Reference AudioManager audioManager) {
        this.voiceManager = voiceManager;
        this.ruleRegistry = ruleRegistry;
        this.thingRegistry = thingRegistry;
        this.itemRegistry = itemRegistry;
        this.audioManager = audioManager;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_SAMPLE.equals(thingTypeUID)) {
            return new VoiceControlledRuleEditorHandler(thing, voiceManager, ruleRegistry, itemRegistry, thingRegistry,
                    audioManager);
        }

        return null;
    }
}
