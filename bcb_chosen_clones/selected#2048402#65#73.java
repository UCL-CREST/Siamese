    public void openUrl(URL url) throws IOException {
        if (!Desktop.isDesktopSupported()) throw new IOException("Desktop not supported");
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(url.toURI());
        } catch (URISyntaxException e) {
            throw new IOException("URL to URI conversion failed", e);
        }
    }
