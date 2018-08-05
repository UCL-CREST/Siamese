    public static void openURL(String url) {
        URI uri = null;
        try {
            uri = new URI(url.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
