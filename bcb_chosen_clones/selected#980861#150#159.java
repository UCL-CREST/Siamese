            public void mouseClicked(MouseEvent me) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("http://trac.jsystemtest.org/wiki/DetailedOSProjectsList"));
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Failed openning browser to JSystem website: " + e.getMessage());
                    }
                }
            }
