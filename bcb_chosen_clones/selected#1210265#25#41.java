    public void playSound(String name) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getResource(name));
            AudioFormat af = ais.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, af.getSampleRate(), 16, af.getChannels(), af.getChannels() * 2, af.getSampleRate(), false);
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodedFormat, ais);
            Clip c = AudioSystem.getClip();
            c.open(dais);
            c.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
