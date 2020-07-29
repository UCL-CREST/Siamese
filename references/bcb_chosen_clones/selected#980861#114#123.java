            public void mouseClicked(MouseEvent me) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("http://www.ignissoft.com"));
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Failed openning browser to Ignis website: " + e.getMessage());
                    }
                }
            }
