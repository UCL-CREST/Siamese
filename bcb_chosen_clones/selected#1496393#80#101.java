    private void onCreditsHyperlinkUpdate(final HyperlinkEvent event) {
        if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            if ("http".equalsIgnoreCase(event.getURL().getProtocol()) || "https".equalsIgnoreCase(event.getURL().getProtocol())) {
                if (Desktop.isDesktopSupported()) {
                    final Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(event.getURL().toURI());
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        } catch (URISyntaxException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                } else {
                    LOGGER.log(Level.SEVERE, "Desktop not supported");
                }
            } else {
                LOGGER.log(Level.SEVERE, "Unsupported url type!");
            }
        }
    }
