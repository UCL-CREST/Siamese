    public void play(int a) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        if (a == 0) {
            soundFile = new File("sound\\title.wav");
        } else if (a == 1) {
            soundFile = new File("sound\\1.wav");
        } else if (a == 2) {
            soundFile = new File("sound\\2.wav");
        } else if (a == 3) {
            soundFile = new File("sound\\3.wav");
        }
        audioIn = AudioSystem.getAudioInputStream(soundFile);
        clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
