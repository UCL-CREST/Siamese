            @Override
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                    final Desktop dt = Desktop.getDesktop();
                    if (dt.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            dt.browse(evt.getURL().toURI());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
