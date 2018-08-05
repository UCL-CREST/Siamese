    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (EventType.ACTIVATED.equals(e.getEventType())) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
