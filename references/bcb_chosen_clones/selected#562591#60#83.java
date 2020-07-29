    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() != EventType.ACTIVATED) {
            return;
        }
        URI uri;
        try {
            uri = e.getURL().toURI();
        } catch (URISyntaxException e1) {
            return;
        }
        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.BROWSE)) {
                try {
                    d.browse(uri);
                    return;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        JOptionPane.showMessageDialog(getOwner(), "Sorry, your systen doesn't support opening URI\nPlease visit the address manually: " + uri, "Lack of Browsing Support", JOptionPane.ERROR_MESSAGE);
    }
