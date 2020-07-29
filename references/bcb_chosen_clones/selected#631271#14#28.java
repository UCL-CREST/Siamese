    public static void play(final InputStream in) {
        threadPool.execute(new Runnable() {

            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream ais = AudioSystem.getAudioInputStream(in);
                    clip.open(ais);
                    clip.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
