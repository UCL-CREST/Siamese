    @Override
    public void run() {
        if (soundQueueIndex > 0) {
            decreaseSoundQueue();
            try {
                Clip audioClip = AudioSystem.getClip();
                String filePath = getAudioFile(soundQueue[soundQueueIndex]);
                if (filePath == null) {
                    System.out.println("Null detected @ soundplayer");
                }
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(super.getClass().getResourceAsStream(filePath));
                audioClip.open(audioStream);
                audioClip.start();
                while (audioClip.getFramePosition() < audioClip.getFrameLength()) {
                    Thread.sleep(500);
                }
                audioStream.close();
                audioClip.drain();
                audioClip.flush();
                audioClip.close();
                audioClip = null;
                audioStream = null;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
