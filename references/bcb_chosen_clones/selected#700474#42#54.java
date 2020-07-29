    public void youtube_search(String input) throws URISyntaxException {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            URI uri;
            uri = new URI("http://www.youtube.com/results?search_query=" + input.replace(' ', '+') + "&search_type=&aq=f");
            desktop.browse(uri);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
