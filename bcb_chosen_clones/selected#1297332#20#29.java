    public void actionPerformed(ActionEvent evt) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI("http://alx-library.sourceforge.net/"));
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error", e);
        }
    }
