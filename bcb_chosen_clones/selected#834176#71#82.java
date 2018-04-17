                    public void hyperlinkUpdate(HyperlinkEvent e) {
                        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(e.getURL().toURI());
                            } catch (URISyntaxException use) {
                                use.printStackTrace();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }
                    }
