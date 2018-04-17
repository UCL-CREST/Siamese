            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(event.getEventType())) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(event.getURL().toURI());
                            } catch (IOException e) {
                                log.error("Cannot open URL", e);
                            } catch (URISyntaxException e) {
                                log.error("Cannot open URL", e);
                            }
                        } else log.info("Clicked on info URL, but no browser support detected");
                    } else log.info("Clicked on info URL, but no browser support detected");
                }
            }
