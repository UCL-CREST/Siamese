    public boolean openTheDefaultBrowser(String url) {
        boolean result = Desktop.isDesktopSupported();
        if (result) {
            Desktop desktop = Desktop.getDesktop();
            if (result = desktop.isSupported(Desktop.Action.BROWSE)) {
                java.net.URI uri;
                try {
                    uri = new java.net.URI(url);
                    desktop.browse(uri);
                } catch (URISyntaxException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return result;
    }
