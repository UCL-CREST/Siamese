        public void run() {
            try {
                clip = AudioSystem.getClip();
                InputStream is;
                if (data != null) is = new ByteArrayInputStream(data); else is = fileName.getInputStream();
                while (!bExit) {
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(is);
                    clip.open(inputStream);
                    clip.start();
                    if (!loop) break;
                }
                clip.close();
                clip = null;
            } catch (Exception e) {
            }
            playSound.remove(this);
            thread = null;
        }
