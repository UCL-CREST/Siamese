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
                                System.err.println(MessageFormat.format(Messages.getString("dialog.about.uri.error"), e.getURL()));
                            } catch (IOException ex) {
                                System.err.println(Messages.getString("dialog.about.navigate.error"));
                            }
                        }
                    }
                }
            }
