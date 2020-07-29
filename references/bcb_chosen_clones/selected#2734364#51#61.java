            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } else Tools.openURL(e.getURL().toString());
                    } catch (Exception x) {
                        x.printStackTrace();
                    }
                }
            }
