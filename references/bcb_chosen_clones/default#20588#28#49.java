    public static synchronized void playSound(final String soundFile) {
        Thread soundThread = new Thread(new Runnable() {

            public void run() {
                try {
                    String url = Config.SOUNDS_DIR + soundFile;
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
                    Clip clip = AudioSystem.getClip();
                    clip.open(inputStream);
                    clip.stop();
                    clip.setFramePosition(0);
                    clip.start();
                    url = null;
                    inputStream = null;
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        soundThread.setName("Sound: " + soundFile);
        soundThread.start();
    }
