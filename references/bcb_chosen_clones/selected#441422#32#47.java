    @Override
    public void run() {
        try {
            if (clip[soundID] == null) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResource(Sounds.values()[soundID].getFilename()));
                clip[soundID] = AudioSystem.getClip();
                clip[soundID].open(stream);
                clip[soundID].loop(0);
            }
            clip[soundID].setMicrosecondPosition(0);
            clip[soundID].start();
            clip[soundID].drain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
