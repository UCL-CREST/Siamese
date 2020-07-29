                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        URL url = e.getURL();
                        String protocol = url.getProtocol();
                        if (ActionURLStreamHandler.PROTOCOL_ACTION.equals(protocol)) {
                            String actionId = url.getPath();
                            Map<String, String> request = Utils.parseQuery(url);
                            doAction(actionId, request);
                        } else {
                            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                try {
                                    Desktop.getDesktop().browse(url.toURI());
                                } catch (Exception ex) {
                                    log.error(String.format("Cannot open URL %s", url), ex);
                                }
                            }
                        }
                    }
                }
