    public static void launchWebBrowser(String address) {
        if (Desktop.isDesktopSupported()) {
            final Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                URL url = null;
                String string = address;
                try {
                    url = new URL(string);
                } catch (MalformedURLException ex) {
                    return;
                }
                try {
                    desktop.browse(url.toURI());
                } catch (URISyntaxException ex) {
                } catch (IOException ex) {
                }
            }
        }
    }
