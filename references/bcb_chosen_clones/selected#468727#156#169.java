                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Desktop desktop = null;
                                    if (Desktop.isDesktopSupported()) {
                                        desktop = Desktop.getDesktop();
                                        try {
                                            desktop.browse(uri);
                                        } catch (IOException ioe) {
                                            JOptionPane.showMessageDialog((Component) aggregator, ioe.getMessage());
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog((Component) aggregator, "Hyperlinks not supported by OS.");
                                    }
                                }
