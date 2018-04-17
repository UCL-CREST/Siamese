                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Desktop desktop = null;
                                    if (Desktop.isDesktopSupported()) {
                                        desktop = Desktop.getDesktop();
                                        try {
                                            desktop.browse(uri);
                                        } catch (IOException ioe) {
                                            ErrorDialog.show(ioe);
                                        }
                                    } else {
                                        JOptionPane.showInputDialog(desktop, "Hyperlinks not supported by OS.");
                                    }
                                }
