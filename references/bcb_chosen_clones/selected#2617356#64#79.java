    public static void playSound(String fileName) {
        Clip sound = null;
        if (sfx.containsKey(fileName)) {
            sound = sfx.get(fileName);
        } else {
            try {
                sound = AudioSystem.getClip();
                sound.open(AudioSystem.getAudioInputStream(loader.getFile(fileName)));
            } catch (Exception ex) {
                Log.warn("Error loading file: %s", ex);
            }
            sfx.put(fileName, sound);
        }
        if (!GameSettings.soundEnabled || sound == null) return;
        sound.start();
    }
