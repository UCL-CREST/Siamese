    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        URL url = e.getURL();
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
