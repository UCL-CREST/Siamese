            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://www.circuitsmith.com"));
                    } catch (Exception ioe) {
                    }
                }
            }
