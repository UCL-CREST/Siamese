    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            clickClip = AudioSystem.getClip();
            URL clipUrl = SONIVISCore.findFileInBundle(MediaWikiActivator.PLUGIN_ID, "doc/Ente.wav").toURI().toURL();
            AudioInputStream ais = AudioSystem.getAudioInputStream(clipUrl);
            clickClip.open(ais);
            clickClip.start();
        } catch (LineUnavailableException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (UnsupportedAudioFileException e) {
        }
        return null;
    }
