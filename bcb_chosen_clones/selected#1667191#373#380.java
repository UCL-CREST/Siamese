                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(monitor.getMainPageURI());
                            } catch (IOException err) {
                            }
                        }
                    }
