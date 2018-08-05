            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String url = e.getURL().toString();
                    try {
                        if (StringUtils.isNotEmpty(url)) {
                            if (url.equals("xy_ungelöst")) {
                            } else {
                                if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                            }
                        }
                    } catch (IOException e1) {
                    } catch (URISyntaxException e1) {
                    }
                }
            }
