        @Override
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                JEditorPane pane = (JEditorPane) e.getSource();
                if (e instanceof HTMLFrameHyperlinkEvent) {
                    HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                    HTMLDocument doc = (HTMLDocument) pane.getDocument();
                    doc.processHTMLFrameHyperlinkEvent(evt);
                } else {
                    try {
                        if (Desktop.isDesktopSupported() && (Desktop.getDesktop() != null)) {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                            triggerAction("REMOVE");
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
