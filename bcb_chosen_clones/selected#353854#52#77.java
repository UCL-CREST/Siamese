    public void testSampledSupport() {
        try {
            Clip clip = AudioSystem.getClip();
            Assert.assertTrue("AndroidClip must exist", clip instanceof AndroidClip);
            Line.Info info = clip.getLineInfo();
            Assert.assertNotNull("Line info must exist", info);
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File("/system/media/audio/ringtones/ringer.ogg"));
            Assert.assertNotNull("AudioInputStream must exist", stream);
            Assert.assertFalse("Clip must not be open", clip.isOpen());
            clip.open(stream);
            Assert.assertTrue("Clip must be open", clip.isOpen());
            Assert.assertFalse("Clip must not be running", clip.isRunning());
            clip.start();
            Thread.sleep(1000);
            Assert.assertTrue("Clip must be running (after 1 second)", clip.isRunning());
            Thread.sleep(2000);
            Assert.assertTrue("Clip must be running", clip.isRunning());
            clip.stop();
            Thread.sleep(1000);
            Assert.assertFalse("Clip must not be running (after 1 second)", clip.isRunning());
            clip.close();
            Assert.assertFalse("Clip must not be open", clip.isOpen());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
