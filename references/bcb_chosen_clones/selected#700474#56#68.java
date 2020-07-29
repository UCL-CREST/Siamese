    public void imdb_search(String input) throws URISyntaxException {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            URI uri;
            uri = new URI("http://www.imdb.com/find?s=all&q=" + input.replace(' ', '+') + "&x=0&y=0");
            desktop.browse(uri);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
