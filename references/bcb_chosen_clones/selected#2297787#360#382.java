    public static void launchWebBrowser(javax.swing.event.HyperlinkEvent evt) {
        if (HyperlinkEvent.EventType.ACTIVATED.equals(evt.getEventType())) {
            URL url = evt.getURL();
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    if (url == null) {
                        String string = "http://" + evt.getDescription();
                        try {
                            url = new URL(string);
                        } catch (MalformedURLException ex) {
                            return;
                        }
                    }
                    try {
                        desktop.browse(url.toURI());
                    } catch (URISyntaxException ex) {
                    } catch (IOException ex) {
                    }
                }
            }
        }
    }
