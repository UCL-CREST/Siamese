            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(URI.create(e.getDescription()));
                                textEntry.requestFocusInWindow();
                                viewPane.setCaretPosition(viewPane.getStyledDocument().getLength());
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(viewPane, messages.getString("ChatPane.msg.CouldNotLaunchDefaultBrowserSeeLogForReason"));
                                logger.log(Level.INFO, messages.getString("ChatPane.msg.CouldNotLaunchBrowser"), ex);
                            }
                        }
                    }
                }
            }
