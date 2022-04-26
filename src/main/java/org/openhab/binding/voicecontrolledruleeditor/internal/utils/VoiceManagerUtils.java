package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

import org.openhab.core.voice.VoiceManager;

public class VoiceManagerUtils {
    private static VoiceManager voiceManager;

    public static void Prepare(VoiceManager voiceManager) {
        if (VoiceManagerUtils.voiceManager == null)
            VoiceManagerUtils.voiceManager = voiceManager;
    }

    public static void say(String text) {
        voiceManager.say(text);
    }
}
