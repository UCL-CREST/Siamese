    public void google_search(String input) throws URISyntaxException {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            URI uri;
            uri = new URI("http://www.google.gr/search?hl=el&q=" + input.replace(' ', '+') + "&btnG=%CE%91%CE%BD%CE%B1%CE%B6%CE%AE%CF%84%CE%B7%CF%83%CE%B7&meta=");
            desktop.browse(uri);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
