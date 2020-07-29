    public void isohunt_search(String input) throws URISyntaxException {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            URI uri;
            uri = new URI("http://isohunt.com/torrents/?ihq=" + input.replace(' ', '+'));
            desktop.browse(uri);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
