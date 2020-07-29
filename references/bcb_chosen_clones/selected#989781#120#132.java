        @Override
        public void actionPerformed(ActionEvent e) {
            if (Desktop.isDesktopSupported()) {
                Desktop d = Desktop.getDesktop();
                try {
                    if (d.isSupported(Desktop.Action.BROWSE)) {
                        d.browse(new URI(url));
                    }
                } catch (Exception ex) {
                    System.err.println("DialogHinweis.BeobUrl: " + ex.getMessage());
                }
            }
        }
