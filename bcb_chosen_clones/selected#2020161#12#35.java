    public void playClip(String clip) {
        if (clip != null && !clip.equals("")) {
            logger.info("Playing clip: " + clip);
            try {
                File soundFile = new File(clip);
                URL clipURL = soundFile.toURI().toURL();
                final Clip clickClip = AudioSystem.getClip();
                clickClip.addLineListener(new LineListener() {

                    public void update(LineEvent evt) {
                        if (evt.getType() == LineEvent.Type.STOP) {
                            clickClip.close();
                        }
                    }
                });
                AudioInputStream ais = AudioSystem.getAudioInputStream(clipURL);
                clickClip.open(ais);
                clickClip.start();
            } catch (Exception e) {
                System.err.println("There was a problem playing the file:" + clip);
                e.printStackTrace();
            }
        }
    }
