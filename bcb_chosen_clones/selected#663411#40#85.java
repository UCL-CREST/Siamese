    public void play(final String pathToSound) {
        Boolean isPlaying = soundsPlaying.get(pathToSound);
        if (isPlaying == null || !isPlaying) {
            soundsPlaying.put(pathToSound, true);
            try {
                File soundFile = new File(pathToSound);
                AudioFileFormat inFileFormat = AudioSystem.getAudioFileFormat(soundFile);
                AudioFileFormat.Type fileType = inFileFormat.getType();
                final AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
                if (AudioSystem.isFileTypeSupported(fileType, stream)) {
                    final Clip clip = AudioSystem.getClip();
                    clip.addLineListener(new LineListener() {

                        public void update(LineEvent arg0) {
                            LineEvent.Type type = arg0.getType();
                            if (type == LineEvent.Type.STOP || type == LineEvent.Type.CLOSE) {
                                try {
                                    soundsPlaying.put(pathToSound, false);
                                    clip.drain();
                                    clip.close();
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                } finally {
                                    try {
                                        clip.close();
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                    try {
                                        stream.close();
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    clip.open(stream);
                    clip.start();
                }
            } catch (Throwable t) {
                Raptor.getInstance().onError("Error playing sound " + pathToSound, t);
                soundsPlaying.put(pathToSound, false);
            }
        }
    }
