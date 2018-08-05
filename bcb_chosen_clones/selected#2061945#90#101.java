            @Override
            public void mouseClicked(MouseEvent me) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI(resourceMap.getString("Application.homepage")));
                    } catch (URISyntaxException urise) {
                        logger.log(Level.WARNING, "Incorrect URI", urise);
                    } catch (IOException ioe) {
                        logger.log(Level.WARNING, "General IO Error", ioe);
                    }
                }
            }
