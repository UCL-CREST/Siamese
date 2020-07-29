    public void openBrowser(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException us) {
            System.out.println("URI syntax exception in " + url);
            us.printStackTrace();
        }
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                if (uri != null) desktop.browse(uri);
            } catch (IOException e) {
                p("IOException ");
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop.isDesktopSupported() - NOT");
        }
    }
