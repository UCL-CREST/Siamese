            @Override
            public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    HyperlinkEvent.EventType eventType = hyperlinkEvent.getEventType();
                    if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        try {
                            Desktop.getDesktop().browse(hyperlinkEvent.getURL().toURI());
                        } catch (URISyntaxException uriSyntaxException) {
                            showMessageDialog(null, new MessageFormat(Messages.getString("MessageDialog.6")).format(new Object[] { hyperlinkEvent.getURL() }), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                        } catch (IOException ioException) {
                            showMessageDialog(null, Messages.getString("MessageDialog.8"), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                        }
                    }
                } else {
                    showMessageDialog(null, Messages.getString("MessageDialog.7"), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                }
            }
