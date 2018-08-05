    public SoundTest() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("media\\REMINDER.WAV"));
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                try {
                    clip.start();
                    clip.drain();
                } finally {
                    clip.close();
                }
            } catch (LineUnavailableException ex) {
                Logger.getLogger(SoundTest.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ais.close();
            }
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(SoundTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SoundTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
