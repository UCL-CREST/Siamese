    public static HyperlinkListener getDefaultLinkListener() {
        return new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (Desktop.isDesktopSupported() && e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.browse(e.getURL().toURI());
                    } catch (IOException ex) {
                        Logger.getLogger(KeySizeChooser.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(KeySizeChooser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
    }
