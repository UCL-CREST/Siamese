    @Override
    public void playSpotifySong(Track track) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(track.getId()));
            } catch (IOException e) {
                GroofyLogger.getInstance().logException(e);
            } catch (URISyntaxException e) {
                GroofyLogger.getInstance().logException(e);
            }
        }
    }
