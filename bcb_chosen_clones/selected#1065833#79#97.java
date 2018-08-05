            public void actionPerformed(ActionEvent e) {
                String websiteUrl = "http://amun.phpsx.org";
                try {
                    URI websiteUri = new URI(websiteUrl);
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            desktop.browse(websiteUri);
                        } else {
                            JOptionPane.showMessageDialog(null, websiteUrl);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, websiteUrl);
                    }
                } catch (Exception ex) {
                    Zubat.handleException(ex);
                    JOptionPane.showMessageDialog(null, websiteUrl);
                }
            }
