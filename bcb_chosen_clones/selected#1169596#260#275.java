    @Override
    public void hyperlinkUpdate(final HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (final IOException e1) {
                    this.log.error("{}", e1.getLocalizedMessage());
                } catch (final URISyntaxException e1) {
                    this.log.error("{}", e1.getLocalizedMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please view link at " + e.getURL().toString());
            }
        }
    }
