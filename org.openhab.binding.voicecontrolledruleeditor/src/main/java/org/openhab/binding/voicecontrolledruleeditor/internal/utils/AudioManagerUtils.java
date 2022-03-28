package org.openhab.binding.voicecontrolledruleeditor.internal.utils;

import java.io.File;
import java.util.Set;
import java.util.stream.Stream;

import org.openhab.core.OpenHAB;
import org.openhab.core.audio.AudioManager;
import org.openhab.core.audio.AudioSink;

public class AudioManagerUtils {
    private static AudioManager audioManager;

    public static void Prepare(AudioManager audioManager) {
        if (AudioManagerUtils.audioManager == null)
            AudioManagerUtils.audioManager = audioManager;
    }

    public static AudioSink getDefaultSink() {
        return audioManager.getSink();
    }

    // Let's hope i never need this...
    public static Set<AudioSink> getAllSinks() {
        return audioManager.getAllSinks();
    }

    public static File[] getAudioFiles() {
        File folder = new File(OpenHAB.getConfigFolder() + File.separator + AudioManager.SOUND_DIR + File.separator);
        File[] filesList = Stream.of(folder.listFiles()).filter(fileName -> fileName.getName().endsWith(".mp3"))
                .toArray(File[]::new);

        return filesList;
    }
}
