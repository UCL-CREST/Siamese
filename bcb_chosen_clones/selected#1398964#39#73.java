            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    boolean browserLaunched = false;
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        URL u = e.getURL();
                        if (u.getProtocol().equalsIgnoreCase("mailto")) {
                            if (desktop.isSupported(Desktop.Action.MAIL)) {
                                browserLaunched = true;
                                try {
                                    desktop.mail(u.toURI());
                                } catch (IOException ioe) {
                                    browserLaunched = false;
                                } catch (URISyntaxException es) {
                                    browserLaunched = false;
                                }
                            }
                        } else {
                            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                                browserLaunched = true;
                                try {
                                    desktop.browse(u.toURI());
                                } catch (IOException ioe) {
                                    browserLaunched = false;
                                } catch (URISyntaxException es) {
                                    browserLaunched = false;
                                }
                            }
                        }
                    }
                    if (!browserLaunched) {
                        JOptionPane.showMessageDialog(null, "Sorry. The application was unable to interact with the desktop to launch the URL. Desktop functionality may not be enabled on this computer.");
                    }
                }
            }
