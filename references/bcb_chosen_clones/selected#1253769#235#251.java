    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ENTERED) {
        } else if (event.getEventType() == HyperlinkEvent.EventType.EXITED) {
        } else if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(event.getURL().toURI());
                }
            } catch (URISyntaxException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
