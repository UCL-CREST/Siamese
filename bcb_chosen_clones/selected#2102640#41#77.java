    public void Play(Type type) {
        AudioInputStream sound = null;
        switch(type) {
            case COLLISION:
                try {
                    sound = AudioSystem.getAudioInputStream(getURL("/Sons/abre_porta_1.wav"));
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case SHOT:
                try {
                    sound = AudioSystem.getAudioInputStream(getURL("/Sons/Hit.wav"));
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
        ;
        try {
            clip.open(sound);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clip.start();
    }
