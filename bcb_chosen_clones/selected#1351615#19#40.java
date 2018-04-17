    private synchronized void doPlay(Sound sound) {
        if (!soundAvailable) return;
        try {
            Clip clip = clips.get(sound);
            if (clip == null) {
                URL url = getClass().getResource("resources/" + sound.getFile());
                if (url != null) {
                    AudioInputStream is = AudioSystem.getAudioInputStream(url);
                    clip = AudioSystem.getClip();
                    clips.put(sound, clip);
                    clip.open(is);
                }
            }
            if (clip != null) {
                clip.setFramePosition(0);
                clip.start();
            }
        } catch (Exception ex) {
            soundAvailable = false;
            Log.error(ex);
        }
    }
