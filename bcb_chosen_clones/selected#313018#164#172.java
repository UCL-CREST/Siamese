            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://popcornforum.de/forumdisplay.php?fid=67"));
                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(null, "Could not acces http://popcornforum.de/forumdisplay.php?fid=67", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
