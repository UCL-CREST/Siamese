    private JEditorPane getJTextPane() {
        if (jTextPane == null) {
            jTextPane = new JEditorPane();
            jTextPane.setEditable(false);
            jTextPane.setEditorKit(new HTMLEditorKit());
            loadTextIntoTextPane();
            jTextPane.addHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(HyperlinkEvent evt) {
                    if (evt.getEventType() == EventType.ACTIVATED) {
                        try {
                            if (Desktop.isDesktopSupported()) {
                                Desktop desktop = Desktop.getDesktop();
                                if (evt.getURL() != null) {
                                    desktop.browse(new URI(evt.getURL().toString()));
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            log.log(Level.WARNING, "Error", e);
                        }
                    }
                }
            });
        }
        return jTextPane;
    }
