            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                URI uri = e.getURL().toURI();
                                desktop.browse(uri);
                            } catch (URISyntaxException ex) {
                                System.err.println("A link in the description.html is not correct: " + e.getURL());
                            } catch (IOException ex) {
                                System.err.println("Cannot navigate to link since a default program is not set or does not exist.");
                            }
                        }
                    }
                }
            }
