        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                JEditorPane pane = (JEditorPane) e.getSource();
                if (e instanceof HTMLFrameHyperlinkEvent) {
                    HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                    HTMLDocument doc = (HTMLDocument) pane.getDocument();
                    doc.processHTMLFrameHyperlinkEvent(evt);
                } else {
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    Runtime rm = Runtime.getRuntime();
                    String temp = "";
                    if (e.getDescription().toString().indexOf('@') != -1) {
                        temp = "mailto:" + (e.getDescription().toString());
                        Desktop desktop = null;
                        if (Desktop.isDesktopSupported()) {
                            try {
                                desktop = Desktop.getDesktop();
                                desktop.mail(new URI(temp));
                            } catch (Exception ex) {
                            }
                        }
                    } else {
                        temp = e.getDescription().toString();
                        Desktop desktop = null;
                        if (Desktop.isDesktopSupported()) {
                            desktop = Desktop.getDesktop();
                            try {
                                desktop = Desktop.getDesktop();
                                desktop.browse(new URI(temp));
                            } catch (Exception ex) {
                            }
                        }
                    }
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
