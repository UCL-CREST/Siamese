                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(uri);
                            } catch (Exception ex) {
                            }
                        }
                    }
