            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI(String.format(txtURL.getText(), 0, 0, 0)));
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Could not open browser.");
                }
            }
