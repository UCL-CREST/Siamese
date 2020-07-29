    public static void main(String[] args) throws Exception {
        Clip clickClip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(ClassLoader.getSystemResourceAsStream("org/evertree/lettres/resource/wordfound.wav"));
        clickClip.open(ais);
        clickClip.start();
        Thread.sleep(5000);
    }
