            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    clip.addLineListener(new LineListener() {

                        @Override
                        public void update(LineEvent event) {
                            if (event.getType() == LineEvent.Type.STOP) {
                                event.getLine().close();
                            }
                        }
                    });
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(Utils.class.getResourceAsStream("/sounds/doorbell.wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    log.error(null, e);
                }
            }
