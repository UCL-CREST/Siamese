                public void actionPerformed(ActionEvent e) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(monitor.getBuildURI(e.getActionCommand()));
                        } catch (IOException err) {
                        }
                    }
                }
