    private void launchBrowser(String uri) throws IOException, URISyntaxException {
        if (uri != null) {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null) desktop.browse(new URI(uri));
        }
    }
