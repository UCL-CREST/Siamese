    @Override
    public void launchUpdate() {
        if (getUpdateUrl() != null && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(getUpdateUrl().toURI());
            } catch (IOException e) {
                GroofyLogger.getInstance().logException(e);
            } catch (URISyntaxException e) {
                GroofyLogger.getInstance().logException(e);
            }
        }
    }
