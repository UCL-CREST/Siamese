            public void mouseClicked(MouseEvent e) {
                java.net.URI uri = null;
                try {
                    uri = new java.net.URI(Constants.HOMEPAGE_LINK);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                if (java.awt.Desktop.isDesktopSupported()) {
                    try {
                        if (uri != null) {
                            java.awt.Desktop.getDesktop().browse(uri);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
