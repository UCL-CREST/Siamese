            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                    try {
                        logger.info("Url: " + e.getDescription());
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                }
            }
