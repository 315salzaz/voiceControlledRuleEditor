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
package org.openhab.binding.voicecontrolledruleeditor.internal;

import static org.openhab.binding.voicecontrolledruleeditor.internal.VoiceControlledRuleEditorBindingConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.voicecontrolledruleeditor.internal.commandHandlers.UsersCommandHandler;
import org.openhab.binding.voicecontrolledruleeditor.internal.constants.TTSConstants;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.AudioManagerUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.ItemUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.RuleManagerUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.RuleRegistryUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.ThingUtils;
import org.openhab.binding.voicecontrolledruleeditor.internal.utils.VoiceManagerUtils;
import org.openhab.core.audio.AudioManager;
import org.openhab.core.automation.RuleManager;
import org.openhab.core.automation.RuleRegistry;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.voice.VoiceManager;

/**
 * The {@link VoiceControlledRuleEditorHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Povilas Grusas - Initial contribution
 */
@NonNullByDefault
public class VoiceControlledRuleEditorHandler extends BaseThingHandler {
    private @Nullable VoiceControlledRuleEditorConfiguration config;
    private RuleRegistry ruleRegistry;

    public VoiceControlledRuleEditorHandler(Thing thing, VoiceManager voiceManager, RuleRegistry ruleRegistry,
            ItemRegistry itemRegistry, ThingRegistry thingRegistry, AudioManager audioManager,
            RuleManager ruleManager) {
        super(thing);

        this.ruleRegistry = ruleRegistry;
        RuleRegistryUtils.Prepare(ruleRegistry);
        VoiceManagerUtils.Prepare(voiceManager);
        AudioManagerUtils.Prepare(audioManager);
        ThingUtils.Prepare(thingRegistry);
        ItemUtils.Prepare(itemRegistry);
        RuleManagerUtils.Prepare(ruleManager);
    }

    private class IdentifiedCommand {
        public String commandString = "";
        public String deviceIdentifier = "";

        public IdentifiedCommand(Command command) {
            if (!command.toString().contains("|"))
                return;

            var commandStringSplit = command.toString().split("\\|");
            deviceIdentifier = commandStringSplit[0].toLowerCase();
            commandString = commandStringSplit[1].toLowerCase();
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (CHANNEL_1.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }

            if (command.toString().contains("fail") || command.toString().contains("safe")) {
                VoiceManagerUtils.say("Fail safe activated");
                UsersCommandHandler.activateFailSafe();
                return;
            }

            var identifiedCommand = new IdentifiedCommand(command);
            if (identifiedCommand.deviceIdentifier.isEmpty()) {
                VoiceManagerUtils.say(TTSConstants.ERROR_NO_IDENTIFIER);
                return;
            }

            UsersCommandHandler.handleUserCommand(identifiedCommand.commandString, identifiedCommand.deviceIdentifier,
                    ruleRegistry);

            // TODO: handle command

            // Note: if communication with thing fails for some reason,
            // indicate that by setting the status with detail information:
            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
            // "Could not control device at IP address x.x.x.x");
        }
    }

    @Override
    public void initialize() {
        config = getConfigAs(VoiceControlledRuleEditorConfiguration.class);

        // TODO: Initialize the handler.
        // The framework requires you to return from this method quickly, i.e. any network access must be done in
        // the background initialization below.
        // Also, before leaving this method a thing status from one of ONLINE, OFFLINE or UNKNOWN must be set. This
        // might already be the real thing status in case you can decide it directly.
        // In case you can not decide the thing status directly (e.g. for long running connection handshake using WAN
        // access or similar) you should set status UNKNOWN here and then decide the real status asynchronously in the
        // background.

        // set the thing status to UNKNOWN temporarily and let the background task decide for the real status.
        // the framework is then able to reuse the resources from the thing handler initialization.
        // we set this upfront to reliably check status updates in unit tests.
        updateStatus(ThingStatus.UNKNOWN);

        // Example for background initialization:
        scheduler.execute(() -> {
            boolean thingReachable = true; // <background task with long running initialization here>
            // when done do:
            if (thingReachable) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE);
            }
        });

        // These logging types should be primarily used by bindings
        // logger.trace("Example trace message");
        // logger.debug("Example debug message");
        // logger.warn("Example warn message");
        //
        // Logging to INFO should be avoided normally.
        // See https://www.openhab.org/docs/developer/guidelines.html#f-logging

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }
}
